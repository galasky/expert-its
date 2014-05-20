package com.me.mygdxgame;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GUI implements IGUI {
	private OrthographicCamera 	camera;
	private SpriteBatch			_spriteBatch;
	private ShapeRenderer		_shapeRenderer;
	private BitmapFont			_font;
	private String				_str;
	private World				_world;
	private boolean				_loadBubble;
	private BubbleStop			_bubbleSelect;

	public GUI() {
		camera = new OrthographicCamera(100, 100);
		_shapeRenderer = new ShapeRenderer();
		_str = new String();
		_world = World.instance();
		_spriteBatch = new SpriteBatch();
    	_font = new BitmapFont();
    	_font.setColor(Color.GREEN);
        _font.setScale(3F, 3F);
        _bubbleSelect = null;
        _loadBubble = true;
	}
	
	static float random(float Min, float Max) {
		return (float) (Min + (Math.random() * ((Max - Min) + 1)));
	}
	
	private void loadBubbleStop() {
		if (_world.listStop != null)
		{
			_world.listBubbleStop = new ArrayList<BubbleStop>();
			Iterator<Stop> i = _world.listStop.iterator();
	    	Stop stop;
	    	int	nb = 0;
	    	
	    	while (i.hasNext())
	    	{
	    		stop = i.next();
	    		BubbleStop bubble = new BubbleStop(stop);
	    		bubble.position.x = random(40, Gdx.graphics.getWidth() - 40 * stop.stop_name.length() / 2);
	    		bubble.position.y = random(Gdx.graphics.getHeight(), Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 8);
	    		_world.listBubbleStop.add(bubble);
	    		nb++;
	    	}
	    	_loadBubble = false;
		}
	}
	
	@Override
	public void touche(float x, float y, float deltaX, float deltaY)
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
	
	private void updateBubbleStop() {
		Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
		BubbleStop bubbleStop = null;
		while (i.hasNext())
		{
			bubbleStop = i.next();
			bubbleStop.update();
		}
	}
	
	private void renderAll() {
		if (_world.listStop != null)
		{
			_spriteBatch.begin();
			Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
			BubbleStop bubbleStop = null;
			while (i.hasNext())
			{
				bubbleStop = i.next();
				_font.setColor(bubbleStop.getColor());
				_font.draw(_spriteBatch, bubbleStop.stop.stop_name, bubbleStop.position.x, bubbleStop.position.y);
			}
			//_font.draw(_spriteBatch,_str, 20, 100);
			_spriteBatch.end();
		}
	}
	
	private void renderSelect() {
		_spriteBatch.begin();
		_font.setColor(_bubbleSelect.getColor());

		_font.draw(_spriteBatch, _bubbleSelect.stop.stop_name, _bubbleSelect.position.x, _bubbleSelect.position.y);
		_font.draw(_spriteBatch, (int) (_bubbleSelect.distance * 1000) + "m", _bubbleSelect.position.x, _bubbleSelect.position.y - _bubbleSelect.slide);
		_font.draw(_spriteBatch, _bubbleSelect.min + "min", _bubbleSelect.position.x, _bubbleSelect.position.y - 2 * _bubbleSelect.slide);
		
		_spriteBatch.end();
		
		if (_bubbleSelect.select == false && _bubbleSelect.slide <= 0)
			_bubbleSelect = null;
	}
	
	@Override
	public void render() {
		if (_loadBubble)
			loadBubbleStop();
		else
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
	public IGUI inverte() {
		return new GUI2();
	}
}
