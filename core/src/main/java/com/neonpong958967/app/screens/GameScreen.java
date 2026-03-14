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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.neonpong958967.app.Constants;
import com.neonpong958967.app.MainGame;

/**
 * Main Pong game screen.
 *
 * <p>Layout (left/right pong, landscape 854×480):
 * <ul>
 *   <li>Player paddle — left side, moves vertically, controlled by touch Y.</li>
 *   <li>AI paddle    — right side, tracks ball Y at AI_SPEED.</li>
 *   <li>Ball          — bounces off top/bottom walls and both paddles.</li>
 *   <li>Ball exits left (x < 0)  → AI scores.</li>
 *   <li>Ball exits right (x > W) → Player scores.</li>
 *   <li>First to {@code SCORE_TO_WIN_SET} points wins a set.</li>
 *   <li>First to {@code SETS_TO_WIN_MATCH} sets wins the match.</li>
 * </ul>
 */
public class GameScreen implements Screen {

    private final MainGame game;
    private final int difficulty;
    private final int theme;

    private final OrthographicCamera camera;
    private final StretchViewport viewport;
    private final Stage stage;
    private final ShapeRenderer sr;

    // --- Paddle state ---
    private final float playerPaddleX;
    private float playerPaddleY;
    private final float aiPaddleX;
    private float aiPaddleY;

    // Paddle logical dimensions: horizontal thickness = PADDLE_WIDTH, vertical length = PADDLE_HEIGHT
    private static final float PW = Constants.PADDLE_WIDTH;   // 12 — thickness
    private static final float PH = Constants.PADDLE_HEIGHT;  // 80 — length

    // --- Ball state ---
    private float ballX;
    private float ballY;
    private float ballVX;
    private float ballVY;
    private float ballSpeed; // current magnitude

    // Reset / serve countdown (seconds ball stays at centre before launch)
    private float serveTimer;
    private static final float SERVE_DELAY = 0.8f;
    private boolean servingToAi; // direction of next serve

    // --- Match state ---
    private int playerScore;   // current-set points
    private int aiScore;
    private int playerSets;    // sets won
    private int aiSets;
    private int currentSet;    // 1-based display index

    // Total points the player has accumulated for the leaderboard score
    private int totalPlayerPoints;

    // --- Swipe hint ---
    private float swipeHintTimer = Constants.SWIPE_HINT_DURATION;

    // --- AI difficulty speed ---
    private final float aiSpeed;

    // --- Pause button ---
    private static final float PAUSE_BTN_SIZE = Constants.BUTTON_SMALL_SIZE; // 48
    private static final float PAUSE_BTN_X    = Constants.WORLD_WIDTH - PAUSE_BTN_SIZE - 16f;
    private static final float PAUSE_BTN_Y    = Constants.WORLD_HEIGHT - PAUSE_BTN_SIZE - 16f;

    // --- Set dots (top-centre) ---
    private static final float DOT_SIZE   = Constants.SET_DOT_SIZE;   // 12
    private static final float DOT_GAP    = 8f;
    private static final float DOTS_START_X =
            Constants.WORLD_WIDTH * 0.5f - (Constants.TOTAL_SETS * (DOT_SIZE + DOT_GAP) - DOT_GAP) * 0.5f;
    private static final float DOTS_Y    = Constants.WORLD_HEIGHT - 30f;

    // --- Win progress bar (bottom-left) ---
    private static final float BAR_X = 20f;
    private static final float BAR_Y = 20f;
    private static final float BAR_W = Constants.WIN_BAR_WIDTH;   // 200
    private static final float BAR_H = Constants.WIN_BAR_HEIGHT;  // 12

    // --- Theme colors (RGB) ---
    private final float[] playerColor = new float[3];
    private final float[] aiColor     = new float[3];
    private final float[] ballColor   = new float[3];
    private String backgroundAsset;

    // Primary palette color #00FF41
    private static final Color COLOR_PRIMARY = new Color(0f, 1f, 0.255f, 1f);

    // -------------------------------------------------------------------------

    public GameScreen(MainGame game, int difficulty, int theme) {
        this.game       = game;
        this.difficulty = difficulty;
        this.theme      = theme;

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);
        sr       = new ShapeRenderer();

        // Theme colours
        applyTheme();

