package gr.uom.agelogeo.androidproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class ListViewItemDetail extends AppCompatActivity {
    boolean hasInbound = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_item_detail);
        Intent i = this.getIntent();
        String response = i.getStringExtra("JSON_result");
        int itinerary_ind = i.getIntExtra("LVI_itinerary_ind",0);

        Button clicktopay = (Button) findViewById(R.id.clicktobuy);
        clicktopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_list_view_item_detail), R.string.thanksforpurchase, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        try {
            JSONObject result = new JSONObject(response);
            JSONArray itineraries = (JSONArray) result.get("itineraries");
            JSONObject selected_itinerary = itineraries.getJSONObject(itinerary_ind);

            clicktopay.setText(getString(R.string.clicktobuyString)+" "+result.getJSONObject("fare").getString("total_price")+" €");

            /*System.out.println(selected_itinerary);
            System.out.println(result.get("fare"));*/

            JSONObject outbound = selected_itinerary.getJSONObject("outbound");
            JSONArray out_flights = (JSONArray) outbound.get("flights");



            if(!selected_itinerary.has("inbound")){
                LinearLayout inInfo = (LinearLayout) findViewById(R.id.inboundInfo);
                inInfo.setVisibility(View.GONE);
                LinearLayout inJourneyInfo = (LinearLayout) findViewById(R.id.InjourneyInfo);
                inJourneyInfo.setVisibility(View.GONE);
                hasInbound=false;
            }else{
                JSONObject inbound = selected_itinerary.getJSONObject("inbound");
                JSONArray in_flights = (JSONArray) inbound.get("flights");
                ViewHolder inboundHolder = new ViewHolder();
                JSONObject in_flight = in_flights.getJSONObject(0);

                inboundHolder.stopView = (TextView) findViewById(R.id.InstopView);
                inboundHolder.stopView.setText( getString(R.string.stops)+" "+(in_flights.length()-1));

                String originCode = in_flight.getJSONObject("origin").getString("airport");

                inboundHolder.journeyView = (TextView) findViewById(R.id.InjourneyView);
                inboundHolder.journeyDuration = (TextView) findViewById(R.id.InjourneyDuration);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                inboundHolder.dDate = (TextView) findViewById(R.id.indDate);
                inboundHolder.aDate = (TextView) findViewById(R.id.inaDate);
                try {
                    Date departs_date = sdf.parse(in_flight.getString("departs_at"));
                    Date arrives_date = sdf.parse(in_flights.getJSONObject(in_flights.length()-1).getString("arrives_at"));
                    String diffMins = TimeDifferenceToString(arrives_date.getTime(),departs_date.getTime());
                    sdf = new SimpleDateFormat("HH:mm   dd-MM-yyyy");
                    inboundHolder.dDate.setText(sdf.format(departs_date));
                    inboundHolder.aDate.setText(sdf.format(arrives_date));
                    inboundHolder.journeyDuration.setText(getString(R.string.totalDuration)+" "+diffMins);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                inboundHolder.dPortCode = (TextView) findViewById(R.id.indPortCode);
                inboundHolder.dPortCode.setText(originCode);

                inboundHolder.dPortName = (TextView) findViewById(R.id.indPortName);
                String[] temp = CallingAPIs(originCode,0).split("[|]");
                inboundHolder.dPortName.setText(temp[0]);
                inboundHolder.journeyView.setText(temp[1]+" - ");

                inboundHolder.dAirline = (TextView) findViewById(R.id.indAirline);
                inboundHolder.dAirline.setText(CallingAPIs(in_flight.getString("operating_airline"),1));

                if(in_flights.length()==1){
                    LinearLayout outm1 = (LinearLayout) findViewById(R.id.inmInfo1);
                    LinearLayout outm2 = (LinearLayout) findViewById(R.id.inmInfo2);
                    LinearLayout outm3 = (LinearLayout) findViewById(R.id.inmInfo3);
                    outm1.setVisibility(View.GONE);
                    outm2.setVisibility(View.GONE);
                    outm3.setVisibility(View.GONE);
                }else if(in_flights.length()==2){
                    LinearLayout outm2 = (LinearLayout) findViewById(R.id.inmInfo2);
                    LinearLayout outm3 = (LinearLayout) findViewById(R.id.inmInfo3);
                    outm2.setVisibility(View.GONE);
                    outm3.setVisibility(View.GONE);
                }else if(in_flights.length()==3) {
                    LinearLayout outm3 = (LinearLayout) findViewById(R.id.inmInfo3);
                    outm3.setVisibility(View.GONE);
                }
                if(in_flights.length()!=1) {
                    for (int z = 1; z < in_flights.length() ; z++) {
                        ViewHolder InsideHolder = new ViewHolder() ;
                        if (z == 1) {
                            InsideHolder.mPortCode = (TextView) findViewById(R.id.inm1PortCode);
                            InsideHolder.mPortName = (TextView) findViewById(R.id.inm1PortName);
                            InsideHolder.mAirline = (TextView) findViewById(R.id.inm1Airline);
                            InsideHolder.mDuration = (TextView) findViewById(R.id.inm1Duration);
                        } else if (z == 2) {
                            InsideHolder.mPortCode = (TextView) findViewById(R.id.inm2PortCode);
                            InsideHolder.mPortName = (TextView) findViewById(R.id.inm2Portname);
                            InsideHolder.mAirline = (TextView) findViewById(R.id.inm2Airline);
                            InsideHolder.mDuration = (TextView) findViewById(R.id.inm2Duration);
                        } else if (z == 3) {
                            InsideHolder.mPortCode = (TextView) findViewById(R.id.inm3PortCode);
                            InsideHolder.mPortName = (TextView) findViewById(R.id.inm3PortName);
                            InsideHolder.mAirline = (TextView) findViewById(R.id.inm3Airline);
                            InsideHolder.mDuration = (TextView) findViewById(R.id.inm3Duration);
                        }
                        in_flight=in_flights.getJSONObject(z);
                        originCode=in_flight.getJSONObject("origin").getString("airport");
                        InsideHolder.mPortCode.setText(originCode);
                        String[] temp2 = CallingAPIs(originCode,0).split("[|]");
                        InsideHolder.mPortName.setText(temp2[0]);
                        InsideHolder.mAirline.setText(CallingAPIs(in_flight.getString("operating_airline"),1));
                        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                        try {
                            Date departs_date = sdf3.parse(in_flights.getJSONObject(z-1).getString("arrives_at"));
                            Date arrives_date = sdf3.parse(in_flights.getJSONObject(z).getString("departs_at"));
                            String diffMins = TimeDifferenceToString(departs_date.getTime(),arrives_date.getTime());
                            InsideHolder.mDuration.setText(getString(R.string.standbyDuration)+" "+diffMins);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
                in_flight = in_flights.getJSONObject(in_flights.length()-1);

                String destCode = in_flight.getJSONObject("destination").getString("airport");
                inboundHolder.aPortCode = (TextView) findViewById(R.id.inaPortCode);
                inboundHolder.aPortCode.setText(destCode);

                inboundHolder.aPortName = (TextView) findViewById(R.id.inaPortName);
                temp = CallingAPIs(destCode,0).split("[|]");
                inboundHolder.aPortName.setText(temp[0]);
                inboundHolder.journeyView.setText(inboundHolder.journeyView.getText()+temp[1]);

                inboundHolder.aAirline = (TextView) findViewById(R.id.inaAirline);
                inboundHolder.aAirline.setText(CallingAPIs(in_flight.getString("operating_airline"),1));
            }
//--------------------------------------------------------------------------------------------------



            ViewHolder outboundHolder = new ViewHolder();
            JSONObject out_flight = out_flights.getJSONObject(0);

            outboundHolder.stopView = (TextView) findViewById(R.id.stopView);
            outboundHolder.stopView.setText( getString(R.string.stops)+" "+(out_flights.length()-1));

            String originCode = out_flight.getJSONObject("origin").getString("airport");

            outboundHolder.journeyView = (TextView) findViewById(R.id.journeyView);
            outboundHolder.journeyDuration = (TextView) findViewById(R.id.journeyDuration);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            outboundHolder.dDate = (TextView) findViewById(R.id.outdDate);
            outboundHolder.aDate = (TextView) findViewById(R.id.outaDate);
            try {
                Date departs_date = sdf.parse(out_flight.getString("departs_at"));
                Date arrives_date = sdf.parse(out_flights.getJSONObject(out_flights.length()-1).getString("arrives_at"));
                String diffMins = TimeDifferenceToString(departs_date.getTime(),arrives_date.getTime());
                sdf = new SimpleDateFormat("HH:mm   dd-MM-yyyy");
                outboundHolder.dDate.setText(sdf.format(departs_date));
                outboundHolder.aDate.setText(sdf.format(arrives_date));
                outboundHolder.journeyDuration.setText(getString(R.string.totalDuration)+" "+diffMins);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            outboundHolder.dPortCode = (TextView) findViewById(R.id.outdPortCode);
            outboundHolder.dPortCode.setText(originCode);

            outboundHolder.dPortName = (TextView) findViewById(R.id.outdPortName);
            String[] temp = CallingAPIs(originCode,0).split("[|]");
            outboundHolder.dPortName.setText(temp[0]);
            outboundHolder.journeyView.setText(temp[1]+" - ");

            outboundHolder.dAirline = (TextView) findViewById(R.id.outdAirline);
            outboundHolder.dAirline.setText(CallingAPIs(out_flight.getString("operating_airline"),1));

            if(out_flights.length()==1){
                LinearLayout outm1 = (LinearLayout) findViewById(R.id.outmInfo1);
                LinearLayout outm2 = (LinearLayout) findViewById(R.id.outmInfo2);
                LinearLayout outm3 = (LinearLayout) findViewById(R.id.outmInfo3);
                outm1.setVisibility(View.GONE);
                outm2.setVisibility(View.GONE);
                outm3.setVisibility(View.GONE);
            }else if(out_flights.length()==2){
                LinearLayout outm2 = (LinearLayout) findViewById(R.id.outmInfo2);
                LinearLayout outm3 = (LinearLayout) findViewById(R.id.outmInfo3);
                outm2.setVisibility(View.GONE);
                outm3.setVisibility(View.GONE);
            }else if(out_flights.length()==3) {
                LinearLayout outm3 = (LinearLayout) findViewById(R.id.outmInfo3);
                outm3.setVisibility(View.GONE);
            }
            if(out_flights.length()!=1) {
                for (int z = 1; z < out_flights.length() ; z++) {
                    ViewHolder InsideHolder = new ViewHolder() ;
                    if (z == 1) {
                        InsideHolder.mPortCode = (TextView) findViewById(R.id.outm1PortCode);
                        InsideHolder.mPortName = (TextView) findViewById(R.id.outm1PortName);
                        InsideHolder.mAirline = (TextView) findViewById(R.id.outm1Airline);
                        InsideHolder.mDuration = (TextView) findViewById(R.id.outm1Duration);
                    } else if (z == 2) {
                        InsideHolder.mPortCode = (TextView) findViewById(R.id.outm2Portcode);
                        InsideHolder.mPortName = (TextView) findViewById(R.id.outm2PortName);
                        InsideHolder.mAirline = (TextView) findViewById(R.id.outm2Airline);
                        InsideHolder.mDuration = (TextView) findViewById(R.id.outm2Duration);
                    } else if (z == 3) {
                        InsideHolder.mPortCode = (TextView) findViewById(R.id.outm3PortCode);
                        InsideHolder.mPortName = (TextView) findViewById(R.id.outm3Portname);
                        InsideHolder.mAirline = (TextView) findViewById(R.id.outm3Airline);
                        InsideHolder.mDuration = (TextView) findViewById(R.id.outm3Duration);
                    }
                    out_flight=out_flights.getJSONObject(z);
                    originCode=out_flight.getJSONObject("origin").getString("airport");
                    InsideHolder.mPortCode.setText(originCode);
                    String[] temp2 = CallingAPIs(originCode,0).split("[|]");
                    InsideHolder.mPortName.setText(temp2[0]);
                    InsideHolder.mAirline.setText(CallingAPIs(out_flight.getString("operating_airline"),1));
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    try {
                        Date departs_date = sdf2.parse(out_flights.getJSONObject(z-1).getString("arrives_at"));
                        Date arrives_date = sdf2.parse(out_flights.getJSONObject(z).getString("departs_at"));
                        String diffMins = TimeDifferenceToString(departs_date.getTime(),arrives_date.getTime());
                        InsideHolder.mDuration.setText(getString(R.string.standbyDuration)+" "+diffMins);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            out_flight = out_flights.getJSONObject(out_flights.length()-1);

            String destCode = out_flight.getJSONObject("destination").getString("airport");
            outboundHolder.aPortCode = (TextView) findViewById(R.id.outaPortCode);
            outboundHolder.aPortCode.setText(destCode);

            outboundHolder.aPortName = (TextView) findViewById(R.id.outaPortName);
            temp = CallingAPIs(destCode,0).split("[|]");
            outboundHolder.aPortName.setText(temp[0]);
            outboundHolder.journeyView.setText(outboundHolder.journeyView.getText()+temp[1]);

            outboundHolder.aAirline = (TextView) findViewById(R.id.outaAirline);
            outboundHolder.aAirline.setText(CallingAPIs(out_flight.getString("operating_airline"),1));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static class ViewHolder{
        //journeyInfo
        TextView journeyView;
        TextView journeyDuration;
        TextView stopView;
        //DepartInfo
        TextView dDate;
        TextView dPortCode;
        TextView dPortName;
        TextView dAirline;
        //MiddleInfo1
        TextView mPortCode;
        TextView mPortName;
        TextView mAirline;
        TextView mDuration;
        //ArriveInfo
        TextView aDate;
        TextView aPortCode;
        TextView aPortName;
        TextView aAirline;
    }

    public String CallingAPIs(final String code,final int action){
        try {
             return new AsyncTask<Void, Void, String>() {
                private Exception exception;

                @Override
                protected void onPreExecute() {
                }

                protected String doInBackground(Void... urls) {
                    try {
                        String link="https://api.sandbox.amadeus.com/v1.2/location/"+code+"?apikey="+getString(R.string.AMADEUS_API_KEY);
                        if(action==1)
                            link= "https://iatacodes.org/api/v6/airlines?api_key="+getString(R.string.IATA_API_KEY)+"&code="+code;
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

                            try {
                                // parse the json result returned from the service
                                JSONObject res = new JSONObject(stringBuilder.toString());
                                if(action==1){
                                    JSONArray responseArray = (JSONArray) res.get("response");
                                    return responseArray.getJSONObject(0).getString("name");
                                }else {
                                    JSONArray jsonResult = res.getJSONArray("airports");
                                    JSONObject airport = jsonResult.getJSONObject(0);
                                    return airport.getString("name")+"|"+airport.getString("city_name");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return stringBuilder.toString();
                        } finally {
                            urlConnection.disconnect();
                        }
                    } catch (Exception e) {
                        return null;
                    }
                }

            }.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;

    }

    public String TimeDifferenceToString(long arrives,long departs){
        long milliseconds = Math.abs(arrives-departs);
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
        int days  = (int) ((milliseconds / (1000*60*60*24)));

        String result = "";
        if(days>0){
            if(days==1)
                result+=days+getString(R.string.day);
            else
                result+=days+getString(R.string.days);
        }
        if(hours>0){
            if(hours==1)
                result+=hours+getString(R.string.hour);
            else
                result+=hours+getString(R.string.hours);
        }
        if(minutes>0){
            if(minutes==1)
                result+=minutes+getString(R.string.minute);
            else
                result+=minutes+getString(R.string.minutes);
        }
        System.out.println("Seconds: "+seconds);
        System.out.println("Minutes: "+minutes);
        System.out.println("Hours: "+hours);
        System.out.println("Days: "+days);
        return result;
    }
}
