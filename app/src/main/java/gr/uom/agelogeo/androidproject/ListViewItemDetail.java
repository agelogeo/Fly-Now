package gr.uom.agelogeo.androidproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListViewItemDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_item_detail);

        /*final ProgressDialog loadingDialog = new ProgressDialog(ListViewItemDetail.this);
        loadingDialog.setTitle(getString(R.string.pleaseWait));
        loadingDialog.setMessage(getString(R.string.loadingYourData));
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        Intent i = this.getIntent();
        String response = i.getStringExtra("JSON_response");
        int result_ind = i.getIntExtra("LVI_result_ind",0);
        int itinerary_ind = i.getIntExtra("LVI_itinerary_ind",0);*/

        //loadingDialog.dismiss();

       /*
        try {
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
                    ArrayList<String> airlines = new ArrayList<String>();

                    JSONObject itinerary_one = itinerary.getJSONObject(j);
                    JSONObject outbound = itinerary_one.getJSONObject("outbound");
                    JSONArray out_flights = (JSONArray) outbound.get("flights");

                    JSONObject out_flight = out_flights.getJSONObject(0);

                    tempItem.setPrice(result.getJSONObject("fare").getString("total_price")+" EURO");
                    tempItem.setOutbound_origin(out_flight.getJSONObject("origin").getString("airport"));
                    tempItem.setOutbound_origin_time((out_flight.getString("departs_at")).split("T")[1].trim());
                    String outbound_airline = null;
        */


    }
}
