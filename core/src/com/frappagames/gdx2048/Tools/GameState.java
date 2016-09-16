package com.frappagames.gdx2048.Tools;

import java.util.List;

/**
 * Created by gfp on 16/09/16.
 */
public class GameState {
    private int score, bestScore, cell, bestCell, movements, time;
    private List<Tile> board;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public int getBestCell() {
        return bestCell;
    }

    public void setBestCell(int bestCell) {
        this.bestCell = bestCell;
    }

    public int getMovements() {
        return movements;
    }

    public void setMovements(int movements) {
        this.movements = movements;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<Tile> getBoard() {
        return board;
    }

    public void setBoard(List<Tile> board) {
        this.board = board;
    }
}
