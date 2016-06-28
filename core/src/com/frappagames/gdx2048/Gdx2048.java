package com.frappagames.gdx2048;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.frappagames.gdx2048.Screens.PlayScreen;
import com.frappagames.gdx2048.Tools.GameGestureListener;
import com.frappagames.gdx2048.Tools.GameInputProcessor;

public class Gdx2048 extends Game {
	public SpriteBatch batch;
	public Color       clearColor;
	public static int WIDTH = 800;
	public static int HEIGHT = 1280;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		clearColor = Color.valueOf("#FAF8EFFF");

		// Define input and gesture processors
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(new GestureDetector(new GameGestureListener()));
		inputMultiplexer.addProcessor(new GameInputProcessor());
		Gdx.input.setInputProcessor(inputMultiplexer);

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
