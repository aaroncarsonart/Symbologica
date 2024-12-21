package com.aaroncarsonart.symbol.game;

import com.aaroncarsonart.symbol.gui.SymbolBoard;
import com.aaroncarsonart.symbol.gui.SymbolKeyListener;
import com.aaroncarsonart.symbol.util.Direction;
import com.aaroncarsonart.symbol.util.Position;

/**
 * Class for determining game logic.
 */
public class SymbolGame {
    private SymbolBoard symbolBoard;
    private SymbolKeyListener input;
    private int frameRate = 60;
    private int sleepMilliseconds = 1000 / frameRate;
    private boolean updated = false;

    public SymbolGame(SymbolBoard symbolBoard, SymbolKeyListener input) {
        this.symbolBoard = symbolBoard;
        this.input = input;
    }

    public void start() {
        startGameLoop();
    }

    private void startGameLoop() {
        symbolBoard.fillWithTiles();
        while (true) {
            gameLoop();
            try {
                Thread.sleep(sleepMilliseconds);
            } catch (InterruptedException e) {
                // Ignore.
            }
        }
    }

    private void gameLoop() {
        SymbolCommand command = input.getCommand();

        switch (command) {
            case MOVE_UP -> tryMove(Direction.UP);
            case MOVE_DOWN -> tryMove(Direction.DOWN);
            case MOVE_LEFT -> tryMove(Direction.LEFT);
            case MOVE_RIGHT -> tryMove(Direction.RIGHT);
        }

        if (updated) {
            symbolBoard.repaint();
            updated = false;
        }
    }

    private void tryMove(Direction direction) {
        Position cursor = symbolBoard.getGameCursor();
        Position newCursorTarget = cursor.move(direction);

        if (symbolBoard.withinBounds(newCursorTarget)) {
            symbolBoard.setGameCursor(newCursorTarget);
            updated = true;
        }
    }
}
