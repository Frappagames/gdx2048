package com.frappagames.gdx2048.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frappagames.gdx2048.Gdx2048;
import com.frappagames.gdx2048.Gdx2048.GameType;
import com.frappagames.gdx2048.Tools.GameScreen;

/**
 * Menu principal
 *
 * Created by Miridan on 30/06/16.
 */
public class MenuScreen extends GameScreen {
    public static final String WEBSITE_LINK = "http://frappagames.github.io";
    private final Label moreGamesLbl;
    private final Label linkLbl;
    protected Table table;
    private final TextButton classicGameBtn;
    private final TextButton timeGameBtn;
    private final TextButton scoreBtn;
    private final TextButton howToPlayBtn;
    private final TextButton aboutBtn;
    private final TextButton exitBtn;
    private final Image      titleImg;
    private final Image      authorImg;
    private final BitmapFont font;
    private final BitmapFont font32;

    public MenuScreen(final Gdx2048 game) {
        super(game);

        titleImg = new Image(game.atlas.findRegion("title_big"));
        authorImg = new Image(game.atlas.findRegion("author"));

        Skin skin = new Skin();
        skin.addRegions(game.atlas);

        font = new BitmapFont(Gdx.files.internal("cooper-40-white.fnt"), false);
        font32 = new BitmapFont(Gdx.files.internal("cooper-32-white.fnt"), false);

        TextButtonStyle redBtnSkin = new TextButtonStyle();
        redBtnSkin.font = font;
        redBtnSkin.up = skin.getDrawable("btn_red");
        redBtnSkin.down = skin.getDrawable("btn_gray");

        TextButtonStyle yellowBtnSkin = new TextButtonStyle();
        yellowBtnSkin.font = font;
        yellowBtnSkin.up = skin.getDrawable("btn_yellow");
        yellowBtnSkin.down = skin.getDrawable("btn_gray");

        TextButtonStyle brownBtnSkin = new TextButtonStyle();
        brownBtnSkin.font = font;
        brownBtnSkin.up = skin.getDrawable("btn_brown");
        brownBtnSkin.down = skin.getDrawable("btn_gray");


        classicGameBtn = new TextButton("Partie classique", yellowBtnSkin);
        timeGameBtn    = new TextButton("Contre-la-montre", redBtnSkin);
        scoreBtn       = new TextButton("Meilleurs scores", brownBtnSkin);
        howToPlayBtn   = new TextButton("Comment jouer", brownBtnSkin);
        aboutBtn       = new TextButton("A propos de 2048", brownBtnSkin);
        exitBtn        = new TextButton("Quitter", redBtnSkin);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font32, Color.valueOf("#5F594EFF"));
        Label.LabelStyle labelStyleLink = new Label.LabelStyle(font32, Color.valueOf("#AFA08FFF"));
        moreGamesLbl = new Label("Plus de jeux sur ", labelStyle);
        linkLbl = new Label(WEBSITE_LINK, labelStyleLink);
        linkLbl.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI(WEBSITE_LINK);
            }
        });

        table = new Table();
        table.setFillParent(true);
        table.add(titleImg).pad(30, 0, 50, 0).row();
        table.add(classicGameBtn).pad(15).row();
        table.add(timeGameBtn).pad(15, 15, 50, 15).row();
//        table.add(scoreBtn).pad(15).row();
        table.add(howToPlayBtn).pad(15).row();
        table.add(aboutBtn).pad(15, 15, 15, 15).row();
        table.add(exitBtn).pad(50).row();
        table.add().height(50).row();
        table.add(authorImg).pad(15, 0, 15, 0).row();
        table.add(moreGamesLbl).pad(0, 0, 0, 0).row();
        table.add(linkLbl).pad(0, 0, 15, 0).row();

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);


        classicGameBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new PlayScreen(game, GameType.CLASSIC));
            }
        });


        timeGameBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new PlayScreen(game, GameType.TIME));
            }
        });


        scoreBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new ScoreScreen(game));
            }
        });

        howToPlayBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new HowToPlayScreen(game));
            }
        });

        aboutBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new AboutScreen(game));
            }
        });

        exitBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void draw(float delta) {
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
    }
}
