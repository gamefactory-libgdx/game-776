package com.neonpong958967.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/** Pixel-style button renderer shared by all screens. */
public class UiFactory {

    // Primary color: #00FF41
    private static final float PR = 0f / 255f;
    private static final float PG = 255f / 255f;
    private static final float PB = 65f / 255f;

    private static final GlyphLayout layout = new GlyphLayout();

    /**
     * Draws a pixel-style button.
     *
     * <p>The caller must have SpriteBatch active (batch.begin() called) before
     * invoking this method. Internally it ends the batch, draws shapes, then
     * resumes the batch and draws the label.
     */
    public static void drawButton(ShapeRenderer sr, SpriteBatch batch, BitmapFont font,
                                  String label, float x, float y, float w, float h) {
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        // Outer rect — primary color border (#00FF41)
        sr.setColor(PR, PG, PB, 1f);
        sr.rect(x, y, w, h);
        // Inner rect — semi-transparent black fill (3px inset on all sides)
        sr.setColor(0f, 0f, 0f, 0.6f);
        sr.rect(x + 3, y + 3, w - 6, h - 6);
        sr.end();

        batch.begin();
        font.setColor(Color.WHITE);
        layout.setText(font, label);
        font.draw(batch, label,
                x + (w - layout.width) * 0.5f,
                y + (h + layout.height) * 0.5f);
    }

    /** Returns true if world-coordinate point (wx, wy) is within the button rectangle. */
    public static boolean isHit(float wx, float wy, float x, float y, float w, float h) {
        return wx >= x && wx <= x + w && wy >= y && wy <= y + h;
    }
}
