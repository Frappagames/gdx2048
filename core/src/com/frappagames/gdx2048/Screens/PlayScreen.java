package com.frappagames.gdx2048.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.frappagames.gdx2048.Gdx2048;
import com.frappagames.gdx2048.Gdx2048.Direction;
import com.frappagames.gdx2048.Gdx2048.GameType;
import com.frappagames.gdx2048.Tools.GameGestureListener;
import com.frappagames.gdx2048.Tools.GameInputProcessor;
import com.frappagames.gdx2048.Tools.GameScreen;
import com.frappagames.gdx2048.Tools.Tile;

import java.util.Random;

/**
 * Main play screen
 *
 * TODO * Affichage résultat de partie
 * TODO * Annimation des tuiles
 * TODO * Enregistrement et restitution du meilleur score
 * TODO * Mode de jeu contre la montre
 * TODO * Opt. : Partage de score Google Game
 * TODO * Opt. : Hauts faits Google Game
 */
public class PlayScreen extends GameScreen {
    private static final int GRID_WIDTH = 4;
    private static final int FRAME_THICKNESS = 20;
    private Tile[][] board;
    private int currentScore;
    private int currentCell;
    private int bestScore;
    private int bestCell;

    protected Table table;
    private final TextButton replayBtn;
    private final TextButton menuBtn;
    private final Image titleImg;
    private final Image explanationImg;
    private final Image gridImg;
    private BitmapFont font;
    private Label currentScoreLbl;
    private Label bestScoreLbl;
    private Random random;
    private boolean gameIsOver;

    public PlayScreen(final Gdx2048 game, final GameType gameType) {
        super(game);

        currentScore = 0;
        currentCell = 0;
        bestScore = 0;
        bestCell = 0;
        gameIsOver = false;

        titleImg = new Image(game.atlas.findRegion("title_small"));
        explanationImg = new Image(game.atlas.findRegion("explanation_text"));
        gridImg = new Image(game.atlas.findRegion("grid"));
        this.random = new Random();

        Skin skin = new Skin();
        skin.addRegions(game.atlas);

        font = new BitmapFont(Gdx.files.internal("cooper-32-white.fnt"), false);

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

        table.add(gridImg).pad(100, 0, 50, 0).colspan(3).row();

        table.add().colspan(2);
        table.add().row();

        table.add(explanationImg).colspan(3).row();

        stage.addActor(table);

        // Define input and gesture processors
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new GestureDetector(new GameGestureListener(this)));
        inputMultiplexer.addProcessor(new GameInputProcessor(this));
        Gdx.input.setInputProcessor(inputMultiplexer);

        board = new Tile[4][4];
        initializeGrid();

