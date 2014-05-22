package com.me.mygdxgame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	    while (_life)
	    {
	    	_time += Gdx.graphics.getDeltaTime();
	    	if (_time > 1)
	    	{
	    		_time = 0;
				refreshBubbleStop();
				_gui.refresh();
	    	}
	    }
	  }
	
	public void	refreshBubbleStop() {
		if (_world.listBubbleStop == null)
			return ;
		List<BubbleStop>	listTmp = new ArrayList<BubbleStop>();
		
		Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
		BubbleStop bubble = null;
		while (i.hasNext())
		{
			bubble = i.next();
			bubble.refreshNextTime();
			if (listTmp.size() == 0)
			{
				//Log.d("ok", "galasky add " + 0);
				listTmp.add(bubble);
				
			}
			else
    		{
        		BubbleStop tmp;
        		boolean find = false;
        		
        		for (int u = 0; find == false && u < listTmp.size(); u++)
        		{
        			tmp = listTmp.get(u);
        			if (bubble.isFasterTo(tmp))
        			{
        				listTmp.add(u, bubble);
        				find = true;
        			}
        		}
        		if (find == false)
    			{
    				listTmp.add(bubble);
    			}
    		}
		}
		_world.listBubbleStop = listTmp;
	}
}
