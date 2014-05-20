package com.me.mygdxgame;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Plate {
	private	World					_world;
    private MyCamera				_cam;
    public int						nbStopSelect;
    private Territory 				territory;
    public double					distance;
    private You						_you;
    public Array<ModelInstance>		Stopinstances;
    private Environment				_environment;
    
    public Plate (Environment environment)
    {
    	_world = World.instance();
    	_cam = MyCamera.instance();
    	_environment = environment;
    	_you = You.instance();
    	territory = Territory.instance();

        distance = 0.5;
        Stopinstances = new Array<ModelInstance>();
        Game3D.instance().instances = new Array<ModelInstance>();
    	_you.load();
    	Game3D.instance().instances.add(_you.modelInstance);
        nbStopSelect = 0;
    }
    
    public void update() {
    	//_you.setRotation(_cam.getAngle());
    	if (_world.listStop == null)
    		return ;
    }
    
    public void setListStop() {
    	_world.listStop = territory.getListStopByDistance(distance, _you.coordinate);
    	loadStopInstances();
    }
    
    public void loadStopInstances() 
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
