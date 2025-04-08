CREATE DATABASE budget_management;
USE budget_management;

CREATE TABLE budget_utilisateurs (
    utilisateur_id INT PRIMARY KEY AUTO_INCREMENT,
    nom_utilisateur VARCHAR(50) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO budget_utilisateurs (nom_utilisateur, mot_de_passe) VALUES
('user1', 'password123'),
('user2', 'password456');

CREATE TABLE budget_predictions (
    prediction_id INT PRIMARY KEY AUTO_INCREMENT,
    nom_prediction VARCHAR(100) NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    date_prediction DATE NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);

CREATE TABLE budget_depenses (
    depense_id INT PRIMARY KEY AUTO_INCREMENT,
    prediction_id INT,
    descriptions VARCHAR(255) NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    date_depense DATE NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (prediction_id) REFERENCES budget_predictions(prediction_id)
);