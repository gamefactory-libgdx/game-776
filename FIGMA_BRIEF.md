# Neon Pong — Figma AI Design Brief

---

## 1. Art Style & Color Palette

**Art Style:** Bold, flat design with neon-inspired geometric shapes and smooth curves. The primary aesthetic is retro-futuristic arcade, blending 1980s Pong nostalgia with modern glowing effects and clean typography. No photorealism; all elements are vector-based with sharp edges and bright, saturated colors. Rounded corners (8px–16px radius) soften geometric UI components while maintaining a contemporary feel.

**Primary Color Palette:**
- Neon Electric Blue: `#00D9FF`
- Neon Hot Pink: `#FF006E`
- Deep Space Navy: `#0A0E27`
- Neon Lime Green: `#39FF14`

**Accent Colors:**
- Cyan Glow: `#00FFFF` (highlights, borders, active states)
- Magenta Shadow: `#FF1493` (secondary accents, error states)

**Typography Mood & Weight:** Use geometric, futuristic sans-serif fonts (e.g., *Orbitron*, *Space Mono*, *Roboto Mono Bold*). Headlines in **900–700 weight** with 1.2–1.5 letter-spacing for dramatic impact. Body text in **400–500 weight** at 12–16px for readability. All text should feel machine-like and retro-tech, with slight digital distortion or glow effects applied in-engine.

---

## 2. App Icon — icon_512.png (512×512px)

**Canvas & Background:** 512×512px square with a radial gradient background transitioning from `#FF006E` (hot pink, outer edges) through `#0A0E27` (deep navy, center), creating a vignette effect that pulls focus toward the center.

**Central Symbol:** Two opposing paddles (horizontal rectangles, 80px wide × 20px tall each) positioned top and bottom, rendered in `#00D9FF` (electric blue) with a 4px inner glow effect. A ball (12px diameter circle) centered between them in `#39FF14` (neon lime green) with a 6px outer glow and a subtle motion blur trail extending diagonally upward-right, suggesting active gameplay.

**Glow & Shadow Effects:** Apply a 12px outer glow blur to both paddles using `#00FFFF` (cyan) at 70% opacity. The ball has a 10px glow in `#39FF14` with a secondary 4px glow in `#FF006E` (hot pink) offset slightly for a "double-glow" dimensional effect. Add a subtle radial inner shadow (2px, 20% opacity, `#000000`) along the bottom edge of the canvas to enhance depth.

**Safe Zone & Overall Mood:** All artwork (paddles, ball, glows) remains within the central 400×400px safe zone. The overall mood is energetic, retro-futuristic, and instantly recognizable as a fast-paced arcade game. The icon should feel alive and kinetic, even in a static state.

---

## 3. Backgrounds (854×480 landscape)

**Background File List:**
1. `backgrounds/bg_main.png` — Main Menu / Title Screen
2. `backgrounds/bg_neon.png` — Neon Theme Game Arena
3. `backgrounds/bg_retro_crt.png` — Retro CRT Theme Game Arena
4. `backgrounds/bg_minimal.png` — Minimal Theme Game Arena

---

### backgrounds/bg_main.png (854×480)
Deep space aesthetic with a subtle animated starfield suggestion (static version shows scattered small white dots, 1–2px diameter, sparse distribution at ~3% density across canvas). Primary color is `#0A0E27` (deep navy), with a subtle diagonal gradient overlay transitioning to `#1a1a3f` (darker purple-navy) from top-left to bottom-right. A thin horizontal glowing line (`#00D9FF`, 2px height, 30% opacity) sits at the 240px vertical center, suggesting the Pong dividing line. No UI elements; this is a clean, moody backdrop for the title and buttons to sit above.

---

### backgrounds/bg_neon.png (854×480)
High-energy neon arcade atmosphere. Base color is `#0A0E27` (deep navy). A bold vertical centerline divides the canvas (4px width, `#00FFFF` electric cyan, with a 12px blur glow on both sides creating a light-saber effect). Horizontal scan-effect lines (1px height, `#00D9FF` at 8% opacity) repeat every 8px vertically to suggest CRT scan lines subtly without overwhelming the image. Corners feature soft radial glows: top-left and bottom-right in `#FF006E` (hot pink, 80px radius, 15% opacity), top-right and bottom-left in `#39FF14` (neon lime, 80px radius, 15% opacity). A thin jagged "voltage" border runs along all four edges (3px width, `#00FFFF`, 60% opacity) to reinforce the electrical aesthetic.

---

