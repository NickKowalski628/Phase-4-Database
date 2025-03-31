import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;

/*
 * Nicholas Kowalski
 * CEN3024
 * 30Mar2025
 * Class: UFCAppGUI
 * Purpose: This class creates the GUI for the UFC Database that lets users interact with it
 * through a series of buttons and text box that record their input and interface/
 */
public class UFCAppGUI extends JFrame {
    private JLabel UFCDatabase;
    private JPanel FighterGUI;
    private JTable fighterTable;
    private JButton btnAddFighter, btnDisplayAll, btnUpdateInfo, btnRemoveFighter,
            btnPercentSort, btnExit;
    private DefaultTableModel tableModel;

    public UFCAppGUI() {
        setContentPane(FighterGUI);
        setTitle("UFC Fighter Database");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        String[] columnNames = {"ID", "Name", "Alias", "Weight Class", "Stance", "Height", "Reach",
                "Wins", "Losses", "Draws", "No Contest", "Win %"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        fighterTable.setModel(tableModel);

        btnAddFighter.setMargin(new Insets(0, 0, 0, 0));
        btnDisplayAll.setMargin(new Insets(0, 0, 0, 0));
        btnUpdateInfo.setMargin(new Insets(0, 0, 0, 0));
        btnPercentSort.setMargin(new Insets(0, 0, 0, 0));
        btnRemoveFighter.setMargin(new Insets(0, 0, 0, 0));
        btnExit.setMargin(new Insets(0, 0, 0, 0));

        btnAddFighter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddFighterDialog dialog = new AddFighterDialog(UFCAppGUI.this);
                dialog.setVisible(true);
                if (dialog.isAdded()) {
                    addOneFighter(dialog.getFighter());
                    updateTable();
                }
            }
        });

        btnDisplayAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateTable();
            }
        });

        btnUpdateInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = fighterTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(UFCAppGUI.this, "Please select a fighter to update");
                    return;
                }
                int id = (int) tableModel.getValueAt(row, 0);
                FighterData fighter = getFighterInfo(id);
                if (fighter != null) {
                    UpdateFighterDialog dialog = new UpdateFighterDialog(UFCAppGUI.this, fighter);
                    dialog.setVisible(true);
                    updateTable();
                }
            }
        });

        btnRemoveFighter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = fighterTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(UFCAppGUI.this, "Select a fighter to remove");
                    return;
                }
                int id = (int) tableModel.getValueAt(row, 0);
                int confirmation = JOptionPane.showConfirmDialog(UFCAppGUI.this, "Are you sure you'd like to remove this fighter?");
                if (confirmation == JOptionPane.YES_OPTION) {
                    removeFighter(id);
                    updateTable();
                }
            }
        });

        btnPercentSort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortByWinPercentage();
            }
        });

        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    /*
     * Method: chooseDatabase
     * Parameters: none
     * Returns: String
     * Purpose: This method allows the user to choose the locaition of a database file upon launching the program
     */
    private static String chooseDatabase() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("SQLite Files", "db"));
        chooser.setDialogTitle("Choose a database");
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            return file.getAbsolutePath();
        } else {
            JOptionPane.showMessageDialog(null, "No file selected.");
            System.exit(0);
            return "";
        }
    }
    /*
     * Method: addFighter
     * Parameters: Fighterdata fighter
     * Return: None
     * purpose: This method allows the user to manually enter information of a fighter
     * directly into the database.
     */

    private void addOneFighter(FighterData fighter) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO Fighters (fighter_ID, Name, Alias, Weight_Class, Stance, Height, Reach, Wins, Losses, Draws, No_Contest) " +
                        "VALUES (" + fighter.getFighterID() + ", '" + fighter.getFighterName() + "', '" + fighter.getAlias() + "', '" +
                        fighter.getWeightClass() + "', '" + fighter.getFightingStance() + "', " + fighter.getFighterHeight() + ", " +
                        fighter.getFighterReach() + ", " + fighter.getWins() + ", " + fighter.getLosses() + ", " +
                        fighter.getDraws() + ", " + fighter.getNoContest() + ")";
                stmt.executeUpdate(sql);
                conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding fighter information: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Database connection failed.");
        }
    }
    /*
     * Method: updateTable
     * Parameters: none
     * return: none
     * Purpose: This method handles and refreshes the data within the table when the user attempts to
     * display all data currently stored in the database. This method also outputs the correct message
     * for the win percentage if there is no fights recorded for a fighter to avoid division by zero.
     */
    private void updateTable() {
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Fighters");
                tableModel.setRowCount(0);
                while (rs.next()) {
                    FighterData fighter = new FighterData();
                    fighter.readFromDatabase(rs);
                    String winPercentageStr = fighter.getWinPercentage() == 0 ? "No Fights Recorded" :
                            String.format("%.2f%%", fighter.getWinPercentage());
                    Object[] row = {
                            fighter.getFighterID(),
                            fighter.getFighterName(),
                            fighter.getAlias(),
                            fighter.getWeightClass(),
                            fighter.getFightingStance(),
                            fighter.getFighterHeight(),
                            fighter.getFighterReach(),
                            fighter.getWins(),
                            fighter.getLosses(),
                            fighter.getDraws(),
                            fighter.getNoContest(),
                            winPercentageStr
                    };
                    tableModel.addRow(row);
                }
                conn.close();
                if (tableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "No fighters found.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error, cannot display fighters: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Database connection failed.");
        }
    }

    /*
     * Method: getFighterFromDatabase
     * Parameters: int id
     * Return: fighterdata
     * Purpose: This method returns fighter information when the user selects a fighter to update ensuring that the
     * correct fighter is being updated in the database.
     */
    private FighterData getFighterInfo(int id) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Fighters WHERE fighter_ID = " + id);
                if (rs.next()) {
                    FighterData fighter = new FighterData();
                    fighter.readFromDatabase(rs);
                    conn.close();
                    return fighter;
                }
                conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error getting fighter: " + e.getMessage());
            }
        }
        return null;
    }

    /*
     * Method: removeFighter
     * Parameters: int id
     * Return: none
     * Purpose: This method removes the selected fighter from the database and updates the current db file loaded
     * into the application.
     */

    private void removeFighter(int id) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("DELETE FROM Fighters WHERE fighter_ID = " + id);
                conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error removing fighter: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Database connection failed.");
        }
    }

    /*
     * Method: sortByWinPercentage
     * Parameters: none
     * Return: none
     * Purpose: This method is our custom feature and sorts the table by win percentage in descending order.
     */

    private void sortByWinPercentage() {
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Fighters ORDER BY (Wins * 100 / (Wins + Losses + Draws)) DESC");
                tableModel.setRowCount(0);
                while (rs.next()) {
                    FighterData fighter = new FighterData();
                    fighter.readFromDatabase(rs);
                    String winPercentageStr = fighter.getWinPercentage() == 0 ? "No Fights Recorded" :
                            String.format("%.2f%%", fighter.getWinPercentage());
                    Object[] row = {
                            fighter.getFighterID(),
                            fighter.getFighterName(),
                            fighter.getAlias(),
                            fighter.getWeightClass(),
                            fighter.getFightingStance(),
                            fighter.getFighterHeight(),
                            fighter.getFighterReach(),
                            fighter.getWins(),
                            fighter.getLosses(),
                            fighter.getDraws(),
                            fighter.getNoContest(),
                            winPercentageStr
                    };
                    tableModel.addRow(row);
                }
                conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error sorting fighters: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Database connection failed.");
        }
    }

    public static void main(String[] args) {
        String dbPath = chooseDatabase();
        DatabaseConnection.setDatabaseLocation(dbPath);
        new UFCAppGUI();
    }
}