import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * The {@code Minesweeper} class provides the graphical user interface (GUI)
 * and user interaction logic for the classic Minesweeper game implemented in Java Swing.
 * <p>
 * It manages window creation, tile rendering, user input (left/right clicks),
 * and communication with the underlying minefield logic. The game supports
 * three difficulty levels — Easy, Medium, and Hard — and includes a main menu
 * with settings and restart functionality.
 *
 * <p>
 * This class acts as the visual controller that directly interacts with
 * {@link MineTile} components arranged on a {@link JPanel} grid.
 *
 * @author
 *     Zoran Juras
 * @version
 *     1.5.0
 * @since
 *     2025-11-01
 */
public class Minesweeper extends Component {

    /** Default pixel size for each tile. */
    int tileSize = 40;

    /** Current number of rows on the board. */
    int numRows = 16;

    /** Current number of columns on the board. */
    int numCols = 16;

    /** Window width in pixels (auto-calculated). */
    int boardWidth = numCols * tileSize;

    /** Window height in pixels (auto-calculated). */
    int boardHeight = numRows * tileSize;

    /** Main application frame. */
    JFrame frame = new JFrame("Minesweeper");

    /** Label displaying game information and status. */
    JLabel textLabel = new JLabel();

    /** Panel that holds the status text and menu button. */
    JPanel textPanel = new JPanel();

    /** Panel representing the game board grid. */
    JPanel boardPanel = new JPanel();

    /** 2D array of all game tiles. */
    MineTile[][] board = new MineTile[numRows][numCols];

    /** List containing all tiles that have mines. */
    ArrayList<MineTile> mineList;

    /** Total number of mines on the board. */
    int initialMineCount = 40;
    int mineCount = initialMineCount;

    /** Number of tiles revealed so far. */
    int tilesRevealed = 0;

    /** Indicates whether the game has ended. */
    boolean gameOver = false;

    /** Random number generator used for mine placement. */
    Random random = new Random();

    /**
     * Constructs a new {@code Minesweeper} game window and initializes all UI components.
     * <p>
     * Sets up the main frame, header label, menu button, and creates the grid
     * of {@link MineTile} buttons. Automatically places mines after setup.
     */
    public Minesweeper() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText(mineCount + " mines to find");
        textLabel.setOpaque(true);

