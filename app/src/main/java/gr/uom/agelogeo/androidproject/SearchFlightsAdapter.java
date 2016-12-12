package gr.uom.agelogeo.androidproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 8/12/2016.
 */


public class SearchFlightsAdapter extends ArrayAdapter<ListviewItem> {
    private boolean returnDateExists = false;
    public SearchFlightsAdapter(Context context, ArrayList<ListviewItem> Listrowdata,boolean returndate) {
        super(context, 0, Listrowdata);
        returnDateExists =returndate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ListviewItem listitem = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_row, parent, false);
        }

        // Lookup view for data population
        //Header layout
        LinearLayout header = (LinearLayout) convertView.findViewById(R.id.header_layout);
        TextView airline = (TextView) header.findViewById(R.id.airline);
        TextView price = (TextView) header.findViewById(R.id.price);
        TextView out_stop_counter = (TextView) convertView.findViewById(R.id.out_stop_c);
        TextView in_stop_counter = (TextView) convertView.findViewById(R.id.in_stop_c);
        //Header
        airline.setText(listitem.getAirline());
        price.setText(listitem.getPrice());
        out_stop_counter.setText(listitem.getOut_stops());
        in_stop_counter.setText(listitem.getIn_stops());

        //Outbound inflates Itinerary layout
        LinearLayout outbound = (LinearLayout) convertView.findViewById(R.id.itinerary_layout).findViewById(R.id.outbound_layout);
        LinearLayout outbound_bottom = (LinearLayout) convertView.findViewById(R.id.itinerary_layout).findViewById(R.id.outbound_bottom_layout);
        TextView out_origin = (TextView) outbound.findViewById(R.id.outbound_origin);
        TextView out_origin_time = (TextView) outbound.findViewById(R.id.outbound_origin_time);
        TextView out_destination = (TextView) outbound.findViewById(R.id.outbound_destination);
        TextView out_destination_time = (TextView) outbound.findViewById(R.id.outbound_destination_time);
        TextView out_travel_class = (TextView) outbound_bottom.findViewById(R.id.outbound_travel_class);
        TextView out_available = (TextView) outbound_bottom.findViewById(R.id.outbound_available);
        //Outbound
        out_origin.setText(listitem.getOutbound_origin());
        out_origin_time.setText(listitem.getOutbound_origin_time());
        out_destination.setText(listitem.getOutbound_destination());
        out_destination_time.setText(listitem.getOutbound_destination_time());
        out_travel_class.setText(listitem.getOutbound_travel_class());
        out_available.setText(listitem.getOutbound_available());

        LinearLayout inbound = (LinearLayout) convertView.findViewById(R.id.itinerary_layout).findViewById(R.id.inbound_layout);
        LinearLayout inbound_bottom = (LinearLayout) convertView.findViewById(R.id.itinerary_layout).findViewById(R.id.inbound_bottom_layout);
        if(returnDateExists) {
            //Inbound inflates Itinerary layout
            TextView in_origin = (TextView) inbound.findViewById(R.id.inbound_origin);
            TextView in_origin_time = (TextView) inbound.findViewById(R.id.inbound_origin_time);
            TextView in_destination = (TextView) inbound.findViewById(R.id.inbound_destination);
            TextView in_destination_time = (TextView) inbound.findViewById(R.id.inbound_destination_time);
            TextView in_travel_class = (TextView) inbound_bottom.findViewById(R.id.inbound_travel_class);
            TextView in_available = (TextView) inbound_bottom.findViewById(R.id.inbound_available);
            //Inbound
            in_origin.setText(listitem.getInbound_origin());
            in_origin_time.setText(listitem.getInbound_origin_time());
            in_destination.setText(listitem.getInbound_destination());
            in_destination_time.setText(listitem.getInbound_destination_time());
            in_travel_class.setText(listitem.getInbound_travel_class());
            in_available.setText(listitem.getInbound_available());
        }else {
            inbound.setVisibility(View.GONE);
            inbound_bottom.setVisibility(View.GONE);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
