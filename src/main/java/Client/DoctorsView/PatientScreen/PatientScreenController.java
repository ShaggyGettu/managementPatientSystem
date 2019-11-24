package Client.DoctorsView.PatientScreen;

import Client.DoctorsView.DoctorsMenu.DoctorsMenuPage;
import Client.Login.LoginPage;
import com.mysql.cj.log.Log;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class PatientScreenController implements Initializable {
    @FXML
    Label nameLabel, idLabel, emailLabel, phoneLabel, backgroundLabel;
    @FXML
    Button temperatureButton, bloodPressureButton, glucoseButton, backButton;

    private PatientScreen patientScreen;
    private DoctorsMenuPage doctorsMenuPage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        doctorsMenuPage = DoctorsMenuPage.getInstance();
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
        nameLabel.setText(patientScreen.getPatient().getName());
        idLabel.setText(String.valueOf(patientScreen.getPatient().getId()));
        emailLabel.setText(patientScreen.getPatient().getEmail());
        phoneLabel.setText(patientScreen.getPatient().getPhone());
        backgroundLabel.setText(patientScreen.getPatient().getBackground());
        String tests[] = patientScreen.getPatient().getTests();
        if (Arrays.asList(tests).contains("Temperature"))
            temperatureButton.setVisible(true);
        if (Arrays.asList(tests).contains("BloodPressure"))
            bloodPressureButton.setVisible(true);
        if (Arrays.asList(tests).contains("Glucose"))
            glucoseButton.setVisible(true);
    }

    private void Actions() {
        backButton.setOnMouseClicked(mouseEvent -> LoginPage.getWindow().setScene(doctorsMenuPage.getScene()));
        temperatureButton.setOnMouseClicked(mouseEvent ->{

        });
        bloodPressureButton.setOnMouseClicked(mouseEvent ->{

        });
        glucoseButton.setOnMouseClicked(mouseEvent ->{

        });
    }
}
