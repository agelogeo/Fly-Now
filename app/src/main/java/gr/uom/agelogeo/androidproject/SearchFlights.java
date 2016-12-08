package gr.uom.agelogeo.androidproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchFlights extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flights);
        final String origin = "BOS";
        final String destination = "LON";
        final String departure_date = "2016-12-20";
        final String return_date = "2016-12-22";
        final int adults = 2;
        final int children = 1;
        final int infants = 1;
        final boolean nonstop = true;
        final String travel_class = "ECONOMY";

        final ListView listView = (ListView) findViewById(R.id.listofAirports);

        /*final String sString = s.toString();
        if(s.length()!=0) {
            */
        new AsyncTask<Void, Void, String>() {
            private Exception exception;

            @Override
            protected void onPreExecute() {
            }

            protected String doInBackground(Void... urls) {
                try {
                    String apiKey = getString(R.string.apiKeySearch);
                    String link = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?apikey="+apiKey+"&origin="+origin+
                            "&destination="+destination+"&departure_date="+departure_date+"&return_date="+return_date+
                            "&adults="+adults+"&children="+children+"&infants="+infants+"&nonstop="+nonstop+"&travel_class="+travel_class+"&number_of_results=2";
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
                System.out.println(response);
                /*try {
                    // parse the json result returned from the service
                    JSONArray jsonResult = new JSONArray(response);
                    String[] values = new String[jsonResult.length()];
                    for (int i = 0; i < jsonResult.length(); i++) {
                        String value = jsonResult.getJSONObject(i).getString("value");
                        String label = jsonResult.getJSONObject(i).getString("label");
                        values[i] = label;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchAirportActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
                    listView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }.execute();
    }
}
