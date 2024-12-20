package com.aaroncarsonart.symbol.gui;

import com.aaroncarsonart.symbol.game.SymbolGame;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;

/**
 * The game window.
 */
public class SymbolGui extends JFrame {
    private SymbolBoard symbolBoard;
    private SymbolKeyListener keyListener;
    private SymbolGame symbolGame;

    public SymbolGui() {
        this.setTitle("Symbologica");
        this.symbolBoard = new SymbolBoard(16, 12, 30);
        this.add(symbolBoard, BorderLayout.CENTER);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.keyListener = new SymbolKeyListener();
        this.addKeyListener(keyListener);

        this.symbolGame = new SymbolGame(symbolBoard, keyListener);
    }

    public void display() {
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.symbolGame.start();
    }
}