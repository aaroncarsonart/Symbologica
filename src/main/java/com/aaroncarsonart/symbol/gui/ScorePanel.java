package com.aaroncarsonart.symbol.gui;

import com.aaroncarsonart.symbol.util.Colors;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Displays fields such as moves and score to the player.
 */
public class ScorePanel extends JPanel {
    private SymbolBoard symbolBoard;

    private Font font;
    private FontMetrics fontMetrics;
    private int fontWidth;
    private int fontHeight;
    private int fontAscent;

    private int gridWidth;
    private int gridHeight;
    private int border;

    public ScorePanel(SymbolBoard symbolBoard) {
        this.symbolBoard = symbolBoard;

        font = new Font("Courier New", Font.PLAIN, 17);
        fontMetrics = this.getFontMetrics(font);
        fontWidth = fontMetrics.stringWidth("@");
        fontHeight = fontMetrics.getHeight();
        fontAscent = fontMetrics.getAscent();

        gridWidth = 13;
        gridHeight = 2;

        int width = gridWidth * fontWidth + border * 8;
        int height = gridHeight * fontHeight + border;

        int boardWidth = (int) symbolBoard.getPreferredSize().getWidth();
        if (width < boardWidth) {
            width = boardWidth;
        }

        Dimension dimensions = new Dimension(width, height);
        setPreferredSize(dimensions);

        border = 2;
    }


    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setFont(font);

        // draw background
        g.setColor(Colors.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        int rx = border;
        int ry = 0;
        int rw = getWidth() - border * 2;
        int rh = getHeight() - border;
        g.setColor(Colors.GREY_DARK);
        g.fillRect(rx, ry, rw, rh);

        // draw moves
        int tx = border * 2;
        int ty = fontAscent;
        g.setColor(Color.GREEN);
        g.drawString("Moves: ", tx, ty);

        int moves = symbolBoard.getMoves();
        String text = String.valueOf(moves);
        tx = border * 2 + 7 * fontWidth;
        g.setColor(Color.WHITE);
        g.drawString(text, tx, ty);

        // draw score
        tx = border * 2;
        ty = fontHeight + fontAscent;
        g.setColor(Color.MAGENTA);
        g.drawString("Score: ", tx, ty);

        int score = symbolBoard.getScore();
        text = String.valueOf(score);
        tx = border * 2 + 7 * fontWidth;
        g.setColor(Color.WHITE);
        g.drawString(text, tx, ty);
    }
}