### backgrounds/bg_retro_crt.png (854×480)
Authentic 1980s CRT monitor aesthetic. Base color is `#1a1a1a` (very dark gray). Prominent horizontal scanlines (2px height, `#333333` gray, spaced 6px apart, 40% opacity) cover the entire canvas, creating authentic CRT flicker texture. Vertical pixel-grid pattern (1px lines, `#222222`, 30% opacity, spaced 4px apart) overlays the background. A thick rounded rectangle border (12px width, `#555555` medium gray) frames the inner play area, with darker shadow (4px inset, `#0a0a0a` black, 60% opacity) on bottom and right edges, and lighter highlight (2px inset, `#888888`, 40% opacity) on top and left edges to simulate beveled CRT bezel depth. Center dividing line is `#444444` gray (3px width, no glow), minimal and understated against the retro noise.

---

### backgrounds/bg_minimal.png (854×480)
Clean, modern, monochrome aesthetic. Entire background is a solid `#F5F5F5` (off-white / light gray). A single vertical centerline divides the canvas (2px width, `#1a1a1a` deep black, no effects). No textures, no patterns, no gradients—absolute minimalism. The stark contrast between line and background creates visual clarity and focuses all attention on the game mechanics. Optional: very subtle (1% opacity) horizontal guide lines at 25%, 50%, 75% vertical positions in `#D0D0D0` light gray to aid player spatial awareness without cluttering the design.

---

## 4. UI Screens (854×480 landscape)

### MainMenuScreen (854×480)
**Background:** `backgrounds/bg_main.png`

**Header:** "NEON PONG" title text, centered horizontally, positioned at 60px from top, rendered in `#00D9FF` (electric blue) with a bold, geometric sans-serif font (56px, 900 weight, 1.5 letter-spacing). Apply a 4px text glow effect in `#FF006E` (hot pink) offset 2px downward for a subtle shadow glow.

**Buttons:** Four primary buttons arranged vertically in the center (each 280px wide × 56px tall, 16px border-radius):
- **"PLAY"** button at 200px from top, centered horizontally, background `#39FF14` (neon lime), text `#0A0E27` (navy, 600 weight). Hover state: glow effect `#00FFFF` (cyan, 8px blur).
- **"LEADERBOARD"** button at 280px from top, centered, background `#FF006E` (hot pink), text `#F5F5F5` (white, 600 weight). Hover state: glow effect.
- **"SETTINGS"** button at 360px from top, centered, background `#00D9FF` (electric blue), text `#0A0E27` (navy, 600 weight). Hover state: glow effect.
- **"QUIT"** button at 440px from top, centered, background `#555555` (gray), text `#F5F5F5` (white, 600 weight). Hover state: glow effect.

All buttons have 2px borders in `#00FFFF` (cyan). No additional UI elements visible.

---

### DifficultySelectScreen (854×480)
**Background:** `backgrounds/bg_main.png`

**Header:** "SELECT DIFFICULTY" title text, centered, 60px from top, `#00D9FF` (electric blue), 48px bold, 1.2 letter-spacing. Subtle glow in `#FF006E` (hot pink).

**Three Difficulty Cards:** Arranged horizontally, evenly spaced, positioned 180px from top. Each card is 220px wide × 200px tall, background `#1a1a3f` (dark purple-navy), border 2px `#00FFFF` (cyan), border-radius 12px:
- **"EASY"** (left card) with difficulty description text below: "AI plays defensively. Perfect for beginners." Text color `#39FF14` (lime green), 14px, 400 weight. Central icon: simple smiling paddle visual in `#00FFFF`, 60px size.
- **"MEDIUM"** (center card, slightly larger at 240×220px, subtle glow `#FF006E` 8px) with description: "AI reacts quickly. Balanced challenge." Text color `#39FF14`, 14px. Central icon: focused paddle visual in `#00FFFF`, 60px size.
- **"HARD"** (right card) with description: "AI predicts your moves. Expert difficulty." Text color `#FF006E` (hot pink, to indicate danger), 14px. Central icon: aggressive paddle visual in `#FF006E`, 60px size.

Each card is tappable. **Back button** (48px × 48px) positioned top-left corner, background `#555555` (gray), text/icon `#F5F5F5` (white), border-radius 8px.

---

### ThemeSelectScreen (854×480)
**Background:** `backgrounds/bg_main.png`

**Header:** "SELECT THEME" title text, centered, 60px from top, `#00D9FF` (electric blue), 48px bold, 1.2 letter-spacing.

