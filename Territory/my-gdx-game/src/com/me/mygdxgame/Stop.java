package com.me.mygdxgame;

import java.util.List;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;

public class Stop {

	public Vector2	position;
	public boolean	select;
	public ModelInstance instance;
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

    public Stop(){
    	instance = null;
    	position = new Vector2();
    	select = false;
    }

    public void setCoord(CoordinateGPS c) {coord.longitude = c.longitude; coord.latitude = c.latitude;}

    public void setCoord(double lat, double lon) {coord.latitude = lat; coord.longitude = lon;}
}