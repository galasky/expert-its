package com.me.mygdxgame;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;


public class LoadListStop extends Thread {
	private World		_world;
	private Territory	_territory;
	private	You			_you;
	private Config		_config;
	
	public LoadListStop() {
		_world = World.instance();
		_territory = Territory.instance();
		_you = You.instance();
		_config = Config.instance();
	}
	public void run() {
		_world.listStop = _territory.getListStopByDistance(_config.distance, _you.coordinate);
    	loadStopInstances();
	}
	
    private void loadStopInstances() 
    {
     	if (_world.listStop != null)
    	{
    		Vector3 decal = new Vector3();
        	Stop stop = null;
        	Iterator<Stop> i = _world.listStop.iterator();
        	Model model;
            ModelInstance instance;
    		
   		 	model = Game3D.instance().modelBuilder.createBox(.05f, .5f, .05f, 
   		 	new Material(ColorAttribute.createDiffuse(Color.RED)),
   		 	Usage.Position | Usage.Normal);
   		 
        	while(i.hasNext())
        	{
        		stop = (Stop) i.next();
        		
        		decal.x = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(stop.coord.latitude, _you.coordinate.longitude)) * 10 * (stop.coord.latitude > _you.coordinate.latitude ? 1 : -1);
        		decal.z = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(_you.coordinate.latitude, stop.coord.longitude)) * 10 *(stop.coord.longitude > _you.coordinate.longitude ? 1 : -1);

             	instance = new ModelInstance(model);
             	        
    	        instance.transform.setToTranslation(decal.x, .5f, decal.z);
    	        stop.position.x = decal.z;
    	        stop.position.y = decal.x;
    	        stop.instance = instance;
    	        Game3D.instance().instances.add(instance);
        	}   	
    	}
    }
}
