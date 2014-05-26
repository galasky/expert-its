package com.me.mygdxgame;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;

public class AssetManager {
	 //public Array<ModelInstance>	instances;
	 //public ModelBuilder 			modelBuilder;
	 public boolean					loading;

	 private	AssetManager() {
		 loading = false;
		 //modelBuilder = new ModelBuilder();
	 }
	 
	 public static AssetManager instance() {
        return SingletonHolder.instance;
    }
	
	private static class SingletonHolder {
        private final static AssetManager instance = new AssetManager();
    }
}
