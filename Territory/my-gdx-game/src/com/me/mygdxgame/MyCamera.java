package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;


public class MyCamera {
	private float				_angleBoussole, _angle, _acAngle;
	public PerspectiveCamera	pCam;
	private You					_you;
	private Vector3				_position;
	
	private	MyCamera() {
		_angle = 0;
        _acAngle = 0;
        pCam = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        _you = You.instance();
        pCam = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        _position = new Vector3(5, 5, 5);
        pCam.lookAt(0,0,0);
        pCam.near = 1f;
        pCam.far = 3000f;
        pCam.update();
	}
	
	public static MyCamera instance() {
        return SingletonHolder.instance;
    }
	
	private static class SingletonHolder {
        /** Instance unique non préinitialisée */
        private final static MyCamera instance = new MyCamera();
    }
	
	public void update() {
		
		_angleBoussole = (float) ((Math.PI * Gdx.input.getAzimuth()) / 180);
		float delta = Math.abs(_angleBoussole - _angle);
		if (delta > 0.15)
		{
			_acAngle = _angleBoussole;
		}
		if (_acAngle != _angle)
		{
			if (_acAngle > _angle)
			{
				if (delta < Math.PI * 1.5)
				{
					_angle += 0.1 * delta;
					if (_you.isLoaded())
						_you.modelInstance.transform.rotate(new Vector3(0, 1, 0), -0.1F * delta * 57.2957795F);
					if (_acAngle < _angle)
						_acAngle = _angle;
				}
				else
				{
					_angle += Math.PI * 1.99;
					if (_you.isLoaded())
						_you.modelInstance.transform.rotate(new Vector3(0, 1, 0), (float) (-Math.PI * 1.99 * 57.2957795F));
				}
			}
			if (_acAngle < _angle)
			{
				if (delta < Math.PI * 1.5)
				{
					_angle -= 0.1 * delta;
					if (_you.isLoaded())
						_you.modelInstance.transform.rotate(new Vector3(0, 1, 0), 0.1F * delta * 57.2957795F);
					if (_acAngle > _angle)
						_acAngle = _angle;
				}
				else
				{
					_angle -= Math.PI * 2;
					if (_you.isLoaded())
						_you.modelInstance.transform.rotate(new Vector3(0, 1, 0), (float) (Math.PI * 2 * 57.2957795F));
				}
			}
		}
		pCam.position.x = (float) Math.sin(-_angle) * _position.x;
		pCam.position.y = _position.y;
		pCam.position.z = (float) Math.cos(-_angle) * _position.z;
		pCam.position.x += You.instance().position.x;
		pCam.position.z += You.instance().position.z;
		
		pCam.lookAt(You.instance().position);
		pCam.up.x = 0;
		pCam.up.y = 1;
		pCam.up.z = 0;
		pCam.update();
	}
	
    public void zoom(float DistanceInitial, float DistanceActuel) {
    	_position.y += pCam.position.y * (DistanceInitial - DistanceActuel) / 10000;
    	pCam.lookAt(0,0,0);
    	pCam.update();
    }
}
