package com.me.mygdxgame;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GUI2 implements IGUI {
	private SpriteBatch			_spriteBatch;
	private ShapeRenderer		_shapeRenderer;
	private BitmapFont			_font;
	private String				_str;
	private World				_world;
	private boolean				_loadBubble;
	private BubbleStop			_bubbleSelect;

	public GUI2() {
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
	
	static int random(int Min, int Max) {
		return (int) (Min + (Math.random() * ((Max - Min) + 1)));
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
	    		bubble.position.x = 40;
	    		bubble.position.y = 50 * nb;
	    		bubble.min = random(1, 8);
	    		_world.listBubbleStop.add(bubble);
	    		nb++;
	    	}
	    	_loadBubble = false;
		}
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
				bubbleStop.select = true;
				_bubbleSelect = bubbleStop;
				return ;
			}
		}
		if (_bubbleSelect != null)
		{
			_bubbleSelect.select = false;
			_bubbleSelect = null;
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
				_font.setColor(new Color(1, 1, 1, 1));
				//_font.setColor((bubbleStop.touch ? Color.YELLOW : Color.GREEN));
				_font.setColor(Color.WHITE);
				_font.draw(_spriteBatch, bubbleStop.stop.stop_name, bubbleStop.position.x, bubbleStop.position.y);
				_font.draw(_spriteBatch, (int) (bubbleStop.distance * 1000) + "m", bubbleStop.position.x + 400, bubbleStop.position.y);
				_font.draw(_spriteBatch, bubbleStop.min + "min", bubbleStop.position.x + 900, bubbleStop.position.y);
			}
			//_font.draw(_spriteBatch,_str, 20, 100);
			_spriteBatch.end();
		}
	}
	
	private void renderSelect() {
		_spriteBatch.begin();
		_font.setColor(new Color(1, 1, 1, 1));
		//_font.setColor((bubbleStop.touch ? Color.YELLOW : Color.GREEN));
		_font.setColor(_bubbleSelect.getColor());
		_font.draw(_spriteBatch, _bubbleSelect.stop.stop_name, _bubbleSelect.position.x, _bubbleSelect.position.y);
		_font.draw(_spriteBatch, "distance " + _bubbleSelect.distance, _bubbleSelect.position.x, _bubbleSelect.position.y - 50);
		_spriteBatch.end();
	}
	
	@Override
	public void render() {
		if (_loadBubble)
			loadBubbleStop();
		
		renderAll();
	}

	@Override
	public IGUI inverte() {
		return new GUI();
	}
}
