package gr.uom.agelogeo.androidproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
        String response = i.getStringExtra("JSON_result");
        int result_ind = i.getIntExtra("LVI_result_ind",0);
        int itinerary_ind = i.getIntExtra("LVI_itinerary_ind",0);


        try {
            JSONObject result = new JSONObject(response);
            JSONArray itineraries = (JSONArray) result.get("itineraries");
            JSONObject selected_itinerary = itineraries.getJSONObject(itinerary_ind);
            System.out.println(selected_itinerary);
            System.out.println(result.get("fare"));



            JSONObject outbound = selected_itinerary.getJSONObject("outbound");
            JSONArray out_flights = (JSONArray) outbound.get("flights");

            if(!selected_itinerary.has("inbound")){
                LinearLayout inInfo = (LinearLayout) findViewById(R.id.inboundInfo);
                inInfo.setVisibility(View.GONE);
            }else{
                JSONObject inbound = selected_itinerary.getJSONObject("inbound");
                JSONArray in_flights = (JSONArray) inbound.get("flights");
                if(in_flights.length()==1){
                    LinearLayout inm1 = (LinearLayout) findViewById(R.id.inmInfo1);
                    LinearLayout inm2 = (LinearLayout) findViewById(R.id.inmInfo2);
                    LinearLayout inm3 = (LinearLayout) findViewById(R.id.inmInfo3);
                    inm1.setVisibility(View.GONE);
                    inm2.setVisibility(View.GONE);
                    inm3.setVisibility(View.GONE);
                }
            }

            if(out_flights.length()==1){
                LinearLayout outm1 = (LinearLayout) findViewById(R.id.outmInfo1);
                LinearLayout outm2 = (LinearLayout) findViewById(R.id.outmInfo2);
                LinearLayout outm3 = (LinearLayout) findViewById(R.id.outmInfo3);
                outm1.setVisibility(View.GONE);
                outm2.setVisibility(View.GONE);
                outm3.setVisibility(View.GONE);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
