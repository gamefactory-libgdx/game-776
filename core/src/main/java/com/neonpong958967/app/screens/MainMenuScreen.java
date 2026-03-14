package com.neonpong958967.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.neonpong958967.app.Constants;
import com.neonpong958967.app.MainGame;

public class MainMenuScreen implements Screen {

    private final MainGame game;
    private final OrthographicCamera camera;
    private final StretchViewport viewport;
    private final Stage stage;
    private final ShapeRenderer sr;

    // Background
    private static final float BG_COLOR_R = 0f;
    private static final float BG_COLOR_G = 0f;
    private static final float BG_COLOR_B = 0f;

    // Button layout — landscape 854×480
    private static final float BTN_W = Constants.BUTTON_WIDTH;  // 280
    private static final float BTN_H = Constants.BUTTON_HEIGHT; // 56
    private static final float BTN_X = (Constants.WORLD_WIDTH - BTN_W) * 0.5f;

    private static final float BTN_PLAY_Y        = 280f;
    private static final float BTN_LEADERBOARD_Y = 200f;
    private static final float BTN_SETTINGS_Y    = 120f;

    public MainMenuScreen(MainGame game) {
        this.game = game;

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
                // On the main menu, back key does nothing (already at root)
                return keycode == Input.Keys.BACK;
            }
        }));

        game.playMusic("sounds/music/music_menu.ogg");
    }

    private void handleTouch(float wx, float wy) {
        if (UiFactory.isHit(wx, wy, BTN_X, BTN_PLAY_Y, BTN_W, BTN_H)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            game.setScreen(new DifficultySelectScreen(game));
        } else if (UiFactory.isHit(wx, wy, BTN_X, BTN_LEADERBOARD_Y, BTN_W, BTN_H)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            game.setScreen(new LeaderboardScreen(game));
        } else if (UiFactory.isHit(wx, wy, BTN_X, BTN_SETTINGS_Y, BTN_W, BTN_H)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            game.setScreen(new SettingsScreen(game));
        }
    }

    private void playSfx(String path) {
        if (game.sfxEnabled)
            game.manager.get(path, com.badlogic.gdx.audio.Sound.class).play(1.0f);
    }

    @Override
    public void render(float delta) {
        // Clear
        Gdx.gl.glClearColor(BG_COLOR_R, BG_COLOR_G, BG_COLOR_B, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        game.batch.begin();

        // Background
        Texture bg = game.manager.get("backgrounds/bg_main.png", Texture.class);
        game.batch.draw(bg, 0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        // Title "NEON PONG"
        GlyphLayout titleLayout = new GlyphLayout(game.fontTitle, "NEON PONG");
        game.fontTitle.setColor(0f, 1f, 0.255f, 1f); // #00FF41
        game.fontTitle.draw(game.batch, "NEON PONG",
                (Constants.WORLD_WIDTH - titleLayout.width) * 0.5f,
                400f);

        // Buttons
        UiFactory.drawButton(sr, game.batch, game.fontBody, "PLAY",
                BTN_X, BTN_PLAY_Y, BTN_W, BTN_H);
        UiFactory.drawButton(sr, game.batch, game.fontBody, "LEADERBOARD",
                BTN_X, BTN_LEADERBOARD_Y, BTN_W, BTN_H);
        UiFactory.drawButton(sr, game.batch, game.fontBody, "SETTINGS",
                BTN_X, BTN_SETTINGS_Y, BTN_W, BTN_H);

        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void show()   {}
    @Override public void hide()   { dispose(); }
    @Override public void pause()  {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        sr.dispose();
    }
}
