package com.me.mygdxgame;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Game3D implements ApplicationListener {
	private MyCamera				_cam;
    public CameraInputController	camController;
    public ModelBatch 				modelBatch;
    public AssetManager 			assets;
    private List<Stop> 				_listStop;
    private You						_you;
    public double					distance;
    public Array<ModelInstance>		instances;
    public Environment 				environment;
    public boolean 					loading;
    private Territory 				territory;
    
    private Game3D() {
        territory = Territory.instance();
        _you = You.instance();
        instances = new Array<ModelInstance>();
        _listStop = null;
        distance = 1;
    }
    
	public static Game3D instance() {
        return SingletonHolder.instance;
    }
	
	private static class SingletonHolder {
        /** Instance unique non préinitialisée */
        private final static Game3D instance = new Game3D();
    }
	
    @Override
    public void create () {
    	_cam = MyCamera.instance();
    	setListStop();
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
 
        DetecteurGeste monDetecteurGeste = new DetecteurGeste();
		Gdx.input.setInputProcessor(new GestureDetector (monDetecteurGeste));
         
        assets = new AssetManager();
        assets.load("data/ship/ship.obj", Model.class);
        loading = true;
    }
 
    private void doneLoading() {
    	_you.loadModel(assets, "data/ship/ship.obj");
    	loadInstances();
        loading = false;
    }
    
    public void loadInstances() {
    	instances.clear();
    	instances.add(_you.modelInstance);
    	if (_listStop != null)
    	{
    		Vector3 decal = new Vector3();
        	float _zoom = 5000;
        	Stop stop = null;
        	Iterator<Stop> i = _listStop.iterator();
        	int x = 0;
        	ModelBuilder modelBuilder = new ModelBuilder();
        	while(i.hasNext())
        	{
        		stop = (Stop) i.next();
        		decal.z = (float) ((stop.coord.latitude - _you.coordinate.latitude));
                decal.x = (float) ((stop.coord.longitude - _you.coordinate.longitude));
                decal.z *= _zoom;
    	        decal.x *= _zoom;
    	        Model model;
    	        ModelInstance instance;
    	        
    	        model = modelBuilder.createBox(.5f, 1f, .5f, 
    	        new Material(ColorAttribute.createDiffuse((x == 4 ? Color.GREEN : Color.RED))),
    	        Usage.Position | Usage.Normal);
    	        instance = new ModelInstance(model);
    	        instance.transform.setToTranslation(decal.x, 0, decal.z);
    	        instances.add(instance);
    	        x++;
        	}
    	}
    }
    
    public void setListStop() {
    	_listStop = territory.getListStopByDistance(distance, _you.coordinate);
    }
    
    @Override
    public void render () {
        if (assets.update() && loading)
            doneLoading();
        if (loading == false)
        	_cam.update();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
 
        modelBatch.begin(_cam.pCam);
        modelBatch.render(instances, environment);
        modelBatch.end();
    }
     
    @Override
    public void dispose () {
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }
 
    public void resume () {
    }
 
    public void resize (int width, int height) {
    }

    public void pause () {
    }
}