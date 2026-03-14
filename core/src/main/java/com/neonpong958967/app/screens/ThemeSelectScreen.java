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

public class ThemeSelectScreen implements Screen {

    private final MainGame game;
    private final int difficulty;

    private final OrthographicCamera camera;
    private final StretchViewport viewport;
    private final Stage stage;
    private final ShapeRenderer sr;

    // 3 cards: NEON, RETRO, MINIMAL
    private static final float CARD_W   = Constants.THEME_CARD_WIDTH;   // 230
    private static final float CARD_H   = Constants.THEME_CARD_HEIGHT;  // 240
    private static final float CARD_GAP = 12f;
    private static final float CARD_START_X =
            (Constants.WORLD_WIDTH - 3 * CARD_W - 2 * CARD_GAP) * 0.5f; // ~70
    private static final float CARD_Y       =
            (Constants.WORLD_HEIGHT - CARD_H) * 0.5f - 20f;              // ~100

    private static final float CARD_NEON_X    = CARD_START_X;
    private static final float CARD_RETRO_X   = CARD_START_X + CARD_W + CARD_GAP;
    private static final float CARD_MINIMAL_X = CARD_START_X + 2 * (CARD_W + CARD_GAP);

    // Preview area inside each card (top portion)
    private static final float PREVIEW_H = 140f;
    private static final float PREVIEW_PAD = 5f;

    // Back button — top-left
    private static final float BACK_BTN_SIZE = Constants.BUTTON_SMALL_SIZE;
    private static final float BACK_BTN_X    = 20f;
    private static final float BACK_BTN_Y    = Constants.WORLD_HEIGHT - BACK_BTN_SIZE - 20f;

    // Primary color
    private static final Color COLOR_PRIMARY = new Color(0f, 1f, 0.255f, 1f);
    // Theme label colors matching FIGMA brief
    private static final Color NEON_LABEL    = new Color(1f, 0f, 0.431f, 1f);    // hot pink
    private static final Color RETRO_LABEL   = new Color(0f, 0.851f, 1f, 1f);    // electric blue
    private static final Color MINIMAL_LABEL = new Color(0.1f, 0.1f, 0.1f, 1f);  // dark

