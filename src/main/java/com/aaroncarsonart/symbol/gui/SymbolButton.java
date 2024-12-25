package com.aaroncarsonart.symbol.gui;

import com.aaroncarsonart.symbol.util.Colors;

import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A button with custom graphics styled to match the look and feel of Symbologica.
 */
public class SymbolButton extends JButton {
    private final String text;
    private final Font font;
    private final int fontAscent;

    private final int widthPx;
    private final int heightPx;

    private final int border;
    private final int padding;

    private boolean mouseIsOverComponent;
    private boolean buttonIsPressedDown;

    public SymbolButton(String label) {
        super(label);
        this.text = label;

        font = new Font("Courier New", Font.PLAIN, 15);
        FontMetrics fontMetrics = getFontMetrics(font);
        int fontWidth = fontMetrics.stringWidth("@");
        int fontHeight = fontMetrics.getHeight();
        fontAscent = fontMetrics.getAscent();

        border = 2;
        padding = 4;

        widthPx = fontWidth * text.length() + (border + padding) * 2;
        heightPx = fontHeight + (border + padding) * 2;
        Dimension dimension = new Dimension(widthPx, heightPx);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);

        addMouseListener(getMouseListener());
    }

    public MouseListener getMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                buttonIsPressedDown = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                buttonIsPressedDown = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                mouseIsOverComponent = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseIsOverComponent = false;
                buttonIsPressedDown = false;
                repaint();
            }
        };
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();

        g.setColor(Colors.BLACK);
        g.fillRect(0, 0, widthPx, heightPx);

        int rx = border;
        int ry = border;
        int rw = widthPx - border * 2;
        int rh = heightPx - border * 2;
        g.setColor(getBackground());
        g.fillRect(rx, ry, rw, rh);

        g.setColor(getForeground());
        g.drawRect(rx, ry, rw, rh);

        int tx = border + padding;
        int ty = border + padding + fontAscent;
        g.setFont(font);
        g.drawString(text, tx, ty);

        if (mouseIsOverComponent) {
            int sx = border -1;
            int sy = border -1;
            int sw = widthPx - (border - 1) * 2;
            int sh = heightPx - (border -1) * 2;
            g.setColor(Colors.WHITE);
            g.drawRect(sx, sy, sw, sh);
        }

        if (buttonIsPressedDown) {
            g.setColor(Colors.WHITE_TRANSPARENT);
            g.fillRect(rx, ry, rw, rh);
        }
    }
}
