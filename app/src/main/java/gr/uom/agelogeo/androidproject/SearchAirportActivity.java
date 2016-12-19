package gr.uom.agelogeo.androidproject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class SearchAirportActivity extends AppCompatActivity {
    String lat,lng;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_airport);

        Intent i = SearchAirportActivity.this.getIntent();
        lat=i.getStringExtra("lat");
        lng=i.getStringExtra("lng");
        if(!isNetworkAvailable()){
            Toast.makeText(this,"Please make sure you have internet access.", Toast.LENGTH_LONG).show();
            SearchAirportActivity.this.finish();
            return;
        }

        EditText searchbox = (EditText) findViewById(R.id.searchbox);
        final ListView listView = (ListView) findViewById(R.id.listofAirports);




        NearByAirports(listView);


        searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                TextView nearby = (TextView) findViewById(R.id.nearbyTextView);
                nearby.setVisibility(View.GONE);
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
                                String apiKey = getString(R.string.AMADEUS_API_KEY);
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
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchAirportActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
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
                ListViewItemClickListener(listView);
            }
        });

    }

    public void ListViewItemClickListener(final ListView listView){
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

    public void NearByAirports(final ListView listView){
        if(lat.equals("!") || lng.equals("!")){
            TextView nearby = (TextView) findViewById(R.id.nearbyTextView);
            nearby.setVisibility(View.GONE);
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_search_airport);
            rl.setFocusableInTouchMode(false);
            return ;
        }
        try {
            new AsyncTask<Void, Void, String>() {
                private Exception exception;

                @Override
                protected void onPreExecute() {
                }

                protected String doInBackground(Void... urls) {
                    try {

                        String apiKey = getString(R.string.IATA_API_KEY);

                        String link = "https://iatacodes.org/api/v6/nearby?api_key="+apiKey+"&lat="+lat+"&lng="+lng+"&distance=150";
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
                                JSONObject res = new JSONObject(response);
                                JSONArray jsonResult = res.getJSONArray("response");
                                String[] values = new String[jsonResult.length()];
                                for (int i = 0; i < jsonResult.length(); i++) {
                                    String code = jsonResult.getJSONObject(i).getString("code");
                                    String name = jsonResult.getJSONObject(i).getString("name");
                                    values[i] = name+" ["+code+"]";
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchAirportActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
                                listView.setAdapter(adapter);
                                ListViewItemClickListener(listView);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                }
            }.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    /*private void getLocation(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


    @Override
    public void onConnected(Bundle arg0) {
        fusedLocationProviderApi.requestLocationUpdates(googleApiClient,  locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(mContext, "location :"+location.getLatitude()+" , "+location.getLongitude(), Toast.LENGTH_SHORT).show();
    }*/


}
