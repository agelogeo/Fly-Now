package gr.uom.agelogeo.androidproject;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class TopFlights extends Fragment {
    EditText airport;
    String code2;
    Button dateBtn;
    int year_x,month_x,day_x;
    public TopFlights() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_flights, container, false);

        airport=(EditText) v.findViewById(R.id.airport);

        final Calendar cal = Calendar.getInstance();
        if(cal.get(Calendar.MONTH)-2<0){
            year_x=cal.get(Calendar.YEAR)-1;
            month_x=12+cal.get(Calendar.MONTH)-2;
        }
        month_x=cal.get(Calendar.MONTH)-1;
        year_x=cal.get(Calendar.YEAR);
        day_x=cal.get(Calendar.DAY_OF_MONTH);

        System.out.println("Year : "+year_x+" Month : "+month_x+" Day : "+day_x);

        Intent i = new Intent(getActivity(),SearchAirportActivity.class);
        i.putExtra("lat","!");
        i.putExtra("lng","!");
        startActivityForResult(i, 3);
        airport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),SearchAirportActivity.class);
                i.putExtra("lat","!");
                i.putExtra("lng","!");
                startActivityForResult(i, 3);
            }
        });
        final ListView listView = (ListView) v.findViewById(R.id.listofTopAirports);
        airport.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(airport.getText().length()!=0) {
                    code2 = airport.getText().subSequence(airport.getText().length() - 4, airport.getText().length() - 1).toString();

                }
                final String code = code2;
                try {
                    new AsyncTask<Void, Void, String>() {
                        private Exception exception;

                        @Override
                        protected void onPreExecute() {
                        }

                        protected String doInBackground(Void... urls) {
                            try {

                                String apiKey = getString(R.string.AMADEUS_API_KEY);

                                String link = "https://api.sandbox.amadeus.com/v1.2/travel-intelligence/top-destinations?apikey="+apiKey+"&period="+year_x+"-"+month_x+"&origin="+code;
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
                                if (response != null) {
                                    JSONObject res = new JSONObject(response);
                                    if (res.has("results")) {
                                        JSONArray jsonResult = res.getJSONArray("results");
                                        String[] values = new String[jsonResult.length()];
                                        for (int i = 0; i < jsonResult.length(); i++) {
                                            String code = jsonResult.getJSONObject(i).getString("destination");
                                            String flights = jsonResult.getJSONObject(i).getString("flights");
                                            String travelers = jsonResult.getJSONObject(i).getString("travelers");
                                            values[i] = "[" + code + "] "+getString(R.string.flights)+" : " + flights + " "+getString(R.string.travelers)+" : " + travelers;
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);
                                        listView.setAdapter(adapter);
                                    } else
                                        Toast.makeText(getActivity(), R.string.technicalError, Toast.LENGTH_SHORT).show();
                                }
                                }catch(JSONException e){
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), R.string.technicalError, Toast.LENGTH_SHORT).show();
                                }
                        }
                    }.execute().get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





        return v;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (resultCode == getActivity().RESULT_OK) {
                String stredittext = data.getStringExtra("getSelectedItem");
                airport.setText(stredittext);
            }
        }
    }




}
