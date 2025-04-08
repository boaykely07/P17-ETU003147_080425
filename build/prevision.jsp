<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Prediction" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nouvelle Prédiction Budgétaire</title>
    <link rel="stylesheet" href="css/dashboard.css">

</head>
<body>
<div class="dashboard">
        <div class="header">
            <div class="header-title">
                <h1>Gestion de Budget</h1>
            </div>
            <div class="nav-links">
                <a href="prevision.jsp" class="nav-link" style="color: blue;">Prévision</a>
                <a href="depense" class="nav-link">Dépenses</a>
            </div>
        </div>

    <form action="savePrediction" method="post">
        <fieldset>
            <div class="title-container">
                <legend>Nouvelle Prédiction Budgétaire</legend>
                <% if(request.getAttribute("message") != null) { %>
                    <span class="message success"><%= request.getAttribute("message") %></span>
                <% } %>
                <% if(request.getAttribute("erreur") != null) { %>
                    <span class="message error"><%= request.getAttribute("erreur") %></span>
                <% } %>
            </div>
            
            <p>
                <label for="nom_prediction">Nom de la prédiction:</label><br>
                <input type="text" id="nom_prediction" name="nom_prediction" required>
            </p>

            <p>
                <label for="montant">Montant prévu:</label><br>
                <input type="number" id="montant" name="montant" step="0.01" required>
            </p>
            <p>
                <label for="date_prediction">Date de la prédiction:</label><br>
                <input type="date" id="date_prediction" name="date_prediction" required>
            </p>
            <p>
                <input type="submit" value="Enregistrer la prédiction">
            </p>
        </fieldset>
    </form>

    <div class="liste-predictions">
        <h2>Liste des Prédictions</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Montant</th>
                    <th>Date</th>
                </tr>
            </thead>
            <tbody>
                <%
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    List<Prediction> predictions = Prediction.findAll();
                    for(Prediction p : predictions) {
                %>
                    <tr>
                        <td><%= p.getPrediction_id() %></td>
                        <td><%= p.getNom_prediction() %></td>
                        <td><%= String.format("%.2f €", p.getMontant()) %></td>
                        <td><%= sdf.format(p.getDate_prediction()) %></td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>