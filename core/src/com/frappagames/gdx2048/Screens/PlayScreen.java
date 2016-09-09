package com.frappagames.gdx2048.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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
import java.util.List;
import java.util.Random;

/**
 * Main play screen
 *
 * TODO * Animation des tuiles
 * TODO : Sauvegarde et restauration de partie
 * TODO * Opt. : Partage de score Google Game
 * TODO * Opt. : Hauts faits Google Game
 */
public class PlayScreen extends GameScreen {
    private static final int GRID_WIDTH = 4;
    private static final int SPAWN_SPEED_MS = 1000;
    private static final int MASK_Y = 265;
    private static final int SCORE_Y = 1150;
    private final Label gameOverLbl;
    private final Label timeLbl;
    private final Label movementsLbl;
    private Label addScoreLbl;
    private Label currentScoreLbl;
    private Label bestScoreLbl;
//    private Tile[][] board;
    private List<Tile> board;
    private int currentScore;
    private int currentCell;
    private int bestScore;
    private int bestCell;
    private int movements;
    private GameType gameType;
    private long startTime;
    private long lastSpawnTime;

    protected Table table;
    private final TextButton replayBtn;
    private final TextButton menuBtn;
    private final Image titleImg;
    private final Image explanationImg;
    private final Image gridImg;
    private BitmapFont font;
    private Random random;
    private boolean gameIsOver;

