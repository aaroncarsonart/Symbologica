package com.aaroncarsonart.symbol.util;

/**
 * A two-dimensional vector for an x and y point.
 * @param x The x coordinate.
 * @param y The y coordinate.
 */
public record Position(int x, int y) {

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
}
