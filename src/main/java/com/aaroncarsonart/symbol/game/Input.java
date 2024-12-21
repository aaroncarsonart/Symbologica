package com.aaroncarsonart.symbol.game;

/**
 * For storing and retrieving user input data.
 */
public class Input {
    private SymbolCommand command;

    public Input() {
        command = SymbolCommand.NONE;
    }

    public SymbolCommand getCommand() {
        SymbolCommand currentCommand = command;
        command = SymbolCommand.NONE;
        return currentCommand;
    }

    public void setCommand(SymbolCommand command) {
        this.command = command;
    }
}
