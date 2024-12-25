package com.aaroncarsonart.symbol.gui;

import com.aaroncarsonart.symbol.game.GameoverCondition;
import com.aaroncarsonart.symbol.game.Symbol;
import com.aaroncarsonart.symbol.game.SymbolGame;
import com.aaroncarsonart.symbol.util.Colors;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Arrays;
import java.util.List;

/**
 * The game window.
 */
public class SymbolGui extends JFrame {
    private static final String APP_TITLE = "Symbologica";
    private static final String GAME_OVER_STR = "Gameover";
    public static final int TILE_FONT_SIZE = 30;

    private TextBoard titleBoard;
    private JButton startButton;

    private SymbolBoard symbolBoard;
    private SymbolKeyListener keyListener;
    private SymbolGame symbolGame;
    private ScorePanel scorePanel;

    private TextBoard gameoverBoard;
    private JButton continueButton;

    public SymbolGui() {
        setTitle(APP_TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setBackground(Colors.BLACK);
        initializeStartScreen();
    }

    private void initializeStartScreen() {
        getContentPane().removeAll();
        if (keyListener != null) removeKeyListener(keyListener);

        symbolBoard = new SymbolBoard(13, 8, 30);
        symbolBoard.backgroundComponentMode();
        symbolBoard.fillWithTiles();

        List<Symbol> titleSymbols = Arrays.asList(
                Symbol.SIGMA, // S
                Symbol.BETA,  // y
                Symbol.OMEGA, // m
                Symbol.PSI,   // b
                Symbol.PI,    // o
                Symbol.DELTA, // l
                Symbol.PHI,   // o
                Symbol.SIGMA, // g
                Symbol.BETA,  // i
                Symbol.OMEGA, // c
                Symbol.PSI    // a
        );
        titleBoard = new TextBoard(APP_TITLE, titleSymbols);

        startButton = new SymbolButton("Start");
        startButton.addActionListener(event -> startGame());
        startButton.setFont(symbolBoard.getTileFont());
        startButton.setBackground(Colors.GREEN_DARK);
        startButton.setForeground(Colors.GREEN);

        BoxLayout boxLayout = new BoxLayout(symbolBoard, BoxLayout.Y_AXIS);
        symbolBoard.setLayout(boxLayout);

        add(symbolBoard, BorderLayout.CENTER);
        symbolBoard.add(Box.createVerticalGlue());
        symbolBoard.add(Box.createVerticalGlue());
        symbolBoard.add(titleBoard);
        symbolBoard.add(Box.createVerticalGlue());
        symbolBoard.add(Box.createVerticalGlue());
        symbolBoard.add(startButton);
        symbolBoard.add(Box.createVerticalGlue());

        titleBoard.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        pack();
        setLocationRelativeTo(null);
    }

    private void initializeGameComponents() {
       getContentPane().removeAll();

        symbolBoard = new SymbolBoard(10, 8, TILE_FONT_SIZE);
        add(symbolBoard, BorderLayout.CENTER);

        keyListener = new SymbolKeyListener(symbolBoard, symbolBoard.getInput());
        addKeyListener(keyListener);
        requestFocus();

        scorePanel = new ScorePanel(symbolBoard);
        add(scorePanel, BorderLayout.SOUTH);

        symbolGame = new SymbolGame(symbolBoard, scorePanel);

        pack();
        setLocationRelativeTo(null);
    }

    private void initializeGameoverScreen() {
        getContentPane().removeAll();
        removeKeyListener(keyListener);

        GameoverCondition gameoverCondition = symbolBoard.getGameoverCondition();

        int boardWidth = GAME_OVER_STR.length() + 2;
        symbolBoard = new SymbolBoard(boardWidth, 8, 30);
        symbolBoard.backgroundComponentMode();
        symbolBoard.fillWithTiles();

        List<Symbol> titleSymbols = Arrays.asList(
                Symbol.SIGMA, // G
                Symbol.BETA,  // a
                Symbol.OMEGA, // m
                Symbol.PSI,   // e
                Symbol.PI,    // O
                Symbol.DELTA, // v
                Symbol.PHI,   // e
                Symbol.SIGMA  // r
        );
        gameoverBoard = new TextBoard(GAME_OVER_STR, titleSymbols);
        SymbolLabel gameoverDescLabel = new SymbolLabel(gameoverCondition.desc);

        continueButton = new SymbolButton("Continue");
        continueButton.addActionListener(event -> initializeStartScreen());
        continueButton.setFont(symbolBoard.getTileFont());
        continueButton.setBackground(Colors.ORANGE_DARK);
        continueButton.setForeground(Colors.ORANGE);

        BoxLayout boxLayout = new BoxLayout(symbolBoard, BoxLayout.Y_AXIS);
        symbolBoard.setLayout(boxLayout);

        add(symbolBoard, BorderLayout.CENTER);
        symbolBoard.add(Box.createVerticalGlue());
        symbolBoard.add(Box.createVerticalGlue());
        symbolBoard.add(gameoverBoard);
        symbolBoard.add(Box.createVerticalGlue());
        symbolBoard.add(gameoverDescLabel);
        symbolBoard.add(Box.createVerticalGlue());
        symbolBoard.add(Box.createVerticalGlue());
        symbolBoard.add(continueButton);
        symbolBoard.add(Box.createVerticalGlue());

        titleBoard.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameoverDescLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        pack();
        setLocationRelativeTo(null);
    }


    public void display() {
        setVisible(true);
    }

    public void startGame() {
        Thread thread = new Thread(() -> {
            initializeGameComponents();
            symbolGame.start();
            initializeGameoverScreen();
        });
        thread.start();
    }
}