        JButton menuButton = new JButton("Menu");
        menuButton.setFont(new Font("Arial", Font.PLAIN, 14));
        menuButton.addActionListener(e -> new GameMenuDialog(Minesweeper.this).setVisible(true));

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel, BorderLayout.CENTER);
        textPanel.add(menuButton, BorderLayout.EAST);
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
                tile.addMouseListener(createTileMouseListener());
                boardPanel.add(tile);
            }
        }

        frame.setVisible(true);
        setMines(mineCount);
    }

    /**
     * Applies the selected difficulty setting by rebuilding the board grid
     * with updated dimensions and mine counts.
     *
     * @param difficulty a string label representing difficulty (Easy, Medium, Hard)
     */
    public void applySettings(String difficulty) {
        switch (difficulty) {
            case "Easy (9x9, 10 mines)" -> {
                numRows = 9;
                numCols = 9;
                mineCount = 10;
            }
            case "Medium (16x16, 40 mines)" -> {
                numRows = 16;
                numCols = 16;
                mineCount = 40;
            }
            case "Hard (16x32, 99 mines)" -> {
                numRows = 16;
                numCols = 32;
                mineCount = 99;
            }
        }

        initialMineCount = mineCount;

        frame.getContentPane().remove(boardPanel);
        frame.setSize(numCols * tileSize, numRows * tileSize);
        frame.setLocationRelativeTo(null);
        boardPanel = new JPanel(new GridLayout(numRows, numCols));
        frame.add(boardPanel, BorderLayout.CENTER);

        board = new MineTile[numRows][numCols];
        tilesRevealed = 0;
        gameOver = false;

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = new MineTile(r, c);
                board[r][c] = tile;
                tile.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
                tile.setMargin(new Insets(4, 2, 0, 2));
                tile.addMouseListener(createTileMouseListener());
                boardPanel.add(tile);
            }
        }

        setMines(mineCount);
        textLabel.setText(mineCount + " mines to find");
    }

    /**
     * Starts a new game using the current difficulty settings.
     * Resets all tiles, re-enables the board, and re-places the mines.
     */
    public void newGame() {
        mineCount = initialMineCount;
        tilesRevealed = 0;
        gameOver = false;
        textLabel.setText(mineCount + " mines to find");

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = board[r][c];
                tile.setEnabled(true);
                tile.setText("");
                tile.revealed = false;
                tile.displayText = "";
                tile.setBackground(Color.LIGHT_GRAY);
            }
        }

        setMines(mineCount);
    }

    /**
     * Creates a {@link MouseAdapter} that defines behavior for left and right mouse clicks on tiles.
     * <ul>
     *   <li>Left click: reveals a tile (or ends the game if it’s a mine)</li>
     *   <li>Right click: toggles a flag emoji (🚩)</li>
     * </ul>
     *
     * @return a configured {@link MouseAdapter} for tile interaction
     */
    private MouseAdapter createTileMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameOver) return;
                MineTile tile = (MineTile) e.getSource();
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (Objects.equals(tile.getText(), "")) {
                        if (mineList.contains(tile)) gameLost();
                        else checkMine(tile);
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    if (tile.getText().isEmpty() && tile.isEnabled()) {
                        tile.setText("\uD83D\uDEA9"); // 🚩
                        mineCount -= 1;
                        textLabel.setText("Minesweeper: " + mineCount + " mines to find");}
                    else if (Objects.equals(tile.getText(), "\uD83D\uDEA9")) {
                        tile.setText("");
                        mineCount += 1;
                        textLabel.setText("Minesweeper: " + mineCount + " mines to find");}
                }
            }
        };
    }

    /**
     * Reveals all mines on the board (💣) — used when the player loses or wins.
     */
    private void revealMines() {
        for (MineTile mt : mineList) {
            mt.setText("\uD83D\uDCA3"); // 💣
            mt.setForeground(Color.BLACK);
        }
        mineCount = initialMineCount;
    }

    /**
     * Handles game loss logic by revealing all mines and displaying
     * a "GAME OVER!" message on the UI.
     */
    private void gameLost() {
        revealMines();
        gameOver = true;
        textLabel.setText("GAME OVER!");
    }

    /**
     * Randomly places {@code mineCount} mines across the board.
     * Mines are stored in the {@link #mineList}.
     */
    private void setMines(int mineCount) {
        mineList = new ArrayList<>();
        while (mineList.size() < mineCount) {
            int r = random.nextInt(numRows);
            int c = random.nextInt(numCols);
            MineTile tile = board[r][c];
            if (!mineList.contains(tile)) mineList.add(tile);
        }
    }

    /**
     * Checks and reveals the clicked tile.
     * <p>
     * If the tile has no adjacent mines, recursively reveals surrounding tiles.
     * When all non-mine tiles are revealed, the player wins.
     *
     * @param mt the {@link MineTile} that was clicked
     */
    void checkMine(MineTile mt) {
        MineTile tile = board[mt.row][mt.col];
        if (!tile.isEnabled()) return;

        tile.setEnabled(false);
        tilesRevealed++;

        int minesFound = 0;
        for (MineTile t : mt.getNeighbourTiles(numRows, numCols, board)) {
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
            for (MineTile t : mt.getNeighbourTiles(numRows, numCols, board)) {
                checkMine(t);
            }
        }

        if (tilesRevealed == numRows * numCols - mineList.size()) {
            gameOver = true;
            revealMines();
            textLabel.setText("Bravo! Minefield cleared!");
        }
    }

    /**
     * Counts whether a given tile contains a mine.
     *
     * @param mt the {@link MineTile} to check
     * @return 1 if the tile is a mine, 0 otherwise
     */
    int countMine(MineTile mt) {
        return mineList.contains(board[mt.row][mt.col]) ? 1 : 0;
    }
}
