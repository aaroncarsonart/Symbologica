package com.aaroncarsonart.symbol.gui;

import com.aaroncarsonart.symbol.game.Input;
import com.aaroncarsonart.symbol.game.SymbolCommand;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Handles key input events for the game.
 */
public class SymbolKeyListener implements KeyListener {
    private SymbolBoard symbolBoard;
    private Input input;

    public SymbolKeyListener(SymbolBoard symbolBoard, Input input) {
        this.symbolBoard = symbolBoard;
        this.input = input;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!symbolBoard.isInputPaused()) {
            SymbolCommand command = switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> SymbolCommand.MOVE_UP;
                case KeyEvent.VK_DOWN -> SymbolCommand.MOVE_DOWN;
                case KeyEvent.VK_LEFT -> SymbolCommand.MOVE_LEFT;
                case KeyEvent.VK_RIGHT -> SymbolCommand.MOVE_RIGHT;
                case KeyEvent.VK_X,
                     KeyEvent.VK_SPACE,
                     KeyEvent.VK_ENTER -> SymbolCommand.SELECT_TILE;
                case KeyEvent.VK_C -> SymbolCommand.CLEAR_TILES;
                default -> SymbolCommand.NONE;
            };
            input.setCommand(command);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
