package com.me.mygdxgame;

import java.util.Iterator;
import java.util.List;

import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Game3D implements ApplicationListener {
	private MyCamera				_cam;
    public CameraInputController	camController;
    private ModelBuilder modelBuilder;
    public ModelBatch 				modelBatch;
    private List<Stop> 				_listStop;
    private You						_you;
    private SpriteBatch 			spriteBatch;
    public double					distance;
    public Array<ModelInstance>		instances;
    public Array<ModelInstance>		Stopinstances;
    public Environment 				environment;
    public boolean 					loading;
    private Territory 				territory;
    private BitmapFont 				_font;
    private long diff, start = System.currentTimeMillis();
    
    private Game3D() {
        territory = Territory.instance();
        _you = You.instance();
        Stopinstances = new Array<ModelInstance>();
        instances = new Array<ModelInstance>();
        _listStop = null;
        distance = 0.5;
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
    	modelBuilder = new ModelBuilder();
    	_font = new BitmapFont();
    	_font.setColor(Color.GREEN);
        _font.setScale(3F, 3F);
        spriteBatch = new SpriteBatch();
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
 
        DetecteurGeste monDetecteurGeste = new DetecteurGeste();
		Gdx.input.setInputProcessor(new GestureDetector (monDetecteurGeste));

        loading = true;
    }
 
    private void doneLoading() {
    	Model model;
        ModelInstance instance;
        Texture floor = new Texture("data/radar.png");
      	model = modelBuilder.createBox(50f, .5f, 50f, 
      	new Material(TextureAttribute.createDiffuse(floor)), Usage.Position | Usage.Normal | Usage.TextureCoordinates);
      	instance = new ModelInstance(model);
      	instance.transform.setToTranslation(0, -1f, 0);
      	instances.add(instance);
      	_you.modelInstance = instance;
        loading = false;
    }
    
    public void loadStopInstances() 
    {
     	if (_listStop != null)
    	{
    		Vector3 decal = new Vector3();
        	Stop stop = null;
        	Iterator<Stop> i = _listStop.iterator();
        	Model model;
            ModelInstance instance;
    		
   		 	model = modelBuilder.createBox(.05f, .5f, .05f, 
   		 	new Material(ColorAttribute.createDiffuse(Color.RED)),
   		 	Usage.Position | Usage.Normal);
   		 
        	while(i.hasNext())
        	{
        		stop = (Stop) i.next();
        		
        		decal.x = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(stop.coord.latitude, _you.coordinate.longitude)) * 10 * (stop.coord.latitude > _you.coordinate.latitude ? 1 : -1);
        		decal.z = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(_you.coordinate.latitude, stop.coord.longitude)) * 10 *(stop.coord.longitude > _you.coordinate.longitude ? 1 : -1);

             	instance = new ModelInstance(model);
             	        
    	        instance.transform.setToTranslation(decal.x, .5f, decal.z);
    	        instances.add(instance);
        	}   	
    	}
    }
    
    public void loadInstances() {
    	instances.clear();
    	instances.add(_you.modelInstance);
    }
    
    public void setListStop() {
    	_listStop = territory.getListStopByDistance(distance, _you.coordinate);
    	loadStopInstances();
    }
    
    public void	touchScreen(float x, float y) {
    	//MyCamera.instance().pCam.p
    }
    
    public void sleep(int fps) {
        if(fps>0){
          diff = System.currentTimeMillis() - start;
          long targetDelay = 1000/fps;
          if (diff < targetDelay) {
            try{
                Thread.sleep(targetDelay - diff);
              } catch (InterruptedException e) {}
            }   
          start = System.currentTimeMillis();
        }
    }
    
    @Override
    public void render () {
        if (loading)
            doneLoading();


        
        _cam.update();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(_cam.pCam);
        modelBatch.render(instances, environment);
        //modelBatch.render(Stopinstances, environment);
        modelBatch.end();
        spriteBatch.begin();
        _font.draw(spriteBatch, "boussole  : " + _cam.getAngle(), 20, 500);
        //_font.draw(spriteBatch, "Math.abs(_angleFiltre - Math.PI * 2) == " + Math.abs(_cam.getAngle() - Math.PI * 2), 20, 400);
        //_font.draw(spriteBatch, "Math.abs(_angleFiltre + Math.PI * 2) == " + Math.abs(_cam.getAngle() + Math.PI * 2), 20, 300);
        if (_you.coordinate != null)
        {
            //_font.draw(spriteBatch, "latitude  : " + _you.coordinate.latitude, 20, 900);
           // _font.draw(spriteBatch, "longitude : " + _you.coordinate.longitude, 20, 800);
        }

        spriteBatch.end();
        
        sleep(60);
    }
     
    @Override
    public void dispose () {
        modelBatch.dispose();
        instances.clear();
    }
 
    public void resume () {
    }
 
    public void resize (int width, int height) {
    }

    public void pause () {
    }
}