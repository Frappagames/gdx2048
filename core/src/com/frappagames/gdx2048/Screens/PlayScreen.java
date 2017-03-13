package com.frappagames.gdx2048.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.TimeUtils;
import com.frappagames.gdx2048.Gdx2048;
import com.frappagames.gdx2048.Gdx2048.Direction;
import com.frappagames.gdx2048.Gdx2048.GameType;
import com.frappagames.gdx2048.Tools.GameGestureListener;
import com.frappagames.gdx2048.Tools.GameInputProcessor;
import com.frappagames.gdx2048.Tools.GameScreen;
import com.frappagames.gdx2048.Tools.Settings;
import com.frappagames.gdx2048.Tools.Tile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Main play screen
 *
 * TODO * Opt. : Partage de score Google Game
 * TODO * Opt. : Hauts faits Google Game
 */
public class PlayScreen extends GameScreen {
    private static final int GRID_WIDTH = 4, SPAWN_SPEED_MS = 1000, MASK_Y = 265, SCORE_Y = 1150;
    private long startTime, lastSpawnTime;
    private boolean gameIsOver;
    private Label gameOverLbl, timeLbl, movementsLbl;
    private Label addScoreLbl, currentScoreLbl, bestScoreLbl;
    private GameType gameType;
    private BitmapFont font;
    private Random random;
    private int elapseTime;

    private List<Tile> board;
    private int currentScore, currentCell, bestScore, bestCell, movements;
    private int addScore;
    private boolean hasMove;

    PlayScreen(final Gdx2048 game, final GameType gameType) {
        super(game);

        if (gameType == GameType.TIME) {
            Gdx.graphics.setContinuousRendering(true);
            startTime = TimeUtils.nanoTime();
        }

        this.gameType      = gameType;
        this.currentScore  = 0;
        this.currentCell   = 0;
        this.elapseTime    = 0;
        this.bestScore     = (gameType == GameType.CLASSIC) ? Settings.getBestScore() : Settings.getBestScoreTimeGame();
        this.bestCell      = (gameType == GameType.CLASSIC) ? Settings.getBestCell() : Settings.getBestCellTimeGame();
        this.gameIsOver    = false;
        this.font          = new BitmapFont(Gdx.files.internal("cooper-32-white.fnt"), false);
        this.random        = new Random();

        // Define input and gesture processors
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new GestureDetector(new GameGestureListener(this)));
        inputMultiplexer.addProcessor(new GameInputProcessor(this));
        Gdx.input.setInputProcessor(inputMultiplexer);

        board = new ArrayList<Tile>();

        initializeGrid();

