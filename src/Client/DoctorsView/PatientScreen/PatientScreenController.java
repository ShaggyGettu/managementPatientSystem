package Client.DoctorsView.PatientScreen;

import Client.DoctorsView.DoctorsMenu.DoctorsMenuPage;
import Client.Login.LoginPage;
import Client.ManagerView.ManagerMenuPage;
import Client.PatientsView.PatientMenu.PatientMenuPage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PatientScreenController implements Initializable {
    @FXML
    Label nameLabel, idLabel, emailLabel, phoneLabel, backgroundLabel;
    @FXML
    Button dataButton, backButton;

    private PatientScreen patientScreen;
    private DoctorsMenuPage doctorsMenuPage;
    private ManagerMenuPage managerMenuPage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (DoctorsMenuPage.isExist())
            doctorsMenuPage = DoctorsMenuPage.getInstance();
        else if (ManagerMenuPage.isExist())
            managerMenuPage = ManagerMenuPage.getManagerMenuPage();
        Actions();
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        try {
            patientScreen = PatientScreen.getInstance();
        } catch (ClassNotFoundException | SQLException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        putData();
    }

    private void putData() {
        nameLabel.setText(patientScreen.getPatients().getName());
        idLabel.setText(String.valueOf(patientScreen.getPatients().getId()));
        emailLabel.setText(patientScreen.getPatients().getEmail());
        phoneLabel.setText(patientScreen.getPatients().getPhone());
        backgroundLabel.setText(patientScreen.getPatients().getBackground());
    }

    private void Actions() {
        backButton.setOnMouseClicked(mouseEvent -> {
            if (doctorsMenuPage != null) {
                LoginPage.getWindow().setScene(doctorsMenuPage.getScene());
            }
            else if (managerMenuPage != null)
                LoginPage.getWindow().setScene(managerMenuPage.getScene());
        });
        dataButton.setOnMouseClicked(mouseEvent -> {
            PatientMenuPage patientMenuPage;
            patientMenuPage = PatientMenuPage.getInstance();
            patientMenuPage.setId(patientScreen.getPatients().getId());
            try {
                patientMenuPage.createScreen(patientMenuPage.getId());
                LoginPage.getWindow().setScene(patientMenuPage.getScene());
                patientMenuPage.setBackScreen(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
