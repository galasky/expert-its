package com.example.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;

public class GetStopActivity extends ActionBarActivity {

    Territory territory = Territory.instance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_stop);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    public void next(View v) {
        Intent intent = new Intent(this, IsServiceAvailableByDate.class);
        startActivity(intent);
    }

    public void getStopById(View v) {
        if ((((EditText) findViewById(R.id.idStop)).getText().toString()).equals(""))
        {
            return;
        }
        Stop stop = territory.getStopById(((EditText) findViewById(R.id.idStop)).getText().toString());
        String str = new String();
        str += "Stop id : " + stop.stop_id + "\n";
        str += "Stop latitude : " + stop.coord.latitude + "\n";
        str += "Stop longitude : " + stop.coord.longitude + "\n";
        ((TextView) findViewById(R.id.TextInfoStop)).setText(str);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.get_stop, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_get_stop, container, false);
            return rootView;
        }
    }

}
