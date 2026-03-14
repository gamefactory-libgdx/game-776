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

public class SettingsScreen implements Screen {

    private final MainGame game;
    private final OrthographicCamera camera;
    private final StretchViewport viewport;
    private final Stage stage;
    private final ShapeRenderer sr;

    // Toggle dimensions
    private static final float TOGGLE_W  = 100f;
    private static final float TOGGLE_H  = 44f;
    private static final float ROW_H     = 60f;

    // Layout — landscape 854×480
    private static final float LABEL_X   = 80f;
    private static final float TOGGLE_X  = Constants.WORLD_WIDTH - 80f - TOGGLE_W;
    private static final float MUSIC_ROW_Y = 280f;
    private static final float SFX_ROW_Y   = 200f;

    // Main Menu button
    private static final float BTN_W = Constants.BUTTON_WIDTH;
    private static final float BTN_H = Constants.BUTTON_HEIGHT;
    private static final float BTN_X = (Constants.WORLD_WIDTH - BTN_W) * 0.5f;
    private static final float BTN_Y = 60f;

    private boolean musicOn;
    private boolean sfxOn;

    public SettingsScreen(MainGame game) {
        this.game = game;

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);
        sr       = new ShapeRenderer();

        // Load saved preferences
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        musicOn = prefs.getBoolean(Constants.PREF_MUSIC, true);
        sfxOn   = prefs.getBoolean(Constants.PREF_SFX,   true);
        game.musicEnabled = musicOn;
        game.sfxEnabled   = sfxOn;

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

        game.playMusic("sounds/music/music_menu.ogg");
    }

    private void handleTouch(float wx, float wy) {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);

        // Music toggle row
        if (UiFactory.isHit(wx, wy, TOGGLE_X, MUSIC_ROW_Y, TOGGLE_W, TOGGLE_H)) {
            musicOn = !musicOn;
            game.musicEnabled = musicOn;
            prefs.putBoolean(Constants.PREF_MUSIC, musicOn);
            prefs.flush();
            if (game.currentMusic != null) {
                if (musicOn) game.currentMusic.play();
                else         game.currentMusic.pause();
            }
            playSfx("sounds/sfx/sfx_toggle.ogg");
        }

        // SFX toggle row
        if (UiFactory.isHit(wx, wy, TOGGLE_X, SFX_ROW_Y, TOGGLE_W, TOGGLE_H)) {
            sfxOn = !sfxOn;
            game.sfxEnabled = sfxOn;
            prefs.putBoolean(Constants.PREF_SFX, sfxOn);
            prefs.flush();
            playSfx("sounds/sfx/sfx_toggle.ogg");
        }

        // Main Menu button
        if (UiFactory.isHit(wx, wy, BTN_X, BTN_Y, BTN_W, BTN_H)) {
            playSfx("sounds/sfx/sfx_button_back.ogg");
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void playSfx(String path) {
        if (game.sfxEnabled)
            game.manager.get(path, com.badlogic.gdx.audio.Sound.class).play(1.0f);
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

        // Title
        GlyphLayout titleLayout = new GlyphLayout(game.fontTitle, "SETTINGS");
        game.fontTitle.setColor(0f, 1f, 0.255f, 1f);
        game.fontTitle.draw(game.batch, "SETTINGS",
                (Constants.WORLD_WIDTH - titleLayout.width) * 0.5f, 420f);

        // Music row label
        game.fontBody.setColor(0f, 1f, 0.255f, 1f);
        game.fontBody.draw(game.batch, "MUSIC",
                LABEL_X, MUSIC_ROW_Y + TOGGLE_H * 0.75f);

        // SFX row label
        game.fontBody.draw(game.batch, "SOUND FX",
                LABEL_X, SFX_ROW_Y + TOGGLE_H * 0.75f);

        // Draw toggles (end batch, draw shapes, resume batch)
        game.batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        drawToggle(TOGGLE_X, MUSIC_ROW_Y, musicOn);
        drawToggle(TOGGLE_X, SFX_ROW_Y,   sfxOn);
        sr.end();

        game.batch.begin();

        // Toggle state labels
        game.fontBody.setColor(Color.WHITE);
        game.fontBody.draw(game.batch, musicOn ? "ON" : "OFF",
                TOGGLE_X + 10f, MUSIC_ROW_Y + TOGGLE_H * 0.75f);
        game.fontBody.draw(game.batch, sfxOn ? "ON" : "OFF",
                TOGGLE_X + 10f, SFX_ROW_Y + TOGGLE_H * 0.75f);

        // Main Menu button
        UiFactory.drawButton(sr, game.batch, game.fontBody, "MAIN MENU",
                BTN_X, BTN_Y, BTN_W, BTN_H);

        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    /** Draws a toggle rectangle — green when on, dark when off. */
    private void drawToggle(float x, float y, boolean on) {
        if (on) {
            sr.setColor(0f, 1f, 0.255f, 1f); // #00FF41
        } else {
            sr.setColor(0.2f, 0.2f, 0.2f, 1f);
        }
        sr.rect(x, y, TOGGLE_W, TOGGLE_H);
        // Inner inset
        sr.setColor(0f, 0f, 0f, 0.6f);
        sr.rect(x + 3, y + 3, TOGGLE_W - 6, TOGGLE_H - 6);
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
