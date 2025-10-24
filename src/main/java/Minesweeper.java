/**
 * Minesweeper
 * Version: 1.2.0
 * Author: Zoran Juras
 * Description: Refactored version of Minesweeper in Java Swing.
 *              Introduces a larger 16x16 board, 40 randomly placed mines,
 *              custom tile rendering with centered numbers and color-coded
 *              indicators for adjacent mines.
 * Date: 2025-10-24
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Minesweeper {

    private class MineTile extends JButton {
        int row;
        int col;
        boolean revealed = false;
        String displayText = "";

        public MineTile(int row, int col) {
            this.row = row;
            this.col = col;
            setFont(new Font("Arial", Font.BOLD, 20));
            setFocusable(false);
            setMargin(new Insets(0, 0, 0, 0));
            setBackground(Color.LIGHT_GRAY);
        }

        public void reveal(String text, Color color) {
            this.revealed = true;
            this.displayText = text;
            setForeground(color);
            repaint();
        }

        public ArrayList<MineTile> getNeighbourTiles() {
            ArrayList<MineTile> neighbours = new ArrayList<>();
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {
                    if (i == row && j == col) continue;
                    if (i < 0 || i >= numRows || j < 0 || j >= numCols) continue;
                    neighbours.add(board[i][j]);
                }
            }
            return neighbours;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (revealed) {
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(displayText)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(displayText, x, y);
            }
        }
    }

    int tileSize = 40;
    int numRows = 16;
    int numCols = numRows;
    int boardWidth = numCols * tileSize;
    int boardHeight = numRows * tileSize;

    JFrame frame = new JFrame("Minesweeper");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    MineTile[][] board = new MineTile[numRows][numCols];
    ArrayList<MineTile> mineList;

    int mineCount = 40;
    int tilesRevealed = 0; // goal is to reveal all the tiles except mines
    boolean gameOver= false;
    Random random = new Random();

    public Minesweeper() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper: "+ mineCount + " mines to find");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows, numCols));
        frame.add(boardPanel);

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = new MineTile(r, c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(4, 2, 0, 2));
                tile.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) {
                            return;
                        }
                        MineTile tile = (MineTile) e.getSource();

                        // left click
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (Objects.equals(tile.getText(), "")) {
                                if (mineList.contains(tile)) {
                                    gameLost();
                                }
                                else {
                                    checkMine(tile);
                                }
                            }
                        // right click
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            if (tile.getText().isEmpty() && tile.isEnabled()) {
                                tile.setText("\uD83D\uDEA9"); // 🚩
                            } else if (Objects.equals(tile.getText(), "\uD83D\uDEA9")) {
                                tile.setText("");
                            }
                        }
                    }
                });

                boardPanel.add(tile);
            }
        }
        frame.setVisible(true);

        setMines();
    }

    private void revealMines() {
        for (MineTile mt : mineList) {
            mt.setText("\uD83D\uDCA3"); // 💣
        }
    }

    private void gameLost() {
        revealMines();
        gameOver = true;
        textLabel.setText("GAME OVER!");
    }

    private void setMines() {

        mineList = new ArrayList<>();
        int minesLeft = mineCount;

        while (minesLeft > 0) {
            int r = random.nextInt(numRows); // 0 to numRows - 1
            int c = random.nextInt(numCols);
            MineTile tile = board[r][c];
            if (!mineList.contains(tile)) {
                mineList.add(tile);
                minesLeft -= 1;
            }
        }
    }

    void checkMine(MineTile mt) {

        MineTile tile = board[mt.row][mt.col];
        if (!tile.isEnabled()) {
            return;
        }
        tile.setEnabled(false);
        tilesRevealed += 1;

        int minesFound = 0;

        for (MineTile t : mt.getNeighbourTiles()) {
            minesFound += countMine(t);
        }

        if (minesFound > 0) {
            Color color = switch (minesFound) {
                case 1 -> Color.BLUE;
                case 2 -> new Color(0, 128, 0);
                case 3 -> Color.RED;
                case 4 -> new Color(0, 0, 128);
                case 5 -> new Color(128, 0, 0);
                case 6 -> new Color(64, 224, 208);
                case 7 -> Color.BLACK;
                case 8 -> Color.GRAY;
                default -> Color.BLACK;
            };
            tile.reveal(Integer.toString(minesFound), color);
        } else {
            tile.reveal("", Color.BLACK);


            for (MineTile t : mt.getNeighbourTiles()) {
                checkMine(t);
            }
        }

        if (tilesRevealed == numRows * numCols - mineList.size()) {
            gameOver = true;
            revealMines();
            textLabel.setText("Bravo! Minefield cleared!");
        }
    }

    int countMine(MineTile mt) {

        if (mineList.contains(board[mt.row][mt.col])) {
            return 1;
        }
        return 0;
    }
}
