package com.me.mygdxgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;

public class SkyBox {

	private float				SIZE;
	private ModelInstance 		modelInstance;
	private Environment			_environment;
	public Array<ModelInstance>	instances;
	public ModelBatch 			modelBatch;
	
	public SkyBox(Environment environment)	{
		SIZE = 50;
		_environment = environment;
		modelBatch = new ModelBatch();
		instances = new Array<ModelInstance>();
		Model model;
		Texture sky;
		TextureAttribute textureAttribute;
		
        sky = new Texture("data/cubemap/blue.jpg");
        textureAttribute = new TextureAttribute(TextureAttribute.Diffuse, sky);
      	model = Game3D.instance().modelBuilder.createBox(.5f, SIZE, SIZE, 
      	new Material(), Usage.Position | Usage.Normal | Usage.TextureCoordinates);
      	modelInstance = new ModelInstance(model);
      	modelInstance.transform.setToTranslation(SIZE / 2, 0, 0);
      	modelInstance.materials.get(0).set(textureAttribute);
      	instances.add(modelInstance);
      	
      	sky = new Texture("data/cubemap/blue.jpg");
        textureAttribute = new TextureAttribute(TextureAttribute.Diffuse, sky);
      	model = Game3D.instance().modelBuilder.createBox(SIZE, SIZE, .5f, 
      	new Material(), Usage.Position | Usage.Normal | Usage.TextureCoordinates);
      	modelInstance = new ModelInstance(model);
      	modelInstance.transform.setToTranslation(0, 0, SIZE / 2);
      	modelInstance.materials.get(0).set(textureAttribute);
      	instances.add(modelInstance);
      	
      	sky = new Texture("data/cubemap/blue.jpg");
        textureAttribute = new TextureAttribute(TextureAttribute.Diffuse, sky);
      	model = Game3D.instance().modelBuilder.createBox(.5f, SIZE, SIZE, 
      	new Material(), Usage.Position | Usage.Normal | Usage.TextureCoordinates);
      	modelInstance = new ModelInstance(model);
      	modelInstance.transform.setToTranslation(-SIZE / 2, 0, 0);
      	modelInstance.materials.get(0).set(textureAttribute);
      	instances.add(modelInstance);
      	
     	sky = new Texture("data/cubemap/blue.jpg");
        textureAttribute = new TextureAttribute(TextureAttribute.Diffuse, sky);
      	model = Game3D.instance().modelBuilder.createBox(SIZE, SIZE, .5f, 
      	new Material(), Usage.Position | Usage.Normal | Usage.TextureCoordinates);
      	modelInstance = new ModelInstance(model);
      	modelInstance.transform.setToTranslation(0, 0, -SIZE / 2);
      	modelInstance.materials.get(0).set(textureAttribute);
      	instances.add(modelInstance);
      	
      	
      	
      	
      	/////////////////////////////////////// SINON CA MARCHE PAS ...
    	sky = new Texture("data/cubemap/blue.jpg");
        textureAttribute = new TextureAttribute(TextureAttribute.Diffuse, sky);
      	model = Game3D.instance().modelBuilder.createBox(SIZE, SIZE, .5f, 
      	new Material(), Usage.Position | Usage.Normal | Usage.TextureCoordinates);
      	modelInstance = new ModelInstance(model);
      	modelInstance.transform.setToTranslation(0, 0, -SIZE / 2);
      	modelInstance.materials.get(0).set(textureAttribute);
      	instances.add(modelInstance);
      	
      	sky = new Texture("data/cubemap/blue.jpg");
        textureAttribute = new TextureAttribute(TextureAttribute.Diffuse, sky);
      	model = Game3D.instance().modelBuilder.createBox(SIZE, SIZE, .5f, 
      	new Material(), Usage.Position | Usage.Normal | Usage.TextureCoordinates);
      	modelInstance = new ModelInstance(model);
      	modelInstance.transform.setToTranslation(0, 0, -SIZE / 2);
      	modelInstance.materials.get(0).set(textureAttribute);
      	instances.add(modelInstance);
      	/////////////////////////////////////////////////////////////////////
	}
	
	public void render() {
		modelBatch.begin(MyCamera.instance().pCam);
        modelBatch.render(instances, _environment);
        modelBatch.end();
	}
}
