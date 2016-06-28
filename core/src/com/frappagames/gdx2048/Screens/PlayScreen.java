package com.frappagames.gdx2048.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.frappagames.gdx2048.Gdx2048;
import com.frappagames.gdx2048.Gdx2048.Direction;
import com.frappagames.gdx2048.Tools.GameGestureListener;
import com.frappagames.gdx2048.Tools.GameInputProcessor;
import com.frappagames.gdx2048.Tools.GameScreen;

/**
 * Main play screen
 */
public class PlayScreen extends GameScreen {
    private int[][] board;
    private int score;

    public PlayScreen(Gdx2048 game) {
        super(game);

        // Define input and gesture processors
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new GestureDetector(new GameGestureListener(this)));
        inputMultiplexer.addProcessor(new GameInputProcessor(this));
        Gdx.input.setInputProcessor(inputMultiplexer);

        board = new int[4][4];
        score = 0;

        addRandomTile();
        addRandomTile();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(float delta) {

    }

    public void move(Direction direction) {
        switch (direction) {
            case UP:
                Gdx.app.log("INFO", "Move UP");
                break;
            case DOWN:
                Gdx.app.log("INFO", "Move DOWN");
                break;
            case LEFT:
                Gdx.app.log("INFO", "Move LEFT");
                break;
            case RIGHT:
                Gdx.app.log("INFO", "Move RIGHT");
                break;
        }
    }

    /**
     * Check if a Cell is empty
     */
    private boolean checkCellAvailable() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) return true;
            }
        }

        return false;
    }

    /**
     * Check if a move is possible
     */
    private boolean checkMoveAvailable() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == board[i + 1][j]) return true;
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == board[i][j + 1]) return true;
            }
        }

        return false;
    }

    private void addRandomTile() {
        float value = Math.random() < 0.9f ? 2 : 4;
        System.out.println(value);
    }

    /**
     * Check if a Cell is empty
     */
    private boolean getAvailableCells() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) return true;
            }
        }

        return false;
    }
}
