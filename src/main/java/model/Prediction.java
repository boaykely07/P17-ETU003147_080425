package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date; 
import java.util.ArrayList;
import java.util.List;
import util.DbConnexion;  

public class Prediction {
    private int prediction_id;
    private String nom_prediction;
    private double montant;
    private Date date_prediction;  // java.sql.Date
    private Timestamp date_creation;

    public Prediction() {}

    public Prediction(String nom_prediction, double montant, Date date_prediction) {
        this.nom_prediction = nom_prediction;
        this.montant = montant;
        this.date_prediction = date_prediction;
    }

    // Getters et Setters
    public int getPrediction_id() { return prediction_id; }
    public void setPrediction_id(int prediction_id) { this.prediction_id = prediction_id; }
    
    public String getNom_prediction() { return nom_prediction; }
    public void setNom_prediction(String nom_prediction) { this.nom_prediction = nom_prediction; }
    
    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }
    
    public Date getDate_prediction() { return date_prediction; }
    public void setDate_prediction(Date date_prediction) { this.date_prediction = date_prediction; }
    
    public Timestamp getDate_creation() { return date_creation; }
    public void setDate_creation(Timestamp date_creation) { this.date_creation = date_creation; }

    public void save() throws SQLException {
        DbConnexion db = new DbConnexion();
        String sql = "INSERT INTO budget_predictions (nom_prediction, montant, date_prediction) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nom_prediction);
            stmt.setDouble(2, montant);
            stmt.setDate(3, date_prediction);
            stmt.executeUpdate();
            
            // Récupérer l'ID généré
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                setPrediction_id(rs.getInt(1));
            }
        } finally {
            db.closeConnection();
        }
    }

    public static List<Prediction> findAll() throws SQLException {
        List<Prediction> predictions = new ArrayList<>();
        DbConnexion db = new DbConnexion();
        String sql = "SELECT * FROM budget_predictions";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Prediction pred = new Prediction();
                pred.setPrediction_id(rs.getInt("prediction_id"));
                pred.setNom_prediction(rs.getString("nom_prediction"));
                pred.setMontant(rs.getDouble("montant")); 
                pred.setDate_prediction(rs.getDate("date_prediction"));
                pred.setDate_creation(rs.getTimestamp("date_creation"));
                predictions.add(pred);
            }
        } finally {
            db.closeConnection();
        }
        return predictions;
    }

    public static Prediction getById(int id) throws SQLException {
        DbConnexion db = new DbConnexion();
        String sql = "SELECT * FROM budget_predictions WHERE prediction_id = ?";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Prediction pred = new Prediction();
                pred.setPrediction_id(rs.getInt("prediction_id"));
                pred.setNom_prediction(rs.getString("nom_prediction"));
                pred.setMontant(rs.getDouble("montant"));
                pred.setDate_prediction(rs.getDate("date_prediction"));
                pred.setDate_creation(rs.getTimestamp("date_creation"));
                return pred;
            }
        } finally {
            db.closeConnection();
        }
        return null;
    }



}