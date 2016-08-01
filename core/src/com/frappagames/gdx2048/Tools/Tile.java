package com.frappagames.gdx2048.Tools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.frappagames.gdx2048.Gdx2048;

/**
 * Created by gfp on 11/07/16.
 */
public class Tile {
    private static final int CELL_WIDTH = 135;
    private final SpriteBatch batch;
    private final TextureAtlas atlas;
    private int value;

    private Vector2 cellLocation;

    public Tile(Gdx2048 game, int value) {
        this.batch = game.batch;
        this.atlas = game.atlas;
        setValue(value);
    }

    public static int getCellWidth() {
        return CELL_WIDTH;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isZeroValue() {
        return (value == 0);
    }

    public void setCellLocation(int x, int y) {
        setCellLocation(new Vector2(x, y));
    }

    public void setCellLocation(Vector2 cellLocation) {
        this.cellLocation = cellLocation;
    }

    public void draw() {
        if (!isZeroValue()) {
            batch.draw(
                    atlas.findRegion("tile" + String.valueOf(this.getValue())),
                    cellLocation.x,
                    cellLocation.y
            );
        }
    }

    @Override
    public String toString() {
        return (String.valueOf(this.getValue()));
    }
}
