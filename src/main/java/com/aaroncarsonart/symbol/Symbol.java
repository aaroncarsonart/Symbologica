package com.aaroncarsonart.symbol;

import java.awt.Color;

/**
 * The set of symbols that can be played with in Symbologica.
 */
public enum Symbol {
    RED('R', Colors.RED, Colors.RED_DARK),
    ORANGE('O', Colors.ORANGE, Colors.ORANGE_DARK),
    YELLOW('Y', Colors.YELLOW, Colors.YELLOW_DARK),
    GREEN('G', Colors.GREEN, Colors.GREEN_DARK),
    BLUE('B', Colors.BLUE, Colors.BLUE_DARK),
    PURPLE('P', Colors.PURPLE, Colors.PURPLE_DARK),
    GREY('E', Colors.GREY, Colors.GREY_DARK),
    EMPTY(' ', Colors.GREY_DARK, Colors.GREY_DARK);

    public final char sprite;
    public final Color fg;
    public final Color bg;

    Symbol(char sprite, Color fg, Color bg) {
        this.sprite = sprite;
        this.fg = fg;
        this.bg = bg;
    }
}
