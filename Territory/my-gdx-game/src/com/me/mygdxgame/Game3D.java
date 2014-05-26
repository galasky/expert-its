package com.me.mygdxgame;

import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.Array;

public class Game3D implements ApplicationListener {
	private MyCamera				_cam;
	private SkyBox					_skyBox;
	public ModelBuilder 			modelBuilder;
    private Plate					_plate;
    private	GUIController			_guiController;
    public CameraInputController	camController;
    private DirectionalLight		_light;
    public ModelBatch 				modelBatch;
    public Environment 				environment;
    public boolean 					loading;
    public Array<ModelInstance>		instances;
    private long 					diff, start;
    
    private Game3D() {
    	start = System.currentTimeMillis(); 
    }
    
	public static Game3D instance() {
        return SingletonHolder.instance;
    }
	
	private static class SingletonHolder {
        private final static Game3D instance = new Game3D();
    }
	
	public void loadSound() {
		SoundManager soundManager = SoundManager.instance();
		
		soundManager.load("pop.mp3");
	}
	
    @Override
    public void create () {
    	loadSound();
    	_cam = MyCamera.instance();
    	modelBuilder = new ModelBuilder();
    	modelBatch = new ModelBatch();
    	_light = new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, -1f, 0f);
    	environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(_light);
        _skyBox = new SkyBox(environment);
        _plate = new Plate(environment);
        _guiController = new GUIController();
        DetecteurGeste monDetecteurGeste = new DetecteurGeste();
		Gdx.input.setInputProcessor(new GestureDetector (monDetecteurGeste));
    }
 
    public void loadPlate() {
    	_plate.setListStop();
    }
    
    public void longPress()
    {
    	_guiController.invert();
    }

    public void	touchScreen(float x, float y, float deltaX, float deltaY) {
    	_guiController.touch(x, y, deltaX, deltaY);
    }
    
    public void tap(float x, float y)
    {
    	_guiController.tap(x, y);
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
        _cam.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        _skyBox.render();
        _plate.update();
    	modelBatch.begin(_cam.pCam);
        modelBatch.render(instances);
        modelBatch.end();
        _guiController.render();
        sleep(60);
    }
     
    @Override
    public void dispose () {
    	modelBatch.dispose();
    }
 
    public void resume () {
    }
 
    public void resize (int width, int height) {
    }

    public void pause () {
    }
}