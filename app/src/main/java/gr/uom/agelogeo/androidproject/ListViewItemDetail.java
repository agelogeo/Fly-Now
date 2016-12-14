package gr.uom.agelogeo.androidproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


        try {
            JSONObject jsonResult = new JSONObject(response);
            JSONArray results = (JSONArray) jsonResult.get("results");
            JSONObject result = results.getJSONObject(result_ind);
            JSONArray itineraries = (JSONArray) result.get("itineraries");
            JSONObject selected_itinerary = itineraries.getJSONObject(itinerary_ind);
            System.out.println(selected_itinerary);
        } catch (JSONException e) {
            e.printStackTrace();
        }


            /*

            ListviewItem tempItem = new ListviewItem();

            ArrayList<String> airlines = new ArrayList<String>();


            JSONObject outbound = itinerary_one.getJSONObject("outbound");
            JSONArray out_flights = (JSONArray) outbound.get("flights");

            JSONObject out_flight = out_flights.getJSONObject(0);

            tempItem.setPrice(result.getJSONObject("fare").getString("total_price")+" EURO");
            tempItem.setOutbound_origin(out_flight.getJSONObject("origin").getString("airport"));
            tempItem.setOutbound_origin_time((out_flight.getString("departs_at")).split("T")[1].trim());
            String outbound_airline = null;*/

    }
}
