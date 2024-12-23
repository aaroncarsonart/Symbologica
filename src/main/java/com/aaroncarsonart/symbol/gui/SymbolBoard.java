package com.aaroncarsonart.symbol.gui;

import com.aaroncarsonart.symbol.game.Input;
import com.aaroncarsonart.symbol.game.Symbol;
import com.aaroncarsonart.symbol.util.Colors;
import com.aaroncarsonart.symbol.util.Direction;
import com.aaroncarsonart.symbol.util.Position;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

/**
 * The board containing the grid of squares, drawn for the player to navigate and swap.
 */
public class SymbolBoard extends JPanel {
    private Symbol[][] symbols;
    private int gridWidth;
    private int gridHeight;

    // The width of the grid lines.
    private int widthGL;

    private int widthPx;
    private int heightPx;
    private Dimension dimensions;

    private Font tileFont;
    private int tileSize;
    private int tileFontWidth;
    private int tileFontAscent;

    private Font moveFont;
    private int moveFontWidth;
    private int moveFontAscent;
    private BasicStroke outlineStroke;

    private Position gameCursor;
    private SymbolMouseListener mouseListener;
    private Input input;
    private int fillGridSleepMillis;
    private int finishFillGridSleepMillis;
    private boolean pauseInput;

    private Position selectedTile;
    private Position swapTarget;
    private Set<Position> adjacencySet;
    private Position orphanedTile;

    private EnumMap<Symbol, Integer> symbolCounts;
    private int remainingTiles;
    private long seed;
    private Random random;
    private boolean skipSleepAnimation;
    private boolean debug;

    private boolean blinkAnimationEnabled;
    private boolean orphanBlinkAnimationEnabled;
    private boolean movesBlinkAnimation;
    private boolean orphanBlinkAnimation;
    private long movesBlinkAnimationTimer;
    private long orphanBlinkAnimationTimer;

    private int score;
    private int moves;

