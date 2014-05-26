package com.me.mygdxgame;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication implements LocationListener {

	private LocationManager lm;
	private LoadListStop _loadListStop;
	private TestThread	test;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	test = new TestThread();
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        
        //cfg.useGL20 = true;
        cfg.useCompass = true;
        Territory territory = Territory.instance();
        territory.setContext(this);
        _loadListStop = new LoadListStop();
        initialize(Game3D.instance(), cfg);
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0,
					this);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0,
				this);
	}

    @Override
	protected void onPause() {
		super.onPause();
		lm.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

		Log.d("ok", "galasky LOCATION");
		if (World.instance().loadListStop.loaded == false)
			World.instance().loadListStop.location = location;
		else
			You.instance().setPosition(location);
		Log.d("ok", "galasky END LOCATION");
//		You.instance().setPosition(location);
	}
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}