package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;  // Ajout de cet import
import java.sql.Timestamp;
import java.sql.Date; 
import java.util.ArrayList;
import java.util.List;
import util.DbConnexion;  

public class Depense {
    private int depense_id;
    private int prediction_id;
    private String descriptions;  // Nouveau champ
    private double montant;
    private Date date_depense;
    private Timestamp date_creation;

    public Depense() {}

    public Depense(int prediction_id, String descriptions, double montant, Date date_depense) {
        this.prediction_id = prediction_id;
        this.descriptions = descriptions;
        this.montant = montant;
        this.date_depense = date_depense;
    }

    // Getters et Setters
    public int getDepense_id() { return depense_id; }
    public void setDepense_id(int depense_id) { this.depense_id = depense_id; }
    
    public int getPrediction_id() { return prediction_id; }
    public void setPrediction_id(int prediction_id) { this.prediction_id = prediction_id; }
    
    public String getDescriptions() { return descriptions; }
    public void setDescriptions(String descriptions) { this.descriptions = descriptions; }
    
    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }
    
    public Date getDate_depense() { return date_depense; }
    public void setDate_depense(Date date_depense) { this.date_depense = date_depense; }
    
    public Timestamp getDate_creation() { return date_creation; }
    public void setDate_creation(Timestamp date_creation) { this.date_creation = date_creation; }

    public void save() throws SQLException {
        // Vérifier si le montant total des dépenses ne dépasse pas le budget prévu
        Prediction prediction = Prediction.getById(this.prediction_id);
        double totalDepenses = getTotalDepensesByPrediction(this.prediction_id);
        
        if (totalDepenses + this.montant > prediction.getMontant()) {
            throw new SQLException("Le montant total des dépenses (" + 
                (totalDepenses + this.montant) + 
                ") dépasserait le budget prévu (" + 
                prediction.getMontant() + ")");
        }

        DbConnexion db = new DbConnexion();
        String sql = "INSERT INTO budget_depenses (prediction_id, descriptions, montant, date_depense) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, prediction_id);
            stmt.setString(2, descriptions);
            stmt.setDouble(3, montant);
            stmt.setDate(4, date_depense);
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                setDepense_id(rs.getInt(1));
            }
        } finally {
            db.closeConnection();
        }
    }

    public static double getTotalDepensesByPrediction(int predictionId) throws SQLException {
        DbConnexion db = new DbConnexion();
        String sql = "SELECT SUM(montant) as total FROM budget_depenses WHERE prediction_id = ?";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, predictionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } finally {
            db.closeConnection();
        }
        return 0.0;
    }

    public static List<Depense> findAll() throws SQLException {
        List<Depense> depenses = new ArrayList<>();
        DbConnexion db = new DbConnexion();
        String sql = "SELECT * FROM budget_depenses";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Depense dep = new Depense();
                dep.setDepense_id(rs.getInt("depense_id"));
                dep.setPrediction_id(rs.getInt("prediction_id"));
                dep.setDescriptions(rs.getString("descriptions")); // Ajout de descriptions
                dep.setMontant(rs.getDouble("montant"));
                dep.setDate_depense(rs.getDate("date_depense"));
                dep.setDate_creation(rs.getTimestamp("date_creation"));
                depenses.add(dep);
            }
        } finally {
            db.closeConnection();
        }
        return depenses;
    }
}