        // AI speed
        switch (difficulty) {
            case Constants.DIFF_EASY:   aiSpeed = Constants.AI_SPEED_EASY;   break;
            case Constants.DIFF_HARD:   aiSpeed = Constants.AI_SPEED_HARD;   break;
            default:                    aiSpeed = Constants.AI_SPEED_MEDIUM; break;
        }

        // Paddle positions
        playerPaddleX = Constants.PADDLE_MARGIN;
        aiPaddleX     = Constants.WORLD_WIDTH - Constants.PADDLE_MARGIN - PW;

        currentSet    = 1;
        playerSets    = 0;
        aiSets        = 0;
        playerScore   = 0;
        aiScore       = 0;
        totalPlayerPoints = 0;

        resetPaddles();
        servingToAi = MathUtils.randomBoolean();
        startServe();

        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    openPause();
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 pos = new Vector3(screenX, screenY, 0);
                camera.unproject(pos);
                if (UiFactory.isHit(pos.x, pos.y, PAUSE_BTN_X, PAUSE_BTN_Y,
                        PAUSE_BTN_SIZE, PAUSE_BTN_SIZE)) {
                    openPause();
                }
                return false;
            }
        }));

        game.playMusic("sounds/music/music_gameplay.ogg");
    }

    // -------------------------------------------------------------------------
    // Theme setup
    // -------------------------------------------------------------------------

    private void applyTheme() {
        switch (theme) {
            case Constants.THEME_NEON:
                // player = #00D9FF, ai = #FF006E, ball = #39FF14
                playerColor[0] = 0f;      playerColor[1] = 0.851f; playerColor[2] = 1f;
                aiColor[0]     = 1f;      aiColor[1]     = 0f;     aiColor[2]     = 0.431f;
                ballColor[0]   = 0.224f;  ballColor[1]   = 1f;     ballColor[2]   = 0.078f;
                backgroundAsset = "backgrounds/bg_neon.png";
                break;
            case Constants.THEME_RETRO:
                // player = #888888, ai = #666666, ball = #CCCCCC
                playerColor[0] = 0.533f;  playerColor[1] = 0.533f; playerColor[2] = 0.533f;
                aiColor[0]     = 0.4f;    aiColor[1]     = 0.4f;   aiColor[2]     = 0.4f;
                ballColor[0]   = 0.8f;    ballColor[1]   = 0.8f;   ballColor[2]   = 0.8f;
                backgroundAsset = "backgrounds/bg_retro_crt.png";
                break;
            default: // MINIMAL
                // player = ai = ball = #1a1a1a
                playerColor[0] = 0.102f;  playerColor[1] = 0.102f; playerColor[2] = 0.102f;
                aiColor[0]     = 0.102f;  aiColor[1]     = 0.102f; aiColor[2]     = 0.102f;
                ballColor[0]   = 0.102f;  ballColor[1]   = 0.102f; ballColor[2]   = 0.102f;
                backgroundAsset = "backgrounds/bg_minimal.png";
                break;
        }
    }

    // -------------------------------------------------------------------------
    // Initialization helpers
    // -------------------------------------------------------------------------

    private void resetPaddles() {
        playerPaddleY = (Constants.WORLD_HEIGHT - PH) * 0.5f;
        aiPaddleY     = (Constants.WORLD_HEIGHT - PH) * 0.5f;
    }

    /** Places ball at centre and starts the serve countdown. */
    private void startServe() {
        ballX     = Constants.WORLD_WIDTH  * 0.5f - Constants.BALL_SIZE * 0.5f;
        ballY     = Constants.WORLD_HEIGHT * 0.5f - Constants.BALL_SIZE * 0.5f;
        ballVX    = 0f;
        ballVY    = 0f;
        ballSpeed = Constants.BALL_SPEED_INITIAL;
        serveTimer = SERVE_DELAY;
    }

    /** Launches the ball from centre after the serve delay expires. */
    private void launchBall() {
        // Random angle between 30° and 60° off the horizontal
        float angle = MathUtils.random(MathUtils.PI / 6f, MathUtils.PI / 3f);
        // Vertical direction random
        if (MathUtils.randomBoolean()) angle = -angle;
        // Horizontal direction based on who receives the serve
        float hSign = servingToAi ? 1f : -1f;
        ballVX = hSign * ballSpeed * MathUtils.cos(angle);
        ballVY = ballSpeed * MathUtils.sin(angle);
    }

    // -------------------------------------------------------------------------
    // Game logic
    // -------------------------------------------------------------------------

    private void update(float delta) {
        // Swipe hint countdown
        if (swipeHintTimer > 0f) swipeHintTimer -= delta;

        // Serve countdown
        if (serveTimer > 0f) {
            serveTimer -= delta;
            if (serveTimer <= 0f) launchBall();
            return; // ball frozen during countdown
        }

        // --- Player paddle follows touch Y ---
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            float targetY = touchPos.y - PH * 0.5f;
            playerPaddleY = MathUtils.clamp(targetY, 0f, Constants.WORLD_HEIGHT - PH);
        } else {
            // Keyboard fallback (desktop/emulator) — up/down arrow keys
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                playerPaddleY = Math.min(playerPaddleY + Constants.PADDLE_SPEED * delta,
                        Constants.WORLD_HEIGHT - PH);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                playerPaddleY = Math.max(playerPaddleY - Constants.PADDLE_SPEED * delta, 0f);
            }
        }

        // --- AI paddle tracks ball Y ---
        float aiCentreY = aiPaddleY + PH * 0.5f;
        float ballCentreY = ballY + Constants.BALL_SIZE * 0.5f;
        if (aiCentreY < ballCentreY - 4f) {
            aiPaddleY = Math.min(aiPaddleY + aiSpeed * delta, Constants.WORLD_HEIGHT - PH);
        } else if (aiCentreY > ballCentreY + 4f) {
            aiPaddleY = Math.max(aiPaddleY - aiSpeed * delta, 0f);
        }

        // --- Move ball ---
        ballX += ballVX * delta;
        ballY += ballVY * delta;

        // --- Wall bounces (top / bottom) ---
        if (ballY + Constants.BALL_SIZE >= Constants.WORLD_HEIGHT) {
            ballY  = Constants.WORLD_HEIGHT - Constants.BALL_SIZE;
            ballVY = -Math.abs(ballVY);
        }
        if (ballY <= 0f) {
            ballY  = 0f;
            ballVY = Math.abs(ballVY);
        }

        // --- Paddle collision (player — left side) ---
        Rectangle ballRect   = new Rectangle(ballX, ballY, Constants.BALL_SIZE, Constants.BALL_SIZE);
        Rectangle playerRect = new Rectangle(playerPaddleX, playerPaddleY, PW, PH);
        Rectangle aiRect     = new Rectangle(aiPaddleX, aiPaddleY, PW, PH);

        if (ballVX < 0 && ballRect.overlaps(playerRect)) {
            // Reflect horizontal, add spin based on hit position relative to paddle centre
            float relY    = (ballY + Constants.BALL_SIZE * 0.5f) - (playerPaddleY + PH * 0.5f);
            float normalY = relY / (PH * 0.5f); // -1 to 1
            ballSpeed = Math.min(ballSpeed + Constants.BALL_SPEED_INCREMENT, Constants.BALL_SPEED_MAX);
            float bounceAngle = normalY * MathUtils.PI / 3f; // ±60°
            ballVX = Math.abs(ballVX) * MathUtils.cos(bounceAngle) * (ballSpeed / Constants.BALL_SPEED_INITIAL + 0.5f);
            ballVY = ballSpeed * MathUtils.sin(bounceAngle);
            // Clamp to ballSpeed magnitude
            float mag = (float) Math.sqrt(ballVX * ballVX + ballVY * ballVY);
            if (mag > 0f) { ballVX = ballVX / mag * ballSpeed; ballVY = ballVY / mag * ballSpeed; }
            ballX  = playerPaddleX + PW; // push out
            playSfx("sounds/sfx/sfx_hit.ogg");
        }

        // --- Paddle collision (AI — right side) ---
        if (ballVX > 0 && ballRect.overlaps(aiRect)) {
            float relY    = (ballY + Constants.BALL_SIZE * 0.5f) - (aiPaddleY + PH * 0.5f);
            float normalY = relY / (PH * 0.5f);
            ballSpeed = Math.min(ballSpeed + Constants.BALL_SPEED_INCREMENT, Constants.BALL_SPEED_MAX);
            float bounceAngle = normalY * MathUtils.PI / 3f;
            ballVX = -(Math.abs(ballVX) * MathUtils.cos(bounceAngle) * (ballSpeed / Constants.BALL_SPEED_INITIAL + 0.5f));
            ballVY = ballSpeed * MathUtils.sin(bounceAngle);
            float mag = (float) Math.sqrt(ballVX * ballVX + ballVY * ballVY);
            if (mag > 0f) { ballVX = ballVX / mag * ballSpeed; ballVY = ballVY / mag * ballSpeed; }
            ballX  = aiPaddleX - Constants.BALL_SIZE; // push out
            playSfx("sounds/sfx/sfx_hit.ogg");
        }

        // --- Scoring ---
        if (ballX + Constants.BALL_SIZE < 0f) {
            // Ball exited left — AI scores
            aiScore++;
            servingToAi = false; // serve back to player
            checkSetEnd();
        } else if (ballX > Constants.WORLD_WIDTH) {
            // Ball exited right — Player scores
            playerScore++;
            totalPlayerPoints++;
            servingToAi = true; // serve toward AI
            playSfx("sounds/sfx/sfx_coin.ogg");
            checkSetEnd();
        }
    }

    private void checkSetEnd() {
        if (playerScore >= Constants.SCORE_TO_WIN_SET) {
            playerSets++;
            playSfx("sounds/sfx/sfx_level_complete.ogg");
            if (playerSets >= Constants.SETS_TO_WIN_MATCH) {
                endMatch();
                return;
            }
            resetForNewSet();
        } else if (aiScore >= Constants.SCORE_TO_WIN_SET) {
            aiSets++;
            if (aiSets >= Constants.SETS_TO_WIN_MATCH) {
                endMatch();
                return;
            }
            resetForNewSet();
        } else {
            startServe();
        }
    }

    private void resetForNewSet() {
        currentSet++;
        playerScore = 0;
        aiScore     = 0;
        resetPaddles();
        startServe();
    }

    private void endMatch() {
        boolean playerWon = playerSets >= Constants.SETS_TO_WIN_MATCH;
        int finalScore = playerSets * Constants.SCORE_WIN_SET
                + totalPlayerPoints
                + (playerWon ? Constants.SCORE_WIN_MATCH : 0);
        game.setScreen(new GameOverScreen(game, finalScore, playerSets));
    }

    // -------------------------------------------------------------------------
    // Rendering
    // -------------------------------------------------------------------------

    private void openPause() {
        game.setScreen(new PauseScreen(game, this, difficulty, theme));
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        // Background
        game.batch.begin();
        Texture bg = game.manager.get(backgroundAsset, Texture.class);
        game.batch.draw(bg, 0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

        // Shapes: paddles, ball, centre line, HUD bars
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Centre dividing line
        sr.setColor(0f, 1f, 0.255f, 0.4f); // dim primary
        for (float y = 0; y < Constants.WORLD_HEIGHT; y += 20f) {
            sr.rect(Constants.WORLD_WIDTH * 0.5f - 2f, y, 4f, 12f);
        }

        // Player paddle
        sr.setColor(playerColor[0], playerColor[1], playerColor[2], 1f);
        sr.rect(playerPaddleX, playerPaddleY, PW, PH);

        // AI paddle
        sr.setColor(aiColor[0], aiColor[1], aiColor[2], 1f);
        sr.rect(aiPaddleX, aiPaddleY, PW, PH);

        // Ball
        sr.setColor(ballColor[0], ballColor[1], ballColor[2], 1f);
        sr.rect(ballX, ballY, Constants.BALL_SIZE, Constants.BALL_SIZE);

        // Win progress bar — player progress this set
        float fillFraction = (float) playerScore / Constants.SCORE_TO_WIN_SET;
        // Border
        sr.setColor(COLOR_PRIMARY.r, COLOR_PRIMARY.g, COLOR_PRIMARY.b, 1f);
        sr.rect(BAR_X, BAR_Y, BAR_W, BAR_H);
        // Background
        sr.setColor(0f, 0f, 0f, 0.8f);
        sr.rect(BAR_X + 2, BAR_Y + 2, BAR_W - 4, BAR_H - 4);
        // Fill
        if (fillFraction > 0f) {
            sr.setColor(playerColor[0], playerColor[1], playerColor[2], 1f);
            sr.rect(BAR_X + 2, BAR_Y + 2, (BAR_W - 4) * fillFraction, BAR_H - 4);
        }

        // Set dots (top-centre)
        for (int i = 0; i < Constants.TOTAL_SETS; i++) {
            float dotX = DOTS_START_X + i * (DOT_SIZE + DOT_GAP);
            if (i < playerSets) {
                sr.setColor(playerColor[0], playerColor[1], playerColor[2], 1f);
                sr.rect(dotX, DOTS_Y, DOT_SIZE, DOT_SIZE);
            } else if (i < playerSets + aiSets) {
                // AI-won set
                sr.setColor(aiColor[0], aiColor[1], aiColor[2], 1f);
                sr.rect(dotX, DOTS_Y, DOT_SIZE, DOT_SIZE);
            } else {
                // Pending — border only
                sr.setColor(COLOR_PRIMARY.r, COLOR_PRIMARY.g, COLOR_PRIMARY.b, 1f);
                sr.rect(dotX, DOTS_Y, DOT_SIZE, DOT_SIZE);
                sr.setColor(0f, 0f, 0f, 0.8f);
                sr.rect(dotX + 2, DOTS_Y + 2, DOT_SIZE - 4, DOT_SIZE - 4);
            }
        }

        // Pause button
        sr.setColor(COLOR_PRIMARY.r, COLOR_PRIMARY.g, COLOR_PRIMARY.b, 1f);
        sr.rect(PAUSE_BTN_X, PAUSE_BTN_Y, PAUSE_BTN_SIZE, PAUSE_BTN_SIZE);
        sr.setColor(0f, 0f, 0f, 0.6f);
        sr.rect(PAUSE_BTN_X + 3, PAUSE_BTN_Y + 3, PAUSE_BTN_SIZE - 6, PAUSE_BTN_SIZE - 6);

        sr.end();

        // Text HUD
        game.batch.begin();

        // Player score (top-left, player side)
        game.fontSmall.setColor(Color.WHITE);
        game.fontSmall.draw(game.batch, "PLAYER", 60f, Constants.WORLD_HEIGHT - 12f);
        game.fontScore.setColor(playerColor[0], playerColor[1], playerColor[2], 1f);
        game.fontScore.draw(game.batch, String.valueOf(playerScore), 60f, Constants.WORLD_HEIGHT - 30f);

        // AI score (top-right, AI side)
        game.fontSmall.setColor(Color.WHITE);
        GlyphLayout aiLabel = new GlyphLayout(game.fontSmall, "AI");
        game.fontSmall.draw(game.batch, "AI",
                Constants.WORLD_WIDTH - 60f - aiLabel.width, Constants.WORLD_HEIGHT - 12f);
        GlyphLayout asl = new GlyphLayout(game.fontScore, String.valueOf(aiScore));
        game.fontScore.setColor(aiColor[0], aiColor[1], aiColor[2], 1f);
        game.fontScore.draw(game.batch, String.valueOf(aiScore),
                Constants.WORLD_WIDTH - 60f - asl.width, Constants.WORLD_HEIGHT - 30f);

        // Set label (top-centre)
        String setLabel = "SET " + currentSet + "/" + Constants.TOTAL_SETS;
        GlyphLayout setLayout = new GlyphLayout(game.fontSmall, setLabel);
        game.fontSmall.setColor(COLOR_PRIMARY);
        game.fontSmall.draw(game.batch, setLabel,
                (Constants.WORLD_WIDTH - setLayout.width) * 0.5f,
                Constants.WORLD_HEIGHT - 12f);

        // Pause button label "||"
        game.fontSmall.setColor(Color.WHITE);
        GlyphLayout pauseLayout = new GlyphLayout(game.fontSmall, "II");
        game.fontSmall.draw(game.batch, "II",
                PAUSE_BTN_X + (PAUSE_BTN_SIZE - pauseLayout.width) * 0.5f,
                PAUSE_BTN_Y + (PAUSE_BTN_SIZE + pauseLayout.height) * 0.5f);

        // Swipe hint
        if (swipeHintTimer > 0f) {
            float alpha = Math.min(1f, swipeHintTimer);
            game.fontSmall.setColor(0f, 1f, 0.255f, alpha);
            String hint = "DRAG TO MOVE";
            GlyphLayout hintLayout = new GlyphLayout(game.fontSmall, hint);
            game.fontSmall.draw(game.batch, hint,
                    (Constants.WORLD_WIDTH - hintLayout.width) * 0.5f,
                    80f);
        }

        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    private void playSfx(String path) {
        if (game.sfxEnabled)
            game.manager.get(path, com.badlogic.gdx.audio.Sound.class).play(1.0f);
    }

    // -------------------------------------------------------------------------
    // Screen lifecycle
    // -------------------------------------------------------------------------

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
