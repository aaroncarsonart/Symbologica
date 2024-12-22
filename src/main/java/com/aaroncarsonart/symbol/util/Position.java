package com.aaroncarsonart.symbol.util;

import com.aaroncarsonart.symbol.gui.SymbolBoard;

/**
 * A two-dimensional vector for an x and y point.
 * @param x The x coordinate.
 * @param y The y coordinate.
 */
public record Position(int x, int y) {
    /**
     * Used by the hashCode() function.
     */
    private static int gridWidth = 0;

    /**
     * A SymbolBoard must be registered with the Position class before
     * the hashCode() method can be used.
     * @param symbolBoard The SymbolBoard to register.
     */
    public static void register(SymbolBoard symbolBoard) {
        gridWidth = symbolBoard.getGridWidth();
    }

    public Position move(Direction d) {
        return switch (d) {
            case UP -> new Position(x, y - 1);
            case DOWN -> new Position(x, y + 1);
            case LEFT -> new Position(x - 1, y);
            case RIGHT -> new Position(x + 1, y);
        };
    }

    @Override
    public String toString() {
        return ("Position(" + x + ", " + y + ")");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Position p) {
            return this.x == p.x && this.y == p.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return x + y * gridWidth;
    }
}
