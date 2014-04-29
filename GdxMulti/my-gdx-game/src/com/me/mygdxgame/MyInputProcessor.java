package com.me.mygdxgame;

import android.util.Log;

import com.badlogic.gdx.InputProcessor;

public class MyInputProcessor implements InputProcessor {

	private DrawStop	drawStop = DrawStop.instance();
	
	@Override
	public boolean keyDown (int keycode) {
		return false;
    }

	   @Override
	   public boolean keyUp (int keycode) {
	      return false;
	   }

	   @Override
	   public boolean keyTyped (char character) {
	      return false;
	   }

	   @Override
	   public boolean touchDown (int x, int y, int pointer, int button) {
		   
	      return false;
	   }

	   @Override
	   public boolean touchUp (int x, int y, int pointer, int button) {
	      return false;
	   }

	   @Override
	   public boolean touchDragged (int x, int y, int pointer) {
		  drawStop.setZoom(drawStop.getZoom() + 100);
	      return false;
	   }

	   @Override
	   public boolean scrolled (int amount) {
	      return false;
	   }

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	}