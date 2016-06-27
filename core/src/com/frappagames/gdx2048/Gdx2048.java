package com.frappagames.gdx2048;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

public class Gdx2048 implements ApplicationListener, GestureDetector.GestureListener, InputProcessor {
	Texture     img;
	Color       clearColor;
	String message;
	float startX, startY;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		clearColor = Color.valueOf("#FAF8EFFF");

		InputMultiplexer im = new InputMultiplexer();
		GestureDetector  gd = new GestureDetector(this);
		im.addProcessor(gd);
		im.addProcessor(this);


		Gdx.input.setInputProcessor(im);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.end();
	}

	private SpriteBatch batch;
	private BitmapFont  font;
	private int w,h;


	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		startX = x;
		startY = y;
		return true;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return true;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return true;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {

		return true;
	}

	/**
	 * Called when no longer panning.
	 *
	 * @param x
	 * @param y
	 * @param pointer
	 * @param button
	 */
	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		String direction;
		float moveX = x - startX;
		float moveY = y - startY;

		// Horizontal
		if (Math.abs(moveX) > Math.abs(moveY)) {
			if (moveX < 0) {
				direction = "Gauche";
			} else {
				direction = "Droite";
			}
		} else {
			if (moveY < 0) {
				direction = "Haut";
			} else {
				direction = "Bas";
			}
		}

		Gdx.app.log("INFO", direction);
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
						 Vector2 pointer1, Vector2 pointer2) {

		return true;
	}

	@Override
	public boolean keyDown(int keycode) {
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		String direction;

		switch (keycode) {
			case Input.Keys.LEFT:
				direction = "Gauche";
				break;
			case Input.Keys.RIGHT:
				direction = "Droite";
				break;
			case Input.Keys.UP:
				direction = "Haut";
				break;
			case Input.Keys.DOWN:
				direction = "Bas";
				break;
			default:
				direction = "";
		}
		Gdx.app.log("INFO", direction);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
