package com.me.mygdxgame;

import java.util.Iterator;
import java.util.List;

import android.util.Log;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class DrawStop {
	private double zoom, acZoom;
	private BitmapFont font;
	private BitmapFont fontYou;
	private float centerX, centerY, acCenterX, acCenterY;
	
	private DrawStop() {
		centerX = 0;
		centerY = 0;
		acCenterX = 0;
		acCenterY = 0;
		acZoom = 0;
		font = new BitmapFont();
		fontYou = new BitmapFont();
        font.setColor(Color.RED);
        fontYou.setColor(Color.GREEN);
	}
	
	public void update() {
		Log.d("galasky", "galasky Zoom " + zoom);
		if (!(zoom <= 0 && acZoom < 0))
			zoom += acZoom;
		centerX += acCenterX;
		centerY += acCenterY;
		deceleration();
	}
	
	private void deceleration() {
		if (acZoom > 0) {
			acZoom -= 100;
			if (acZoom < 0)
				acZoom = 0;
		}
		if (acZoom < 0) {
			acZoom += 100;
			if (acZoom > 0)
				acZoom = 0;
		}
		if (acCenterX > 0) {
			acCenterX -= 1;
			if (acCenterX < 0)
				acCenterX = 0;
		}
		if (acCenterX < 0) {
			acCenterX += 1;
			if (acCenterX > 0)
				acCenterX = 0;
		}
		if (acCenterY > 0) {
			acCenterY -= 1;
			if (acCenterY < 0)
				acCenterY = 0;
		}
		if (acCenterY < 0) {
			acCenterY += 1;
			if (acCenterY > 0)
				acCenterY = 0;
		}
	}
	
	public double getZoom() {
		return zoom;
	}
	
	public void translateX(float x, float deltaX) {
		acCenterX = deltaX;
	}
	
	public void translateY(float y, float deltaY) {
		acCenterY = -deltaY;
	}
	
	public void zoom(float delta) {
		acZoom = delta * 10;
	}
	
	public void latitudeTranslate(double x) {
		centerX += x;
	}
	
	public void longitudeTranslate(double y) {
		centerY += y;
	}
	
	public void setZoom(double z) {
		zoom = z;
	}
	
	public static DrawStop instance() {
        return SingletonHolder.instance;
    }
	
	public void setCenter(float centerx, float centery) {
		centerX = centerx;
		centerY = centery;
	}
	
	public void dispose() {
		font.dispose();
		fontYou.dispose();
	}
	
	private static class SingletonHolder {
        /** Instance unique non préinitialisée */
        private final static DrawStop instance = new DrawStop();
    }
	
	public void draw(SpriteBatch batch, List<Stop> listStop, CoordinateGPS me) {
		fontYou.draw(batch, "You", centerX, centerY);
		Iterator i = listStop.iterator();
		Stop stop = null;
		while(i.hasNext()){
	          stop = (Stop) i.next();
	          //calcRaport(stop);
	          font.draw(batch, stop.stop_id + "", centerX + (float) (me.latitude * zoom - stop.coord.latitude * zoom), centerY + (float) (me.longitude * zoom - stop.coord.longitude * zoom));
	        }
	}
}
