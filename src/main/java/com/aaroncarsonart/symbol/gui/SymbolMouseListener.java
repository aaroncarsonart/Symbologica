package com.aaroncarsonart.symbol.gui;

import com.aaroncarsonart.symbol.game.Input;
import com.aaroncarsonart.symbol.game.SymbolCommand;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Handles mouse events for the SymbolBoard.q
 */
public class SymbolMouseListener implements MouseListener, MouseMotionListener {
    private SymbolBoard symbolBoard;
    private Input input;

    public SymbolMouseListener(SymbolBoard symbolBoard, Input input) {
        this.symbolBoard = symbolBoard;
        this.input = input;
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
            input.setCommand(SymbolCommand.SELECT_TILE);
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
