package com.frappagames.gdx2048.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.frappagames.gdx2048.Gdx2048;

/**
 * Created by gfp on 28/06/16.
 */
public class GameGestureListener implements GestureListener {
    private Gdx2048 game;
    float startX, startY;

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        startX = x;
        startY = y;

        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        return false;
    }

    @Override
    public boolean longPress(float x, float y) {

        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        return false;
    }

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
    public boolean zoom (float originalDistance, float currentDistance){

        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
