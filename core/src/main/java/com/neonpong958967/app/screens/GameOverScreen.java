package com.neonpong958967.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
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

public class GameOverScreen implements Screen {

    private final MainGame game;
    private final OrthographicCamera camera;
    private final StretchViewport viewport;
    private final Stage stage;
    private final ShapeRenderer sr;

    private final int score;
    private final int extra;  // sets won by player
    private final int personalBest;

    // Modal bounds (centered)
    private static final float MODAL_W = 500f;
    private static final float MODAL_H = 300f;
    private static final float MODAL_X = (Constants.WORLD_WIDTH  - MODAL_W) * 0.5f;
    private static final float MODAL_Y = (Constants.WORLD_HEIGHT - MODAL_H) * 0.5f;

    // Buttons inside modal
    private static final float BTN_W   = 180f;
    private static final float BTN_H   = 50f;
    private static final float BTN_Y   = MODAL_Y + 30f;
    private static final float BTN_RETRY_X = MODAL_X + 40f;
    private static final float BTN_MENU_X  = MODAL_X + MODAL_W - 40f - BTN_W;

    public GameOverScreen(MainGame game, int score, int extra) {
        this.game  = game;
        this.score = score;
        this.extra = extra;

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);
        sr       = new ShapeRenderer();

        // Load and update personal best
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        int stored = prefs.getInteger(Constants.PREF_HIGH_SCORE, 0);
        if (score > stored) {
            prefs.putInteger(Constants.PREF_HIGH_SCORE, score);
            prefs.flush();
            personalBest = score;
        } else {
            personalBest = stored;
        }

        // Save to leaderboard
        LeaderboardScreen.addScore(score);

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
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                return false;
            }
        }));

        game.playMusicOnce("sounds/music/music_game_over.ogg");
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_game_over.ogg",
                    com.badlogic.gdx.audio.Sound.class).play(1.0f);
    }

    private void handleTouch(float wx, float wy) {
        if (UiFactory.isHit(wx, wy, BTN_RETRY_X, BTN_Y, BTN_W, BTN_H)) {
            if (game.sfxEnabled)
                game.manager.get("sounds/sfx/sfx_button_click.ogg",
                        com.badlogic.gdx.audio.Sound.class).play(1.0f);
            game.setScreen(new DifficultySelectScreen(game));
        } else if (UiFactory.isHit(wx, wy, BTN_MENU_X, BTN_Y, BTN_W, BTN_H)) {
            if (game.sfxEnabled)
                game.manager.get("sounds/sfx/sfx_button_back.ogg",
                        com.badlogic.gdx.audio.Sound.class).play(1.0f);
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        game.batch.begin();

        // Background
        Texture bg = game.manager.get("backgrounds/bg_main.png", Texture.class);
        game.batch.draw(bg, 0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        game.batch.end();

        // Modal background (dark overlay + border)
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        // Border (#00FF41)
        sr.setColor(0f, 1f, 0.255f, 1f);
        sr.rect(MODAL_X, MODAL_Y, MODAL_W, MODAL_H);
        // Fill (deep navy, 95% opacity)
        sr.setColor(0.039f, 0.055f, 0.153f, 0.95f);
        sr.rect(MODAL_X + 3, MODAL_Y + 3, MODAL_W - 6, MODAL_H - 6);
        sr.end();

        game.batch.begin();

        // "MATCH OVER"
        GlyphLayout titleLayout = new GlyphLayout(game.fontTitle, "MATCH OVER");
        game.fontTitle.setColor(1f, 0f, 0.431f, 1f); // #FF006E
        game.fontTitle.draw(game.batch, "MATCH OVER",
                MODAL_X + (MODAL_W - titleLayout.width) * 0.5f,
                MODAL_Y + MODAL_H - 30f);

        // Score
        String scoreLine = "SCORE: " + score;
        GlyphLayout scoreLayout = new GlyphLayout(game.fontBody, scoreLine);
        game.fontBody.setColor(0f, 1f, 0.255f, 1f);
        game.fontBody.draw(game.batch, scoreLine,
                MODAL_X + (MODAL_W - scoreLayout.width) * 0.5f,
                MODAL_Y + MODAL_H - 90f);

        // Sets won
        String setsLine = "SETS WON: " + extra;
        GlyphLayout setsLayout = new GlyphLayout(game.fontBody, setsLine);
        game.fontBody.setColor(Color.WHITE);
        game.fontBody.draw(game.batch, setsLine,
                MODAL_X + (MODAL_W - setsLayout.width) * 0.5f,
                MODAL_Y + MODAL_H - 130f);

        // Personal best
        String pbLine = "PERSONAL BEST: " + personalBest;
        GlyphLayout pbLayout = new GlyphLayout(game.fontSmall, pbLine);
        game.fontSmall.setColor(0f, 1f, 0.255f, 1f);
        game.fontSmall.draw(game.batch, pbLine,
                MODAL_X + (MODAL_W - pbLayout.width) * 0.5f,
                MODAL_Y + MODAL_H - 165f);

        // Buttons
        UiFactory.drawButton(sr, game.batch, game.fontBody, "PLAY AGAIN",
                BTN_RETRY_X, BTN_Y, BTN_W, BTN_H);
        UiFactory.drawButton(sr, game.batch, game.fontBody, "MENU",
                BTN_MENU_X, BTN_Y, BTN_W, BTN_H);

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
