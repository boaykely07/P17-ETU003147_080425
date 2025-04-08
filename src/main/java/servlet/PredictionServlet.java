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
import model.Prediction;

public class PredictionServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String nomPrediction = request.getParameter("nom_prediction");
            double montant = Double.parseDouble(request.getParameter("montant"));
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date datePrediction = dateFormat.parse(request.getParameter("date_prediction"));
            
            Prediction prediction = new Prediction();
            prediction.setNom_prediction(nomPrediction);
            prediction.setMontant(montant);
            prediction.setDate_prediction(new java.sql.Date(datePrediction.getTime()));
            
            prediction.save();
            
            request.setAttribute("message", "Prediction enregistree avec succes!");
            request.getRequestDispatcher("/prevision.jsp").forward(request, response);
            
        } catch (SQLException | ParseException e) {
            request.setAttribute("erreur", "Erreur lors de l'enregistrement: " + e.getMessage());
            request.getRequestDispatcher("/prevision.jsp").forward(request, response);
        }
    }
}
