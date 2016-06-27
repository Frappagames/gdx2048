package com.frappagames.gdx2048.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.frappagames.gdx2048.Gdx2048;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "2048";
		config.width = 400;
		config.height = 640;
		new LwjglApplication(new Gdx2048(), config);
	}
}
