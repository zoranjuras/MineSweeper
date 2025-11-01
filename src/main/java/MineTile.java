import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * The {@code MineTile} class represents a single tile (cell) in the Minesweeper grid.
 * <p>
 * It extends {@link JButton} and is responsible for displaying its visual state —
 * whether it’s hidden, revealed, or flagged — as well as storing information
 * about its position and text value. The tile also provides a method to determine
 * its neighboring tiles within the grid.
 *
 * <p>
 * Each {@code MineTile} is aware of its location on the board via its {@code row}
 * and {@code col} indices. It can render custom graphics using the {@link #paintComponent(Graphics)}
 * method, which supports smooth text rendering and centered display of numbers.
 *
 * @author
 *     Zoran Juras
 * @version
 *     1.4.0
 * @since
 *     2025-11-01
 */
public class MineTile extends JButton {

    /** Row index of this tile in the game grid. */
    int row;

    /** Column index of this tile in the game grid. */
    int col;

    /** Indicates whether this tile has been revealed. */
    boolean revealed = false;

    /** The text displayed when the tile is revealed (e.g., mine count or empty string). */
    String displayText = "";

    /**
     * Constructs a {@code MineTile} at the specified grid position.
     * <p>
     * Initializes the tile’s appearance, including font, background color,
     * and margin settings.
     *
     * @param row the row index of this tile
     * @param col the column index of this tile
     */
    public MineTile(int row, int col) {
        this.row = row;
        this.col = col;
        setFont(new Font("Arial", Font.BOLD, 20));
        setFocusable(false);
        setMargin(new Insets(0, 0, 0, 0));
        setBackground(Color.LIGHT_GRAY);
    }

    /**
     * Reveals this tile with the given text and color.
     * <p>
     * When revealed, the tile becomes visually white and displays the specified
     * text (typically a number indicating adjacent mines).
     *
     * @param text  the text to display (e.g., mine count or empty string)
     * @param color the color of the displayed text
     */
    public void reveal(String text, Color color) {
        this.revealed = true;
        this.displayText = text;
        setForeground(color);
        repaint();
    }

    /**
     * Retrieves all neighboring tiles surrounding this one within the grid.
     * <p>
     * Includes up to 8 neighbors (diagonals included) and automatically excludes
     * any tiles that fall outside the board’s boundaries.
     *
     * @param numRows total number of rows in the board
     * @param numCols total number of columns in the board
     * @param board   2D array containing all tiles
     * @return an {@link ArrayList} of neighboring {@code MineTile} objects
     */
    public ArrayList<MineTile> getNeighbourTiles(int numRows, int numCols, MineTile[][] board) {
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

    /**
     * Custom rendering method for the tile’s visual state.
     * <p>
     * When the tile is revealed, this method draws a white background and centers
     * the display text both horizontally and vertically using anti-aliased text.
     *
     * @param g the {@link Graphics} context used for drawing
     */
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
