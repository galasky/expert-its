package com.me.mygdxgame;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GUIController {
	private	IGUI		_gui;
	private World		_world;
	private float		_time;
	private boolean		_loadBubble;
	private SpriteBatch	_spriteBatch;
	private ZoomController	_zoomController;
	private BitmapFont	_font;
	private Date		_date;
	private String		_sDate;
	private	RefreshGUI	_refreshGUI;
	private StationManager	stationManager;
	
	public GUIController() {
		stationManager = StationManager.instance();
		_zoomController = new ZoomController();
		_date = new Date();
		_time = 0;
		_gui = new GUI();
		_world = World.instance();
		_loadBubble = true;
		_spriteBatch = new SpriteBatch();
		_font = new BitmapFont();
    	_font.setColor(Color.GREEN);
        _font.setScale(3F, 3F);
        _refreshGUI = new RefreshGUI(_gui);
        _refreshGUI.start();
	}
	
	static float random(float Min, float Max) {
		return (float) (Min + (Math.random() * ((Max - Min) + 1)));
	}
	
	/*public void refreshOrder()
	{
		List<BubbleStop>	listTmp = new ArrayList<BubbleStop>();

		Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
    	BubbleStop bubble;
    	int	nb = 0;
    	
    	while (i.hasNext())
    	{
    		bubble = i.next();

    		if (listTmp.size() == 0)
    			listTmp.add(0, bubble);
    		else
    		{
        		BubbleStop tmp;
        		boolean find = false;
        		
        		for (int u = 0; find == false && u < listTmp.size(); u++)
        		{
        			tmp = listTmp.get(u);
        			if (bubble.nextTime.isBeforeTo(tmp.nextTime))
        			{
        				listTmp.add(u, bubble);
        				find = true;
        			}
        		}
    		}
    	}
    	_world.listBubbleStop = listTmp;
	}*/
	
	private void loadBubbleStop() {
		if (stationManager.loadFinish() && stationManager.getListStation() != null)
		{
			_world.listBubbleStop = new ArrayList<BubbleStop>();
			Iterator<Station> i = stationManager.getListStation().iterator();
	    	Station station;
	    	int	nb = 0;
	    	while (i.hasNext())
	    	{
	    		station = i.next();
	    		BubbleStop bubble = new BubbleStop(station);
	    		bubble.position.x = random(40, Gdx.graphics.getWidth() - 40 * station.name.length() / 2);
	    		bubble.position.y = random(Gdx.graphics.getHeight(), Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 8);
	    		_world.listBubbleStop.add(bubble);
	    		Log.d("ok", "galasky add bubble");
	    		_loadBubble = false;
	    		nb++;
	    	}
		}
	}

	
	private void updateTime() {
		_time += Gdx.graphics.getDeltaTime();
		if (_time > 1)
		{
			_time = 0;
			//refreshBubbleStop();
			//_gui.refresh();
		}
	}

	public void	render() {
		updateTime();
		MyTimes time = new MyTimes(new Date());
		
		_spriteBatch.begin();
		_font.setColor(Color.WHITE);
		_font.draw(_spriteBatch, time.hours + ":" + (time.minutes < 10 ? "0" + time.minutes : time.minutes), 40, Gdx.graphics.getHeight() - 40);
		_spriteBatch.end();
		_gui.render();
		_zoomController.render();
	}
	
	public void	invert() {
		_gui = _gui.invert();
	}
	
	public void touch(float x, float y, float deltaX, float deltaY) {
		_gui.touch(x, y, deltaX, deltaY);
		_zoomController.touch(x, y, deltaX, deltaY);
	}
	
	public void tap(float x, float y) {
		_gui.tap(x, y);
	}
	public void	refreshBubbleStop() {
		if (_world.listBubbleStop == null)
			return ;
		Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
		BubbleStop bubble = null;
		while (i.hasNext())
		{
			bubble = i.next();
			Iterator<Stop> u = bubble.station.stops.iterator();
			while (u.hasNext())
			{
				Stop stop = u.next();
				stop.refreshNextTime();
			}
		}
	}
}
