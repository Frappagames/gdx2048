package com.frappagames.gdx2048.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Settings class for the game.
 *
 * Created by jmoreau on 19/08/15.
 */
public class Settings {
    public static  Integer     bestScore;
    public static  Integer     bestCell;
    public static  Integer     bestScoreTimeGame;
    public static  Integer     bestCellTimeGame;
    private static Preferences settings;

    public static void load() {
        settings     = Gdx.app.getPreferences("com.frappagames.gdx2048.settings");
        bestScore    = settings.getInteger("best_score", 0);
    }

    public static Integer getBestScore() {
        return bestScore;
    }

    public static void setBestScore(Integer bestScore) {
        Settings.bestScore = bestScore;
        settings.putInteger("best_score", bestScore);
        settings.flush();
    }

    public static Integer getBestCell() {
        return bestCell;
    }

    public static void setBestCell(Integer bestCell) {
        Settings.bestCell = bestCell;
        settings.putInteger("best_cell", bestCell);
        settings.flush();
    }

    public static Integer getBestScoreTimeGame() {
        return bestScoreTimeGame;
    }

    public static void setBestScoreTimeGame(Integer bestScore) {
        Settings.bestScoreTimeGame = bestScore;
        settings.putInteger("best_score_time_game", bestScore);
        settings.flush();
    }

    public static Integer getBestCellTimeGame() {
        return bestCellTimeGame;
    }

    public static void setBestCellTimeGame(Integer bestCell) {
        Settings.bestCellTimeGame = bestCell;
        settings.putInteger("best_cell_time_game", bestCell);
        settings.flush();
    }
}