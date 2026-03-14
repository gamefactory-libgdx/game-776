package com.neonpong958967.app;

public class Constants {

    // World dimensions (landscape)
    public static final float WORLD_WIDTH  = 854f;
    public static final float WORLD_HEIGHT = 480f;

    // Paddle dimensions
    public static final float PADDLE_WIDTH      = 12f;
    public static final float PADDLE_HEIGHT     = 80f;
    public static final float PADDLE_SPEED      = 400f;
    public static final float PADDLE_MARGIN     = 40f; // distance from top/bottom edge

    // AI paddle speeds per difficulty
    public static final float AI_SPEED_EASY     = 180f;
    public static final float AI_SPEED_MEDIUM   = 280f;
    public static final float AI_SPEED_HARD     = 400f;

    // Ball
    public static final float BALL_SIZE         = 14f;
    public static final float BALL_SPEED_INITIAL = 320f;
    public static final float BALL_SPEED_MAX    = 680f;
    public static final float BALL_SPEED_INCREMENT = 20f; // per rally hit

    // Game rules
    public static final int SCORE_TO_WIN_SET    = 7;   // first to 7 wins a set
    public static final int SETS_TO_WIN_MATCH   = 2;   // best of 3 sets (first to 2)
    public static final int TOTAL_SETS          = 3;

    // Leaderboard
    public static final int LEADERBOARD_MAX_ENTRIES = 10;

    // Font sizes
    public static final int FONT_SIZE_TITLE     = 36;
    public static final int FONT_SIZE_SCORE     = 48;
    public static final int FONT_SIZE_SUBTITLE  = 24;
    public static final int FONT_SIZE_BODY      = 18;
    public static final int FONT_SIZE_SMALL     = 14;
    public static final int FONT_SIZE_HUD_LABEL = 12;

    // UI sizes
    public static final float BUTTON_WIDTH      = 280f;
    public static final float BUTTON_HEIGHT     = 56f;
    public static final float BUTTON_SMALL_SIZE = 48f;
    public static final float BUTTON_PAD        = 16f;
    public static final float DIFF_CARD_WIDTH   = 220f;
    public static final float DIFF_CARD_HEIGHT  = 200f;
    public static final float THEME_CARD_WIDTH  = 230f;
    public static final float THEME_CARD_HEIGHT = 240f;
    public static final float WIN_BAR_WIDTH     = 200f;
    public static final float WIN_BAR_HEIGHT    = 12f;
    public static final float SET_DOT_SIZE      = 12f;

    // Swipe indicator display time (seconds)
    public static final float SWIPE_HINT_DURATION = 2f;

    // Score values
    public static final int SCORE_WIN_SET       = 100;
    public static final int SCORE_WIN_MATCH     = 500;

    // SharedPreferences keys
    public static final String PREFS_NAME           = "NeonPongPrefs";
    public static final String PREF_MUSIC           = "musicEnabled";
    public static final String PREF_SFX             = "sfxEnabled";
    public static final String PREF_HIGH_SCORE      = "highScore";
    public static final String PREF_SELECTED_THEME  = "selectedTheme";   // 0=Neon, 1=Retro, 2=Minimal
    public static final String PREF_DIFFICULTY      = "difficulty";       // 0=Easy, 1=Medium, 2=Hard
    public static final String PREF_LEADERBOARD     = "leaderboard";
    public static final String PREF_HAPTIC          = "hapticEnabled";
    public static final String PREF_VOLUME          = "masterVolume";

    // Theme indices
    public static final int THEME_NEON    = 0;
    public static final int THEME_RETRO   = 1;
    public static final int THEME_MINIMAL = 2;

    // Difficulty indices
    public static final int DIFF_EASY   = 0;
    public static final int DIFF_MEDIUM = 1;
    public static final int DIFF_HARD   = 2;

    // Colors (as packed RGBA ints, 0xRRGGBBAA)
    public static final float COLOR_NEON_BLUE_R    = 0f / 255f;
    public static final float COLOR_NEON_BLUE_G    = 217f / 255f;
    public static final float COLOR_NEON_BLUE_B    = 255f / 255f;

    public static final float COLOR_NEON_PINK_R    = 255f / 255f;
    public static final float COLOR_NEON_PINK_G    = 0f / 255f;
    public static final float COLOR_NEON_PINK_B    = 110f / 255f;

    public static final float COLOR_NAVY_R         = 10f / 255f;
    public static final float COLOR_NAVY_G         = 14f / 255f;
    public static final float COLOR_NAVY_B         = 39f / 255f;

    public static final float COLOR_NEON_LIME_R    = 57f / 255f;
    public static final float COLOR_NEON_LIME_G    = 255f / 255f;
    public static final float COLOR_NEON_LIME_B    = 20f / 255f;
}
