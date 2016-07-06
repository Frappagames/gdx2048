package com.frappagames.gdx2048.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.frappagames.gdx2048.Gdx2048;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(400, 640);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new Gdx2048();
        }
}
