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

public class DifficultySelectScreen implements Screen {

    private final MainGame game;
    private final OrthographicCamera camera;
    private final StretchViewport viewport;
    private final Stage stage;
    private final ShapeRenderer sr;

    // 3 cards side by side in landscape 854×480
    private static final float CARD_W   = Constants.DIFF_CARD_WIDTH;   // 220
    private static final float CARD_H   = Constants.DIFF_CARD_HEIGHT;  // 200
    private static final float CARD_GAP = 16f;
    private static final float CARD_START_X = (Constants.WORLD_WIDTH - 3 * CARD_W - 2 * CARD_GAP) * 0.5f; // ~81
    private static final float CARD_Y       = (Constants.WORLD_HEIGHT - CARD_H) * 0.5f - 10f;             // ~130

    private static final float CARD_EASY_X   = CARD_START_X;
    private static final float CARD_MEDIUM_X = CARD_START_X + CARD_W + CARD_GAP;
    private static final float CARD_HARD_X   = CARD_START_X + 2 * (CARD_W + CARD_GAP);

    // Back button — top-left
    private static final float BACK_BTN_SIZE = Constants.BUTTON_SMALL_SIZE; // 48
    private static final float BACK_BTN_X    = 20f;
    private static final float BACK_BTN_Y    = Constants.WORLD_HEIGHT - BACK_BTN_SIZE - 20f;

    // Primary color #00FF41
    private static final Color COLOR_PRIMARY = new Color(0f, 1f, 0.255f, 1f);
    // Easy=green(lime), Medium=white, Hard=red accent
    private static final Color EASY_COLOR   = new Color(0f, 1f, 0.255f, 1f);
    private static final Color MEDIUM_COLOR = new Color(1f, 1f, 1f, 1f);
    private static final Color HARD_COLOR   = new Color(1f, 0f, 0.431f, 1f);

    public DifficultySelectScreen(MainGame game) {
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
                if (keycode == Input.Keys.BACK) {
                    playSfx("sounds/sfx/sfx_button_back.ogg");
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

        if (isCardHit(wx, wy, CARD_EASY_X)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            prefs.putInteger(Constants.PREF_DIFFICULTY, Constants.DIFF_EASY);
            prefs.flush();
            game.setScreen(new ThemeSelectScreen(game, Constants.DIFF_EASY));

        } else if (isCardHit(wx, wy, CARD_MEDIUM_X)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            prefs.putInteger(Constants.PREF_DIFFICULTY, Constants.DIFF_MEDIUM);
            prefs.flush();
            game.setScreen(new ThemeSelectScreen(game, Constants.DIFF_MEDIUM));

        } else if (isCardHit(wx, wy, CARD_HARD_X)) {
            playSfx("sounds/sfx/sfx_button_click.ogg");
            prefs.putInteger(Constants.PREF_DIFFICULTY, Constants.DIFF_HARD);
            prefs.flush();
            game.setScreen(new ThemeSelectScreen(game, Constants.DIFF_HARD));

        } else if (UiFactory.isHit(wx, wy, BACK_BTN_X, BACK_BTN_Y, BACK_BTN_SIZE, BACK_BTN_SIZE)) {
            playSfx("sounds/sfx/sfx_button_back.ogg");
            game.setScreen(new MainMenuScreen(game));
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
        Texture bg = game.manager.get("backgrounds/bg_main.png", Texture.class);
        game.batch.draw(bg, 0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        // Title
        GlyphLayout titleLayout = new GlyphLayout(game.fontTitle, "SELECT DIFFICULTY");
        game.fontTitle.setColor(COLOR_PRIMARY);
        game.fontTitle.draw(game.batch, "SELECT DIFFICULTY",
                (Constants.WORLD_WIDTH - titleLayout.width) * 0.5f, 450f);

        game.batch.end();

        // Draw cards with ShapeRenderer
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // EASY card — green border
        drawCard(CARD_EASY_X, CARD_Y, EASY_COLOR.r, EASY_COLOR.g, EASY_COLOR.b);
        // MEDIUM card — white border
        drawCard(CARD_MEDIUM_X, CARD_Y, MEDIUM_COLOR.r, MEDIUM_COLOR.g, MEDIUM_COLOR.b);
        // HARD card — pink border
        drawCard(CARD_HARD_X, CARD_Y, HARD_COLOR.r, HARD_COLOR.g, HARD_COLOR.b);

        // Back button border
        sr.setColor(COLOR_PRIMARY.r, COLOR_PRIMARY.g, COLOR_PRIMARY.b, 1f);
        sr.rect(BACK_BTN_X, BACK_BTN_Y, BACK_BTN_SIZE, BACK_BTN_SIZE);
        sr.setColor(0f, 0f, 0f, 0.6f);
        sr.rect(BACK_BTN_X + 3, BACK_BTN_Y + 3, BACK_BTN_SIZE - 6, BACK_BTN_SIZE - 6);

        sr.end();

        game.batch.begin();

        // EASY card label
        drawCardLabel("EASY", CARD_EASY_X, EASY_COLOR, "AI plays defensively");
        // MEDIUM card label
        drawCardLabel("MEDIUM", CARD_MEDIUM_X, MEDIUM_COLOR, "AI reacts quickly");
        // HARD card label
        drawCardLabel("HARD", CARD_HARD_X, HARD_COLOR, "AI predicts moves");

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

    /** Draws a card rectangle with a 3px border and semi-transparent fill. */
    private void drawCard(float x, float y, float r, float g, float b) {
        sr.setColor(r, g, b, 1f);
        sr.rect(x, y, CARD_W, CARD_H);
        sr.setColor(0f, 0f, 0f, 0.8f);
        sr.rect(x + 3, y + 3, CARD_W - 6, CARD_H - 6);
    }

    /** Draws the card title and description text centered within the card. */
    private void drawCardLabel(String label, float cardX, Color color, String desc) {
        // Main label centered in card
        GlyphLayout gl = new GlyphLayout(game.fontBody, label);
        game.fontBody.setColor(color);
        game.fontBody.draw(game.batch, label,
                cardX + (CARD_W - gl.width) * 0.5f,
                CARD_Y + CARD_H * 0.65f);

        // Description centered below main label
        GlyphLayout dl = new GlyphLayout(game.fontSmall, desc);
        game.fontSmall.setColor(Color.WHITE);
        game.fontSmall.draw(game.batch, desc,
                cardX + (CARD_W - dl.width) * 0.5f,
                CARD_Y + CARD_H * 0.35f);
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
