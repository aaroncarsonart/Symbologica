package com.aaroncarsonart.symbol.gui;

import com.aaroncarsonart.symbol.game.Symbol;
import com.aaroncarsonart.symbol.util.Colors;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

/**
 * Used to display a single line of text in a SymbolBoard.
 */
public class TextBoard extends SymbolBoard {
    private String text;

    public TextBoard(String text, List<Symbol> symbols) {
        super(text.length(), 1, SymbolGui.TILE_FONT_SIZE);

        if (text.length() != symbols.size()) {
            throw new IllegalStateException("TextBoard requires text and symbols to be of equal size.");
        }

        this.text = text;
        for (int x = 0; x < symbols.size(); x++) {
            Symbol symbol = symbols.get(x);
            setSymbol(x, 0, symbol);
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();

        // Paint the grid of tiles.
        g.setFont(tileFont);
        g.setColor(Colors.BLACK);
        g.fillRect(0, 0, widthPx, heightPx);

        for (int i = 0; i < text.length(); i++) {
            Symbol symbol = getSymbol(i, 0);
            char sprite = text.charAt(i);
            drawTile(g, i, 0, symbol.bg, symbol.fg, sprite);
        }

        // Draw the white border.
        int bx = 1;
        int by = 1;
        int bw = widthPx - 2;
        int bh = heightPx - 2;

        g.setColor(Colors.WHITE);
        g.drawRect(bx, by, bw, bh);
    }
}
