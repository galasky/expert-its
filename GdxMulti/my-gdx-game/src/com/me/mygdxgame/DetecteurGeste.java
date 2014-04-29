package com.me.mygdxgame;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class DetecteurGeste implements GestureListener {

	private boolean ok;
	
	public DetecteurGeste() {
		ok = false;
	}
	
	DrawStop drawStop = DrawStop.instance();
    @Override
    public boolean zoom (float DistanceInitial, float DistanceActuel) {
       drawStop.zoom(-(DistanceInitial - DistanceActuel));
    	return false;
    }

    @Override
    public boolean pinch (Vector2 posInitialPremierDoigt, Vector2 posInitialDeuxiemeDoigt, Vector2 posActuelPremierDoigt, Vector2 posActuelDeuxiemeDoigt) {
       return false;
    }

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if (ok == false)
		{
			drawStop._timeTap = 0;
			ok = true;
		}
		else
		{	
			if (drawStop._timeTap < 1)
				drawStop._autoZoom = true;
			ok = false;
		}
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		drawStop._nameVisible = (drawStop._nameVisible ? false : true);
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}
 }