**Three Theme Preview Cards:** Arranged horizontally, centered, positioned 160px from top. Each card is 230px wide × 240px tall, border 2px `#00FFFF` (cyan), border-radius 12px:
- **"NEON"** (left) with a 180px × 160px embedded preview of `backgrounds/bg_neon.png` (top of card). Text below: "NEON" in `#FF006E` (hot pink), 24px bold. Description: "Bright glowing arcade vibes" in `#00FFFF`, 12px.
- **"RETRO CRT"** (center) with embedded preview of `backgrounds/bg_retro_crt.png` (top of card, 180×160px). Text below: "RETRO" in `#00D9FF`, 24px bold. Description: "Classic 1980s scanline style" in `#39FF14`, 12px.
- **"MINIMAL"** (right) with embedded preview of `backgrounds/bg_minimal.png` (top of card, 180×160px). Text below: "MINIMAL" in `#1a1a1a`, 24px bold. Description: "Clean and focused design" in `#555555`, 12px.

Each card is tappable. **Back button** (48px × 48px) positioned top-left, background `#555555` (gray), text/icon `#F5F5F5` (white), border-radius 8px.

---

### NeonGameScreen (854×480)
**Background:** `backgrounds/bg_neon.png`

**HUD Elements:**
- **Score display** (top-left corner, 80px from left, 20px from top): "PLAYER" text in `#00FFFF` (cyan), 12px, above a large score number in `#39FF14` (lime green), 48px bold, drop shadow in `#FF006E` (hot pink).
- **AI Score** (top-right corner, 80px from right, 20px from top): "AI" text in `#FF006E` (hot pink), 12px, above AI score number in `#00D9FF` (blue), 48px bold.
- **Set Tracker** (top-center, 20px from top): "SET 1/3" in `#00FFFF` (cyan), 14px. Three small circles (12px diameter each) inline: filled `#FF006E` (hot pink) for completed sets, outlined `#00FFFF` (cyan) for pending sets.
- **Win Progress** (bottom-left, 20px from bottom): Visual bar (200px wide × 12px tall, background `#333333`, border `#00FFFF`) showing player's progress to 7 wins. Fill color `#39FF14` (lime), animates smoothly as score increases.
- **Pause Button** (top-right corner, 20px from top/right): 48px × 48px, background `#FF006E` (hot pink), icon in `#F5F5F5` (white), border-radius 8px.

**Game Elements:**
- Player paddle (bottom of screen, controlled via swipe): rendered in `#00FFFF` (cyan) with 6px glow in `#00FFFF`.
- AI paddle (top of screen): rendered in `#FF006E` (hot pink) with 6px glow in `#FF006E`.
- Ball: 12px diameter circle, `#39FF14` (lime green) with 8px glow in `#39FF14` and 4px secondary glow in `#FF006E`.

**Swipe Indicator** (if first touch detected): Animated arrow or guideline text "SWIPE TO MOVE" in `#00FFFF`, 16px, center-bottom of screen, fades after 2 seconds.

---

### RetroGameScreen (854×480)
**Background:** `backgrounds/bg_retro_crt.png`

**HUD Elements:**
- **Score display** (top-left, 80px from left, 20px from top): "PLAYER" text in `#444444` (medium gray), 12px, above score number in `#222222` (very dark gray), 48px bold, using a monospace font (Courier, Space Mono) to emphasize retro-digital feel.
- **AI Score** (top-right, 80px from right, 20px from top): "AI" text in `#444444`, 12px, above AI score in `#222222`, 48px bold.
- **Set Tracker** (top-center, 20px from top): "SET 1/3" in `#555555` (gray), 14px monospace. Three small squares (12px × 12px) inline, filled `#333333` for completed, outlined `#666666` for pending.
- **Win Progress** (bottom-left, 20px from bottom): Bar (200px wide × 12px tall, background `#1a1a1a`, border `#444444`) with fill in `#555555` (gray).
- **Pause Button** (top-right, 20px from top/right): 48px × 48px, background `#555555` (gray), icon in `#1a1a1a` (black), border-radius 4px (sharp corners to match retro aesthetic).

**Game Elements:**
- Player paddle: rendered in `#888888` (light gray) with 2px black outline, pixelated edges for authentic CRT look.
- AI paddle: rendered in `#666666` (medium-dark gray) with 2px black outline, pixelated edges.
- Ball: 10px diameter circle, `#CCCCCC` (light gray) with no glow (retro CRT would not have glow effects). Slight pixelation.

**Scanline Flicker Effect** (optional, engine-side): Subtle horizontal scan lines animating across the entire HUD at 2px intervals every frame, 10% opacity, to reinforce authentic CRT monitor flicker.

