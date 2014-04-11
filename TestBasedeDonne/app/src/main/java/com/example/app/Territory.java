package com.example.app;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Administrateur on 11/04/14.
 */
public class Territory {
    private DataBaseHelper  myDbHelper;
    private Context         myContext;

    public Territory(Context context) {
        myContext = context;
        this.loadData();
    }

    public HashMap<String, Stop>   getListStopByDistance(double distance, CoordinateGPS position) {
        Date d = new Date();

        Log.d("galasky", "Available : " + myDbHelper.isServiceAvailable("16", d));
        return myDbHelper.getHashMapStopsByDistance(distance, position);
    }

    public Stop getStopById(String id) {
        return myDbHelper.getStopById(id);
    }



    public Path getPath(Date today, String StopId) {
        Path path = new Path();

        return path;
    }

    private void loadData() {
        myDbHelper = new DataBaseHelper(myContext);
        try {
            myDbHelper.createDataBase();

        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {

            myDbHelper.openDataBase();

        }catch(SQLException sqle){

        }
    }
}
