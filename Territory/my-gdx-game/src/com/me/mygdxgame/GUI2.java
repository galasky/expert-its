package com.me.mygdxgame;

import java.util.Date;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GUI2 implements IGUI {
	private SpriteBatch			_spriteBatch;
	private BitmapFont			_font;
	private boolean				_initPosition;
	private String				_str;
	private World				_world;
	private BubbleStop			_bubbleSelect;

	public GUI2() {
		_str = new String();
		_world = World.instance();
		_spriteBatch = new SpriteBatch();
    	_font = new BitmapFont();
    	_font.setColor(Color.GREEN);
        _font.setScale(3F, 3F);
        _bubbleSelect = null;
        initPosition();
	}
	
	static int random(int Min, int Max) {
		return (int) (Min + (Math.random() * ((Max - Min) + 1)));
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
	
	private void refreshOrder() {
		/*Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
		BubbleStop bubbleStop = null;
		int nb = 0;
		while (i.hasNext())
		{
			bubbleStop = i.next();
			bubbleStop.initPosition(new Vector2(50, 50 + bubbleStop.order * 50));
			nb++;
		}*/
	}

	public void	initPosition() {
		_initPosition = true;
		if (_world.listBubbleStop == null)
			return ;
		
		Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
		BubbleStop bubbleStop = null;
		int nb = 0;
		while (i.hasNext())
		{
			bubbleStop = i.next();
			bubbleStop.initPosition(new Vector2(50, 50 + nb * 50));
			nb++;
		}
		_initPosition = false;
	}
	
	private void renderAll() {
		if (_world.listStop != null)
		{
			Date d = new Date();
			_spriteBatch.begin();
			Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
			BubbleStop bubbleStop = null;
			while (i.hasNext())
			{
				bubbleStop = i.next();
				_font.setColor(new Color(1, 1, 1, 1));
				//_font.setColor((bubbleStop.touch ? Color.YELLOW : Color.GREEN));
				if (bubbleStop.nextTime != null)
				{
					int diff = bubbleStop.nextTime.diff(d);
					if (diff > (int) (bubbleStop.distanceTemps * 60))
						_font.setColor(Color.GREEN);
					else if ((int) (bubbleStop.distanceTemps * 60) == diff)
						_font.setColor(Color.ORANGE);
					else
						_font.setColor(Color.RED);
				}
				else
				{
					_font.setColor(Color.GRAY);
				}
				
				_font.draw(_spriteBatch, bubbleStop.stop.stop_name, bubbleStop.position.x, bubbleStop.position.y);
				_font.draw(_spriteBatch, (int) (bubbleStop.distance * 1000) + "m", bubbleStop.position.x + 400, bubbleStop.position.y);
				if (bubbleStop.nextTime != null)
					_font.draw(_spriteBatch, bubbleStop.nextTime.getString(), bubbleStop.position.x + 600, bubbleStop.position.y);
				else
					_font.draw(_spriteBatch, "Fin du service", bubbleStop.position.x + 600, bubbleStop.position.y);
				_font.draw(_spriteBatch, (int) (bubbleStop.distanceTemps * 60) + " min", bubbleStop.position.x + 900, bubbleStop.position.y);
				//_font.draw(_spriteBatch, diff + "", bubbleStop.position.x + 1100, bubbleStop.position.y);
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
		if (_initPosition == true)
			initPosition();
		updateBubbleStop();
		renderAll();
	}

	@Override
	public IGUI invert() {
		return new GUI();
	}

	@Override
	public void refresh() {
		initPosition();
	}
}
