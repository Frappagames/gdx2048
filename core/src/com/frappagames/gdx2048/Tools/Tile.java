package com.frappagames.gdx2048.Tools;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by gfp on 11/07/16.
 */
public class Tile {
    private static final int CELL_WIDTH = 120;
    private int value;

    private Vector2 cellLocation;

    public Tile(int value) {
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

    public void draw(Batch batch) {
    }

    @Override
    public String toString() {
        return (String.valueOf(this.getValue()));
    }
}
