package com.aaroncarsonart.symbol;

import com.aaroncarsonart.symbol.gui.SymbolGui;

/**
 * Entry point for running Symbologica.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, Symbologica!");
        SymbolGui symbolGui = new SymbolGui();
        symbolGui.display();
    }
}