---

### MinimalGameScreen (854×480)
**Background:** `backgrounds/bg_minimal.png`

**HUD Elements:**
- **Score display** (top-left, 40px from left, 20px from top): "PLAYER" text in `#1a1a1a` (dark gray), 12px, above score number in `#1a1a1a`, 48px bold, clean sans-serif (Roboto).
- **AI Score** (top-right, 40px from right, 20px from top): "AI" text in `#1a1a1a`, 12px, above AI score in `#1a1a1a`, 48px bold.
- **Set Tracker** (top-center, 20px from top): "SET 1/3" in `#1a1a1a`, 14px. Three small circles (10px diameter) inline, filled `#1a1a1a` for completed, outlined `#D0D0D0` (light gray) for pending.
- **Win Progress** (bottom-left, 20px from bottom): Horizontal bar (180px wide × 8px tall, background `#E0E0E0` light gray, border `#1a1a1a` 1px) with fill in `#1a1a1a` (dark gray), smooth linear animation.
- **Pause Button** (top-right, 20px from top/right): 48px × 48px, background `#D0D0D0` (light gray), icon in `#1a1a1a` (dark), border-radius 6px.

**Game Elements:**
- Player paddle (bottom): solid `#1a1a1a` (dark gray) rectangle, no effects.
- AI paddle (top): solid `#1a1a1a` (dark gray) rectangle, no effects.
- Ball: 10px diameter circle, solid `#1a1a1a` (dark gray), no glow, clean and minimal.

**Overall Visual Philosophy:** Absolutely no distracting effects. Every element is precisely aligned to a grid. Typography is clean and readable. The aesthetic prioritizes player focus and gameplay clarity over visual spectacle.

---

### GameOverScreen (854×480)
**Background:** Thematic background based on the game mode:
- If Neon mode: `backgrounds/bg_neon.png` (darkened to 40% opacity overlay in `#000000`)
- If Retro mode: `backgrounds/bg_retro_crt.png` (darkened to 40% opacity overlay)
- If Minimal mode: `backgrounds/bg_minimal.png` (darkened to 20% opacity overlay)

**Central Modal:** 500px wide × 320px tall, centered, background `#0A0E27` (deep navy with 95% opacity for all themes), border 3px `#00FFFF` (cyan), border-radius 16px, soft shadow (8px blur, `#000000`, 50% opacity).

**Header:** "MATCH OVER" title text, centered within modal, 40px from modal top, `#FF006E` (hot pink), 40px bold. Drop shadow in `#00000000` (black, 30% opacity).

**Match Summary:**
- **Winner Name** (centered, 100px from modal top): "PLAYER WINS!" or "AI WINS!" in `#39FF14` (lime green if player wins) or `#FF006E` (hot pink if AI wins), 32px bold.
- **Final Set Score** (centered, 160px from modal top): "FINAL SCORE: 7 - 4" in `#00D9FF` (cyan), 20px, monospace font.
- **Match Record** (centered, 190px from modal top): "SETS: 2 - 1" (showing overall match progress) in `#00FFFF` (cyan), 16px.

**Buttons** (two, positioned at bottom of modal, 60px from modal bottom):
- **"PLAY AGAIN"** (left-aligned, 40px from left edge of modal): 180px wide × 48px tall, background `#39FF14` (lime green), text `#0A0E27` (navy), 18px bold, border-radius 8px, border 2px `#00FFFF` (cyan).
- **"MENU"** (right-aligned, 40px from right edge of modal): 180px wide × 48px tall, background `#FF006E` (hot pink), text `#F5F5F5` (white), 18px bold, border-radius 8px, border 2px `#00FFFF` (cyan).

---

### LeaderboardScreen (854×480)
**Background:** `backgrounds/bg_main.png`

**Header:** "LEADERBOARD" title text, centered, 40px from top, `#00FFFF` (cyan), 44px bold, 1.2 letter-spacing.

**Filter Pills** (below header, 100px from top, centered horizontally, 3 pills evenly spaced):
- **"ALL TIME"** pill: 120px wide × 36px tall, background `#39FF14` (lime green, active state), text `#0A0E27` (navy), 12px bold, border-radius 18px. Border 2px `#00FFFF`.
- **"THIS WEEK"** pill: 120px wide × 36px tall, background `#333333` (dark gray, inactive), text `#999999` (light gray), 12px, border-radius 18px. Border 2px `#555555`.
- **"THIS MONTH"** pill: 120px wide × 36px tall, background `#333333`, text `#999999`, 12px, border-radius 18px. Border 2px `#555555`.

