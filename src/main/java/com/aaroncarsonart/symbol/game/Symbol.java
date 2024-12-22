package com.aaroncarsonart.symbol.game;

import com.aaroncarsonart.symbol.util.Colors;

import java.awt.Color;

/**
 * The set of symbols that can be played with in Symbologica.
 */
public enum Symbol {
    SIGMA('Σ', Colors.RED, Colors.RED_DARK),
    BETA('β', Colors.ORANGE, Colors.ORANGE_DARK),
    OMEGA('Ω', Colors.YELLOW, Colors.YELLOW_DARK),
    PSI('Ψ', Colors.GREEN, Colors.GREEN_DARK),
    PI('∏', Colors.BLUE, Colors.BLUE_DARK),
    DELTA('Δ', Colors.PURPLE, Colors.PURPLE_DARK),
    PHI('Φ', Colors.GREY, Colors.GREY_DARK),
    EMPTY(' ', Colors.GREY, Colors.GREY_DARK);

    public final char sprite;
    public final Color fg;
    public final Color bg;

    Symbol(char sprite, Color fg, Color bg) {
        this.sprite = sprite;
        this.fg = fg;
        this.bg = bg;
    }

    @Override
    public String toString() {
        return String.valueOf(sprite);
    }
}
