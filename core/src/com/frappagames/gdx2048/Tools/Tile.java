package com.frappagames.gdx2048.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Scaling;
import com.frappagames.gdx2048.Gdx2048;

import java.util.List;

/**
 * Class for the Tile object.
 * It represent a tile with its value and position and manage its rendering.
 */
public class Tile extends Image implements Json.Serializable {
    private static final int CELL_SIZE = 155;
    private static final int GRID_Y = 750;
    private TextureAtlas atlas;
    private int value;
    private Vector2 cellLocation, nextLocation;

    public Tile() {
        super(null, Scaling.stretch, Align.center);
        this.atlas = ((Gdx2048)Gdx.app.getApplicationListener()).getAtlas();
    }

    public Tile(TextureAtlas atlas, Vector2 cellLocation, int value) {
        super(new TextureRegionDrawable(atlas.findRegion("tile" + String.valueOf(value))), Scaling.stretch, Align.center);
        this.atlas = atlas;
        this.setCellLocation(cellLocation);
        this.setValue(value);
    }

    public int getValue() {
        return value;
    }

    private void setValue(int value) {
        this.value = value;
        this.setDrawable(
                new TextureRegionDrawable(
                        atlas.findRegion("tile" + String.valueOf(this.getValue()))
                )
        );
    }

    private void setCellLocation(Vector2 position) {
        this.cellLocation = position;
        int x = 100 + ((int) position.x * CELL_SIZE);
        int y = GRID_Y - ((int) position.y * CELL_SIZE);
        this.setPosition(x, y);
        this.nextLocation = position;
    }

    public void moveTo(int newX, int newY) {
        this.nextLocation = new Vector2(newX, newY);
    }

    public void processMove() {
        if (this.nextLocation.equals(this.cellLocation)) return;

        int x = 100 + ((int) this.nextLocation.x * CELL_SIZE);
        int y = GRID_Y - ((int) this.nextLocation.y * CELL_SIZE);

        this.addAction(Actions.sequence(
            Actions.moveTo(x, y, 0.3f, Interpolation.circle)
        ));

        this.cellLocation = this.nextLocation;
    }

    public Vector2 getPosition() {
        return nextLocation;
    }

    /**
     * Update cell1 value and delete cell2
     *
     * @param newValue  Valeur de la cellule
     * @param x         Position X
     * @param y         Position Y
     * @param board     Plateau de jeu
     * @param cell2     Cellule 2 à supprimer
     */
    public void updateAndDelete(int newValue, int x, int y, List<Tile> board, Tile cell2) {
        this.setValue(newValue);
        this.moveTo(x, y);
        board.remove(cell2);
        cell2.remove();
    }

    @Override
    public String toString() {
        return (
            this.getPosition().x
            + " : "
            + this.getPosition().y
            + " = "
            + String.valueOf(this.getValue())
        );
    }

    @Override
    public void write(Json json) {
        json.writeValue("value", value);
        json.writeValue("x", cellLocation.x);
        json.writeValue("y", cellLocation.y);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.setValue(jsonData.get("value").asInt());
        this.setCellLocation(new Vector2(jsonData.get("x").asInt(), jsonData.get("y").asInt()));
    }
}
