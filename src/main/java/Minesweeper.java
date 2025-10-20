import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;

public class Minesweeper {

    private class MineTile extends JButton {
        int row;
        int col;

        public MineTile (int row, int col) {
            this.row = row;
            this.col = col;
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

    }

    int tileSize = 70;
    int numRows = 8;
    int numCols = numRows;
    int boardWidth = numCols * tileSize;
    int boardHeight = numRows * tileSize;

    JFrame frame = new JFrame("Minesweeper");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    MineTile[][] board = new MineTile[numRows][numCols];
    ArrayList<MineTile> mineList;

    int tilesClicked = 0; // goal is to click all the tiles except mines
    boolean gameOver= false;

    public Minesweeper() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper");
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
                tile.setMargin(new Insets(0, 0, 0, 0));
//                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
                tile.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));
//                tile.setText("\uD83D\uDCA3");

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
                                    checkMine(tile.row, tile.col);
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
        for (int i = 0; i < mineList.size(); i++) {
            MineTile tile = mineList.get(i);
            tile.setText("\uD83D\uDCA3"); // 💣
        }
    }

    private void gameLost() {
        revealMines();
        gameOver = true;
        textLabel.setText("GAME OVER!");
    }

    private void setMines() {

        mineList = new ArrayList<>();

        mineList.add(board[2][2]);
        mineList.add(board[2][3]);
        mineList.add(board[5][6]);
        mineList.add(board[3][4]);
        mineList.add(board[1][1]);
    }

    void checkMine(int r, int c) {

        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return;
        }

        MineTile tile = board[r][c];
        if (!tile.isEnabled()) {
            return;
        }
        tile.setEnabled(false);
        tilesClicked += 1;

        int minesFound = 0;

        // top 3 neighbour tiles
        minesFound += countMine(r - 1, c - 1); // top left
        minesFound += countMine(r - 1, c); // top
        minesFound += countMine(r - 1, c + 1); // top right

        // left and right
        minesFound += countMine(r, c - 1); // left
        minesFound += countMine(r, c + 1); // right

        // bottom 3
        minesFound += countMine(r + 1, c - 1); // bottom left
        minesFound += countMine(r + 1, c); // bottom
        minesFound += countMine(r + 1, c + 1); // bottom right

        if (minesFound > 0) {
            tile.setText(Integer.toString(minesFound));
        }
        else {
            tile.setText("");

            // top 3 neighbour tiles
            checkMine(r - 1, c - 1); // top left
            checkMine(r - 1, c); // top
            checkMine(r - 1, c + 1); // top right

            // left and right
            checkMine(r, c - 1); // left
            checkMine(r, c + 1); // right

            // bottom 3
            checkMine(r + 1, c - 1); // bottom left
            checkMine(r + 1, c); // bottom
            checkMine(r + 1, c + 1); // bottom right
        }

        if (tilesClicked == numRows * numCols - mineList.size()) {
            gameOver = true;
            revealMines();
            textLabel.setText("Bravo! Minefield cleared!");
        }
    }

    int countMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return 0;
        }
        if (mineList.contains(board[r][c])) {
            return 1;
        }
        return 0;
    }
}
