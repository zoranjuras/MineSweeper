import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * The {@code MineField} class encapsulates the core game logic of Minesweeper,
 * including mine placement, tile revealing, win/loss detection, and board state management.
 * <p>
 * It is completely independent of the graphical user interface (GUI) and
 * can be used in both Swing-based and console-based versions of the game.
 * <p>
 * The class operates on a 2D grid of {@link MineTile} objects, each representing
 * a single cell on the board.
 *
 * @author
 *     Zoran Juras
 * @version
 *     1.4.0
 * @since
 *     2025-11-01
 */
public class MineField {

    /** Number of rows on the board. */
    private final int numRows;

    /** Number of columns on the board. */
    private final int numCols;

    /** Total number of mines placed on the board. */
    private final int mineCount;

    /** Random generator used for mine placement. */
    private final Random random = new Random();

    /** 2D array representing the game board. */
    private final MineTile[][] board;

    /** List of all tiles that contain mines. */
    private final ArrayList<MineTile> mineList = new ArrayList<>();

    /** Number of tiles that have been revealed so far. */
    private int tilesRevealed = 0;

    /** Indicates whether the game has ended. */
    private boolean gameOver = false;

    /**
     * Constructs a new {@code MineField} with the specified dimensions and mine count.
     *
     * @param rows  number of rows on the board
     * @param cols  number of columns on the board
     * @param mines total number of mines to be placed
     */
    public MineField(int rows, int cols, int mines) {
        this.numRows = rows;
        this.numCols = cols;
        this.mineCount = mines;
        this.board = new MineTile[rows][cols];
        initializeBoard();
        setMines();
    }

    /**
     * Initializes the game board with empty {@link MineTile} objects.
     */
    private void initializeBoard() {
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                board[r][c] = new MineTile(r, c);
            }
        }
    }

    /**
     * Returns the entire 2D board array.
     *
     * @return the board of {@link MineTile} objects
     */
    public MineTile[][] getBoard() {
        return board;
    }

    /**
     * Checks whether the game is currently over.
     *
     * @return {@code true} if the game is over, otherwise {@code false}
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Resets the minefield to its initial state, clearing all revealed tiles
     * and re-randomizing mine placement.
     */
    public void resetField() {
        tilesRevealed = 0;
        gameOver = false;
        mineList.clear();

        for (MineTile[] row : board) {
            for (MineTile tile : row) {
                tile.setEnabled(true);
                tile.setText("");
                tile.revealed = false;
                tile.displayText = "";
                tile.setBackground(Color.LIGHT_GRAY);
            }
        }

        setMines();
    }

    /**
     * Randomly places mines on the board.
     * Mines are stored in the {@link #mineList}.
     */
    private void setMines() {
        while (mineList.size() < mineCount) {
            int r = random.nextInt(numRows);
            int c = random.nextInt(numCols);
            MineTile tile = board[r][c];
            if (!mineList.contains(tile)) {
                mineList.add(tile);
            }
        }
    }

    /**
     * Reveals all mines on the board.
     * Called when the game is lost or won.
     */
    public void revealMines() {
        for (MineTile mt : mineList) {
            mt.setText("\uD83D\uDCA3"); // 💣
        }
        gameOver = true;
    }

    /**
     * Checks if a given tile contains a mine.
     *
     * @param tile the {@link MineTile} to check
     * @return {@code true} if the tile contains a mine, otherwise {@code false}
     */
    public boolean isMine(MineTile tile) {
        return mineList.contains(board[tile.row][tile.col]);
    }

    /**
     * Reveals the specified tile and recursively reveals its neighbors
     * if no adjacent mines are found.
     * <p>
     * This method also checks for a win condition — if all safe tiles are revealed,
     * the game is marked as completed.
     *
     * @param mt the {@link MineTile} to check
     * @return {@code true} if the player has cleared all safe tiles (won), otherwise {@code false}
     */
    public boolean checkMine(MineTile mt) {
        if (gameOver) return false;

        MineTile tile = board[mt.row][mt.col];
        if (!tile.isEnabled()) return false;

        tile.setEnabled(false);
        tilesRevealed++;

        int minesFound = 0;
        for (MineTile t : mt.getNeighbourTiles(numRows, numCols, board)) {
            minesFound += mineList.contains(board[t.row][t.col]) ? 1 : 0;
        }

        if (minesFound > 0) {
            tile.reveal(Integer.toString(minesFound), getColorForNumber(minesFound));
        } else {
            tile.reveal("", Color.BLACK);
            for (MineTile t : mt.getNeighbourTiles(numRows, numCols, board)) {
                checkMine(t);
            }
        }

        if (tilesRevealed == numRows * numCols - mineList.size()) {
            gameOver = true;
            revealMines();
            return true; // game won
        }

        return false; // game still ongoing
    }

    /**
     * Returns a color associated with the given number of adjacent mines.
     *
     * @param n number of adjacent mines (1–8)
     * @return the corresponding {@link Color}
     */
    private Color getColorForNumber(int n) {
        return switch (n) {
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
    }
}
