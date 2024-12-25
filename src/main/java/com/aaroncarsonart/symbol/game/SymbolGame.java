package com.aaroncarsonart.symbol.game;

import com.aaroncarsonart.symbol.gui.ScorePanel;
import com.aaroncarsonart.symbol.gui.SymbolBoard;
import com.aaroncarsonart.symbol.util.Direction;
import com.aaroncarsonart.symbol.util.Position;

/**
 * Class for determining game logic.
 */
public class SymbolGame {
    private SymbolBoard symbolBoard;
    private ScorePanel scorePanel;
    private Input input;
    private final int frameRate = 60;
    private final long sleepMilliseconds = 1000 / frameRate;
    private boolean updated = false;
    private boolean doGameLoop = true;

    public SymbolGame(SymbolBoard symbolBoard, ScorePanel scorePanel) {
        this.symbolBoard = symbolBoard;
        this.scorePanel = scorePanel;
        this.input = symbolBoard.getInput();
    }

    public void start() {
        startGameLoop();
    }

    private void startGameLoop() {
        symbolBoard.fillWithTiles();
        while (doGameLoop) {
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
                case SELECT_TILE -> selectTile();
                case DESELECT_TILE -> deselectTile();
                case CLEAR_ALL_TILES -> symbolBoard.debugClearTiles();
                case GAME_OVER -> symbolBoard.debugGameOver();
            }

            updateBlinkAnimationTimer();

            if (updated) {
                symbolBoard.repaint();
                scorePanel.repaint();
                updated = false;
            }

            if (symbolBoard.isGameOver()) {
                doGameLoop = false;
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

    private void selectTile() {
        symbolBoard.selectTile();
        updated = true;
    }

    private void deselectTile() {
        symbolBoard.setSelectedTile(null);
        updated = true;
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

    private void updateBlinkAnimationTimer() {
        if (symbolBoard.isBlinkAnimationEnabled()) {
            if (symbolBoard.updateBlinkAnimationTimer(sleepMilliseconds)) {
                updated = true;
            }
        }
    }
}
