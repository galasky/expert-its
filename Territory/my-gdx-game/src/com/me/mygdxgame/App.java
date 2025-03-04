package com.me.mygdxgame;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;

/**
 *
 * @author Aurelien Ribon
 */

public class App implements ApplicationListener {
    public static void main(String args[]) {
      new LwjglApplication(new App(), "2D overlay over 3D scene", 800, 400, false);
   }

   // -------------------------------------------------------------------------

   // 3D stuff
   private ImmediateModeRenderer imr;
   private PerspectiveCamera camera3D;

   // 2D stuff
   private SpriteBatch spriteBatch;
   private OrthographicCamera camera2D;
   private Sprite[] ballSprites;

   // Misc
   private float cubeAngle = 0;

   // -------------------------------------------------------------------------

   @Override
   public void create() {
      this.imr = new toto();
      this.camera3D = new PerspectiveCamera(75, 10, 5);
      camera3D.position.set(0, 0, 5);
      camera3D.update();

      this.spriteBatch = new SpriteBatch();
      this.camera2D = new OrthographicCamera(200, 100);
      camera2D.update();

      Texture ballTexture = new Texture(Gdx.files.classpath("overlay2d3d/ball.png"));
      ballTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
      
      Random rand = new Random();
      ballSprites = new Sprite[20];
      for (int i=0; i<ballSprites.length; i++) {
         float x = rand.nextFloat() * 200 - 100;
         float y = rand.nextFloat() * 100 - 50;

         ballSprites[i] = new Sprite(ballTexture);
         ballSprites[i].setSize(10, 10);
         ballSprites[i].setPosition(x-5, y-5);
      }
   }

   @Override
   public void render() {
      // Update
      cubeAngle += Gdx.graphics.getDeltaTime() * 120;

      // Render init
      GL30 gl = Gdx.gl30;
      gl.glEnable(GL10.GL_DEPTH_TEST);
      gl.glClearColor(1, 1, 1, 1);
      gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

      // 3D render
      camera3D.update();
      ((GL10) gl).glRotatef(cubeAngle, 0, 1, 0);
      drawCube();

      // 2D render
      spriteBatch.setProjectionMatrix(camera2D.combined);
      spriteBatch.begin();
      for (int i=0; i<ballSprites.length; i++)
         ballSprites[i].draw(spriteBatch);
      spriteBatch.end();
   }

   @Override public void resize(int width, int height) {}
   @Override public void pause() {}
   @Override public void resume() {}
   @Override public void dispose() {}

   // -------------------------------------------------------------------------

   private void drawCube() {
      imr.begin(null, GL10.GL_TRIANGLES);

      imr.color(1, 0, 0, 1); imr.vertex(-1, -1, +1);
      imr.color(1, 0, 0, 1); imr.vertex(+1, -1, +1);
      imr.color(1, 0, 0, 1); imr.vertex(-1, +1, +1);
      imr.color(1, 0, 0, 1); imr.vertex(+1, -1, +1);
      imr.color(1, 0, 0, 1); imr.vertex(-1, +1, +1);
      imr.color(1, 0, 0, 1); imr.vertex(+1, +1, +1);

      imr.color(0, 1, 0, 1); imr.vertex(+1, -1, +1);
      imr.color(0, 1, 0, 1); imr.vertex(+1, -1, -1);
      imr.color(0, 1, 0, 1); imr.vertex(+1, +1, +1);
      imr.color(0, 1, 0, 1); imr.vertex(+1, -1, -1);
      imr.color(0, 1, 0, 1); imr.vertex(+1, +1, +1);
      imr.color(0, 1, 0, 1); imr.vertex(+1, +1, -1);

      imr.color(0, 0, 1, 1); imr.vertex(+1, -1, -1);
      imr.color(0, 0, 1, 1); imr.vertex(-1, -1, -1);
      imr.color(0, 0, 1, 1); imr.vertex(+1, +1, -1);
      imr.color(0, 0, 1, 1); imr.vertex(-1, -1, -1);
      imr.color(0, 0, 1, 1); imr.vertex(+1, +1, -1);
      imr.color(0, 0, 1, 1); imr.vertex(-1, +1, -1);

      imr.color(1, 0, 1, 1); imr.vertex(-1, -1, -1);
      imr.color(1, 0, 1, 1); imr.vertex(-1, -1, +1);
      imr.color(1, 0, 1, 1); imr.vertex(-1, +1, -1);
      imr.color(1, 0, 1, 1); imr.vertex(-1, -1, +1);
      imr.color(1, 0, 1, 1); imr.vertex(-1, +1, -1);
      imr.color(1, 0, 1, 1); imr.vertex(-1, +1, +1);

      imr.end();
   }
}

