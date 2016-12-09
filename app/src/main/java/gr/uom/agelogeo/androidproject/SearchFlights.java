package gr.uom.agelogeo.androidproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SearchFlights extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flights);
        Intent i = this.getIntent();
        final String origin = i.getStringExtra("origin");
        final String destination = i.getStringExtra("destination");
        final String departure_date = i.getStringExtra("departure_date");
        final String return_date = i.getStringExtra("return_date");
        final int adults = i.getIntExtra("adults",1);
        final int children = i.getIntExtra("children",0);
        final int infants = i.getIntExtra("infants",0);
        final boolean nonstop = i.getBooleanExtra("nonstop",false);
        final String travel_class = i.getStringExtra("travel_class");
        if(adults+children+infants==1)
            this.setTitle(departure_date+" - "+return_date+" | "+(adults+children+infants)+" "+getString(R.string.passengers_single));
        else
            this.setTitle(departure_date+" - "+return_date+" | "+(adults+children+infants)+" "+getString(R.string.passengers_plurar));

        final ListView listView = (ListView) findViewById(R.id.listview);


        new AsyncTask<Void, Void, String>() {
            private Exception exception;

            @Override
            protected void onPreExecute() {
            }

            protected String doInBackground(Void... urls) {
                try {
                    String apiKey = getString(R.string.apiKeySearch);
                    String link = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?apikey="+apiKey+"&origin="+origin+"&currency=EUR"+
                            "&destination="+destination+"&departure_date="+departure_date+"&adults="+adults+"&travel_class="+travel_class;

                    if(!return_date.equals(""))
                        link+="&return_date="+return_date;
                    if(children>0)
                        link+="&children="+children;
                    if(infants>0)
                        link+="&infants="+infants;
                    if(nonstop)
                        link+="&nonstop="+nonstop;
                    if(false)
                        link+="&number_of_results=20";

                    System.out.println(link);

                    URL url = new URL(link);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        System.out.println(stringBuilder.toString());
                        return stringBuilder.toString();
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    return null;
                }
            }
            protected void onPostExecute(String response) {
                ArrayList<ListviewItem> adapterList = new ArrayList<ListviewItem>();
                boolean returnDate = false;
                try {
                    // parse the json result returned from the service
                    JSONObject jsonResult = new JSONObject(response);
                    JSONArray results = (JSONArray) jsonResult.get("results");
                    int counter = 0;
                    for(int i=0;i<results.length();i++){
                        ListviewItem tempItem = new ListviewItem();
                        JSONObject result = results.getJSONObject(i);
                        JSONArray itinerary = (JSONArray) result.get("itineraries");

                        for(int j=0;j<itinerary.length();j++) {
                            counter++;

                            JSONObject itinerary_one = itinerary.getJSONObject(j);
                            JSONObject outbound = itinerary_one.getJSONObject("outbound");
                            JSONArray out_flights = (JSONArray) outbound.get("flights");

                            JSONObject out_flight = out_flights.getJSONObject(0);

                            tempItem.setAirline(out_flight.getString("operating_airline"));
                            tempItem.setPrice(result.getJSONObject("fare").getString("total_price")+" EURO");
                            tempItem.setOutbound_origin(out_flight.getJSONObject("origin").getString("airport"));
                            tempItem.setOutbound_origin_time((out_flight.getString("departs_at")).split("T")[1].trim());

                            if(out_flights.length()>1){
                                tempItem.setStops(String.valueOf(out_flights.length()-1));
                                tempItem.setOutbound_destination(out_flights.getJSONObject(out_flights.length()-1).getJSONObject("destination").getString("airport"));
                                tempItem.setOutbound_destination_time((out_flights.getJSONObject(out_flights.length()-1).getString("arrives_at")).split("T")[1].trim());
                            }else {
                                tempItem.setStops("0");
                                tempItem.setOutbound_destination(out_flight.getJSONObject("destination").getString("airport"));
                                tempItem.setOutbound_destination_time((out_flight.getString("arrives_at")).split("T")[1].trim());
                            }
                            tempItem.setOutbound_travel_class(out_flight.getJSONObject("booking_info").getString("travel_class"));
                            tempItem.setOutbound_available(out_flight.getJSONObject("booking_info").getString("seats_remaining"));

                            if (itinerary_one.has("inbound")) {
                                returnDate = true;
                                JSONObject inbound = itinerary_one.getJSONObject("inbound");
                                JSONArray in_flights = (JSONArray) inbound.get("flights");
                                JSONObject in_flight = in_flights.getJSONObject(0);

                                tempItem.setInbound_origin(in_flight.getJSONObject("origin").getString("airport"));
                                tempItem.setInbound_origin_time((in_flight.getString("departs_at")).split("T")[1].trim());
                                if(in_flights.length()>1){
                                    tempItem.setInbound_destination(in_flights.getJSONObject(in_flights.length()-1).getJSONObject("destination").getString("airport"));
                                    tempItem.setInbound_destination_time((in_flights.getJSONObject(in_flights.length()-1).getString("arrives_at")).split("T")[1].trim());
                                }else {
                                    tempItem.setInbound_destination(in_flight.getJSONObject("destination").getString("airport"));
                                    tempItem.setInbound_destination_time((in_flight.getString("arrives_at")).split("T")[1].trim());
                                }
                                tempItem.setInbound_travel_class(in_flight.getJSONObject("booking_info").getString("travel_class"));
                                tempItem.setInbound_available(in_flight.getJSONObject("booking_info").getString("seats_remaining"));

                            }
                            adapterList.add(tempItem);
                        }
                    }
                    System.out.println("Counter : "+counter);
                    SearchFlightsAdapter myAdapter = new SearchFlightsAdapter(SearchFlights.this, adapterList , returnDate);
                    listView.setAdapter(myAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}