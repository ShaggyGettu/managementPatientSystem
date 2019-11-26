package Client.PatientsView.PatientMenu;

import Client.DoctorsView.DoctorsMenu.DoctorMenuPageController;
import Client.Login.LoginPage;
import Client.PatientsView.PatientMenuPage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class PatientMenuPageController implements Initializable {
    @FXML
    Label nameLabel, idLabel, emailLabel, phoneLabel;
    @FXML
    Button temperatureButton, bloodPressureButton, glucoseButton, backButton;

    PatientMenuPage patientMenuPage;
    PatientMenuPageModel patientMenuPageModel;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Actions();
        patientMenuPage = PatientMenuPage.getInstance();
        try {
            patientMenuPageModel = PatientMenuPageModel.getInstance();
            loadPatient();
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void loadPatient() throws SQLException {
        String details[] = patientMenuPageModel.getPatient(patientMenuPage.getId());
        idLabel.setText(patientMenuPage.getId());
        emailLabel.setText(details[0]);
        phoneLabel.setText(details[1]);
        String tests[] = details[2].split(" ");
        if (Arrays.asList(tests).contains("Temperature"))
            temperatureButton.setVisible(true);
        if (Arrays.asList(tests).contains("BloodPressure"))
            bloodPressureButton.setVisible(true);
        if (Arrays.asList(tests).contains("Glucose"))
            glucoseButton.setVisible(true);
    }

    private void Actions() {
        DoctorMenuPageController doctorMenuPageController = new DoctorMenuPageController();
        doctorMenuPageController.BackAction(backButton);
        temperatureButton.setOnMouseClicked(mouseEvent ->{

        });
        glucoseButton.setOnMouseClicked(mouseEvent ->{

        });
        bloodPressureButton.setOnMouseClicked(mouseEvent ->{

        });

    }
}
