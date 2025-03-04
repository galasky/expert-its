package com.me.mygdxgame;

public class Config {

	public float	distance;
	
    private Config() {
    	distance = 0.5f;
    }

    public static Config instance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder
    {
        /** Instance unique non préinitialisée */
        private final static Config instance = new Config();
    }
}
