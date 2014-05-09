package com.me.mygdxgame;

import java.util.ArrayList;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;



public class MyCamera {
	private float				_angleBoussole, _angle, _acAngle;
	private float				_angleFiltre;
	public PerspectiveCamera	pCam;
	private You					_you;
	private float				_zoom;
	private int					_t;
	private float				_m;
	private Vector3				_position;
	private Vector3				_start;
	private int					i;
	private boolean				ok;
	private ArrayList<Float>			tab;
	
	private	MyCamera() {
		_t = 100;
		ok = false;
		tab = new ArrayList<Float>();
		_zoom = 4;
		_angle = 0;
        _acAngle = 0;
        _m = 0;
        _angleFiltre = 0;
        pCam = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        _you = You.instance();
        pCam = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        _start = new Vector3(-1, 0, 0);
        _position = new Vector3(-1, 0, 0);
        pCam.lookAt(0,0,0);
        pCam.near = 1f;
        pCam.far = 3000f;
        pCam.update();
        i = 0;
	}
	
	public static MyCamera instance() {
        return SingletonHolder.instance;
    }
	
	private static class SingletonHolder {
        /** Instance unique non préinitialisée */
        private final static MyCamera instance = new MyCamera();
    }
	
	private float moyenne()
	{
		int i = 0;
		float	m = 0;
		
		while (i < tab.size())
		{
			m += tab.get(i);
			i++;
		}
		return m / tab.size();
	}
	
	private void filtre() {
			if (Math.abs(_angleBoussole + Math.PI * 2 - _angleFiltre) < 1)
				_angleBoussole += Math.PI * 2;
			if (Math.abs(_angleBoussole - Math.PI * 2 - _angleFiltre) < 1)
				_angleBoussole -= Math.PI * 2;
			if (Math.abs(_angleFiltre - Math.PI * 2) < 0.1 || Math.abs(_angleFiltre + Math.PI * 2) < 0.1)
			{
				_angleFiltre = 0;
				tab.clear();
				return ;
			}
		tab.add(new Float(_angleBoussole));
		if (tab.size() >= 20)
		{
			_angleFiltre = moyenne();
			Log.d("ok", "galasky filtre = " + _angleFiltre);
			tab.remove(0);
		}
		else
		{
			//_angleFiltre = (float) ((Math.PI * Gdx.input.getAzimuth()) / 180);
		}
	}
	
	public void update() {
		
		_angleBoussole = (float) ((Math.PI * Gdx.input.getAzimuth()) / 180);
		//Log.d("ok", "galasky size = " + tab.size());
		filtre();
		/*float delta = Math.abs(_angleBoussole - _angle);
		if (delta > 0.2)
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
		}*/
		
		/*pCam.position.x = _start.x;
		pCam.position.y = _start.y;
		pCam.position.z = _start.z;
		
		rotation_y(_angleFiltre - Math.PI / 2, pCam.position);*/
		pCam.position.x = (float) Math.sin(-_angleFiltre - Math.PI / 2) * _zoom;
		pCam.position.y = 0.5f * _zoom;
		pCam.position.z = (float) Math.cos(-_angleFiltre - Math.PI / 2) * _zoom;
		pCam.position.x += You.instance().position.x;
		pCam.position.z += You.instance().position.z;
		
		pCam.lookAt(You.instance().position);
		pCam.up.x = 0;
		pCam.up.y = 1;
		pCam.up.z = 0;
		pCam.update();
	}
	
	public float	getAngle() {
		return _angleFiltre;
	}
	
    public void zoom(float DistanceInitial, float DistanceActuel) {
    	_position.y += pCam.position.y * (DistanceInitial - DistanceActuel) / 10000;
    	_zoom += (DistanceInitial - DistanceActuel) / 10000;
    	pCam.lookAt(0,0,0);
    	pCam.update();
    }
    
	void rotation_x(double a, Vector3 f)
	{
	  Vector3  tempo;

	  tempo = f;
	  f.x *= 1;
	  f.y = (float) (Math.cos(a) * tempo.y - Math.sin(a) * tempo.z);
	  f.z = (float) (Math.sin(a) * tempo.y + Math.cos(a) * tempo.z);
	}

	void rotation_y(double a, Vector3 f)
	{
	  Vector3  tempo;

	  tempo = f;
	  f.x = (float) (Math.cos(a) * tempo.x + Math.sin(a) * tempo.z);
	  f.y *= 1;
	  f.z = (float) (-Math.sin(a) * tempo.x + Math.cos(a) * tempo.z);
	}

	void rotation_z(double a, Vector3 f)
	{
	  Vector3  tempo = new Vector3();

	  tempo.x = f.x;
	  tempo.y = f.y;
	  tempo.z = f.z;
	  f.x = (float) (Math.cos(a) * tempo.x - Math.sin(a) * tempo.y);
	  f.y = (float) (Math.sin(a) * tempo.x + Math.cos(a) * tempo.y);
	  f.z = tempo.z;
	}
}
