package com.aaroncarsonart.symbol.game;

import com.aaroncarsonart.symbol.gui.SymbolBoard;
import com.aaroncarsonart.symbol.util.Direction;
import com.aaroncarsonart.symbol.util.Position;

/**
 * Class for determining game logic.
 */
public class SymbolGame {
    private SymbolBoard symbolBoard;
    private Input input;
    private final int frameRate = 60;
    private final long sleepMilliseconds = 1000 / frameRate;
    private boolean updated = false;

    public SymbolGame(SymbolBoard symbolBoard) {
        this.symbolBoard = symbolBoard;
        this.input = symbolBoard.getInput();
    }

    public void start() {
        startGameLoop();
    }

    private void startGameLoop() {
        symbolBoard.fillWithTiles();
        while (true) {
            gameLoop();
            sleep(sleepMilliseconds);
        }
    }

    private void gameLoop() {
        try {
            SymbolCommand command = input.getCommand();

            switch (command) {
                case MOVE_UP -> tryMove(Direction.UP);
                case MOVE_DOWN -> tryMove(Direction.DOWN);
                case MOVE_LEFT -> tryMove(Direction.LEFT);
                case MOVE_RIGHT -> tryMove(Direction.RIGHT);
                case SELECT_TILE -> symbolBoard.selectTile();
                // debug features
                case CLEAR_TILES -> symbolBoard.clearTiles();
            }

            if (updated) {
                symbolBoard.repaint();
                updated = false;
            }
        } catch (Exception e) {
            printDebugInfo();
            throw e;
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

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // Ignore.
        }
    }

    private void printDebugInfo() {
        long seed = symbolBoard.getRandomSeed();
        System.out.println("seed: " + seed);
        System.out.println(symbolBoard);
        symbolBoard.printTileTotals();
    }
}
