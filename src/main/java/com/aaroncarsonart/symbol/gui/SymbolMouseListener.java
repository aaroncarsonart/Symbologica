package com.aaroncarsonart.symbol.gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Handles mouse events for the SymbolBoard.
 */
public class SymbolMouseListener implements MouseMotionListener {
    private SymbolBoard symbolBoard;

    public SymbolMouseListener(SymbolBoard symbolBoard) {
        this.symbolBoard = symbolBoard;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!symbolBoard.isInputPaused()) {
            Point p = symbolBoard.getMousePosition();
            if (p != null) {
                symbolBoard.setGameCursor(p.x, p.y);
                symbolBoard.repaint();
            }
        }
    }
}
