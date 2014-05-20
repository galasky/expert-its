package com.me.mygdxgame;

import java.util.List;


public class World {
	
	public List<Stop> 				listStop;
	public List<BubbleStop> 		listBubbleStop;
	
	private	World() {
        listStop = null;
        listBubbleStop = null;
	}
	
	public static World instance() {
        return SingletonHolder.instance;
    }
	
	private static class SingletonHolder {
        /** Instance unique non préinitialisée */
        private final static World instance = new World();
    }
}
