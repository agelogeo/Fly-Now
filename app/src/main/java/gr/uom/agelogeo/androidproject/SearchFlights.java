package gr.uom.agelogeo.androidproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchFlights extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flights);
        final ProgressDialog loadingDialog = new ProgressDialog(SearchFlights.this);
        loadingDialog.setTitle(getString(R.string.pleaseWait));
        loadingDialog.setMessage(getString(R.string.bestpricesText));
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        Intent i = this.getIntent();
        final String origin = i.getStringExtra("origin");
        final String destination = i.getStringExtra("destination");
        String departure_date_temp = i.getStringExtra("departure_date");
        String return_date_temp = i.getStringExtra("return_date");
        final int adults = i.getIntExtra("adults",1);
        final int children = i.getIntExtra("children",0);
        final int infants = i.getIntExtra("infants",0);
        final boolean nonstop = i.getBooleanExtra("nonstop",false);
        final String travel_class = i.getStringExtra("travel_class");
        final ListView listView = (ListView) findViewById(R.id.listview);

        //Set Activity Title
        if(adults+children+infants==1)
            this.setTitle((adults+children+infants)+" "+getString(R.string.passengers_single)+" | "+departure_date_temp.substring(0,6));
        else
            this.setTitle((adults+children+infants)+" "+getString(R.string.passengers_plurar)+" | "+departure_date_temp.substring(0,6));


        //Change Date Format
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date temp = sdf.parse(departure_date_temp);
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            departure_date_temp=sdf.format(temp).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            SearchFlights.this.finish();
        }
        sdf = new SimpleDateFormat("dd MMM yyyy");
        if(!return_date_temp.equals("")){
            this.setTitle(this.getTitle()+" - "+return_date_temp.substring(0,6));
            try {
                Date temp = sdf.parse(return_date_temp);
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                return_date_temp=sdf.format(temp).toString();
            } catch (ParseException e) {
                e.printStackTrace();
                SearchFlights.this.finish();
            }
        }
        final String departure_date=departure_date_temp;
        final String return_date=return_date_temp;


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
                    loadingDialog.dismiss();
                    SearchFlights.this.finish();
                    return null;
                }
            }
            protected void onPostExecute(final String response) {
                final ArrayList<ListviewItem> adapterList = new ArrayList<ListviewItem>();
                boolean returnDate = false;
                try {
                    // parse the json result returned from the service
                    if(response==null)
                        Toast.makeText(SearchFlights.this, R.string.notAvailableTickets, Toast.LENGTH_LONG).show();
                    JSONObject jsonResult = new JSONObject(response);
                    JSONArray results = (JSONArray) jsonResult.get("results");
                    int counter = 0;
                    for(int i=0;i<results.length();i++){

                        JSONObject result = results.getJSONObject(i);
                        JSONArray itinerary = (JSONArray) result.get("itineraries");

                        for(int j=0;j<itinerary.length();j++) {
                            ListviewItem tempItem = new ListviewItem();
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
                            tempItem.setResult_indicator(i);
                            tempItem.setItinerary_indicator(j);
                            adapterList.add(tempItem);
                        }
                    }
                    System.out.println("Counter : "+counter);
                    loadingDialog.dismiss();
                    SearchFlightsAdapter myAdapter = new SearchFlightsAdapter(SearchFlights.this, adapterList , returnDate);
                    listView.setAdapter(myAdapter);


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            /*Intent i = new Intent(SearchFlights.this, MainActivity.class);
                            i.putExtra("JSON_response",response);
                            i.putExtra("LVI_result_ind",adapterList.get(position).getResult_indicator());
                            i.putExtra("LVI_itinerary_ind",adapterList.get(position).getItinerary_indicator());*/
                            RelativeLayout ml = (RelativeLayout) findViewById(R.id.activity_search_flights);
                            Snackbar.make(ml, "Loading Master/Detail Flow...", Snackbar.LENGTH_LONG).show();

                        }
                    });

                } catch (JSONException e) {
                    SearchFlights.this.finish();
                    e.printStackTrace();
                } catch (Exception e){
                    SearchFlights.this.finish();

                }
            }
        }.execute();
    }
}
