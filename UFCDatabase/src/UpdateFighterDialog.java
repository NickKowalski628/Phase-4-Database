import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/*
 * Nicholas Kowalski
 * CEN3024
 * 30Mar2025
 * Class: UpdateFighterDialog
 * Purpose: Allows updating specific fighter info (weight class, wins, losses, draws, no contest) with dialog boxes
 * that update the database after valid information is entered.
 */
public class UpdateFighterDialog extends JDialog {
    private JTextField weightField, winsField, lossesField, drawsField, noContestField;
    private FighterData fighter;

    public UpdateFighterDialog(JFrame parent, FighterData fighter) {
        super(parent, "Update Fighter Information", true);
        this.fighter = fighter;
        setLayout(new GridLayout(6, 2));
        setSize(400, 300);

        add(new JLabel("Weightclass: "));
        weightField = new JTextField(fighter.getWeightClass());
        add(weightField);
        add(new JLabel("Wins: "));
        winsField = new JTextField(String.valueOf(fighter.getWins()));
        add(winsField);
        add(new JLabel("Losses: "));
        lossesField = new JTextField(String.valueOf(fighter.getLosses()));
        add(lossesField);
        add(new JLabel("Draws: "));
        drawsField = new JTextField(String.valueOf(fighter.getDraws()));
        add(drawsField);
        add(new JLabel("No Contest: "));
        noContestField = new JTextField(String.valueOf(fighter.getNoContest()));
        add(noContestField);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateFighter();
            }
        });
        add(new JLabel(""));
        add(confirmButton);
        setLocationRelativeTo(parent);
    }
    /*
     * Method: UpdateFighter
     * Parameters: none
     * Returns: none
     * Purpose: This method handles input validation and updates the corresponding fighter selected
     */
    private void updateFighter() {
        try {
            String weight = weightField.getText().trim();
            if (weight.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Weight class cannot be empty");
                return;
            }
            String winsText = winsField.getText().trim();
            if (winsText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wins cannot be empty");
                return;
            }
            int wins = Integer.parseInt(winsText);
            if (wins < 0) {
                JOptionPane.showMessageDialog(this, "Wins cannot be negative");
                return;
            }
            String lossesText = lossesField.getText().trim();
            if (lossesText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Losses cannot be empty");
                return;
            }
            int losses = Integer.parseInt(lossesText);
            if (losses < 0) {
                JOptionPane.showMessageDialog(this, "Losses cannot be negative");
                return;
            }
            String drawsText = drawsField.getText().trim();
            if (drawsText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Draws cannot be empty");
                return;
            }
            int draws = Integer.parseInt(drawsText);
            if (draws < 0) {
                JOptionPane.showMessageDialog(this, "Draws cannot be negative");
                return;
            }
            String noContestText = noContestField.getText().trim();
            if (noContestText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No Contest cannot be empty");
                return;
            }
            int noContest = Integer.parseInt(noContestText);
            if (noContest < 0) {
                JOptionPane.showMessageDialog(this, "No Contest cannot be negative");
                return;
            }

            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                try {
                    Statement stmt = conn.createStatement();
                    String sql = "UPDATE Fighters SET Weight_Class = '" + weight + "', Wins = " + wins + ", Losses = " + losses +
                            ", Draws = " + draws + ", No_Contest = " + noContest + " WHERE fighter_ID = " + fighter.getFighterID();
                    stmt.executeUpdate(sql);
                    conn.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error updating information: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Database connection failed.");
            }

            fighter.setWeightClass(weight);
            fighter.setWins(wins);
            fighter.setLosses(losses);
            fighter.setDraws(draws);
            fighter.setNoContest(noContest);
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Wins, Losses, Draws, and No Contest");
        }
    }
}