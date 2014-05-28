package com.me.mygdxgame;

import java.util.Date;
import java.util.Iterator;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GUI implements IGUI {
	private SpriteBatch			_spriteBatch;
	private ShapeRenderer		_shapeRenderer;
	private BitmapFont			_font;
	private	World				_world;
	private String				_str;
	private BubbleStop			_bubbleSelect;
	private boolean				_initPosition;

	public GUI() {
		StationManager.instance().endDraw = true;
		_shapeRenderer = new ShapeRenderer();
		_str = new String();
		_world = World.instance();
		_spriteBatch = new SpriteBatch();
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
	//	Log.d("gakla", "galasky SIZE RENDER LISTE BUBBLE STOP " + _world.listBubbleStop.size());
	//	Log.d("gakla", "galasky SIZE RENDER LISTE STATION " + _world.stationManager.getListStation().size());
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
			//_font.draw(_spriteBatch,_str, 20, 100);
			_spriteBatch.end();
		}
	}
	
	private void renderSelect() {
		_spriteBatch.begin();
		Date d = new Date();
		_font.setColor(Color.WHITE);
		_font.draw(_spriteBatch, _bubbleSelect.station.name, _bubbleSelect.position.x, _bubbleSelect.position.y);
		
		Iterator<Stop> i = _bubbleSelect.station.stops.iterator();
		int nb = 0;
		while (i.hasNext())
		{
			Stop stop = i.next();
			if (stop.nextTime != null)
			{
				nb++;
				_font.draw(_spriteBatch, (int) (_bubbleSelect.station.distanceTemps * 60) + " min marche + " + stop.nextTime.diff(new Date()) + " min " + stop.nextTime.getString(), _bubbleSelect.position.x, _bubbleSelect.position.y - nb * _bubbleSelect.slide);
			}
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