    public PlayScreen(final Gdx2048 game, final GameType gameType) {
        super(game);

        if (gameType == GameType.TIME) {
            Gdx.graphics.setContinuousRendering(true);
            startTime = TimeUtils.nanoTime();
        }

        this.gameType = gameType;
        currentScore  = 0;
        currentCell   = 0;
        bestScore     = (gameType == GameType.CLASSIC) ? Settings.getBestScore() : Settings.getBestScoreTimeGame();
        bestCell      = (gameType == GameType.CLASSIC) ? Settings.getBestCell() : Settings.getBestCellTimeGame();
        gameIsOver    = false;

        titleImg       = new Image(game.atlas.findRegion("title_small"));
        explanationImg = new Image(game.atlas.findRegion("explanation_text"));
        gridImg        = new Image(game.atlas.findRegion("grid"));
        font           = new BitmapFont(Gdx.files.internal("cooper-32-white.fnt"), false);
        this.random    = new Random();

        Skin skin = new Skin();
        skin.addRegions(game.atlas);

        TextButton.TextButtonStyle redBtnSkin = new TextButton.TextButtonStyle();
        redBtnSkin.font = font;
        redBtnSkin.up = skin.getDrawable("btn_red_small");
        redBtnSkin.down = skin.getDrawable("btn_small_gray");

        replayBtn = new TextButton("REJOUER", redBtnSkin);
        menuBtn = new TextButton("MENU", redBtnSkin);


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

        LabelStyle labelStyleAddScore = new LabelStyle(font, Color.valueOf("#E8BB31FF"));
        addScoreLbl = new Label("", labelStyleAddScore);
        addScoreLbl.setAlignment(Align.center, Align.bottom);
        addScoreLbl.setPosition(305, SCORE_Y);
        addScoreLbl.setWidth(200);
        addScoreLbl.setVisible(false);
        stage2.addActor(addScoreLbl);


        LabelStyle labelStyleMovementsAndTime = new LabelStyle(font, Color.valueOf("#AFA08FFF"));
        movementsLbl = new Label(movements + " déplacements", labelStyleMovementsAndTime);
        timeLbl = new Label("", labelStyleMovementsAndTime);

        replayBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new PlayScreen(game, gameType));
            }
        });

        menuBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });

        table = new Table();
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


        stage.addActor(new Tile(game.atlas, new Vector2(0, 0), 2048));

        // Define input and gesture processors
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new GestureDetector(new GameGestureListener(this)));
        inputMultiplexer.addProcessor(new GameInputProcessor(this));
        Gdx.input.setInputProcessor(inputMultiplexer);

        board = new ArrayList<Tile>();

        // Define Game OVer Screen
        BitmapFont font40 = new BitmapFont(Gdx.files.internal("cooper-40-white.fnt"), false);
        labelStyleMovementsAndTime = new LabelStyle(font40, Color.WHITE);
        labelStyleMovementsAndTime.background = skin.getDrawable("mask_yellow");
        gameOverLbl = new Label("", labelStyleMovementsAndTime);
        gameOverLbl.setSize(640, 640);
        gameOverLbl.setAlignment(Align.center);
        gameOverLbl.setPosition(80, MASK_Y);
        gameOverLbl.setVisible(false);

        stage2.addActor(gameOverLbl);

        addRandomTile();
        addRandomTile();
        Gdx.graphics.requestRendering();
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

            int elapseTime = Math.round((TimeUtils.nanoTime() - startTime) / 1000000000);
            timeLbl.setText(formatTime(elapseTime));

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
        showGrid(delta);
    }

    public void move(Direction direction) {
        if (gameIsOver) return;

        boolean hasMove = false;
        int addScore = 0;

//        switch (direction) {
//            case UP:
//                for (int x = 0; x < 4; x++) {
//                    for (int y1 = 0; y1 < 3; y1++) {
//                        for (int y2 = y1 + 1; y2 < 4; y2++) {
//                            if (board[x][y1].getValue() == 0 && board[x][y2].getValue() != 0) {
//                                board[x][y1].setValue(board[x][y2].getValue());
//                                board[x][y2].setValue(0);
//                                hasMove = true;
//                            } else if (board[x][y1].getValue() != 0 && board[x][y1].getValue() == board[x][y2].getValue()) {
//                                int newValue = board[x][y1].getValue() * 2;
//                                board[x][y1].setValue(newValue);
//                                board[x][y2].setValue(0);
//                                addScore += newValue;
//                                hasMove = true;
//                                break;
//                            } else if (board[x][y2].getValue() != 0) {
//                                break;
//                            }
//                        }
//                    }
//                }
//                break;
//            case DOWN:
//                for (int x = 0; x < 4; x++) {
//                    for (int y1 = 3; y1 >= 1; y1--) {
//                        for (int y2 = y1 - 1; y2 >= 0; y2--) {
//                            if (board[x][y1].getValue() == 0 && board[x][y2].getValue() != 0) {
//                                board[x][y1].setValue(board[x][y2].getValue());
//                                board[x][y2].setValue(0);
//                                hasMove = true;
//                            } else if (board[x][y1].getValue() != 0 && board[x][y1].getValue() == board[x][y2].getValue()) {
//                                int newValue = board[x][y1].getValue() * 2;
//                                board[x][y1].setValue(newValue);
//                                board[x][y2].setValue(0);
//                                addScore += newValue;
//                                hasMove = true;
//                                break;
//                            } else if (board[x][y2].getValue() != 0) {
//                                break;
//                            }
//                        }
//                    }
//                }
//                break;
//            case LEFT:
//                for (int y = 0; y < 4; y++) {
//                    for (int x1 = 0; x1 < 3; x1++) {
//                        for (int x2 = x1 + 1; x2 < 4; x2++) {
//                            if (board[x1][y].getValue() == 0 && board[x2][y].getValue() != 0) {
//                                board[x1][y].setValue(board[x2][y].getValue());
//                                board[x2][y].setValue(0);
//                                hasMove = true;
//                            } else if (board[x1][y].getValue() != 0 && board[x1][y].getValue() == board[x2][y].getValue()) {
//                                int newValue = board[x1][y].getValue() * 2;
//                                board[x1][y].setValue(newValue);
//                                board[x2][y].setValue(0);
//                                addScore += newValue;
//                                hasMove = true;
//                                break;
//                            } else if (board[x2][y].getValue() != 0) {
//                                break;
//                            }
//                        }
//                    }
//                }
//                break;
//            case RIGHT:
//                for (int y = 0; y < 4; y++) {
//                    for (int x1 = 3; x1 >= 1; x1--) {
//                        for (int x2 = x1 - 1; x2 >= 0; x2--) {
//                            if (board[x1][y].getValue() == 0 && board[x2][y].getValue() != 0) {
//                                board[x1][y].setValue(board[x2][y].getValue());
//                                board[x2][y].setValue(0);
//                                hasMove = true;
//                            } else if (board[x1][y].getValue() != 0 && board[x1][y].getValue() == board[x2][y].getValue()) {
//                                int newValue = board[x1][y].getValue() * 2;
//                                board[x1][y].setValue(newValue);
//                                board[x2][y].setValue(0);
//                                addScore += newValue;
//                                hasMove = true;
//                                break;
//                            } else if (board[x2][y].getValue() != 0) {
//                                break;
//                            }
//                        }
//                    }
//                }
//                break;
//        }

        if ((gameType == GameType.TIME) && (addScore != 0)) {
            addScore += 5;
        }
        updateScore(addScore, 0);

        if (hasMove) {
            movements++;
            movementsLbl.setText(movements + " déplacements");

            // Ajout d'une nouvelle tuile
            addRandomTile();

            // Affichage de la grille
            Gdx.graphics.requestRendering();

            // Vérification de l'état du jeu
            if (isGameOver()) {
                setGameOver();
            }
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

    public void showGrid(float delta) {
        game.batch.begin();
        for (Tile cell : board) {
            cell.draw(game.batch, delta);
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
                if ((cell2.getPosition() == positionRight || cell2.getPosition() == positionDown)
                        && cell1.getValue() == cell2.getValue()) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean cellExists(Vector2 position) {
        for (Tile cell : board) {
            if (cell.getPosition() == position) {
                return true;
            }
        }

        return false;
    }

    private Tile getCellAt(Vector2 position) {
        for (Tile cell : board) {
            if (cell.getPosition() == position) {
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
                Tile newCell = new Tile(game.atlas, position, value);
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

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
    }
}
