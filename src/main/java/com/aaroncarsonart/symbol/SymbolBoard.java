package com.aaroncarsonart.symbol;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

/**
 * The board containing the grid of squares, drawn for the player to navigate and swap.
 */
public class SymbolBoard extends JPanel {
    private Symbol[][] symbols;
    private int gridWidth;
    private int gridHeight;

    // The width of the grid lines.
    private int widthGL;

    private int widthPx;
    private int heightPx;
    private Dimension dimensions;

    private Font font;
    private FontMetrics fontMetrics;
    private int tileSize;
    private int fontWidth;
    private int fontAscent;

    public SymbolBoard(int width, int height, int fontSize) {
        this.gridWidth = width;
        this.gridHeight = height;
        this.symbols = new Symbol[height][width];

        this.widthGL = 2;

        this.font = new Font("Courier New", Font.PLAIN, fontSize);
        this.fontMetrics = this.getFontMetrics(font);
        this.tileSize = fontMetrics.getHeight();
        this.fontWidth = fontMetrics.stringWidth("@");
        this.fontAscent = fontMetrics.getAscent();

        this.widthPx = (gridWidth) * (tileSize + widthGL) + widthGL;
        this.heightPx = (gridHeight) * (tileSize + widthGL) + widthGL;
        this.dimensions = new Dimension(widthPx, heightPx);
        this.setPreferredSize(dimensions);

        Random random = new Random();
        Symbol[] allSymbols = Symbol.values();

        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                int nextIndex = random.nextInt(allSymbols.length - 1);
                Symbol nextSymbol = allSymbols[nextIndex];
                setSymbol(x, y, nextSymbol);
            }
        }
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setFont(font);
        g.setColor(Colors.BLACK);
        g.fillRect(0, 0, widthPx, heightPx);

        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                Symbol symbol = symbols[y][x];

                int rx = x * (tileSize + widthGL) + widthGL;
                int ry = y * (tileSize + widthGL) + widthGL;
                g.setColor(symbol.bg);
                g.fillRect(rx, ry, tileSize, tileSize);

                int tx = rx + tileSize / 2 - fontWidth / 2;
                int ty = ry + fontAscent;
                g.setColor(symbol.fg);
                g.drawString(symbol.sprite + "", tx, ty);
            }
        }
    }

    public void setSymbol(int x, int y, Symbol symbol) {
        this.symbols[y][x] = symbol;
    }

    public Symbol getSymbol(int x, int y) {
        return this.symbols[y][x];
    }
}
