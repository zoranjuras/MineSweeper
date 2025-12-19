import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GameMenuDialog
 * A simple modal dialog that provides options to start a new game, open settings, or exit the application.
 * Intended to be used as part of the Minesweeper game.
 *
 * Version: 1.5.0
 * Author: Zoran Juras
 * Date: 2025-10-28
 */
public class GameMenuDialog extends JDialog {

    public GameMenuDialog(Minesweeper parent) {
        super();
        setLayout(new GridLayout(3, 1, 10, 10));
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JButton newGameButton = new JButton("New Game");
        JButton settingsButton = new JButton("Settings");
        JButton exitButton = new JButton("Exit");

        newGameButton.setFont(new Font("Arial", Font.BOLD, 16));
        settingsButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));

        add(newGameButton);
        add(settingsButton);
        add(exitButton);

        newGameButton.addActionListener(e -> {
            dispose();
            parent.newGame();

        });

        settingsButton.addActionListener(e -> {
            SettingsDialog settingsDialog = new SettingsDialog((Frame) SwingUtilities.getWindowAncestor(parent));
            settingsDialog.setVisible(true);

            if (settingsDialog.isConfirmed()) {
                String difficulty = settingsDialog.getSelectedDifficulty();

                dispose();
                parent.applySettings(difficulty);

            }
        });

        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    parent,
                    "Are you sure you want to exit?",
                    "Exit Game",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }
}

