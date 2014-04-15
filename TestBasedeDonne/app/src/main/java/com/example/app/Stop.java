package com.example.app;

import java.util.List;

public class Stop {

    public String stop_id;
    public String stop_code;
    public String stop_name;
    public String stop_desc;
    public String zone_id;
    public String stop_url;
    public String location_type;
    public String parent_station;
    public String stop_timezone;
    public String wheelchair_boarding;
    public List<String> list_trip;

    public CoordinateGPS coord = new CoordinateGPS();

    public Stop(){}

    public void setCoord(CoordinateGPS c) {coord.longitude = c.longitude; coord.latitude = c.latitude;}

    public void setCoord(double lat, double lon) {coord.latitude = lat; coord.longitude = lon;}
}