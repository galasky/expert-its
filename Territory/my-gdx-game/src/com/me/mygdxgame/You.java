package com.me.mygdxgame;

import android.location.Location;
import android.util.Log;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;


public class You {
	public ModelInstance 	modelInstance;
	public Vector3			position;
	private boolean 		_loaded;
	public CoordinateGPS	coordinate;
	public CoordinateGPS	start;
	
	private	You() {
		position = new Vector3(0, 0, 0);
		_loaded = false;
		coordinate = new CoordinateGPS(48.09275716032735, -1.64794921875);
		start = new CoordinateGPS(48.09275716032735, -1.64794921875);
	}
	
	public void loadModel(AssetManager assets, String path) {
		Model model = assets.get(path, Model.class);
    	modelInstance = new ModelInstance(model);
    	modelInstance.transform.setToTranslation(0, 0, 0);
    	modelInstance.transform.rotate(new Vector3(0, 1, 0), 180);
    	_loaded = true;
	}
	
	public boolean isLoaded() {
		return _loaded;
	}
	
	public void setPosition(Location location) {
		if (_loaded == false)
			return ;

		Vector3 p = new Vector3();
		p.x = (float) (location.getLongitude() - coordinate.longitude) * 5000;
		p.z = (float) (location.getLatitude() - coordinate.latitude) * 5000;
		coordinate.latitude = location.getLatitude();
		coordinate.longitude = location.getLongitude();
		/*Log.d("galasky", "galasky Px " + p.x);
		Log.d("galasky", "galasky Pz " + p.z);
		Log.d("galasky", "galasky ");*/
		modelInstance.transform.setTranslation(p);
		position.x = p.x;
		position.y = p.y;
		position.z = p.z;
	}
	
	public static You instance() {
        return SingletonHolder.instance;
    }
	
	private static class SingletonHolder {
        /** Instance unique non préinitialisée */
        private final static You instance = new You();
    }
}