    public SymbolBoard(int width, int height, int fontSize) {
        gridWidth = width;
        gridHeight = height;
        symbols = new Symbol[height][width];

        widthGL = 2;

        tileFont = new Font("Courier New", Font.PLAIN, fontSize);
        FontMetrics tileFontMetrics = getFontMetrics(tileFont);
        tileSize = tileFontMetrics.getHeight();
        tileFontWidth = tileFontMetrics.stringWidth("@");
        tileFontAscent = tileFontMetrics.getAscent();

        moveFont = new Font("Courier New", Font.BOLD, 15);
        FontMetrics moveFontMetrics = getFontMetrics(moveFont);
        moveFontWidth = moveFontMetrics.charWidth('@');
        moveFontAscent = moveFontMetrics.getAscent();
        outlineStroke = new BasicStroke(3.0f);

        widthPx = (gridWidth) * (tileSize + widthGL) + widthGL;
        heightPx = (gridHeight) * (tileSize + widthGL) + widthGL;
        dimensions = new Dimension(widthPx, heightPx);
        setPreferredSize(dimensions);

        gameCursor = new Position(0, 0);
        input = new Input();
        mouseListener = new SymbolMouseListener(this, input);
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);

        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                setSymbol(x, y, Symbol.EMPTY);
            }
        }

        // Calculate a reasonable sleep rate for animating filling the grid.
        fillGridSleepMillis = 1000 / (gridWidth * gridHeight);
        if (fillGridSleepMillis == 0) {
            fillGridSleepMillis = 1;
        } else if (fillGridSleepMillis > 50) {
            fillGridSleepMillis = 50;
        }
        finishFillGridSleepMillis = 500;

        Position.register(this);

        adjacencySet = new HashSet<>();
        symbolCounts = new EnumMap<>(Symbol.class);
        for (Symbol symbol : Symbol.values()) {
            if (symbol == Symbol.EMPTY) {
                continue;
            }
            symbolCounts.put(symbol, 0);
        }
        remainingTiles = 0;

        random = new Random();
        seed = random.nextLong();
        random.setSeed(seed);

        skipSleepAnimation = false;
        debug = true;
        movesBlinkAnimation = false;
        orphanBlinkAnimation = false;
        blinkAnimationEnabled = false;
        orphanBlinkAnimationEnabled = false;

        score = 0;
        moves = 10;

        // Hide mouse cursor in component.
        Toolkit toolKit = Toolkit.getDefaultToolkit();
        BufferedImage cursorImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = toolKit.createCustomCursor(cursorImage, new Point(0, 0), "blank cursor");
        setCursor(blankCursor);
    }

    public void clearTiles() {
        if (debug) {
            pauseInput = true;

            // Clear the board.
            for (int x = 0; x < gridWidth; x++) {
                for (int y = 0; y < gridHeight; y++) {
                    setSymbol(x, y, Symbol.EMPTY);
                }
            }

            // Clear the counts.
            for (Symbol symbol : Symbol.values()) {
                symbolCounts.compute(symbol, (s, i) -> 0);
            }
            remainingTiles = 0;

            // Update display, pause, and refill the board.
            repaint();
            sleep(finishFillGridSleepMillis);
            fillWithTiles();
        }
    }

    /**
     * Fill the SymbolBoard with tiles. Ensure no two symbols are adjacent to one another.
     * Animates the process by drawing one random tile at a time to the SymbolBoard.
     */
    public void fillWithTiles() {
        pauseInput = true;

        // Collect a list of all positions.
        List<Position> cells = new ArrayList<>();
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                cells.add(new Position(x, y));
            }
        }

        // Shuffle the list.
        Collections.shuffle(cells, random);
        Symbol[] allSymbols = Symbol.values();

        // Populate the shuffled list with random symbols.
        // Symbols are guaranteed to not be adjacent.
        for (Position cell : cells) {
            List<Symbol> symbolsToUse = new ArrayList<>(List.of(allSymbols));
            symbolsToUse.remove(Symbol.EMPTY);
            addRandomCell(symbolsToUse, cell);
            remainingTiles++;

            repaint();
            sleep(fillGridSleepMillis);
        }

        // Replace orphaned symbols with a different symbol.
        List<Symbol> symbolsToRemove = new ArrayList<>();
        for (Symbol symbol : symbolCounts.keySet()) {
            int count = symbolCounts.get(symbol);
            if (count == 0) {
                symbolsToRemove.add(symbol);
            }
        }

        for (Symbol symbol : symbolCounts.keySet()) {
            int count = symbolCounts.get(symbol);

            if (count == 1) {
                symbolsToRemove.add(symbol);

                for (Position cell : cells) {
                    if (getSymbol(cell) == symbol) {

                        List<Symbol> availableSymbols = new ArrayList<>(List.of(allSymbols));
                        availableSymbols.remove(Symbol.EMPTY);
                        availableSymbols.removeAll(symbolsToRemove);
                        symbolCounts.put(symbol, 0);
                        addRandomCell(availableSymbols, cell);

                        repaint();
                        sleep(fillGridSleepMillis);
                        break;
                    }
                }
            }
        }

        repaint();
        pauseInput = false;
    }

    private void sleep(long millis) {
        if (!skipSleepAnimation) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                // Ignore.
            }
        }
    }

    /**
     * Add a random symbol to the board at the given position.
     * @param availableSymbols The list of symbols available to be added.
     * @param cell The position to add the symbol at.
     */
    private void addRandomCell(List<Symbol> availableSymbols, Position cell) {
        List<Position> neighbors = getNeighbors(cell);
        for (Position  neighbor : neighbors) {
            Symbol adjacentSymbol = getSymbol(neighbor);
            availableSymbols.remove(adjacentSymbol);
        }

        Symbol nextSymbol;
        if (availableSymbols.isEmpty()) {
            Position neighbor = neighbors.get(random.nextInt(neighbors.size()));
            nextSymbol = getSymbol(neighbor);
        } else {
            int nextIndex = random.nextInt(availableSymbols.size());
            nextSymbol = availableSymbols.get(nextIndex);
        }

        setSymbol(cell.x(), cell.y(), nextSymbol);
        symbolCounts.compute(nextSymbol, (s, i) -> (i == null) ? 1 : i + 1);
    }

    private List<Position> getNeighbors(Position p) {
        List<Position> neighbors = new ArrayList<>();

        if (p.x() > 0) {
            neighbors.add(p.move(Direction.LEFT));
        }
        if (p.x() < gridWidth - 1) {
            neighbors.add(p.move(Direction.RIGHT));
        }
        if (p.y() > 0) {
            neighbors.add(p.move(Direction.UP));
        }
        if (p.y() < gridHeight - 1) {
            neighbors.add(p.move(Direction.DOWN));
        }

        return neighbors;
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        // Paint the grid of tiles.
        g.setFont(tileFont);
        g.setColor(Colors.BLACK);
        g.fillRect(0, 0, widthPx, heightPx);

        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                Symbol symbol = getSymbol(x, y);

                int rx = x * (tileSize + widthGL) + widthGL;
                int ry = y * (tileSize + widthGL) + widthGL;
                g.setColor(symbol.bg);
                g.fillRect(rx, ry, tileSize, tileSize);

                int tx = rx + tileSize / 2 - tileFontWidth / 2;
                int ty = ry + tileFontAscent;
                g.setColor(symbol.fg);
                g.drawString(symbol.sprite + "", tx, ty);
            }
        }

        // Paint the cursor.
        if (!pauseInput) {
            int cx = gameCursor.x() * (tileSize + 2) + 1;
            int cy = gameCursor.y() * (tileSize + 2) + 1;
            int cw = tileSize + 2;
            int ch = cw;

            g.setColor(Colors.WHITE);
            g.drawRect(cx, cy, cw, ch);

            // Paint move updates in cursor.
            if (movesBlinkAnimation) {
                int moveModifier = 0;
                if (!adjacencySet.isEmpty()) {
                    moveModifier = calculatePoints(adjacencySet.size());
                } else if (selectedTile != null) {
                    // Negated as swaps cost moves.
                    moveModifier = -calculateDistance(selectedTile, gameCursor);
                }
                if (moveModifier != 0) {
                    int mx = gameCursor.x() * (tileSize + 2) + 2;
                    int my = gameCursor.y() * (tileSize + 2) + 2;
                    int mw = tileSize;
                    int mh = tileSize;
                    g.setColor(Colors.BLACK_TRANSPARENT);
                    g.fillRect(mx, my, mw, mh);

                    // Draw the new move count.
                    String newMoveCountText = String.valueOf(moves + moveModifier);

                    int mx1 = mx + tileSize / 2 - moveFontWidth * newMoveCountText.length() / 2;
                    int my1 = my + tileSize / 2 - moveFontAscent - 2 + moveFontAscent;

                    drawTextOutline(g, moveFont, newMoveCountText, mx1, my1, Color.BLACK);

                    g.setFont(moveFont);
                    g.setColor(Colors.WHITE);
                    g.drawString(newMoveCountText, mx1, my1);

                    // Draw the move modifier.
                    String moveModifierText = ((moveModifier > 0) ? "+" : "") + moveModifier;

                    int mx2 = mx + tileSize / 2 - moveFontWidth * moveModifierText.length() / 2;
                    int my2 = my + tileSize / 2 - moveFontAscent - 2 + moveFontAscent * 2;

                    drawTextOutline(g, moveFont, moveModifierText, mx2, my2, Color.BLACK);

                    g.setFont(moveFont);
                    Color modifierColor = (moveModifier > 0) ? Colors.GREEN : Colors.RED;
                    g.setColor(modifierColor);
                    g.drawString(moveModifierText, mx2, my2);
                }
            }
        }

        // Paint the selected tile.
        if (selectedTile != null) {
            int sx = selectedTile.x() * (tileSize + 2) + 2;
            int sy = selectedTile.y() * (tileSize + 2) + 2;
            int sw = tileSize;
            int sh = sw;

            Symbol symbol = getSymbol(selectedTile);
            g.setColor(symbol.fg);
            g.drawRect(sx, sy, sw, sh);
        }

        // Paint the adjacency set.
        if (!adjacencySet.isEmpty()) {
            for (Position tile : adjacencySet) {
                int tx = tile.x() * (tileSize + 2) + 2;
                int ty = tile.y() * (tileSize + 2) + 2;
                int tw = tileSize;
                int th = tw;

                Symbol symbol = getSymbol(tile);
                g.setColor(symbol.fg);
                g.drawRect(tx, ty, tw, th);
            }
        }

        // Paint the orphaned tile warning blinking cursor.
        if (orphanBlinkAnimation && orphanedTile != null) {
            int tx = orphanedTile.x() * (tileSize + 2) + 2;
            int ty = orphanedTile.y() * (tileSize + 2) + 2;
            int tw = tileSize;
            int th = tw;

            Symbol symbol = getSymbol(orphanedTile);
            g.setColor(symbol.fg);
            g.drawRect(tx, ty, tw, th);
        }
    }

    public void drawTextOutline(Graphics2D g, Font font, String text, int x, int y, Color outlineColor) {
        Stroke originalStroke = g.getStroke();

        GlyphVector glyphVector = font.createGlyphVector(g.getFontRenderContext(), text);
        Shape textOutline = glyphVector.getOutline(x, y);

        g.setFont(font);
        g.setColor(outlineColor);
        g.setStroke(outlineStroke);
        g.draw(textOutline);

        g.setStroke(originalStroke);
    }

    public void setSymbol(int x, int y, Symbol symbol) {
        symbols[y][x] = symbol;
    }

    public void setSymbol(Position p, Symbol symbol) {
        setSymbol(p.x(), p.y(), symbol);
    }

    public Symbol getSymbol(int x, int y) {
        return symbols[y][x];
    }

    public Symbol getSymbol(Position p) {
        return getSymbol(p.x(), p.y());
    }

    public boolean withinBounds(Position p) {
        return 0 <= p.x() && p.x() < gridWidth && 0 <= p.y() && p.y() <gridHeight;
    }

    public Position getGameCursor() {
        return gameCursor;
    }

    public void setGameCursor(Position gameCursor) {
        if (!this.gameCursor.equals(gameCursor)) {
            this.gameCursor = gameCursor;
            checkAdjacency(this.gameCursor);

            if (!adjacencySet.isEmpty() || (selectedTile != null &&
                    calculateDistance(selectedTile, this.gameCursor) > 0)) {
                enableAndResetBlinkAnimation();
            } else {
                disableBlinkAnimation();
            }
        }
    }

    public void setGameCursor(int mouseX, int mouseY) {
        int width = widthPx - 2;
        int height = heightPx - 2;
        int x = mouseX - 1;
        int y = mouseY - 1;

        if (x == -1) x = 0;
        if (x == width) x = width - 1;
        if (y == -1) y = 0;
        if (y == height) y = height - 2;

        int cx = x / (tileSize + 2);
        int cy = y / (tileSize + 2);

        if (cx < 0) cx = 0;
        if (cx >= gridWidth) cx = gridWidth - 1;
        if (cy < 0) cy = 0;
        if (cy >= gridHeight) cy = gridHeight - 1;

        Position cursor = new Position(cx, cy);
        setGameCursor(cursor);
    }

    public boolean isInputPaused() {
        return pauseInput;
    }

    public void setSelectedTile(Position selectedTile) {
        this.selectedTile = selectedTile;
    }

    public void setSwapTarget(Position swapTarget) {
        this.swapTarget = swapTarget;
    }

    public void performSwap() {
        Symbol symbol1 = getSymbol(selectedTile);
        Symbol symbol2 = getSymbol(swapTarget);

        setSymbol(selectedTile, symbol2);
        setSymbol(swapTarget, symbol1);
        checkAdjacency(swapTarget);

        calculateMoveCost();

        setSelectedTile(null);
        setSwapTarget(null);

        if (!adjacencySet.isEmpty()) {
            enableAndResetBlinkAnimation();
        } else {
            disableBlinkAnimation();
        }
    }

    public void selectTile() {
        if (!adjacencySet.isEmpty()) {
            clearAdjacencySet();
            setSelectedTile(null);
        } else if (selectedTile == null) {
            setSelectedTile(gameCursor);
        } else if (gameCursor.equals(selectedTile)) {
            setSelectedTile(null);
        } else {
            setSwapTarget(gameCursor);
            performSwap();
        }
        repaint();
    }

    public Input getInput() {
        return input;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    private void checkAdjacency(Position tile) {
        if (hasAdjacency(tile)) {
            if (adjacencySet.isEmpty() || !adjacencySet.contains(tile)) {
                deselectAdjacencySet();
                buildAdjacencySet(tile);
                checkForOrphanedTile();
            }
        } else if (!adjacencySet.isEmpty()) {
            deselectAdjacencySet();
        }
    }

    private boolean hasAdjacency(Position tile) {
        Symbol tileSymbol = getSymbol(tile);
        if (tileSymbol == Symbol.EMPTY) {
            return false;
        }

        List<Position> neighbors = getNeighbors(tile);
        for (Position neighbor : neighbors) {
            Symbol neighboringSymbol = getSymbol(neighbor);
            if (tileSymbol == neighboringSymbol) {
                return true;
            }
        }
        return false;
    }

    private void buildAdjacencySet(Position start) {
        Symbol symbol = getSymbol(start);
        Stack<Position> stack = new Stack<>();
        Set<Position> visited = new HashSet<>();

        stack.add(start);
        while (!stack.isEmpty()) {
            Position next = stack.pop();
            visited.add(next);
            Symbol nextSymbol = getSymbol(next);

            if (symbol == nextSymbol) {
                adjacencySet.add(next);

                List<Position> neighbors = getNeighbors(next);
                neighbors.removeIf(visited::contains);
                stack.addAll(neighbors);
            }
        }
    }

    private void clearAdjacencySet() {
        // Adjust the tile counts.
        Symbol symbol = getSymbol(adjacencySet.iterator().next());
        int tileCount = adjacencySet.size();
        symbolCounts.compute(symbol, (s, i) -> (i == null) ? -1 : i - tileCount);
        remainingTiles -= tileCount;

        // Clear the tiles.
        for (Position next : adjacencySet) {
            setSymbol(next, Symbol.EMPTY);
        }
        deselectAdjacencySet();
        repaint();

        calculateMoveCost(tileCount);

        // Fill again if board is empty.
        if (remainingTiles == 0) {
            sleep(finishFillGridSleepMillis);
            fillWithTiles();
        }
    }

    private void deselectAdjacencySet() {
        adjacencySet.clear();
        orphanedTile = null;
    }

    private void checkForOrphanedTile() {
        Symbol symbol = getSymbol(adjacencySet.iterator().next());
        int tileCount = adjacencySet.size();
        int countIfOrphanExists = tileCount + 1;
        if (symbolCounts.getOrDefault(symbol, 0) == countIfOrphanExists) {
            for (int x = 0; x < gridWidth; x++) {
                for (int y = 0; y < gridHeight; y++) {
                    Position tile = new Position(x, y);
                    Symbol nextSymbol = getSymbol(tile);
                    if (symbol == nextSymbol && !adjacencySet.contains(tile)) {
                        orphanedTile = tile;
                        return;
                    }
                }
            }
        }
    }

    public void printTileTotals() {
        for (Symbol symbol : symbolCounts.keySet()) {
            System.out.println(symbol.sprite + ": " + symbolCounts.get(symbol));
        }
        System.out.println("remainingTiles: " + remainingTiles);
        System.out.println();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                sb.append(getSymbol(x, y).sprite);
                sb.append(' ');
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    public long getRandomSeed() {
        return seed;
    }

    private int calculatePoints(int tileCount) {
        return tileCount * 2 - 2;
    }

    private void calculateMoveCost(int tileCount) {
        int points = calculatePoints(tileCount);
        score += points;
        moves += points;
    }

    private int calculateDistance(Position p1, Position p2) {
        double term1 = Math.pow(p1.x() - p2.x(), 2);
        double term2 = Math.pow(p1.y() - p2.y(), 2);
        double distance = Math.sqrt(term1 + term2);
        return (int) Math.ceil(distance);
    }

    private void calculateMoveCost() {
        int moveCost = calculateDistance(selectedTile, swapTarget);
        moves -= moveCost;
    }

    public int getScore() {
        return score;
    }

    public int getMoves() {
        return moves;
    }

    public boolean isBlinkAnimationEnabled() {
        return blinkAnimationEnabled;
    }

    private void enableAndResetBlinkAnimation() {
        blinkAnimationEnabled = true;
        movesBlinkAnimation = true;
        resetMovesBlinkAnimationTimer();

        if (orphanedTile != null) {
            orphanBlinkAnimationEnabled = true;
            orphanBlinkAnimation = true;
            resetOrphanBlinkAnimationTimer();
        }
    }

    private void disableBlinkAnimation() {
        blinkAnimationEnabled = false;
        orphanBlinkAnimationEnabled = false;
        movesBlinkAnimation = false;
        orphanBlinkAnimation = false;
    }

    private void resetMovesBlinkAnimationTimer() {
        movesBlinkAnimationTimer = 750;
    }

    private void resetOrphanBlinkAnimationTimer() {
        orphanBlinkAnimationTimer = 325;
    }

    private void toggleMovesBlinkAnimation() {
        movesBlinkAnimation = !movesBlinkAnimation;
    }

    private void toggleOrphanBlinkAnimation() {
        orphanBlinkAnimation = !orphanBlinkAnimation;
    }

    /**
     * Updates the blink animation timer, and returns true if the animation is toggled.
     * @param millis The number of milliseconds to update the timer by.
     * @return True if the animatin is toggled, else false.
     */
    public boolean updateBlinkAnimationTimer(long millis) {
        boolean updated = false;

        movesBlinkAnimationTimer -= millis;
        if (movesBlinkAnimationTimer <= 0) {
            toggleMovesBlinkAnimation();
            resetMovesBlinkAnimationTimer();
            updated = true;
        }

        if (orphanBlinkAnimationEnabled) {
            orphanBlinkAnimationTimer -= millis;
            if (orphanBlinkAnimationTimer <= 0) {
                toggleOrphanBlinkAnimation();
                resetOrphanBlinkAnimationTimer();
                updated = true;
            }
        }

        return updated;
    }
}
