package util;
import java.sql.*;

public class DbConnexion {
    // private static final String URL = "jdbc:mysql://localhost:3306/budget_management";
    // private static final String USER = "root";
    // private static final String PASSWORD = "";
    private static final String URL = "jdbc:mysql://localhost:3306/db_s2_ETU003147";
    private static final String USER = "ETU003147";
    private static final String PASSWORD = "3UH8N3hj";
    private Connection connection;
    
    public DbConnexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Connection getConnection() {
        return this.connection;
    }
    
    public void closeConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        DbConnexion conn = new DbConnexion();
        try {
            if (conn.getConnection() != null) {
                System.out.println("Connexion réussie à la base de données!");
            } else {
                System.out.println("Échec de la connexion.");
            }
        } finally {
            conn.closeConnection();
        }
    }
}
