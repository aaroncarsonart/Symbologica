package com.aaroncarsonart.symbol;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;

public class SymbolGui extends JFrame {
    private SymbolBoard symbolBoard;

    public SymbolGui() {
        this.setTitle("Symbologica");
        this.symbolBoard = new SymbolBoard(16, 12, 30);
        this.add(symbolBoard, BorderLayout.CENTER);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void display() {
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
