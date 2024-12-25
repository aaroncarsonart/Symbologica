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
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
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

    private Point prevWindowLocation;
    private Dimension prevWindowSize;

    public SymbolGui() {
        setTitle(APP_TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setBackground(Colors.BLACK);
        initializeStartScreen();
    }

    private void initializeStartScreen() {
        cacheWindowLocationAndSize();
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
        calculateNewWindowLocation();
    }

    private void initializeGameComponents() {
        cacheWindowLocationAndSize();
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
        calculateNewWindowLocation();
    }

    private void initializeGameoverScreen() {
        cacheWindowLocationAndSize();
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
        calculateNewWindowLocation();
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

    private void cacheWindowLocationAndSize() {
        if (isVisible()) {
            prevWindowLocation = getLocationOnScreen();
            prevWindowSize = getSize();
        }
    }

    private void calculateNewWindowLocation() {
        if (isVisible()) {
            Dimension newWindowSize = getSize();

            int nx = prevWindowLocation.x + prevWindowSize.width / 2 - newWindowSize.width / 2;
            int ny = prevWindowLocation.y + prevWindowSize.height / 2 - newWindowSize.height / 2;

            if (nx < 0) nx = 0;
            if (ny < 0) ny = 0;

            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();

            if (nx + newWindowSize.width > screenSize.width) {
                nx = screenSize.width - newWindowSize.width;
            }
            if (ny + newWindowSize.height > screenSize.height) {
                ny = screenSize.height - newWindowSize.height;
            }

            setLocation(nx, ny);
        } else {
            setLocationRelativeTo(null);
        }
    }
}
