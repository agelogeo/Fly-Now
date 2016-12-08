package gr.uom.agelogeo.androidproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class SearchAirportActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_airport);

        EditText searchbox = (EditText) findViewById(R.id.searchbox);
        final ListView listView = (ListView) findViewById(R.id.listofAirports);
        searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                final String sString = s.toString();
                if(s.length()!=0) {
                    new AsyncTask<Void, Void, String>() {
                        private Exception exception;

                        @Override
                        protected void onPreExecute() {
                        }

                        protected String doInBackground(Void... urls) {
                            try {
                                String apiKey = getString(R.string.apiKey);
                                String link = "https://api.sandbox.amadeus.com/v1.2/airports/autocomplete?apikey=" + apiKey + "&term=" + sString;
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
                                    //System.out.println(stringBuilder.toString());
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
                            }
                        }
                    }.execute();
                }else
                    listView.setAdapter(null);

            }

            @Override
            public void afterTextChanged(Editable s) {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        intent.putExtra("getSelectedItem",listView.getItemAtPosition(position).toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        });

    }
}
