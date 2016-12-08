package gr.uom.agelogeo.androidproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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



        final ListView listView = (ListView) findViewById(R.id.listview);


        new AsyncTask<Void, Void, String>() {
            private Exception exception;

            @Override
            protected void onPreExecute() {
            }

            protected String doInBackground(Void... urls) {
                try {
                    String apiKey = getString(R.string.apiKeySearch);
                    String link = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?apikey="+apiKey+"&origin="+origin+
                            "&destination="+destination+"&departure_date="+departure_date+"&return_date="+return_date+"&adults="+adults+
                            "&children="+children+"&infants="+infants+"&nonstop="+nonstop+"&travel_class="+travel_class+"&number_of_results=2";
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
                try {
                    // parse the json result returned from the service
                    JSONObject jsonResult = new JSONObject(response);
                    JSONArray results = (JSONArray) jsonResult.get("results");
                    JSONObject result_one = results.getJSONObject(0);
                    JSONArray itinerary = (JSONArray) result_one.get("itineraries");
                    JSONObject itinerary_one = itinerary.getJSONObject(0);
                    JSONObject outbound = itinerary_one.getJSONObject("outbound");
                    JSONArray flights = (JSONArray) outbound.get("flights");
                    JSONObject flight_one = flights.getJSONObject(0);
                    System.out.println(flight_one.getString("departs_at"));
                    System.out.println(flight_one.getString("arrives_at"));
                    System.out.println(flight_one.getJSONObject("origin").getString("airport"));
                    System.out.println(flight_one.getJSONObject("origin").getString("terminal"));
                   /* JSONArray outbound_flights = outbound.getJSONArray("flights");
                    String test = outbound_flights.getString(0);
                    System.out.println(test);*/




                    //System.out.println(jsonResult);


                   /*String[] values = new String[jsonResult.length()];
                    for (int i = 0; i < jsonResult.length(); i++) {
                        String value = jsonResult.getJSONObject(i).getString("value");
                        String label = jsonResult.getJSONObject(i).getString("label");
                        values[i] = label;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchAirportActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
                    listView.setAdapter(adapter);*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
