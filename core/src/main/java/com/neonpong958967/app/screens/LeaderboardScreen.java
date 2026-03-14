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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardScreen implements Screen {

    private final MainGame game;
    private final OrthographicCamera camera;
    private final StretchViewport viewport;
    private final Stage stage;
    private final ShapeRenderer sr;

    // Layout
    private static final float BTN_W    = Constants.BUTTON_WIDTH;
    private static final float BTN_H    = Constants.BUTTON_HEIGHT;
    private static final float BTN_X    = (Constants.WORLD_WIDTH - BTN_W) * 0.5f;
    private static final float BTN_Y    = 20f;

    private static final float LIST_X   = 80f;
    private static final float LIST_TOP = 360f;
    private static final float ROW_H    = 28f;

    private final int[] scores;

    public LeaderboardScreen(MainGame game) {
        this.game = game;

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);
        sr       = new ShapeRenderer();

        scores = loadScores();

        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 pos = new Vector3(screenX, screenY, 0);
                camera.unproject(pos);
                if (UiFactory.isHit(pos.x, pos.y, BTN_X, BTN_Y, BTN_W, BTN_H)) {
                    if (game.sfxEnabled)
                        game.manager.get("sounds/sfx/sfx_button_back.ogg",
                                com.badlogic.gdx.audio.Sound.class).play(1.0f);
                    game.setScreen(new MainMenuScreen(game));
                }
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

    // -------------------------------------------------------------------------
    // Static helpers
    // -------------------------------------------------------------------------

    /** Inserts score into the persisted top-10 list. */
    public static void addScore(int newScore) {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        String raw = prefs.getString(Constants.PREF_LEADERBOARD, "");
        List<Integer> list = parseScores(raw);
        list.add(newScore);
        Collections.sort(list, Collections.reverseOrder());
        if (list.size() > Constants.LEADERBOARD_MAX_ENTRIES)
            list = list.subList(0, Constants.LEADERBOARD_MAX_ENTRIES);
        prefs.putString(Constants.PREF_LEADERBOARD, joinScores(list));
        prefs.flush();
    }

    private static List<Integer> parseScores(String raw) {
        List<Integer> list = new ArrayList<>();
        if (raw == null || raw.isEmpty()) return list;
        for (String part : raw.split(",")) {
            try { list.add(Integer.parseInt(part.trim())); }
            catch (NumberFormatException ignored) {}
        }
        return list;
    }

    private static String joinScores(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(',');
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    private int[] loadScores() {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        String raw = prefs.getString(Constants.PREF_LEADERBOARD, "");
        List<Integer> list = parseScores(raw);
        int[] arr = new int[list.size()];
        for (int i = 0; i < arr.length; i++) arr[i] = list.get(i);
        return arr;
    }

    // -------------------------------------------------------------------------
    // Rendering
    // -------------------------------------------------------------------------

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
        GlyphLayout titleLayout = new GlyphLayout(game.fontTitle, "LEADERBOARD");
        game.fontTitle.setColor(0f, 1f, 0.255f, 1f);
        game.fontTitle.draw(game.batch, "LEADERBOARD",
                (Constants.WORLD_WIDTH - titleLayout.width) * 0.5f, 440f);

        // Scores list
        if (scores.length == 0) {
            game.fontBody.setColor(Color.WHITE);
            GlyphLayout emptyLayout = new GlyphLayout(game.fontBody, "NO SCORES YET");
            game.fontBody.draw(game.batch, "NO SCORES YET",
                    (Constants.WORLD_WIDTH - emptyLayout.width) * 0.5f,
                    LIST_TOP);
        } else {
            for (int i = 0; i < scores.length; i++) {
                float rowY = LIST_TOP - i * ROW_H;

                // Rank — #00FF41
                game.fontSmall.setColor(0f, 1f, 0.255f, 1f);
                game.fontSmall.draw(game.batch, "#" + (i + 1), LIST_X, rowY);

                // Score — white
                game.fontSmall.setColor(Color.WHITE);
                game.fontSmall.draw(game.batch, String.valueOf(scores[i]),
                        LIST_X + 80f, rowY);
            }
        }

        // Main Menu button
        UiFactory.drawButton(sr, game.batch, game.fontBody, "MAIN MENU",
                BTN_X, BTN_Y, BTN_W, BTN_H);

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
