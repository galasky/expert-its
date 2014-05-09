package com.me.mygdxgame;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
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
    public ModelBatch 				modelBatch;
    public AssetManager 			assets;
    private List<Stop> 				_listStop;
    private You						_you;
    private SpriteBatch spriteBatch;
    public double					distance;
    public Array<ModelInstance>		instances;
    public Environment 				environment;
    public boolean 					loading;
    private Territory 				territory;
    private BitmapFont 				_font;
    
    private Game3D() {
        territory = Territory.instance();
        _you = You.instance();
        instances = new Array<ModelInstance>();
        _listStop = null;
        distance = 2;
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
    	//setListStop();
    	spriteBatch = new SpriteBatch();
    	_font = new BitmapFont();
    	_font.setColor(Color.GREEN);
        _font.setScale(3F, 3F);
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
        	Model model;
	        ModelInstance instance;
        	while(i.hasNext())
        	{
        		stop = (Stop) i.next();
        		
        		decal.x = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(stop.coord.latitude, _you.coordinate.longitude)) * 10 * (stop.coord.latitude > _you.coordinate.latitude ? 1 : -1);
        		decal.z = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(_you.coordinate.latitude, stop.coord.longitude)) * 10 *(stop.coord.longitude > _you.coordinate.longitude ? 1 : -1);
        		
    	        model = modelBuilder.createBox(.05f, .5f, .05f, 
    	        new Material(ColorAttribute.createDiffuse((x == 4 ? Color.GREEN : Color.RED))),
    	        Usage.Position | Usage.Normal);
    	        instance = new ModelInstance(model);
    	        instance.transform.setToTranslation(decal.x, 0, decal.z);
    	        instances.add(instance);
    	        x++;
        	}
        	Texture t = new Texture("data/test.png");
        	model = modelBuilder.createBox(50, 0.1F, 50,
        	new Material(), Usage.Position | Usage.Normal | Usage.TextureCoordinates);
        	instance = new ModelInstance(model);
        	instance.materials.first().set(TextureAttribute.createDiffuse(t));
        	//instance.materials.first().set(ColorAttribute.createDiffuse(Color.RED));
        	instances.add(instance);
        	
    	}
    }
    
    public void setListStop() {
    	_listStop = territory.getListStopByDistance(distance, _you.coordinate);
    	loadInstances();
    }
    
    public void	touchScreen(float x, float y) {
    	//MyCamera.instance().pCam.p
    }
    
    @Override
    public void render () {
        if (loading && assets.update())
            doneLoading();
        //if (loading == false)
        	_cam.update();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
 
        modelBatch.begin(_cam.pCam);
        modelBatch.render(instances, environment);
        modelBatch.end();
        spriteBatch.begin();
        //_font.draw(spriteBatch, "boussole  : " + _cam.getAngle(), 20, 500);
        //_font.draw(spriteBatch, "Math.abs(_angleFiltre - Math.PI * 2) == " + Math.abs(_cam.getAngle() - Math.PI * 2), 20, 400);
        //_font.draw(spriteBatch, "Math.abs(_angleFiltre + Math.PI * 2) == " + Math.abs(_cam.getAngle() + Math.PI * 2), 20, 300);
        if (_you.coordinate != null)
        {
            //_font.draw(spriteBatch, "latitude  : " + _you.coordinate.latitude, 20, 900);
           // _font.draw(spriteBatch, "longitude : " + _you.coordinate.longitude, 20, 800);
        }

        spriteBatch.end();
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