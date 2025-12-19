import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * SettingsDialog
 * Provides options for adjusting game difficulty and board configuration.
 * Intended to be used as part of the Minesweeper game.
 *
 * Version: 1.5.0
 * Author: Zoran Juras
 * Date: 2025-10-28
 */
public class SettingsDialog extends JDialog {

    private final JComboBox<String> difficultyBox;
    private boolean confirmed = false;

    public SettingsDialog(Frame parent) {
        super(parent, "Settings", true);
        setLayout(new GridLayout(3, 1, 10, 10));
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JLabel difficultyLabel = new JLabel("Select difficulty:");
        difficultyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        difficultyLabel.setHorizontalAlignment(SwingConstants.CENTER);

        String[] difficulties = {"Easy (9x9, 10 mines)", "Medium (16x16, 40 mines)", "Hard (16x32, 99 mines)"};
        difficultyBox = new JComboBox<>(difficulties);
        difficultyBox.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(difficultyLabel);
        add(difficultyBox);
        add(buttonPanel);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getSelectedDifficulty() {
        return (String) difficultyBox.getSelectedItem();
    }
}

