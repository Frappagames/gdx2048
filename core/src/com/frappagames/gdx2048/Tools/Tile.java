package com.frappagames.gdx2048.Tools;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.frappagames.gdx2048.Gdx2048;
import com.frappagames.gdx2048.Screens.PlayScreen;

import java.util.List;

/**
 * Class for the Tile object.
 * It represent a tile with its value and position and manage its rendering.
 */
public class Tile extends Image {
    private static final int CELL_SIZE = 155;
    private static final int GRID_Y = 750;
    private TextureAtlas atlas;
    private int value, nextValue;
    private Vector2 cellLocation, nextLocation;

    public Tile(TextureAtlas atlas, Vector2 cellLocation, int value) {
        super(new TextureRegionDrawable(atlas.findRegion("tile" + String.valueOf(value))), Scaling.stretch, Align.center);
        this.atlas = atlas;
        this.setCellLocation(cellLocation);
        this.setValue(value);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        this.setDrawable(
                new TextureRegionDrawable(
                        atlas.findRegion("tile" + String.valueOf(this.getValue()))
                )
        );
    }

    public void setCellLocation(Vector2 position) {
        this.cellLocation = new Vector2(position.x, position.y);
        int x = 100 + ((int) position.x * CELL_SIZE);
        int y = GRID_Y - ((int) position.y * CELL_SIZE);
        this.setPosition(x, y);
        this.nextLocation = position;
//        this.moveTo(x, y);
    }

    public void moveTo(int newX, int newY) {
        int x = 100 + (newX * CELL_SIZE);
        int y = GRID_Y - (newY * CELL_SIZE);
        this.nextLocation = new Vector2(newX, newY);
        this.setPosition(x, y);
    }

    @Override
    public String toString() {
        return (String.valueOf(this.getValue()));
    }

    public Vector2 getPosition() {
        return nextLocation;
    }

    public void updateAnDelete(int newValue, int x, int y, List<Tile> board, Tile cell2) {
        this.setValue(newValue);
        this.setCellLocation(new Vector2(x, y));
        board.remove(cell2);
        cell2.remove();
    }
}
