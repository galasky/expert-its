package com.me.mygdxgame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.Log;

import com.badlogic.gdx.Gdx;

public class RefreshGUI extends Thread {
	private IGUI	_gui;
	private	boolean	_life;
	private	float	_time;
	private World	_world;
	
	public RefreshGUI(IGUI gui) {
		_gui = gui;
		_life = true;
		_world = World.instance();
	}
	
	public void run() {
		_time = 0;
Log.d("ok", "galasky RUUUUUN");
	    while (_life)
	    {
	    	_time += Gdx.graphics.getDeltaTime();
	    	if (_time > 1 && StationManager.instance().loadFinish())
	    	{
	    		_time = 0;
				//refreshBubbleStop();
				//_gui.refresh();
	    	}
	    }
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
