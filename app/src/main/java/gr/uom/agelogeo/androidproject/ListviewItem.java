package gr.uom.agelogeo.androidproject;

/**
 * Created by Admin on 8/12/2016.
 */

public class ListviewItem {
    String airline,price ;
    String outbound_origin,outbound_origin_time,outbound_destination,outbound_destination_time;
    String inbound_origin,inbound_origin_time,inbound_destination,inbound_destination_time;
    String inbound_travel_class,inbound_available,outbound_travel_class,outbound_available,out_stops,in_stops;
    int result_indicator,itinerary_indicator;

    public ListviewItem() {
    }

    public ListviewItem(String airline, String price, String outbound_origin, String outbound_origin_time, String outbound_destination, String outbound_destination_time, String inbound_origin, String inbound_origin_time, String inbound_destination, String inbound_destination_time, String inbound_travel_class, String inbound_available, String outbound_travel_class, String outbound_available, String out_stops, String in_stops, int result_indicator, int itinerary_indicator) {
        this.airline = airline;
        this.price = price;
        this.outbound_origin = outbound_origin;
        this.outbound_origin_time = outbound_origin_time;
        this.outbound_destination = outbound_destination;
        this.outbound_destination_time = outbound_destination_time;
        this.inbound_origin = inbound_origin;
        this.inbound_origin_time = inbound_origin_time;
        this.inbound_destination = inbound_destination;
        this.inbound_destination_time = inbound_destination_time;
        this.inbound_travel_class = inbound_travel_class;
        this.inbound_available = inbound_available;
        this.outbound_travel_class = outbound_travel_class;
        this.outbound_available = outbound_available;
        this.out_stops = out_stops;
        this.in_stops = in_stops;
        this.result_indicator = result_indicator;
        this.itinerary_indicator = itinerary_indicator;
    }

    public String getOut_stops() {
        return out_stops;
    }

    public void setOut_stops(String out_stops) {
        this.out_stops = out_stops;
    }

    public String getIn_stops() {
        return in_stops;
    }

    public void setIn_stops(String in_stops) {
        this.in_stops = in_stops;
    }

    public int getResult_indicator() {
        return result_indicator;
    }

    public void setResult_indicator(int result_indicator) {
        this.result_indicator = result_indicator;
    }

    public int getItinerary_indicator() {
        return itinerary_indicator;
    }

    public void setItinerary_indicator(int itinerary_indicator) {
        this.itinerary_indicator = itinerary_indicator;
    }

    public String getInbound_travel_class() {
        return inbound_travel_class;
    }

    public void setInbound_travel_class(String inbound_travel_class) {
        this.inbound_travel_class = inbound_travel_class;
    }

    public String getInbound_available() {
        return inbound_available;
    }

    public void setInbound_available(String inbound_available) {
        this.inbound_available = inbound_available;
    }

    public String getOutbound_travel_class() {
        return outbound_travel_class;
    }

    public void setOutbound_travel_class(String outbound_travel_class) {
        this.outbound_travel_class = outbound_travel_class;
    }

    public String getOutbound_available() {
        return outbound_available;
    }

    public void setOutbound_available(String outbound_available) {
        this.outbound_available = outbound_available;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOutbound_origin() {
        return outbound_origin;
    }

    public void setOutbound_origin(String outbound_origin) {
        this.outbound_origin = outbound_origin;
    }

    public String getOutbound_origin_time() {
        return outbound_origin_time;
    }

    public void setOutbound_origin_time(String outbound_origin_time) {
        this.outbound_origin_time = outbound_origin_time;
    }

    public String getOutbound_destination() {
        return outbound_destination;
    }

    public void setOutbound_destination(String outbound_destination) {
        this.outbound_destination = outbound_destination;
    }

    public String getOutbound_destination_time() {
        return outbound_destination_time;
    }

    public void setOutbound_destination_time(String outbound_destination_time) {
        this.outbound_destination_time = outbound_destination_time;
    }

    public String getInbound_origin() {
        return inbound_origin;
    }

    public void setInbound_origin(String inbound_origin) {
        this.inbound_origin = inbound_origin;
    }

    public String getInbound_origin_time() {
        return inbound_origin_time;
    }

    public void setInbound_origin_time(String inbound_origin_time) {
        this.inbound_origin_time = inbound_origin_time;
    }

    public String getInbound_destination() {
        return inbound_destination;
    }

    public void setInbound_destination(String inbound_destination) {
        this.inbound_destination = inbound_destination;
    }

    public String getInbound_destination_time() {
        return inbound_destination_time;
    }

    public void setInbound_destination_time(String inbound_destination_time) {
        this.inbound_destination_time = inbound_destination_time;
    }


}
