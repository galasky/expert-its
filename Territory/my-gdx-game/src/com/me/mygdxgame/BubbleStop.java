package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class BubbleStop {
	public Vector2		position;
	private Vector2		_direction;
	public	float		slide;
	public Stop			stop;
	private float		_inertie;
	public	float		distance;
	public boolean		touch;
	private float		_timeTouch;
	private float		_timeSelect;
	public int			min;
	public boolean		select;
	
	public BubbleStop(Stop s) {
		stop = s;
		slide = 0;
		distance = (float) Territory.distanceAB(You.instance().coordinate, stop.coord);
		position = new Vector2();
		_direction = new Vector2(GUI.random(-10, 10), GUI.random(-10, 10));
		_inertie = 0.5f;
		touch = false;
		select = false;
		_timeTouch = 0;
		_timeSelect = 0;
	}
	
	public boolean collision(float x, float y) {
		return (x >= position.x && x <= position.x + 40 * stop.stop_name.length() && y >= position.y - 50 && y <= position.y + 50);
	}
	
	public void		move(float deltaX, float deltaY)
	{
		_timeTouch = 0;
		_inertie = 1;
		_direction.x = deltaX;
		_direction.y = deltaY;
	}
	
	public Color	getColor() {
		distance = (float) Territory.distanceAB(You.instance().coordinate, stop.coord);
		if (distance < 0.3)
		{
			return Color.GREEN;
		}
		if (distance > 0.3 && distance < 0.45)
		{
			return Color.ORANGE;
		}
		return Color.RED;
	}
	
	public void select()
	{
		slide = 0;
		select = true;
	}
	
	public void unSelect()
	{
		slide = 50;
		select = false;
	}
	
	public void update() {

		position.x += _direction.x * _inertie;
		position.y += _direction.y * _inertie;
		
		if (position.x > Gdx.graphics.getWidth() - 40 * stop.stop_name.length() / 2 || position.x < 40)
			_direction.x *= -1;
		if (position.y > Gdx.graphics.getHeight() || position.y < Gdx.graphics.getHeight() / 8)
			_direction.y *= -1;
		
		if (_inertie > 0)
		{
			_inertie -= 0.01;
			if (_inertie < 0)
				_inertie = 0;
		}
		
		if (select)
		{
			if (slide < 50)
				slide += 5;
		}
		else
		{
			if (slide > 0)
				slide -= 5;
		}
		if (touch)
		{
			_timeTouch += Gdx.graphics.getDeltaTime();
			if (_timeTouch > 0.2)
			{
				_timeTouch = 0;
				touch = false;
			}
		}
	}
}
