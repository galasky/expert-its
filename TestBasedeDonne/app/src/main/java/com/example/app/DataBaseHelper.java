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

    public Cursor execSQL(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(query, null);
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

    public int  serviceException(String idService, Date today) {
        String available  = new String();

        String query = "SELECT * FROM calendar_dates WHERE service_id = " + idService;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            String str = new String();

            int year, month, day;

            str = cursor.getString(1);
            Log.d("galasky", str);
            year = Integer.valueOf(str.substring(0, 4));
            Log.d("galasky", year +"");
            month = Integer.valueOf(str.substring(2, 2));
            Log.d("galasky", month+"");
            day = Integer.valueOf(str.substring(0, 2));
            Log.d("galasky", year + "/" + month + "/" + day);

            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            int year2 = cal.get(Calendar.YEAR);
            int month2 = cal.get(Calendar.MONTH);
            int day2 = cal.get(Calendar.DAY_OF_WEEK);
            if (year == year2 && month == month2 && day == day2)
                return Integer.valueOf(cursor.getString(2));
        }
        return 0;
    }

    public boolean isServiceAvailableByDate(String idService, Date today) {
        switch (serviceException("2", today)) {
            case 1:
                return true;
            case 2:
                return false;
        }
        return isServiceAvailableAll(idService, today);
    }
    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

}
