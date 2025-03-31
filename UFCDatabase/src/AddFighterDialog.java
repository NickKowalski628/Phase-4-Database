import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/*
 * Nicholas Kowalski
 * CEN3024
 * 30Mar2025
 * Class: AddFighterDialog
 * Purpose: This class allows users to enter fighter data manually through a series of text boxes.
 * It also holds the methods necessary to store and verify data was entered properly.
 */
public class AddFighterDialog extends JDialog {
    private JTextField idField, nameField, aliasField, weightClassField, stanceField,
            heightField, reachField, winsField, lossesField, drawsField, noContestField;
    private boolean isAdded = false;
    private FighterData fighter;

    public AddFighterDialog(JFrame parent) {
        super(parent, "Add Fighter", true);
        setLayout(new GridLayout(13, 2));
        setSize(400, 500);

        add(new JLabel("Enter 4-Digit fighter ID: "));
        idField = new JTextField();
        add(idField);
        add(new JLabel("Fighter Name: "));
        nameField = new JTextField();
        add(nameField);
        add(new JLabel("Fighter Alias: "));
        aliasField = new JTextField();
        add(aliasField);
        add(new JLabel("Fighter WieghtClass: "));
        weightClassField = new JTextField();
        add(weightClassField);
        add(new JLabel("Fighting Stance: "));
        stanceField = new JTextField();
        add(stanceField);
        add(new JLabel("Fighter Height (inches): "));
        heightField = new JTextField();
        add(heightField);
        add(new JLabel("Fighter Reach (inches): "));
        reachField = new JTextField();
        add(reachField);
        add(new JLabel("Enter Wins: "));
        winsField = new JTextField();
        add(winsField);
        add(new JLabel("Enter Losses: "));
        lossesField = new JTextField();
        add(lossesField);
        add(new JLabel("Enter Draws: "));
        drawsField = new JTextField();
        add(drawsField);
        add(new JLabel("Enter No-Contest: "));
        noContestField = new JTextField();
        add(noContestField);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addFighter();
            }
        });
        add(new JLabel(""));
        add(confirmButton);

        setLocationRelativeTo(parent);
    }
    /*
     * Method: addFighter
     * Parameters: None
     * Returns: none
     * Purpose: This method handles input validation while allowing a user to enter a new entry into the database
     */
    private void addFighter() {
        try {
            String fighterID = idField.getText().trim();
            if (fighterID.isEmpty() || fighterID.length() != 4 || !fighterID.chars().allMatch(Character::isDigit)) {
                JOptionPane.showMessageDialog(this, "Invalid fighter ID, please enter a 4-digit number");
                return;
            }
            int id = Integer.parseInt(fighterID);

            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                try {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM Fighters WHERE fighter_ID = " + id);
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Fighter already exists");
                        conn.close();
                        return;
                    }
                    conn.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error checking fighter ID: " + e.getMessage());
                    return;
                }
            }

            String fighterName = nameField.getText().trim();
            if (fighterName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fighter name cannot be empty");
                return;
            }
            String fighterAlias = aliasField.getText().trim();
            if (fighterAlias.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Alias cannot be empty");
                return;
            }
            String fighterWeightclass = weightClassField.getText().trim();
            if (fighterWeightclass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Weight class cannot be empty");
                return;
            }
            String fightingStance = stanceField.getText().trim();
            if (fightingStance.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Stance cannot be empty");
                return;
            }
            String heightText = heightField.getText().trim();
            if (heightText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Height cannot be empty");
                return;
            }
            double fighterHeight = Double.parseDouble(heightText);
            if (fighterHeight <= 0) {
                JOptionPane.showMessageDialog(this, "Height must be greater than 0");
                return;
            }
            String reachText = reachField.getText().trim();
            if (reachText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Reach cannot be empty");
                return;
            }
            double fighterReach = Double.parseDouble(reachText);
            if (fighterReach <= 0) {
                JOptionPane.showMessageDialog(this, "Reach must be greater than 0");
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
                JOptionPane.showMessageDialog(this, "No Contests cannot be empty");
                return;
            }
            int noContest = Integer.parseInt(noContestText);
            if (noContest < 0) {
                JOptionPane.showMessageDialog(this, "No Contests cannot be negative");
                return;
            }

            fighter = new FighterData(id, fighterName, fighterAlias, fighterWeightclass, fightingStance, fighterHeight, fighterReach,
                    wins, losses, draws, noContest);
            isAdded = true;
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number");
        }
    }

    public boolean isAdded() {
        return isAdded;
    }

    public FighterData getFighter() {
        return fighter;
    }
}