package com.frappagames.gdx2048;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.frappagames.gdx2048.Screens.PlayScreen;

public class Gdx2048 extends Game {
	public SpriteBatch batch;
	public Color       clearColor;
	public static int WIDTH = 800;
	public static int HEIGHT = 1280;

	public enum Direction {
		LEFT, RIGHT, UP, DOWN
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		clearColor = Color.valueOf("#FAF8EFFF");

		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
