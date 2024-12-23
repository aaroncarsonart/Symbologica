package com.aaroncarsonart.symbol.gui;

import com.aaroncarsonart.symbol.game.SymbolGame;
import com.aaroncarsonart.symbol.util.Colors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;

/**
 * The game window.
 */
public class SymbolGui extends JFrame {
    private SymbolBoard symbolBoard;
    private SymbolKeyListener keyListener;
    private SymbolGame symbolGame;
    private ScorePanel scorePanel;

    public SymbolGui() {
        setTitle("Symbologica");
        symbolBoard = new SymbolBoard(10, 8, 30);
        add(symbolBoard, BorderLayout.CENTER);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        keyListener = new SymbolKeyListener(symbolBoard, symbolBoard.getInput());
        addKeyListener(keyListener);

        scorePanel = new ScorePanel(symbolBoard);
        add(scorePanel, BorderLayout.SOUTH);

        symbolGame = new SymbolGame(symbolBoard, scorePanel);
        setBackground(Colors.BLACK);
    }

    public void display() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        symbolGame.start();
    }
}
