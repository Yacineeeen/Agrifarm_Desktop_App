package controllers;

import entities.GestionAnimal.Suivi;
import services.GestionAnimal.SuiviServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SuiviDeleteController {

    @FXML private Label confirmationLabel;
    @FXML private Label animalLabel;
    @FXML private Label veterinaireLabel;

    private final SuiviServiceImpl suiviService = new SuiviServiceImpl();
    private Suivi suivi;

    public void setSuivi(Suivi suivi) {
        this.suivi = suivi;
        try {
            confirmationLabel.setText("Are you sure you want to delete Suivi for " +
                    (suivi.getAnimal() != null ? suivi.getAnimal().getNom() : "N/A") + "?");
            animalLabel.setText("Animal: " + (suivi.getAnimal() != null ? suivi.getAnimal().getNom() : "N/A"));
            veterinaireLabel.setText("Veterinaire: " + (suivi.getVeterinaire() != null ? suivi.getVeterinaire().getNom() : "N/A"));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error setting up Suivi information: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteSuivi() {
        try {
            if (suivi != null) {
                suiviService.deleteSuivi(suivi.getId());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Suivi deleted successfully!");
                Stage stage = (Stage) confirmationLabel.getScene().getWindow();
                stage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No Suivi selected for deletion.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete Suivi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) confirmationLabel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}