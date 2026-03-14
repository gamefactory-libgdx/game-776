package com.neonpong958967.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.neonpong958967.app.screens.MainMenuScreen;

public class MainGame extends Game {

    public SpriteBatch   batch;
    public AssetManager  manager;

    // Shared fonts — generated once, used by all screens
    public BitmapFont fontTitle;   // PressStart2P — retro arcade headlines
    public BitmapFont fontBody;    // Roboto — body text / buttons
    public BitmapFont fontScore;   // PressStart2P large — HUD score display
    public BitmapFont fontSmall;   // Roboto small — labels / leaderboard rows

    // Audio state
    public boolean musicEnabled = true;
    public boolean sfxEnabled   = true;
    public Music   currentMusic = null;

    @Override
    public void create() {
        batch   = new SpriteBatch();
        manager = new AssetManager();

        generateFonts();
        loadAssets();
        manager.finishLoading();

        setScreen(new MainMenuScreen(this));
    }

    // -------------------------------------------------------------------------
    // Font generation
    // -------------------------------------------------------------------------

    private void generateFonts() {
        FreeTypeFontGenerator titleGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/PressStart2P.ttf"));
        FreeTypeFontGenerator bodyGen  = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Roboto-Regular.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();

        p.size    = Constants.FONT_SIZE_TITLE;
        fontTitle = titleGen.generateFont(p);

        p.size    = Constants.FONT_SIZE_SCORE;
        fontScore = titleGen.generateFont(p);

        p.size    = Constants.FONT_SIZE_BODY;
        fontBody  = bodyGen.generateFont(p);

        p.size    = Constants.FONT_SIZE_SMALL;
        fontSmall = bodyGen.generateFont(p);

        titleGen.dispose();
        bodyGen.dispose();
    }

    // -------------------------------------------------------------------------
    // Asset loading
    // -------------------------------------------------------------------------

    private void loadAssets() {
        // Backgrounds
        loadTexture("backgrounds/bg_main.png");
        loadTexture("backgrounds/bg_neon.png");
        loadTexture("backgrounds/bg_retro_crt.png");
        loadTexture("backgrounds/bg_minimal.png");

        // Sprite buttons
        loadTexture("sprites/button_blue.png");
        loadTexture("sprites/button_blue_pressed.png");
        loadTexture("sprites/button_green.png");
        loadTexture("sprites/button_green_pressed.png");
        loadTexture("sprites/button_grey.png");
        loadTexture("sprites/button_grey_pressed.png");
        loadTexture("sprites/button_red.png");
        loadTexture("sprites/button_red_pressed.png");

        // HUD / settings icons
        loadTexture("sprites/icon_music_on.png");
        loadTexture("sprites/icon_music_off.png");
        loadTexture("sprites/icon_sfx_on.png");
        loadTexture("sprites/icon_sfx_off.png");
        loadTexture("sprites/icon_settings.png");
        loadTexture("sprites/icon_leaderboard.png");

        // Music (loaded via AssetManager)
        manager.load("sounds/music/music_menu.ogg",      Music.class);
        manager.load("sounds/music/music_gameplay.ogg",  Music.class);
        manager.load("sounds/music/music_game_over.ogg", Music.class);

        // SFX
        manager.load("sounds/sfx/sfx_button_click.ogg",   Sound.class);
        manager.load("sounds/sfx/sfx_button_back.ogg",    Sound.class);
        manager.load("sounds/sfx/sfx_toggle.ogg",         Sound.class);
        manager.load("sounds/sfx/sfx_hit.ogg",            Sound.class);
        manager.load("sounds/sfx/sfx_game_over.ogg",      Sound.class);
        manager.load("sounds/sfx/sfx_level_complete.ogg", Sound.class);
        manager.load("sounds/sfx/sfx_coin.ogg",           Sound.class);
    }

    private void loadTexture(String path) {
        manager.load(path, com.badlogic.gdx.graphics.Texture.class);
    }

    // -------------------------------------------------------------------------
    // Music helpers
    // -------------------------------------------------------------------------

    /** Play looping music — skips restart if already playing. */
    public void playMusic(String path) {
        Music requested = manager.get(path, Music.class);
        if (requested == currentMusic && currentMusic.isPlaying()) return;
        if (currentMusic != null) currentMusic.stop();
        currentMusic = requested;
        currentMusic.setLooping(true);
        currentMusic.setVolume(0.7f);
        if (musicEnabled) currentMusic.play();
    }

    /** Play non-looping music (game over jingle). */
    public void playMusicOnce(String path) {
        if (currentMusic != null) currentMusic.stop();
        currentMusic = manager.get(path, Music.class);
        currentMusic.setLooping(false);
        currentMusic.setVolume(0.7f);
        if (musicEnabled) currentMusic.play();
    }

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        manager.dispose();
        fontTitle.dispose();
        fontBody.dispose();
        fontScore.dispose();
        fontSmall.dispose();
    }
}
