package com.example.app;

import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

public class MainActivity extends ActionBarActivity {

    private Territory territory = new Territory(this);
    private Button button;
    private HashMap<String, Stop> hashMapStops;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    //distance in Km
    public void listStop(View v) {
       // double distance = (EditText) findViewById(R.id.distance);
        double time = System.currentTimeMillis();
        hashMapStops = territory.getListStopByDistance(5, new CoordinateGPS(48.09275716032735, -1.64794921875));
        TextView text = (TextView) findViewById(R.id.text);
        Iterator i = hashMapStops.entrySet().iterator();
        Stop stop = null;
        String str = new String();
        str += "Temps de la requête : " + (System.currentTimeMillis() - time) + " ms\n";
        str += "Nombre d'arrêts : " + hashMapStops.entrySet().size() + "\n\n";
        /*while(i.hasNext()){
            stop = (Stops) i.next();
            str += "Latitude : " + stop.getCoord().latitude + " Longitude : " + stop.getCoord().longitude + "\n";
        }*/
        text.setText(str);
    }

    public void infoStop(View w) {
        EditText dist = (EditText) findViewById(R.id.distance);
        Stop stop = territory.getStopById(dist.getText().toString());
        TextView text = (TextView) findViewById(R.id.text);
        String str = new String();
        str += "Stop info : \n";
        str += "Nom de l'arret : " + stop.stop_name + "\n";
        str += "Description : " + stop.stop_desc + "\n";
        str += "latitude : " + stop.stop_lat + "\n";
        str += "lontitide : " + stop.stop_lon + "\n";
        str += "list trip :\n";
        Iterator<String> i = stop.list_trip.iterator();
        while (i.hasNext()) {
            String trip = i.next();
            str += "\t\t" + trip + "\n";
        }
        text.setText(str);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