**Leaderboard List** (positioned 160px from top, extends to 420px from top):
- List of top 10 entries. Each row is 780px wide × 40px tall, alternating background colors: `#1a1a3f` (odd rows) and `#0A0E27` (even rows), no borders.
- **Rank** column (left, 20px padding): "#1", "#2", etc. in `#FF006E` (hot pink), 16px bold, right-aligned within 40px width.
- **Player Name** column (60px from left, 200px width): "PlayerName" or "AI Opponent" in `#00D9FF` (cyan), 14px, left-aligned.
- **Difficulty** column (280px from left, 120px width): "HARD", "MEDIUM", "EASY" in `#39FF14` (lime), 12px, center-aligned.
- **Score** column (420px from left, 100px width): "7 - 2" in `#00FFFF` (cyan), 14px bold, right-aligned.
- **Date** column (right, 100px width): "2025-01-15" in `#999999` (light gray), 11px, right-aligned, 20px padding.

Hover state: Row background becomes `#2a2a5f` (slightly lighter purple).

**Back Button** (top-left, 20px from top/left): 48px × 48px, background `#555555` (gray), icon `#F5F5F5` (white), border-radius 8px.

---

### SettingsScreen (854×480)
**Background:** `backgrounds/bg_main.png`

**Header:** "SETTINGS" title text, centered, 40px from top, `#00D9FF` (cyan), 44px bold.

**Settings List** (positioned 120px from top, 6 setting rows, each 720px wide × 56px tall, centered horizontally):

1. **Audio Toggle**
   - Label "AUDIO" on left: `#00FFFF` (cyan), 16px bold.
   - Toggle switch on right (80px × 40px): background `#39FF14` (lime, ON state), border-radius 20px. Circle indicator (36px diameter, `#F5F5F5` white) positioned right.

2. **Haptic Feedback Toggle**
   - Label "HAPTIC FEEDBACK" on left: `#00FFFF` (cyan), 16px bold.
   - Toggle switch on right: background `#FF006E` (hot pink, ON state), border-radius 20px. Circle indicator `#F5F5F5`.

3. **Theme Default**
   - Label "DEFAULT THEME" on left: `#00FFFF` (cyan), 16px bold.
   - Dropdown on right (180px × 40px, background `#1a1a3f`, border 2px `#00FFFF`, text `#39FF14`): shows "Neon", "Retro CRT", or "Minimal".

4. **Difficulty Default**
   - Label "DEFAULT DIFFICULTY" on left: `#00FFFF` (cyan), 16px bold.
   - Dropdown on right: shows "Easy", "Medium", or "Hard" (same styling as Theme Default).

5. **Master Volume Slider** (if audio is ON)
   - Label "MASTER VOLUME" on left: `#00FFFF` (cyan), 16px bold.
   - Horizontal slider (300px wide × 8px tall, background `#333333`, fill `#39FF14`) with a circular handle (20px diameter, background `#FF006E`, border 2px `#00FFFF`). Positioned on right.

6. **Version Info**
   - Label "VERSION" on left: `#999999` (light gray), 12px.
   - Text "1.0.0" on right: `#999999` (light gray), 12px monospace.

Each setting row has a subtle border-bottom (1px, `#333333`, 20% opacity) for visual separation.

**Back Button** (top-left, 20px from top/left): 48px × 48px, background `#555555` (gray), icon `#F5F5F5` (white), border-radius 8px.

---

## 5. Export Checklist

- icon_512.png (512×512)
- backgrounds/bg_main.png (854×480)
- backgrounds/bg_neon.png (854×480)
- backgrounds/bg_retro_crt.png (854×480)
- backgrounds/bg_minimal.png (854×480)
- ui/screen_main_menu.png (854×480)
- ui/screen_difficulty_select.png (854×480)
- ui/screen_theme_select.png (854×480)
- ui/screen_game_neon.png (854×480)
- ui/screen_game_retro_crt.png (854×480)
- ui/screen_game_minimal.png (854×480)
- ui/screen_game_over.png (854×480)
- ui/screen_leaderboard.png (854×480)
- ui/screen_settings.png (854×480)

---

**END OF BRIEF**

This brief is production-ready for Figma AI. All measurements, colors, typography, spacing, and naming conventions are explicit and actionable. Export all files at 2x pixel density (native resolution) in PNG format with transparency where applicable (backgrounds and screens), or opaque backgrounds as specified.