        Gdx.graphics.requestRendering();
    }

    private void initializeGrid() {
        Image titleImg = new Image(this.game.getAtlas().findRegion("title_small"));
        Image explanationImg = new Image(this.game.getAtlas().findRegion("explanation_text"));
        Image gridImg = new Image(this.game.getAtlas().findRegion("grid"));

        Skin skin = new Skin();
        skin.addRegions(this.game.getAtlas());

        TextButton.TextButtonStyle redBtnSkin = new TextButton.TextButtonStyle();
        redBtnSkin.font = font;
        redBtnSkin.up = skin.getDrawable("btn_red_small");
        redBtnSkin.down = skin.getDrawable("btn_small_gray");

        TextButton replayBtn = new TextButton("REJOUER", redBtnSkin);
        TextButton menuBtn = new TextButton("MENU", redBtnSkin);


        LabelStyle labelStyle = new LabelStyle(font, Color.WHITE);
        currentScoreLbl = new Label(String.valueOf(currentScore), labelStyle);
        currentScoreLbl.setAlignment(Align.center, Align.bottom);

        Table currentScoreContainer = new Table();
        currentScoreContainer.add(currentScoreLbl).pad(70, 10, 10, 10);
        currentScoreContainer.setBackground(skin.getDrawable("current_score"));

        bestScoreLbl = new Label(String.valueOf(bestScore), labelStyle);
        bestScoreLbl.setAlignment(Align.center, Align.bottom);

        Table bestScoreContainer = new Table();
        bestScoreContainer.add(bestScoreLbl).pad(70, 10, 10, 10);
        bestScoreContainer.setBackground(skin.getDrawable("best_score"));

        LabelStyle labelStyleMovementsAndTime = new LabelStyle(font, Color.valueOf("#AFA08FFF"));
        movementsLbl = new Label("", labelStyleMovementsAndTime);
        timeLbl = new Label("", labelStyleMovementsAndTime);

        LabelStyle labelStyleGameOver = new LabelStyle(font, Color.WHITE);
        gameOverLbl = new Label("", labelStyleGameOver);

        LabelStyle labelStyleAddScore = new LabelStyle(font, Color.valueOf("#E8BB31FF"));
        addScoreLbl = new Label("", labelStyleAddScore);
        addScoreLbl.setAlignment(Align.center, Align.bottom);
        addScoreLbl.setPosition(305, SCORE_Y);
        addScoreLbl.setWidth(200);
        addScoreLbl.setVisible(false);
        stage2.addActor(addScoreLbl);
        movementsLbl.setText("0 déplacement");

        replayBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                resetGame();
                game.setScreen(new PlayScreen(game, gameType));
            }
        });

        menuBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });

        Table table = new Table();
        table.setFillParent(true);

        table.add(titleImg).pad(0, 0, 0, 30);
        table.add(currentScoreContainer);
        table.add(bestScoreContainer).row();

        table.add();
        table.add(replayBtn).pad(10);
        table.add(menuBtn).pad(10).row();

        table.add(gridImg).pad(135, 0, 5, 0).colspan(3).row();

        table.add(movementsLbl).colspan(2).align(Align.left).pad(0, 15, 60, 0);
        table.add(timeLbl).pad(0, 0, 60, 15).align(Align.right).row();

        table.add(explanationImg).pad(50, 0, 0, 0).colspan(3).row();

        stage.addActor(table);

        // Define Game OVer Screen
        gameOverLbl.setSize(640, 640);
        gameOverLbl.setAlignment(Align.center);
        gameOverLbl.setPosition(80, MASK_Y);
        gameOverLbl.setVisible(false);
        gameOverLbl.getStyle().background = skin.getDrawable("mask_yellow");

        stage2.addActor(gameOverLbl);
    }

    private void resetGame() {
        // Suppression des tuiles actuelles
        clearBoard();

        // Ajout de nouvelles tuiles
        addRandomTile();
        addRandomTile();
    }

    private String formatTime(int time) {
        Integer minutes = ((int) Math.floor(time / 60));
        Integer seconds = time % 60;
        String result = "";

        result += String.valueOf(minutes);
        result += ":";

        if (seconds > 10) {
            result += String.valueOf(seconds);
        } else {
            result += "0" + String.valueOf(seconds);
        }

        return result;
    }

    @Override
    public void update(float delta) {
        // check if we need to add a new cell
        if (!gameIsOver && gameType == GameType.TIME) {

            this.elapseTime = Math.round((TimeUtils.nanoTime() - startTime) / 1000000000);
            timeLbl.setText(formatTime(this.elapseTime));

            if (TimeUtils.nanoTime() - lastSpawnTime > (SPAWN_SPEED_MS * 1000000)) {
                if (checkCellAvailable()) {
                    updateScore(5, 0);
                    addRandomTile();
                } else {
                    setGameOver();
                }
            }
        }
    }

    @Override
    public void draw(float delta) {
        showGrid();
    }

    private void joinCells(int x1, int y1, int x2, int y2) {
        Tile cell = getCellAt(new Vector2(x1, y1));
        Tile cell2 = getCellAt(new Vector2(x2, y2));

        if (cell != null && cell2 != null && cell.getValue() == cell2.getValue()) {
            int newValue = 2 * cell.getValue();
            cell2.updateAndDelete(newValue, x1, y1, board, cell);
            this.addScore += newValue;
            this.hasMove = true;
        }
    }

    public void move(Direction direction) {
        if (gameIsOver) return;

        addScore = 0;
        hasMove = false;

        switch (direction) {
            case UP:
                for (int x = 0; x < 4; x++) {
                    // Move cells to empty cells
                    for (int y = 0; y < 3; y++) {
                        if (!cellExists(new Vector2(x, y))) {
                            for (int y2 = y + 1; y2 < 4; y2++) {
                                Tile cell = getCellAt(new Vector2(x, y2));

                                if (cell != null) {
                                    cell.moveTo(x, y);
                                    hasMove = true;
                                    break;
                                }
                            }
                        }
                    }

                    // Join cells
                    for (int y = 0; y < 3; y++) {
                        joinCells(x, y, x, y + 1);
                    }

                    // Move cells to empty cells
                    for (int y = 0; y < 3; y++) {
                        if (!cellExists(new Vector2(x, y))) {
                            for (int y2 = y + 1; y2 < 4; y2++) {
                                Tile cell = getCellAt(new Vector2(x, y2));

                                if (cell != null) {
                                    cell.moveTo(x, y);
                                    hasMove = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case DOWN:
                for (int x = 3; x >= 0; x--) {
                    // Move cells to empty cells
                    for (int y = 3; y > 0; y--) {
                        if (!cellExists(new Vector2(x, y))) {
                            for (int y2 = y - 1; y2 >= 0; y2--) {
                                Tile cell = getCellAt(new Vector2(x, y2));

                                if (cell != null) {
                                    cell.moveTo(x, y);
                                    hasMove = true;
                                    break;
                                }
                            }
                        }
                    }

                    // Join cells
                    for (int y = 3; y > 0; y--) {
                        joinCells(x, y, x, y - 1);
                    }

                    // Move cells to empty cells
                    for (int y = 3; y > 0; y--) {
                        if (!cellExists(new Vector2(x, y))) {
                            for (int y2 = y - 1; y2 >= 0; y2--) {
                                Tile cell = getCellAt(new Vector2(x, y2));

                                if (cell != null) {
                                    cell.moveTo(x, y);
                                    hasMove = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case LEFT:
                for (int y = 0; y < 4; y++) {
                    // Move cells to empty cells
                    for (int x = 0; x < 3; x++) {
                        if (!cellExists(new Vector2(x, y))) {
                            for (int x2 = x + 1; x2 < 4; x2++) {
                                Tile cell = getCellAt(new Vector2(x2, y));

                                if (cell != null) {
                                    cell.moveTo(x, y);
                                    hasMove = true;
                                    break;
                                }
                            }
                        }
                    }

                    // Join cells
                    for (int x = 0; x < 3; x++) {
                        joinCells(x, y, x + 1, y);
                    }

                    // Move cells to empty cells
                    for (int x = 0; x < 3; x++) {
                        if (!cellExists(new Vector2(x, y))) {
                            for (int x2 = x + 1; x2 < 4; x2++) {
                                Tile cell = getCellAt(new Vector2(x2, y));

                                if (cell != null) {
                                    cell.moveTo(x, y);
                                    hasMove = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case RIGHT:
                for (int y = 3; y >= 0; y--) {
                    // Move cells to empty cells
                    for (int x = 3; x > 0; x--) {
                        if (!cellExists(new Vector2(x, y))) {
                            for (int x2 = x - 1; x2 >= 0; x2--) {
                                Tile cell = getCellAt(new Vector2(x2, y));

                                if (cell != null) {
                                    cell.moveTo(x, y);
                                    hasMove = true;
                                    break;
                                }
                            }
                        }
                    }

                    // Join cells
                    for (int x = 3; x > 0; x--) {
                        joinCells(x, y, x - 1, y);
                    }

                    // Move cells to empty cells
                    for (int x = 3; x > 0; x--) {
                        if (!cellExists(new Vector2(x, y))) {
                            for (int x2 = x - 1; x2 >= 0; x2--) {
                                Tile cell = getCellAt(new Vector2(x2, y));

                                if (cell != null) {
                                    cell.moveTo(x, y);
                                    hasMove = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
        }

        if ((gameType == GameType.TIME) && (addScore != 0)) {
            addScore += 5;
        }
        updateScore(addScore, 0);

        if (hasMove) {
            movements++;
            movementsLbl.setText(movements + " déplacements");

            for (Tile cell : board) {
                cell.processMove();
            }

            // Ajout d'une nouvelle tuile
            stage.addAction(Actions.sequence(
                Actions.delay(0.3f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            addRandomTile();

                            // Vérification de l'état du jeu
                            if (isGameOver()) {
                                setGameOver();
                            }
                        }
                    })
            ));

            // Affichage de la grille
            Gdx.graphics.requestRendering();
        }
    }

    private void setGameOver() {
        if (currentCell >= 2048) {
            gameOverLbl.setText("Vous avez GAGNÉ !!!");
        } else {
            gameOverLbl.setText("Vous avez perdu !");
        }
        gameOverLbl.setVisible(true);
        AlphaAction resetAlphaAction = new AlphaAction();
        resetAlphaAction.setAlpha(0);
        gameOverLbl.addAction(resetAlphaAction);

        AlphaAction setAlphaAction = new AlphaAction();
        setAlphaAction.setAlpha(1);
        setAlphaAction.setDuration(0.5f);
        setAlphaAction.finish();
        gameOverLbl.addAction(setAlphaAction);
        gameIsOver = true;
    }

    private void showGrid() {
        game.batch.begin();
        for (Tile cell : board) {
            cell.draw(game.batch, 1);
        }
        game.batch.end();
    }

    private boolean isGameOver() {
        return !(checkCellAvailable() || checkMoveAvailable());
    }

    /**
     * Check if a Cell is empty
     */
    private boolean checkCellAvailable() {
        return board.size() < GRID_WIDTH * GRID_WIDTH;
    }

    /**
     * Check if a move is possible
     */
    private boolean checkMoveAvailable() {
        for (Tile cell1 : board) {
            Vector2 positionRight = new Vector2(cell1.getPosition().x + 1, cell1.getPosition().y);
            Vector2 positionDown  = new Vector2(cell1.getPosition().x, cell1.getPosition().y + 1);

            for (Tile cell2 : board) {
                if ((cell2.getPosition().equals(positionRight) || cell2.getPosition().equals(positionDown))
                        && cell1.getValue() == cell2.getValue()) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean cellExists(Vector2 position) {
        return this.getCellAt(position) != null;
    }

    private Tile getCellAt(Vector2 position) {
        for (Tile cell : board) {
            if (cell.getPosition().equals(position)) {
                return cell;
            }
        }

        return null;
    }

    private void addRandomTile() {
        if (!checkCellAvailable()) return;

        int value = (random.nextInt(10) < 9) ? 2 : 4;

        boolean locationFound = false;
        while (!locationFound) {
            int x = random.nextInt(4);
            int y = random.nextInt(4);
            Vector2 position = new Vector2(x, y);

            if (!cellExists(position)) {
                Tile newCell = new Tile(this.game.getAtlas(), position, value);
                newCell.addAction(Actions.sequence(
                    Actions.alpha(0),
                    Actions.alpha(1, 0.5f, Interpolation.circleOut)
                ));
                board.add(newCell);
                stage.addActor(newCell);
                locationFound = true;
            }
        }

        updateScore(0, value);
        lastSpawnTime = TimeUtils.nanoTime();
    }

    private void updateScore(int value, int cellValue) {
        // Update current score
        if (value > 0) {
            addScoreLbl.setText("+" + String.valueOf(value));
            addScoreLbl.setVisible(true);
            addScoreLbl.addAction(Actions.sequence(
                    Actions.alpha(1),
                    Actions.moveTo(305, SCORE_Y),
                    Actions.parallel(
                            Actions.fadeOut(0.5f, Interpolation.circleIn),
                            Actions.moveTo(305, SCORE_Y + 30, 0.5f, Interpolation.circleOut)
                    )
            ));
            currentScore += value;
            currentScoreLbl.setText(String.valueOf(currentScore));
        }

        // Update best score
        if (currentScore > bestScore) {
            bestScore = currentScore;
            if (gameType == GameType.CLASSIC) {
                Settings.setBestScore(currentScore);
            } else {
                Settings.setBestScoreTimeGame(currentScore);
            }
            bestScoreLbl.setText(String.valueOf(bestScore));

        }

        // Update best cell
        if (cellValue > bestCell) {
            bestCell = cellValue;
            if (gameType == GameType.CLASSIC) {
                Settings.setBestCell(cellValue);
            } else {
                Settings.setBestCellTimeGame(cellValue);
            }
        }
    }

    private Preferences getSaveFile() {
        if (this.gameType == GameType.TIME) {
            return Gdx.app.getPreferences("com.frappagames.gdx2048.saveTimeGame");
        } else {
            return Gdx.app.getPreferences("com.frappagames.gdx2048.saveClassicGame");
        }
    }

    private void save() {
        if (this.gameIsOver) {
            resetGame();
        }

        Preferences saveFile = this.getSaveFile();
        Json json = new Json();

        saveFile.putInteger("currentScore", this.currentScore);
        saveFile.putInteger("currentCell", this.currentCell);
        saveFile.putInteger("bestCell", this.bestCell);
        saveFile.putInteger("movements", this.movements);
        saveFile.putInteger("elapseTime", this.elapseTime);
        saveFile.putString("board", json.toJson(this.board));
        saveFile.flush();
    }

    private void restore() {
        Preferences saveFile = this.getSaveFile();
        Json json = new Json();
        List<Tile> savedBoard;

        String gameBoard = saveFile.getString("board", "");
        if (!gameBoard.equals("") && !gameBoard.equals("NULL")) {
            // Suppression des tuiles actuelles
            this.clearBoard();

            // Ajout des tuiles sauvegardées
            savedBoard = json.fromJson(List.class, gameBoard);

            this.currentScore = saveFile.getInteger("currentScore", 0);
            this.currentCell = saveFile.getInteger("currentCell", 0);
            this.bestCell = saveFile.getInteger("bestCell", 0);
            this.movements = saveFile.getInteger("movements", 0);
            this.elapseTime = saveFile.getInteger("elapseTime", 0);

            for (Tile cell : savedBoard) {
                Tile newCell = new Tile(this.game.getAtlas(), cell.getPosition(), cell.getValue());
                board.add(newCell);
                stage.addActor(newCell);
            }

            // Mise à jour des scores
            currentScoreLbl.setText(String.valueOf(currentScore));
            bestScoreLbl.setText(String.valueOf(bestScore));

            // Mise à jour des déplacements et du temps
            movementsLbl.setText(movements + " déplacements");

            if (!gameIsOver && gameType == GameType.TIME) {
                timeLbl.setText(formatTime(this.elapseTime));
            }
        } else {
            addRandomTile();
            addRandomTile();
        }

        // Vérification de l'état du jeu
        if (isGameOver()) {
            setGameOver();
        }
    }

    /**
     *  Suppression des tuiles actuelles
     */
    private void clearBoard() {
        if (!this.board.isEmpty()) {
            for (Iterator<Tile> iterator = this.board.iterator(); iterator.hasNext(); ) {
                iterator.next();
                iterator.remove();
            }
            stage.getActors().clear();
            stage2.getActors().clear();
            initializeGrid();
        }

        this.currentScore = 0;
        this.currentCell  = 0;
        this.movements    = 0;
        this.elapseTime   = 0;

        // Mise à jour des scores
        currentScoreLbl.setText("0");

        // Mise à jour des déplacements et du temps
        movementsLbl.setText("0 déplacement");

        if (!gameIsOver && gameType == GameType.TIME) {
            timeLbl.setText(formatTime(0));
        }
    }

    @Override
    public void pause() {
        this.save();
    }

    @Override
    public void hide() {
        this.save();
    }

    @Override
    public void resume() {
        this.restore();
    }

    @Override
    public void show() {
        this.restore();
    }

    @Override
    public void dispose() {
        this.save();
        super.dispose();
        font.dispose();
    }
}
