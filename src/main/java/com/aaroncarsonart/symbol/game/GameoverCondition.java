package com.aaroncarsonart.symbol.game;

public enum GameoverCondition {
    NEGATIVE_MOVE_COUNT("Ran out of moves"),
    ORPHANED_TILE("Orphaned tile");

    public final String desc;

    GameoverCondition(String desc) {
        this.desc = desc;
    }
}
