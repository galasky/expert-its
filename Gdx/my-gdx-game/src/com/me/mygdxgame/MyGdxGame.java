package com.me.mygdxgame;

import java.util.List;

import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;

public class MyGdxGame implements ApplicationListener {
	//private OrthographicCamera camera;
	private Territory territory;
	private List<Stop> listStop;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	
	ShapeRenderer shapeDebugger;
	CoordinateGPS me;
	private float w;
	private float h;
	private int F;
	private DrawStop drawStop;
	private double raport;
	
	@Override
	public void create() {

		DetecteurGeste monDetecteurGeste = new DetecteurGeste();
		Gdx.input.setInputProcessor(new GestureDetector (monDetecteurGeste));
		
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		F = 100000;
		me = new CoordinateGPS(48.09275716032735, -1.64794921875);
		territory = Territory.instance();
		listStop = territory.getListStopByDistance(1, me);
		drawStop = DrawStop.instance();
		drawStop.setCenter(w / 2,  h / 2);
		drawStop.setZoom(100000);
		double gps, km;
		
		km = territory.distanceAB(me, listStop.get(0).coord);
		Log.d("galaksy", "galasky distance km = " + km);
		double X, Y, x;
		X = me.latitude - listStop.get(0).coord.latitude;
		Y = me.longitude - listStop.get(0).coord.longitude;
		gps = Math.sqrt(X * X + Y * Y);
		x = gps / km;
		raport = 1 / x;
		Log.d("galaksy", "galasky distance gps = " + gps);
		Log.d("galaksy", "galasky rapport = " + raport);
		//camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();
		
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
		drawStop.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
        //batch.draw(texture, 0, 0);
        drawStop.draw(batch, listStop, me);
        batch.end();
        drawStop.update();
        // Log.d("galasky", "galasky " + Gdx.input.getAccelerometerX());
	}
	
	private void calcRaport(Stop stop) {
		double pxl, km;
		
		km = territory.distanceAB(me, listStop.get(0).coord);
		double X, Y;
		X = me.latitude - stop.coord.latitude;
		Y = me.longitude - stop.coord.longitude;
		pxl = Math.sqrt(X * X + Y * Y);
		raport = pxl / km;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
