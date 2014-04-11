package com.example.app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.example.app/databases/";

    private static String DB_NAME = "utagtfs.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    public boolean dbExist = false;
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Stop getStopById(String id) {
        String query = "SELECT * FROM stops WHERE stop_id = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Stop stop = new Stop();
        if (cursor.moveToFirst()) {
            stop.stop_id = cursor.getString(0);
            stop.stop_code = cursor.getString(1);
            stop.stop_name = cursor.getString(2);
            stop.stop_desc = cursor.getString(3);
            stop.stop_lat = cursor.getString(4);
            stop.stop_lon = cursor.getString(5);
            stop.zone_id = cursor.getString(6);
            stop.stop_url = cursor.getString(7);
            stop.location_type = cursor.getString(8);
            stop.parent_station = cursor.getString(9);
            stop.stop_timezone = cursor.getString(10);
            stop.wheelchair_boarding = cursor.getString(11);
        }
        return stop;
    }

    public HashMap<String, Stop> getHashMapStopsByDistance(double distance, CoordinateGPS myPosition) {
        HashMap<String, Stop> listStops = new HashMap<String, Stop>();

        String query = "SELECT * FROM " + "stops";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Stop stop = null;
        if (cursor.moveToFirst()) {
            cursor.moveToNext();
            do {
                stop = new Stop();
                stop.setCoord(new Double(cursor.getString(4)), new Double(cursor.getString(5)));
                stop.stop_id = cursor.getString(0);
                // Add book to books
                if (distanceAB(stop.coord, myPosition) < distance) {
                    listStops.put(stop.stop_id, stop);
                }
            } while (cursor.moveToNext());
        }
        return listStops;
    }

    private boolean isServiceAvailableAll(String idService, Date today) {
        String available  = new String();

        String query = "SELECT * FROM calendar WHERE service_id = " + idService;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
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

    public boolean isServiceAvailable(String idService, Date today) {
        return isServiceAvailableAll(idService, today);
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
    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

}
