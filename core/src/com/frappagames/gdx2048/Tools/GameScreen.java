package com.frappagames.gdx2048.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.frappagames.gdx2048.Gdx2048;

/**
 * Created by gfp on 28/06/16.
 */
abstract public class GameScreen implements Screen  {
    protected final OrthographicCamera camera;
    private final Viewport viewport;
    protected Gdx2048 game;
    protected Stage stage;
    protected Stage stage2;

    public GameScreen(Gdx2048 game) {
        this.game = game;
        camera    = new OrthographicCamera();
        viewport  = new FitViewport(Gdx2048.WIDTH, Gdx2048.HEIGHT, camera);
        stage     = new Stage(viewport);
        stage2    = new Stage(viewport);

        camera.position.set(Gdx2048.WIDTH / 2, Gdx2048.HEIGHT / 2, 0);

        Gdx.graphics.setContinuousRendering(false);
    }

    @Override
    public void show() {

    }

    abstract public void update(float delta);
    abstract public void draw(float delta);

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(game.clearColor.r, game.clearColor.g, game.clearColor.b, game.clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act(delta);
        stage.draw();

        update(delta);
        draw(delta);

        stage2.act(delta);
        stage2.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
