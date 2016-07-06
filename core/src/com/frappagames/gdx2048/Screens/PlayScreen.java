package com.frappagames.gdx2048.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.frappagames.gdx2048.Gdx2048;
import com.frappagames.gdx2048.Gdx2048.Direction;
import com.frappagames.gdx2048.Gdx2048.GameType;
import com.frappagames.gdx2048.Tools.GameGestureListener;
import com.frappagames.gdx2048.Tools.GameInputProcessor;
import com.frappagames.gdx2048.Tools.GameScreen;

/**
 * Main play screen
 */
public class PlayScreen extends GameScreen {
    private       int[][] board;
    private       int     score;

    protected     Table      table;
    private final TextButton replayBtn;
    private final TextButton menuBtn;
    private final Image      titleImg;
    private final Image      currentScoreImg;
    private final Image      bestScoreImg;
    private final Image      explanationImg;
    private final Image      backgroundImg;
    private BitmapFont       font;

    public PlayScreen(final Gdx2048 game, final GameType gameType) {
        super(game);

        titleImg        = new Image(game.atlas.findRegion("title_small"));
        currentScoreImg = new Image(game.atlas.findRegion("current_score"));
        bestScoreImg    = new Image(game.atlas.findRegion("best_score"));
        explanationImg = new Image(game.atlas.findRegion("explanation_text"));
        backgroundImg  = new Image(game.atlas.findRegion("grid"));

        Skin skin = new Skin();
        skin.addRegions(game.atlas);

        font = new BitmapFont(Gdx.files.internal("cooper-32-white.fnt"), false);

        TextButton.TextButtonStyle redBtnSkin = new TextButton.TextButtonStyle();
        redBtnSkin.font = font;
        redBtnSkin.up = skin.getDrawable("btn_red_small");
        redBtnSkin.down = skin.getDrawable("btn_small_gray");

        replayBtn = new TextButton("REJOUER", redBtnSkin);
        menuBtn   = new TextButton("MENU", redBtnSkin);


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
        table.add(currentScoreImg).pad(10);
        table.add(bestScoreImg).pad(10).row();

        table.add();
        table.add(replayBtn).pad(10);
        table.add(menuBtn).pad(10).row();

        table.add(backgroundImg).pad(100, 0, 50, 0).colspan(3).row();

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

        board = new int[4][4];
        score = 0;

        addRandomTile();
        addRandomTile();
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void draw(float delta) {
        stage.draw();
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

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
    }
}
