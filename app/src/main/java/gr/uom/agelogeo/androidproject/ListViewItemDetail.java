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
        int itinerary_ind = i.getIntExtra("LVI_itinerary_ind",0);

        try {
            JSONObject result = new JSONObject(response);
            JSONArray itineraries = (JSONArray) result.get("itineraries");
            JSONObject selected_itinerary = itineraries.getJSONObject(itinerary_ind);
            System.out.println(selected_itinerary);
            System.out.println(result.get("fare"));

            JSONObject outbound = selected_itinerary.getJSONObject("outbound");
            JSONArray out_flights = (JSONArray) outbound.get("flights");

            ViewHolder outboundHolder = new ViewHolder();
            ViewHolder inboundHolder = new ViewHolder();
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

                    JSONObject in_flight = in_flights.getJSONObject(0);

                    String originCode = in_flight.getJSONObject("origin").getString("airport");
                    String destCode = in_flight.getJSONObject("destination").getString("airport");

                    inboundHolder.dDate = (TextView) findViewById(R.id.indDate);
                    inboundHolder.dDate.setText(in_flight.getString("departs_at"));

                    inboundHolder.dPortCode = (TextView) findViewById(R.id.indPortCode);
                    inboundHolder.dPortCode.setText(originCode);

                    inboundHolder.dAirline = (TextView) findViewById(R.id.indAirline);
                    inboundHolder.dAirline.setText(in_flight.getString("operating_airline"));

                    inboundHolder.aDate = (TextView) findViewById(R.id.inaDate);
                    inboundHolder.aDate.setText(in_flight.getString("arrives_at"));

                    inboundHolder.aPortCode = (TextView) findViewById(R.id.inaPortCode);
                    inboundHolder.aPortCode.setText(destCode);

                    inboundHolder.aAirline = (TextView) findViewById(R.id.inaAirline);
                    inboundHolder.aAirline.setText(in_flight.getString("operating_airline"));
                    }
            }

            if(out_flights.length()==1){
                LinearLayout outm1 = (LinearLayout) findViewById(R.id.outmInfo1);
                LinearLayout outm2 = (LinearLayout) findViewById(R.id.outmInfo2);
                LinearLayout outm3 = (LinearLayout) findViewById(R.id.outmInfo3);
                outm1.setVisibility(View.GONE);
                outm2.setVisibility(View.GONE);
                outm3.setVisibility(View.GONE);
                JSONObject out_flight = out_flights.getJSONObject(0);

                String originCode = out_flight.getJSONObject("origin").getString("airport");
                String destCode = out_flight.getJSONObject("destination").getString("airport");
                outboundHolder.journeyView = (TextView) findViewById(R.id.journeyView);
                outboundHolder.journeyView.setText(originCode+" - "+destCode);

                outboundHolder.dDate = (TextView) findViewById(R.id.outdDate);
                outboundHolder.dDate.setText(out_flight.getString("departs_at"));

                outboundHolder.dPortCode = (TextView) findViewById(R.id.outdPortCode);
                outboundHolder.dPortCode.setText(originCode);

                outboundHolder.dAirline = (TextView) findViewById(R.id.outdAirline);
                outboundHolder.dAirline.setText(out_flight.getString("operating_airline"));

                outboundHolder.aDate = (TextView) findViewById(R.id.outaDate);
                outboundHolder.aDate.setText(out_flight.getString("arrives_at"));

                outboundHolder.aPortCode = (TextView) findViewById(R.id.outaPortCode);
                outboundHolder.aPortCode.setText(destCode);

                outboundHolder.aAirline = (TextView) findViewById(R.id.outaAirline);
                outboundHolder.aAirline.setText(out_flight.getString("operating_airline"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static class ViewHolder{
        //journeyInfo
        TextView journeyView;
        TextView journeyDuration;
        TextView stopView;
        //boundInfo
        TextView Title;
        //DepartInfo
        TextView dDate;
        TextView dPortCode;
        TextView dPortName;
        TextView dAirline;
        //MiddleInfo1
        TextView m1PortCode;
        TextView m1PortName;
        TextView m1Duration;
        //MiddleInfo2
        TextView m2PortCode;
        TextView m2PortName;
        TextView m2Duration;
        //MiddleInfo3
        TextView m3PortCode;
        TextView m3PortName;
        TextView m3Duration;
        //ArriveInfo
        TextView aDate;
        TextView aPortCode;
        TextView aPortName;
        TextView aAirline;




    }
}
