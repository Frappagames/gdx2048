package com.frappagames.gdx2048;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.frappagames.gdx2048.Screens.MenuScreen;
import com.frappagames.gdx2048.Tools.Settings;

public class Gdx2048 extends Game {
	public SpriteBatch batch;
	public Color       clearColor;
	public static int WIDTH = 800;
	public static int HEIGHT = 1280;
	public TextureAtlas atlas;

	public enum Direction {
		LEFT, RIGHT, UP, DOWN
	}

	public enum GameType {
		CLASSIC, TIME
	}
	
	@Override
	public void create () {
		batch 	   = new SpriteBatch();
		clearColor = Color.valueOf("#FAF8EFFF");
		atlas      = new TextureAtlas(Gdx.files.internal("2048.pack"));
		Settings.load();

		setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		atlas.dispose();
	}
}
