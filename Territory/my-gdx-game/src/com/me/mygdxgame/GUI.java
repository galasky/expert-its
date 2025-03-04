package com.me.mygdxgame;

import java.util.Date;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class GUI implements IGUI {
	private SpriteBatch			_spriteBatch;
	private BitmapFont			_font;
	private	World				_world;
	private OrthographicCamera camera;
	private SpriteBatch myBatch;
	private Vector3 			V;
	private double				k;
	private String				_str;
	private BubbleStop			_bubbleSelect;
	private ShapeRenderer shapeDebugger;
	private boolean				_initPosition;

	public GUI() {
		StationManager.instance().endDraw = true;
		shapeDebugger = new ShapeRenderer();
		_str = new String();
		_world = World.instance();
		myBatch = new SpriteBatch();
		_spriteBatch = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		_font = new BitmapFont();
    	_font.setColor(Color.GREEN);
        _font.setScale(3F, 3F);
        _bubbleSelect = null;
	   
        initPosition();
	}
	
	@Override
	public void touch(float x, float y, float deltaX, float deltaY)
	{
		//_str = "x = " + x + " y = " + y + " deltaX = " + deltaX + " deltaY = " + deltaY;
		if (_world.listBubbleStop == null)
			return ;
		Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
		BubbleStop bubbleStop = null;
		while (i.hasNext())
		{
			bubbleStop = i.next();
			if (bubbleStop.touch)
			{
				bubbleStop.move(deltaX, -deltaY);
				return;
			}
			if (bubbleStop.collision(x, Gdx.graphics.getHeight() - y))
			{
				bubbleStop.touch = true;
			}
		}
	}
	
	public void	initPosition() {
		_initPosition = true;
		if (_world.listBubbleStop == null)
			return ;
		Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
		BubbleStop bubbleStop = null;
		while (i.hasNext())
		{
			bubbleStop = i.next();
			bubbleStop.initPosition(null);
			bubbleStop.check();
		}
		_initPosition = false;
	}
	
	private void updateBubbleStop() {
		if (_world.listBubbleStop == null)
			return ;
		Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
		BubbleStop bubbleStop = null;
		while (i.hasNext())
		{
			bubbleStop = i.next();
			bubbleStop.update();
		}
	}
	
	private void renderAll() {
		if (StationManager.instance().getListStation() != null && _world.listBubbleStop != null)
		{
			StationManager.instance().endDraw = false;
			Date d = new Date();
			_spriteBatch.begin();
			Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
			BubbleStop bubbleStop = null;
			_font.setColor(Color.WHITE);
			while (i.hasNext())
			{
				bubbleStop = i.next();
				_font.draw(_spriteBatch, bubbleStop.station.name, bubbleStop.position.x, bubbleStop.position.y);
			}
			StationManager.instance().endDraw = true;
			_spriteBatch.end();
		}
	}
	
	private void renderSelect() {

		myBatch.begin();
		Gdx.gl20.glLineWidth(10);
		
		//Enable transparency
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		    
		shapeDebugger.setProjectionMatrix(camera.combined);
	    shapeDebugger.begin(ShapeType.Line);
	    shapeDebugger.setColor(0f, 0f, 0f, 0.80f);
	    shapeDebugger.end();
	    shapeDebugger.begin(ShapeType.Filled);
	    shapeDebugger.rect(_bubbleSelect.position.x - Gdx.graphics.getWidth() / 2 - 20, _bubbleSelect.position.y - Gdx.graphics.getHeight() / 2 + 20, 650,  -(_bubbleSelect.station.stops.size() + 1.5f)* _bubbleSelect.slide);
	    shapeDebugger.end();
	    myBatch.end();
	    
		myBatch.begin();
		Gdx.gl20.glLineWidth(20);
		
		//Enable transparency
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		    
		shapeDebugger.setProjectionMatrix(MyCamera.instance().pCam.combined);
	    shapeDebugger.begin(ShapeType.Line);
	    shapeDebugger.setColor(1f, 1f, 1f, 0.80f);
	    Vector3 A = new Vector3(_bubbleSelect.station.position.y, 0.8f, _bubbleSelect.station.position.x);
	    Vector3 B = MyCamera.instance().transorm(_bubbleSelect.position);
	    
	    shapeDebugger.line(A, B);
	    shapeDebugger.end();
	    myBatch.end();
	    
	    Gdx.gl.glDisable(GL20.GL_BLEND);
	    
		_spriteBatch.begin();
		Date d = new Date();
		_font.setColor(Color.WHITE);
		_font.draw(_spriteBatch, _bubbleSelect.station.name, _bubbleSelect.position.x, _bubbleSelect.position.y);
		_font.draw(_spriteBatch, "position ->  : viewportHeight " + MyCamera.instance().pCam.viewportHeight + " viewportWdith : " + MyCamera.instance().pCam.viewportWidth, 50, 50);
		Iterator<Stop> i = _bubbleSelect.station.stops.iterator();
		int nb = 0;
		while (i.hasNext())
		{
			Stop stop = i.next();
			nb++;
			if (stop.nextTime != null)
			{
				_font.draw(_spriteBatch, (int) (_bubbleSelect.station.distanceTemps * 60) + " min marche + " + stop.nextTime.diff(new Date()) + " min " + stop.nextTime.getString(), _bubbleSelect.position.x, _bubbleSelect.position.y - nb * _bubbleSelect.slide);
			}
			else
				_font.draw(_spriteBatch, "Service termin�e", _bubbleSelect.position.x, _bubbleSelect.position.y - nb * _bubbleSelect.slide);
		}
		_spriteBatch.end();
		
		if (_bubbleSelect.select == false && _bubbleSelect.slide <= 0)
			_bubbleSelect = null;
	}
	
	@Override
	public void render() {
		if (_initPosition == true)
			initPosition();
		updateBubbleStop();
		
		if (_bubbleSelect == null)
			renderAll();
		else
			renderSelect();

	}

	@Override
	public void tap(float x, float y) {
		if (_world.listBubbleStop == null)
			return ;
		Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
		BubbleStop bubbleStop = null;
		
		while (i.hasNext())
		{
			bubbleStop = i.next();
			if (_bubbleSelect == null && bubbleStop.collision(x, Gdx.graphics.getHeight() - y))
			{
				bubbleStop.select();
				_bubbleSelect = bubbleStop;
				return ;
			}
		}
		if (_bubbleSelect != null)
		{
			_bubbleSelect.unSelect();
		}
	}

	@Override
	public IGUI invert() {
		return new GUI2();
	}

	@Override
	public void refresh() {
		
	}
}
