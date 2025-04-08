<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Prediction" %>
<%@ page import="model.Depense" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nouvelle Dépense</title>
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>
    <div class="dashboard">
        <div class="header">
            <div class="header-title">
                <h1>Gestion de Budget</h1>
            </div>
            <div class="nav-links">
                <a href="prevision.jsp" class="nav-link">Prévision</a>
                <a href="depense" class="nav-link" style="color: blue;">Dépenses</a>
            </div>
        </div>
    <form action="saveDepense" method="post">
        <fieldset>
            <div class="title-container">
                <legend>Nouvelle Dépense</legend>
                <% if(request.getAttribute("message") != null) { %>
                    <span class="message success"><%= request.getAttribute("message") %></span>
                <% } %>
                <% if(request.getAttribute("erreur") != null) { %>
                    <span class="message error"><%= request.getAttribute("erreur") %></span>
                <% } %>
            </div>

            <% if(request.getAttribute("montantPrevu") != null) { 
                double montantPrevu = (Double)request.getAttribute("montantPrevu");
                double totalDepenses = (Double)request.getAttribute("totalDepenses");
                double montantRestant = (Double)request.getAttribute("montantRestant");
            %>
                <div class="budget-info">
                    <h3>État du budget pour cette prédiction :</h3>
                    <p>Budget prévu : <%= String.format("%.2f", montantPrevu) %> €</p>
                    <p>Total dépensé : <%= String.format("%.2f", totalDepenses) %> €</p>
                    <p class="<%= montantRestant > 0 ? "montant-restant" : "montant-depasse" %>">
                        Solde disponible : <%= String.format("%.2f", montantRestant) %> €
                    </p>
                </div>
            <% } %>

            <p>
                <label for="prediction_id">Prédiction associée:</label><br>
                <select id="prediction_id" name="prediction_id" required onchange="this.form.action='depense';this.form.method='get';this.form.submit();">
                    <option value="">Sélectionner une prédiction</option>
                    <% 
                    List<Prediction> predictions = (List<Prediction>)request.getAttribute("predictions");
                    if(predictions != null) {
                        for(Prediction pred : predictions) {
                    %>
                        <option value="<%= pred.getPrediction_id() %>" 
                            <%= request.getParameter("prediction_id") != null && 
                                request.getParameter("prediction_id").equals(String.valueOf(pred.getPrediction_id())) 
                                ? "selected" : "" %>>
                            <%= pred.getNom_prediction() %> - <%= String.format("%.2f", pred.getMontant()) %> €
                        </option>
                    <% 
                        }
                    }
                    %>
                </select>
            </p>

            <p>
                <label for="descriptions">Description détaillée:</label><br>
                <textarea id="descriptions" name="descriptions" 
                    rows="4" cols="50" 
                    placeholder="Décrivez votre dépense..." 
                    required></textarea>
            </p>

            <p>
                <label for="montant">Montant dépensé:</label><br>
                <input type="number" id="montant" name="montant" step="0.01" required>
            </p>
            
            <p>
                <label for="date_depense">Date de la dépense:</label><br>
                <input type="date" id="date_depense" name="date_depense" required>
            </p>
            
            <p>
                <input type="submit" value="Enregistrer la dépense">
            </p>
        </fieldset>
    </form>

    <div class="liste-depenses">
        <h2>Liste des Dépenses</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Prédiction</th>
                    <th>Description</th>
                    <th>Montant</th>
                    <th>Date</th>
                </tr>
            </thead>
            <tbody>
                <%
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    List<Depense> depenses = Depense.findAll();
                    for(Depense d : depenses) {
                        Prediction pred = Prediction.getById(d.getPrediction_id());
                %>
                    <tr>
                        <td><%= d.getDepense_id() %></td>
                        <td><%= pred != null ? pred.getNom_prediction() : "N/A" %></td>
                        <td class="description-cell" title="<%= d.getDescriptions() %>">
                            <%= d.getDescriptions() %>
                        </td>
                        <td><%= String.format("%.2f €", d.getMontant()) %></td>
                        <td><%= sdf.format(d.getDate_depense()) %></td>
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
