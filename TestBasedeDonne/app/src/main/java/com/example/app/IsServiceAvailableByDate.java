package com.example.app;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class IsServiceAvailableByDate extends ActionBarActivity {

    Territory  territory = Territory.instance();
    DatePicker d = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_service_available_by_date);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    public void resetButtonColor(View v) {
        Button btn = (Button) findViewById(R.id.available_button);
        btn.setBackground(getResources().getDrawable(R.drawable.apptheme_btn_default_holo_light));
    }

    private DatePicker.OnDateChangedListener dateSetListener = new DatePicker.OnDateChangedListener() {

        public void onDateChanged(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
            if ((((EditText) findViewById(R.id.idService)).getText().toString()).equals(""))
            {
                return;
            }
            String idService = ((EditText) findViewById(R.id.idService)).getText().toString();

            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DATE, dayOfMonth);
            cal.set(Calendar.YEAR, year);
            date = cal.getTime();
            System.out.println(date);
            Button btn = (Button) findViewById(R.id.available_button);
            if (territory.isServiceAvailableByDate(idService, date)) {
                btn.setBackgroundColor(Color.GREEN);
            } else {
                btn.setBackgroundColor(Color.RED);
            }
        }};

    public void isServiceAvailableByDate(View v) {
        if ((((EditText) findViewById(R.id.idService)).getText().toString()).equals(""))
        {
            return;
        }
        String idService = ((EditText) findViewById(R.id.idService)).getText().toString();
        int day = ((DatePicker) findViewById(R.id.datePicker)).getDayOfMonth();
        int month = ((DatePicker) findViewById(R.id.datePicker)).getMonth();
        int year = ((DatePicker) findViewById(R.id.datePicker)).getYear();
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.YEAR, year);
        date = cal.getTime();
        System.out.println(date);
        Button btn = (Button) findViewById(R.id.available_button);
        if (territory.isServiceAvailableByDate(idService, date)) {
            btn.setBackgroundColor(Color.GREEN);
        } else {
            btn.setBackgroundColor(Color.RED);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.is_service_available_by_date, menu);
        DatePicker d = (DatePicker) findViewById(R.id.datePicker);
        Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        d.init(year, month, day, dateSetListener);
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
            View rootView = inflater.inflate(R.layout.fragment_is_service_available_by_date, container, false);

            return rootView;
        }
    }

}
