package com.aaroncarsonart.symbol.gui;

import javax.swing.JLabel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import static com.aaroncarsonart.symbol.gui.SymbolBoard.drawTextOutline;

/**
 * For displaying the gameover reason on the gameover screen.
 */
public class SymbolLabel extends JLabel {
    private Font font;
    private int fontAscent;
    private int padding;
    private BasicStroke outlineStroke;

    public SymbolLabel(String text) {
        super(text);

        font = new Font("Courier New", Font.PLAIN, 20);
        FontMetrics fontMetrics = getFontMetrics(font);
        int fontWidth = fontMetrics.stringWidth("@");
        int fontHeight = fontMetrics.getHeight();
        fontAscent = fontMetrics.getAscent();
        padding = 4;

        int componentWidth = fontWidth * getText().length() + padding * 2;
        int componentHeight = fontHeight + padding * 2;
        Dimension dimension = new Dimension(componentWidth, componentHeight);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);

        outlineStroke = new BasicStroke(5.0F);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();

        int x = padding;
        int y = padding + fontAscent;
        String text = getText();
        drawTextOutline(g, font, text, x, y, Color.DARK_GRAY, outlineStroke);

        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
    }
}
