package com.aaroncarsonart.symbol.gui;

import com.aaroncarsonart.symbol.game.SymbolCommand;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Handles key input events for the game.
 */
public class SymbolKeyListener implements KeyListener {
    private SymbolBoard symbolBoard;
    private SymbolCommand command = SymbolCommand.NONE;

    public SymbolKeyListener(SymbolBoard symbolBoard) {
        this.symbolBoard = symbolBoard;
    }

    public SymbolCommand getCommand() {
        SymbolCommand currentCommand = command;
        command = SymbolCommand.NONE;
        return currentCommand;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!symbolBoard.isInputPaused()) {
            command = switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> SymbolCommand.MOVE_UP;
                case KeyEvent.VK_DOWN -> SymbolCommand.MOVE_DOWN;
                case KeyEvent.VK_LEFT -> SymbolCommand.MOVE_LEFT;
                case KeyEvent.VK_RIGHT -> SymbolCommand.MOVE_RIGHT;
                default -> SymbolCommand.NONE;
            };
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
