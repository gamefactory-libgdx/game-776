package com.neonpong958967.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.neonpong958967.app.Constants;
import com.neonpong958967.app.MainGame;

/**
 * Pause overlay shown when the player pauses during gameplay.
 *
 * <ul>
 *   <li>RESUME   — returns to the existing {@link GameScreen} instance.</li>
 *   <li>RESTART  — creates a new {@link GameScreen} with the same difficulty/theme.</li>
 *   <li>MAIN MENU — returns to {@link MainMenuScreen}.</li>
 * </ul>
 */
public class PauseScreen implements Screen {

    private final MainGame game;
    private final Screen resumeTarget;   // the GameScreen to resume
    private final int difficulty;
    private final int theme;

    private final OrthographicCamera camera;
    private final StretchViewport viewport;
    private final Stage stage;
    private final ShapeRenderer sr;

    // Modal dimensions
    private static final float MODAL_W = 400f;
    private static final float MODAL_H = 280f;
    private static final float MODAL_X = (Constants.WORLD_WIDTH  - MODAL_W) * 0.5f;
    private static final float MODAL_Y = (Constants.WORLD_HEIGHT - MODAL_H) * 0.5f;

    // Buttons stacked vertically inside the modal
    private static final float BTN_W    = 280f;
    private static final float BTN_H    = 52f;
    private static final float BTN_X    = MODAL_X + (MODAL_W - BTN_W) * 0.5f;
    private static final float BTN_GAP  = 14f;

    private static final float BTN_RESUME_Y    = MODAL_Y + 30f + 2 * (BTN_H + BTN_GAP);
    private static final float BTN_RESTART_Y   = MODAL_Y + 30f + (BTN_H + BTN_GAP);
    private static final float BTN_MENU_Y      = MODAL_Y + 30f;

    private static final Color COLOR_PRIMARY = new Color(0f, 1f, 0.255f, 1f);

    public PauseScreen(MainGame game, Screen resumeTarget, int difficulty, int theme) {
        this.game         = game;
        this.resumeTarget = resumeTarget;
        this.difficulty   = difficulty;
        this.theme        = theme;

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);
        sr       = new ShapeRenderer();

        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 pos = new Vector3(screenX, screenY, 0);
                camera.unproject(pos);
                handleTouch(pos.x, pos.y);
                return false;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    doResume();
                    return true;
                }
                return false;
            }
        }));

        // Pause music while on pause screen
        if (game.currentMusic != null && game.currentMusic.isPlaying()) {
            game.currentMusic.pause();
        }
    }

    private void handleTouch(float wx, float wy) {
        if (UiFactory.isHit(wx, wy, BTN_X, BTN_RESUME_Y, BTN_W, BTN_H)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            doResume();
        } else if (UiFactory.isHit(wx, wy, BTN_X, BTN_RESTART_Y, BTN_W, BTN_H)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            game.setScreen(new GameScreen(game, difficulty, theme));
        } else if (UiFactory.isHit(wx, wy, BTN_X, BTN_MENU_Y, BTN_W, BTN_H)) {
            playSfx("sounds/sfx/sfx_button_back.ogg");
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void doResume() {
        // Resume music
        if (game.currentMusic != null && game.musicEnabled) {
            game.currentMusic.play();
        }
        game.setScreen(resumeTarget);
    }

    private void playSfx(String path) {
        if (game.sfxEnabled)
            game.manager.get(path, com.badlogic.gdx.audio.Sound.class).play(1.0f);
    }

    @Override
    public void render(float delta) {
        // Clear with a dark transparent overlay
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        // Full-screen dark overlay
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Overlay background
        sr.setColor(0f, 0f, 0f, 0.75f);
        sr.rect(0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        // Modal border (#00FF41)
        sr.setColor(COLOR_PRIMARY.r, COLOR_PRIMARY.g, COLOR_PRIMARY.b, 1f);
        sr.rect(MODAL_X, MODAL_Y, MODAL_W, MODAL_H);

        // Modal fill
        sr.setColor(0f, 0f, 0f, 0.92f);
        sr.rect(MODAL_X + 3, MODAL_Y + 3, MODAL_W - 6, MODAL_H - 6);

        sr.end();

        // Buttons & text
        game.batch.begin();

        // "PAUSED" title
        GlyphLayout titleLayout = new GlyphLayout(game.fontTitle, "PAUSED");
        game.fontTitle.setColor(COLOR_PRIMARY);
        game.fontTitle.draw(game.batch, "PAUSED",
                MODAL_X + (MODAL_W - titleLayout.width) * 0.5f,
                MODAL_Y + MODAL_H - 20f);

        // Buttons
        UiFactory.drawButton(sr, game.batch, game.fontBody, "RESUME",
                BTN_X, BTN_RESUME_Y, BTN_W, BTN_H);
        UiFactory.drawButton(sr, game.batch, game.fontBody, "RESTART",
                BTN_X, BTN_RESTART_Y, BTN_W, BTN_H);
        UiFactory.drawButton(sr, game.batch, game.fontBody, "MAIN MENU",
                BTN_X, BTN_MENU_Y, BTN_W, BTN_H);

        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void show()   {}
    @Override public void hide()   { /* do NOT dispose — resumeTarget may still be alive */ }
    @Override public void pause()  {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        sr.dispose();
    }
}