    public ThemeSelectScreen(MainGame game, int difficulty) {
        this.game       = game;
        this.difficulty = difficulty;

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
                    playSfx("sounds/sfx/sfx_button_back.ogg");
                    game.setScreen(new DifficultySelectScreen(game));
                    return true;
                }
                return false;
            }
        }));

        game.playMusic("sounds/music/music_menu.ogg");
    }

    private void handleTouch(float wx, float wy) {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);

        if (isCardHit(wx, wy, CARD_NEON_X)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            prefs.putInteger(Constants.PREF_SELECTED_THEME, Constants.THEME_NEON);
            prefs.flush();
            game.setScreen(new GameScreen(game, difficulty, Constants.THEME_NEON));

        } else if (isCardHit(wx, wy, CARD_RETRO_X)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            prefs.putInteger(Constants.PREF_SELECTED_THEME, Constants.THEME_RETRO);
            prefs.flush();
            game.setScreen(new GameScreen(game, difficulty, Constants.THEME_RETRO));

        } else if (isCardHit(wx, wy, CARD_MINIMAL_X)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            prefs.putInteger(Constants.PREF_SELECTED_THEME, Constants.THEME_MINIMAL);
            prefs.flush();
            game.setScreen(new GameScreen(game, difficulty, Constants.THEME_MINIMAL));

        } else if (UiFactory.isHit(wx, wy, BACK_BTN_X, BACK_BTN_Y, BACK_BTN_SIZE, BACK_BTN_SIZE)) {
            playSfx("sounds/sfx/sfx_button_back.ogg");
            game.setScreen(new DifficultySelectScreen(game));
        }
    }

    private boolean isCardHit(float wx, float wy, float cardX) {
        return wx >= cardX && wx <= cardX + CARD_W && wy >= CARD_Y && wy <= CARD_Y + CARD_H;
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
        Texture bgMain = game.manager.get("backgrounds/bg_main.png", Texture.class);
        game.batch.draw(bgMain, 0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        // Title
        GlyphLayout titleLayout = new GlyphLayout(game.fontTitle, "SELECT THEME");
        game.fontTitle.setColor(COLOR_PRIMARY);
        game.fontTitle.draw(game.batch, "SELECT THEME",
                (Constants.WORLD_WIDTH - titleLayout.width) * 0.5f, 450f);

        // Draw background previews within card top area
        Texture bgNeon    = game.manager.get("backgrounds/bg_neon.png",     Texture.class);
        Texture bgRetro   = game.manager.get("backgrounds/bg_retro_crt.png", Texture.class);
        Texture bgMinimal = game.manager.get("backgrounds/bg_minimal.png",   Texture.class);

        float previewY = CARD_Y + CARD_H - PREVIEW_H - PREVIEW_PAD;
        float previewW = CARD_W - PREVIEW_PAD * 2;

        game.batch.draw(bgNeon,    CARD_NEON_X    + PREVIEW_PAD, previewY, previewW, PREVIEW_H - PREVIEW_PAD);
        game.batch.draw(bgRetro,   CARD_RETRO_X   + PREVIEW_PAD, previewY, previewW, PREVIEW_H - PREVIEW_PAD);
        game.batch.draw(bgMinimal, CARD_MINIMAL_X + PREVIEW_PAD, previewY, previewW, PREVIEW_H - PREVIEW_PAD);

        game.batch.end();

        // Card outlines + fills
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Draw card borders (border behind preview — just the card border)
        drawCardBorder(CARD_NEON_X);
        drawCardBorder(CARD_RETRO_X);
        drawCardBorder(CARD_MINIMAL_X);

        // Back button border
        sr.setColor(COLOR_PRIMARY.r, COLOR_PRIMARY.g, COLOR_PRIMARY.b, 1f);
        sr.rect(BACK_BTN_X, BACK_BTN_Y, BACK_BTN_SIZE, BACK_BTN_SIZE);
        sr.setColor(0f, 0f, 0f, 0.6f);
        sr.rect(BACK_BTN_X + 3, BACK_BTN_Y + 3, BACK_BTN_SIZE - 6, BACK_BTN_SIZE - 6);

        // Fill bottom text area of each card (below preview)
        float textAreaH = CARD_H - PREVIEW_H;
        sr.setColor(0f, 0f, 0f, 0.85f);
        sr.rect(CARD_NEON_X    + 3, CARD_Y + 3, CARD_W - 6, textAreaH - 6);
        sr.rect(CARD_RETRO_X   + 3, CARD_Y + 3, CARD_W - 6, textAreaH - 6);
        sr.rect(CARD_MINIMAL_X + 3, CARD_Y + 3, CARD_W - 6, textAreaH - 6);

        sr.end();

        game.batch.begin();

        // Card name labels in bottom text area
        float labelY = CARD_Y + textAreaH * 0.6f;
        drawCardLabel("NEON",    CARD_NEON_X,    NEON_LABEL,    "Glowing arcade vibes",  labelY);
        drawCardLabel("RETRO",   CARD_RETRO_X,   RETRO_LABEL,   "Classic CRT scanlines",  labelY);
        drawCardLabel("MINIMAL", CARD_MINIMAL_X, MINIMAL_LABEL, "Clean focused design",   labelY);

        // Back button label
        GlyphLayout backLayout = new GlyphLayout(game.fontSmall, "<");
        game.fontSmall.setColor(COLOR_PRIMARY);
        game.fontSmall.draw(game.batch, "<",
                BACK_BTN_X + (BACK_BTN_SIZE - backLayout.width) * 0.5f,
                BACK_BTN_Y + (BACK_BTN_SIZE + backLayout.height) * 0.5f);

        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    /** Draws a 3px-border card rectangle (no fill — fill is drawn separately). */
    private void drawCardBorder(float x) {
        sr.setColor(0f, 1f, 0.255f, 1f); // #00FF41 border
        sr.rect(x, CARD_Y, CARD_W, CARD_H);
    }

    /** Draws name + description text centered in card's bottom text area. */
    private void drawCardLabel(String name, float cardX, Color nameColor, String desc, float nameY) {
        GlyphLayout nl = new GlyphLayout(game.fontBody, name);
        game.fontBody.setColor(nameColor);
        game.fontBody.draw(game.batch, name,
                cardX + (CARD_W - nl.width) * 0.5f,
                nameY);

        GlyphLayout dl = new GlyphLayout(game.fontSmall, desc);
        game.fontSmall.setColor(Color.WHITE);
        game.fontSmall.draw(game.batch, desc,
                cardX + (CARD_W - dl.width) * 0.5f,
                nameY - 22f);
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
