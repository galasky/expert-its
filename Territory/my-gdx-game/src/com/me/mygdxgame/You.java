package com.me.mygdxgame;

import android.location.Location;
import android.util.Log;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;


public class You {
	public ModelInstance 	modelInstance;
	public Vector3			position;
	private boolean 		_loaded;
	public CoordinateGPS	coordinate;
	private float			_angle;
	private ModelBuilder 	modelBuilder;
	public CoordinateGPS	start;
	
	private	You() {
		modelBuilder = new ModelBuilder();
		position = new Vector3(0, 0, 0);
		_loaded = true;
		coordinate = null;
		start = null;
		_angle = 0f;
	}
	
	public void setRotation(float angle) {
		modelInstance.transform.setToRotation(new Vector3(0, 1, 0), (float) (-180 * angle / Math.PI));
		_angle = angle;
	}
	
	public void load() {
    	Model model;
        
        Texture floor = new Texture("data/radar.png");
      	model = modelBuilder.createBox(50f, .5f, 50f, 
      	new Material(TextureAttribute.createDiffuse(floor)), Usage.Position | Usage.Normal | Usage.TextureCoordinates);
      	modelInstance = new ModelInstance(model);
      	modelInstance.transform.setToTranslation(0, -1f, 0);
      	_loaded = true;
	}
	
	public void loadModel(AssetManager assets, String path) {
		Log.d("ok", "galasky LOAD MODEL");
		Model model = assets.get(path, Model.class);
    	modelInstance = new ModelInstance(model);
    	modelInstance.transform.setToTranslation(0, 0, 0);
    	modelInstance.transform.rotate(new Vector3(0, 1, 0), 29.1F);
    	_loaded = true;
	}
	
	public boolean isLoaded() {
		return _loaded;
	}
	
	public void setPosition(Location location) {
		if (_loaded == false || modelInstance == null)
			return ;

		if (start == null)
		{
			start = new CoordinateGPS(location.getLatitude(), location.getLongitude());
			coordinate = new CoordinateGPS(start.latitude, start.longitude);
			Game3D.instance().loadPlate();
		}
		
		Vector3 p = new Vector3();
		p.x = (float) Territory.distanceAB(start, new CoordinateGPS(location.getLatitude(), start.longitude)) * 10 * (location.getLatitude() > start.latitude ? 1 : -1);
		p.z = (float) Territory.distanceAB(start, new CoordinateGPS(start.latitude, location.getLongitude())) * 10 *(location.getLongitude() > start.longitude ? 1 : -1);
		
		//p.x = (float) (location.getLongitude() - coordinate.longitude) * 5000;
		//p.z = (float) (location.getLatitude() - coordinate.latitude) * 5000;
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
