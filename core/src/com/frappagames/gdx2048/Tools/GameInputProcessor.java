package com.frappagames.gdx2048.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by gfp on 28/06/16.
 */
public class GameInputProcessor implements InputProcessor {
    public boolean keyDown (int keycode) {
        return false;
    }

    public boolean keyUp (int keycode) {
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

        return false;
    }

    public boolean keyTyped (char character) {
        return false;
    }

    public boolean touchDown (int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchUp (int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchDragged (int x, int y, int pointer) {
        return false;
    }

    public boolean mouseMoved (int x, int y) {
        return false;
    }

    public boolean scrolled (int amount) {
        return false;
    }
}
