package com.aaroncarsonart.symbol.gui;

import com.aaroncarsonart.symbol.util.Position;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Handles mouse events for the SymbolBoard.q
 */
public class SymbolMouseListener implements MouseListener, MouseMotionListener {
    private SymbolBoard symbolBoard;

    public SymbolMouseListener(SymbolBoard symbolBoard) {
        this.symbolBoard = symbolBoard;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!symbolBoard.isInputPaused()) {
            Position cursor = symbolBoard.getGameCursor();
            Position selectedTile = symbolBoard.getSelectedTile();
            if (selectedTile == null) {
                symbolBoard.setSelectedTile(cursor);
            } else if (cursor == selectedTile) {
                symbolBoard.setSelectedTile(null);
            } else {
                symbolBoard.setSwapTarget(cursor);
                symbolBoard.performSwap();
            }
            symbolBoard.repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
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
