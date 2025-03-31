import java.sql.*;

/*
 * Nicholas Kowalski
 * CEN3024C
 * 30Mar2025
 * Class: FighterData
 * This class stores the information of ufc fighters in the UFC Fighter Database. In this class
 * each fighter has a unique ID, Name, Weight class, stance, Wins, Losses, draws, and no contests.
 */
public class FighterData {
    private int fighterID;
    private String fighterName;
    private String alias;
    private String weightClass;
    private String fightingStance;
    private double fighterHeight;
    private double fighterReach;
    private int wins;
    private int losses;
    private int draws;
    private int noContest;

    public FighterData(int fighterID, String fighterName, String alias, String weightClass,
                       String fightingStance, double fighterHeight, double fighterReach, int wins,
                       int losses, int draws, int noContest) {
        this.fighterID = fighterID;
        this.fighterName = fighterName;
        this.alias = alias;
        this.weightClass = weightClass;
        this.fightingStance = fightingStance;
        this.fighterHeight = fighterHeight;
        this.fighterReach = fighterReach;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.noContest = noContest;
    }

    public FighterData() {
    }

    /*
     * Method: readFromDatabase
     * Parameters: ResultSet rs
     * Return: None
     * Purpose: This method loads fighter data found in the database into the fighter objects.
     */
    public void readFromDatabase(ResultSet rs) {
        try {
            fighterID = rs.getInt("fighter_ID");
            fighterName = rs.getString("Name");
            alias = rs.getString("Alias");
            weightClass = rs.getString("Weight_Class");
            fightingStance = rs.getString("Stance");
            fighterHeight = rs.getDouble("Height");
            fighterReach = rs.getDouble("Reach");
            wins = rs.getInt("Wins");
            losses = rs.getInt("Losses");
            draws = rs.getInt("Draws");
            noContest = rs.getInt("No_Contest");
        } catch (SQLException e) {
            System.out.println("Error reading fighter data: " + e.getMessage());
        }
    }

    // Getters and Setters
    public int getFighterID() {
        return fighterID;
    }

    public void setFighterID(int fighterID) {
        this.fighterID = fighterID;
    }

    public String getFighterName() {
        return fighterName;
    }

    public void setFighterName(String fighterName) {
        this.fighterName = fighterName;
    }
    /*
     * Method: getwinPercentage
     * Parameters: none
     * Return: double
     * Purpose: This method handles the calculations needed for our custom sorting feature.
     * It also returns the proper number so the application can display the correct message.
     */
    public double getWinPercentage() {
        int totalFights = wins + losses + draws;
        if (totalFights == 0) {
            return 0.0;
        }
        return ((double) wins / totalFights) * 100;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getWeightClass() {
        return weightClass;
    }

    public void setWeightClass(String weightClass) {
        this.weightClass = weightClass;
    }

    public String getFightingStance() {
        return fightingStance;
    }

    public void setFightingStance(String fightingStance) {
        this.fightingStance = fightingStance;
    }

    public double getFighterHeight() {
        return fighterHeight;
    }

    public void setFighterHeight(double fighterHeight) {
        this.fighterHeight = fighterHeight;
    }

    public double getFighterReach() {
        return fighterReach;
    }

    public void setFighterReach(double fighterReach) {
        this.fighterReach = fighterReach;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getNoContest() {
        return noContest;
    }

    public void setNoContest(int noContest) {
        this.noContest = noContest;
    }
}