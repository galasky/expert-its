package com.example.app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;
import android.widget.DatePicker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrateur on 11/04/14.
 */
public class Territory {
    private DataBaseHelper  myDbHelper;
    private Context         myContext;

    private Territory() {
    }

    public void setContext(Context context) {
        myContext = context;
        this.loadData();
    }

    public static Territory instance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder
    {
        /** Instance unique non préinitialisée */
        private final static Territory instance = new Territory();
    }

    public List<Stop> getListStopByDistance(double distance, CoordinateGPS position) {
        Cursor cursor = myDbHelper.execSQL("SELECT * FROM stops");

        List<Stop> listStops = new ArrayList<Stop>();
        Stop stop = null;
        if (cursor.moveToFirst()) {
            cursor.moveToNext();
            do {
                stop = new Stop();
                stop.setCoord(new Double(cursor.getString(4)), new Double(cursor.getString(5)));
                stop.stop_id = cursor.getString(0);
                // Add book to books
                if (distanceAB(stop.coord, position) < distance) {
                    listStops.add(stop);
                }
            } while (cursor.moveToNext());
        }
        return listStops;
    }

    public Stop getStopById(String id) {
        Cursor cursor = myDbHelper.execSQL("SELECT * FROM stops WHERE stop_id = " + id);

        Stop stop = new Stop();
        if (cursor.moveToFirst()) {
            stop.stop_id = cursor.getString(0);
            stop.stop_code = cursor.getString(1);
            stop.stop_name = cursor.getString(2);
            stop.stop_desc = cursor.getString(3);
            stop.coord.latitude = Double.parseDouble(cursor.getString(4));
            stop.coord.longitude = Double.parseDouble(cursor.getString(5));
            stop.zone_id = cursor.getString(6);
            stop.stop_url = cursor.getString(7);
            stop.location_type = cursor.getString(8);
            stop.parent_station = cursor.getString(9);
            stop.stop_timezone = cursor.getString(10);
            stop.wheelchair_boarding = cursor.getString(11);
        }
        return stop;
    }

    public int  serviceException(String idService, Date today) {
        Cursor cursor = myDbHelper.execSQL("SELECT * FROM calendar_dates WHERE service_id = " + idService);

        if (cursor.moveToFirst()) {
            String str = new String();
            int year, month, day;
            str = cursor.getString(1);
            year = Integer.valueOf(str.substring(0, 4));
            month = Integer.valueOf(str.substring(4, 6));
            day = Integer.valueOf(str.substring(6, 8));

            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            int year2 = cal.get(Calendar.YEAR);
            int month2 = cal.get(Calendar.MONTH) + 1;
            int day2 = cal.get(Calendar.DAY_OF_MONTH);
            if (year == year2 && month == month2 && day == day2)
                return Integer.valueOf(cursor.getString(2));
        }
        return 0;
    }

    private boolean dateExpired(String SBegin, String SEnd, Date date) {
        MyDate begin = new MyDate(SBegin);
        MyDate end = new MyDate(SEnd);
        MyDate current = new MyDate(date);

        if (current.isLowerThan(end) && current.isSuperiorThan(begin))
            return false;
        if (current.isSame(begin) || current.isSame(end))
            return false;
        return true;
    }

    public boolean isServiceAvailableByDate(String idService, Date date) {
        int type = serviceException(idService, date);
        switch (type) {
            case 1:
                return true;
            case 2:
                return false;
            default:
                break;
        }
        Cursor cursor = myDbHelper.execSQL("SELECT * FROM calendar WHERE service_id = " + idService);

        String available  = new String();
        if (cursor.moveToFirst()) {
            if (dateExpired(cursor.getString(8), cursor.getString(9), date))
                return false;


            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (day == 1) {
                available  = cursor.getString(7);
            } else {
                available  = cursor.getString(day - 1);
            }
        }
        return (available.equals("1"));
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

    // lat1, lat2, lon1, lon2 in degrees
    public double distanceAB(CoordinateGPS A, CoordinateGPS B) {
        double R = 6371; // km
        double dLat = Math.toRadians(B.latitude - A.latitude);
        double dLon = Math.toRadians(B.longitude - A.longitude);
        double lat1 = Math.toRadians(A.latitude);
        double lat2 = Math.toRadians(B.latitude);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d; // Km
    }
}
