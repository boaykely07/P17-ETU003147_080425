package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.Prediction;
import model.Depense;

@WebServlet("/depense")
public class DepenseServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Prediction> predictions = Prediction.findAll();
            request.setAttribute("predictions", predictions);
            
            // Si une prédiction est sélectionnée, calculer le budget restant
            String predictionId = request.getParameter("prediction_id");
            if (predictionId != null && !predictionId.isEmpty()) {
                int predId = Integer.parseInt(predictionId);
                Prediction pred = Prediction.getById(predId);
                double totalDepenses = Depense.getTotalDepensesByPrediction(predId);
                double restant = pred.getMontant() - totalDepenses;
                
                request.setAttribute("montantPrevu", pred.getMontant());
                request.setAttribute("totalDepenses", totalDepenses);
                request.setAttribute("montantRestant", restant);
            }
            
            request.getRequestDispatcher("/depense.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("erreur", "Erreur: " + e.getMessage());
            request.getRequestDispatcher("/depense.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int predictionId = Integer.parseInt(request.getParameter("prediction_id"));
            String descriptions = request.getParameter("descriptions");
            double montant = Double.parseDouble(request.getParameter("montant"));
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateDepense = dateFormat.parse(request.getParameter("date_depense"));
            
            Depense depense = new Depense();
            depense.setPrediction_id(predictionId);
            depense.setDescriptions(descriptions);
            depense.setMontant(montant);
            depense.setDate_depense(new java.sql.Date(dateDepense.getTime()));
            
            depense.save();
            
            List<Prediction> predictions = Prediction.findAll();
            request.setAttribute("predictions", predictions);
            request.setAttribute("message", "Depense enregistree avec succes!");
            
            request.getRequestDispatcher("/depense.jsp").forward(request, response);
            
        } catch (SQLException | ParseException e) {
            request.setAttribute("erreur", "Erreur lors de l'enregistrement: " + e.getMessage());
            try {
                List<Prediction> predictions = Prediction.findAll();
                request.setAttribute("predictions", predictions);
            } catch (SQLException ex) {
                request.setAttribute("erreur", "Erreur systeme: " + ex.getMessage());
            }
            request.getRequestDispatcher("/depense.jsp").forward(request, response);
        }
    }
}
