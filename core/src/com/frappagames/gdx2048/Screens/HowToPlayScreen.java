package com.frappagames.gdx2048.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.frappagames.gdx2048.Gdx2048;
import com.frappagames.gdx2048.Tools.GameScreen;

/**
 * Comment jouer au 2048
 *
 * Created by Miridan on 30/06/16.
 */
public class HowToPlayScreen extends GameScreen {
    private final Image      titleImg;
    private final Image      texteImg;
    private final TextButton returnBtn;
    private BitmapFont font;
    private Texture texte;
    protected Table table;

    public HowToPlayScreen(final Gdx2048 game) {
        super(game);

        titleImg = new Image(game.getAtlas().findRegion("lblHowToPlay"));
        texte = new Texture("txtHowToPlay.png");
        texteImg = new Image(texte);

        Skin skin = new Skin();
        skin.addRegions(game.getAtlas());

        font = new BitmapFont(Gdx.files.internal("cooper-40-white.fnt"), false);

        TextButton.TextButtonStyle brownBtnSkin = new TextButton.TextButtonStyle();
        brownBtnSkin.font = font;
        brownBtnSkin.up = skin.getDrawable("btn_brown");
        brownBtnSkin.down = skin.getDrawable("btn_gray");

        returnBtn = new TextButton("Retour", brownBtnSkin);
        returnBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });

        table = new Table();
        table.setFillParent(true);
        table.add(titleImg).pad(60).row();
        table.add(texteImg).row();
        table.add(returnBtn).pad(60).row();

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(float delta) {

    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
        texte.dispose();
    }
}