        addRandomTile();
        addRandomTile();
        showGrid();
    }

    public void initializeGrid() {
        int xx = 100;

        for (int x = 0; x < GRID_WIDTH; x++) {
            int yy = 720;

            for (int y = 0; y < GRID_WIDTH; y++) {
                Tile cell = new Tile(game, 0);
                cell.setCellLocation(xx, yy);
                board[x][y] = cell;
                yy -= FRAME_THICKNESS + Tile.getCellWidth();
            }
            xx += FRAME_THICKNESS + Tile.getCellWidth();
        }
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void draw(float delta) {
        showGrid();
    }

    public void move(Direction direction) {
        if (gameIsOver) return;

        boolean hasMove = false;

        switch (direction) {
            case UP:
                for (int x = 0; x < 4; x++) {
                    for (int y1 = 0; y1 < 3; y1++) {
                        for (int y2 = y1 + 1; y2 < 4; y2++) {
                            if (board[x][y1].getValue() == 0 && board[x][y2].getValue() != 0) {
                                board[x][y1].setValue(board[x][y2].getValue());
                                board[x][y2].setValue(0);
                                hasMove = true;
                            } else if (board[x][y1].getValue() != 0 && board[x][y1].getValue() == board[x][y2].getValue()) {
                                int newValue = board[x][y1].getValue() * 2;
                                board[x][y1].setValue(newValue);
                                board[x][y2].setValue(0);
                                updateScore(newValue, 0);
                                hasMove = true;
                                break;
                            } else if (board[x][y2].getValue() != 0) {
                                break;
                            }
                        }
                    }
                }
                break;
            case DOWN:
                for (int x = 0; x < 4; x++) {
                    for (int y1 = 3; y1 >= 1; y1--) {
                        for (int y2 = y1 - 1; y2 >= 0; y2--) {
                            if (board[x][y1].getValue() == 0 && board[x][y2].getValue() != 0) {
                                board[x][y1].setValue(board[x][y2].getValue());
                                board[x][y2].setValue(0);
                                hasMove = true;
                            } else if (board[x][y1].getValue() != 0 && board[x][y1].getValue() == board[x][y2].getValue()) {
                                int newValue = board[x][y1].getValue() * 2;
                                board[x][y1].setValue(newValue);
                                board[x][y2].setValue(0);
                                updateScore(newValue, 0);
                                hasMove = true;
                                break;
                            } else if (board[x][y2].getValue() != 0) {
                                break;
                            }
                        }
                    }
                }
                break;
            case LEFT:
                for (int y = 0; y < 4; y++) {
                    for (int x1 = 0; x1 < 3; x1++) {
                        for (int x2 = x1 + 1; x2 < 4; x2++) {
                            if (board[x1][y].getValue() == 0 && board[x2][y].getValue() != 0) {
                                board[x1][y].setValue(board[x2][y].getValue());
                                board[x2][y].setValue(0);
                                hasMove = true;
                            } else if (board[x1][y].getValue() != 0 && board[x1][y].getValue() == board[x2][y].getValue()) {
                                int newValue = board[x1][y].getValue() * 2;
                                board[x1][y].setValue(newValue);
                                board[x2][y].setValue(0);
                                updateScore(newValue, 0);
                                hasMove = true;
                                break;
                            } else if (board[x2][y].getValue() != 0) {
                                break;
                            }
                        }
                    }
                }
                break;
            case RIGHT:
                for (int y = 0; y < 4; y++) {
                    for (int x1 = 3; x1 >= 1; x1--) {
                        for (int x2 = x1 - 1; x2 >= 0; x2--) {
                            if (board[x1][y].getValue() == 0 && board[x2][y].getValue() != 0) {
                                board[x1][y].setValue(board[x2][y].getValue());
                                board[x2][y].setValue(0);
                                hasMove = true;
                            } else if (board[x1][y].getValue() != 0 && board[x1][y].getValue() == board[x2][y].getValue()) {
                                int newValue = board[x1][y].getValue() * 2;
                                board[x1][y].setValue(newValue);
                                board[x2][y].setValue(0);
                                updateScore(newValue, 0);
                                hasMove = true;
                                break;
                            } else if (board[x2][y].getValue() != 0) {
                                break;
                            }
                        }
                    }
                }
                break;
        }

        if (hasMove) {
            // Ajout d'une nouvelle tuile
            addRandomTile();

            // Affichage de la grille
            showGrid();

            // Vérification de l'état du jeu
            if (isGameOver()) {
                if (currentCell >= 2048) {
                    Gdx.app.log("INFO", "You WIN !!!");
                } else {
                    Gdx.app.log("INFO", "Game Over");
                }
                gameIsOver = true;
            }
        }
    }

    public void showGrid() {
        game.batch.begin();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                board[x][y].draw();
            }
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
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j].isZeroValue()) return true;
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
                if (board[i][j].getValue() == board[i + 1][j].getValue()) return true;
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].getValue() == board[i][j + 1].getValue()) return true;
            }
        }

        return false;
    }

    private void addRandomTile() {
        if (!checkCellAvailable()) return;

        int value = (random.nextInt(10) < 9) ? 2 : 4;

        boolean locationFound = false;
        while (!locationFound) {
            int x = random.nextInt(4);
            int y = random.nextInt(4);
            if (board[x][y].isZeroValue()) {
                board[x][y].setValue(value);
                locationFound = true;
            }
        }

        updateScore(0, value);
    }

    private void updateScore(int value, int cellValue) {
        // Update current score
        currentScore += value;
        currentScoreLbl.setText(String.valueOf(currentScore));

        // Update best score
        bestScore = (currentScore > bestScore) ? currentScore : bestScore;
        bestScoreLbl.setText(String.valueOf(bestScore));

        // Update best cell
        currentCell = (cellValue > currentCell) ? cellValue : currentCell;
    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
    }
}
