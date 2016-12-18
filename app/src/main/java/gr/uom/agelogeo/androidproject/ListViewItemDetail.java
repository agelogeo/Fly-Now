package gr.uom.agelogeo.androidproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListViewItemDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_item_detail);

        Intent i = this.getIntent();
        String response = i.getStringExtra("JSON_response");
        int result_ind = i.getIntExtra("LVI_result_ind",0);
        int itinerary_ind = i.getIntExtra("LVI_itinerary_ind",0);


        /*try {
            JSONObject jsonResult = new JSONObject(response);
            JSONArray results = (JSONArray) jsonResult.get("results");
            JSONObject result = results.getJSONObject(result_ind);
            JSONArray itineraries = (JSONArray) result.get("itineraries");
            JSONObject selected_itinerary = itineraries.getJSONObject(itinerary_ind);
            System.out.println(selected_itinerary);
            System.out.println(result.get("fare"));



            JSONObject outbound = selected_itinerary.getJSONObject("outbound");
            JSONArray out_flights = (JSONArray) outbound.get("flights");
            JSONObject out_flight = out_flights.getJSONObject(0);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }
}
