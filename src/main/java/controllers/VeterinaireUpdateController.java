package controllers;

import entities.GestionAnimal.Veterinaire;
import services.GestionAnimal.VeterinaireServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class VeterinaireUpdateController {

    @FXML private TextField idField;
    @FXML private TextField nomField;
    @FXML private TextField numTelField;
    @FXML private TextField emailField;
    @FXML private TextField adresseCabineField;

    private final VeterinaireServiceImpl service = new VeterinaireServiceImpl();
    private Veterinaire veterinaire;

    // Setter to pre-fill the form
    public void setVeterinaire(Veterinaire veterinaire) {
        this.veterinaire = veterinaire;
        if (veterinaire != null) {
            // Remplir tous les champs avec les données du vétérinaire
            idField.setText(String.valueOf(veterinaire.getId()));
            nomField.setText(veterinaire.getNom());
            numTelField.setText(String.valueOf(veterinaire.getnum_tel()));
            emailField.setText(veterinaire.getEmail());
            adresseCabineField.setText(veterinaire.getadresse_cabine());
        }
    }

    @FXML
    public void handleUpdateVeterinaire() {
        try {
            int id = Integer.parseInt(idField.getText());
            // Validation des champs
            if (nomField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom est obligatoire.");
                return;
            }
            if (numTelField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone est obligatoire.");
                return;
            }
            if (emailField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "L'email est obligatoire.");
                return;
            }
            if (adresseCabineField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "L'adresse du cabinet est obligatoire.");
                return;
            }

            Veterinaire v = new Veterinaire(id,
                    nomField.getText(),
                    Integer.parseInt(numTelField.getText()),
                    emailField.getText(),
                    adresseCabineField.getText());
            service.updateVeterinaire(v);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Mise à jour effectuée !");
            Stage stage = (Stage) idField.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit être un nombre.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur: " + e.getMessage());
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}