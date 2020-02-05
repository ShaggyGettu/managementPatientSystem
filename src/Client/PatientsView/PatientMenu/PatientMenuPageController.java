package Client.PatientsView.PatientMenu;

import Client.DataTypes.BloodPressure;
import Client.DataTypes.Point;
import Client.DataTypes.Temperature;
import Client.DataTypes.Value;
import Client.DoctorsView.DoctorsMenu.DoctorMenuPageController;
import Client.Login.LoginPage;
import Client.PatientsView.PatientData.PatientData;
import Client.PatientsView.PatientMenuPage;
import Server.CreateUserData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.*;

public class PatientMenuPageController implements Initializable {
    @FXML
    Label nameLabel, idLabel, emailLabel, phoneLabel;
    @FXML
    Label temperatureLabel, bloodPressureLabel, glucoseLabel;
    @FXML
    Label maxTemperatureValueLabel, maxBloodPressureValueLabel, maxGlucoseValueLabel;
    @FXML
    Label minTemperatureValueLabel, minBloodPressureValueLabel, minGlucoseValueLabel;
    @FXML
    Label maxTemperatureDateLabel, maxTemperatureTimeLabel;
    @FXML
    Label maxBloodPressureDateLabel, maxBloodPressureTimeLabel;
    @FXML
    Label maxGlucoseDateLabel, maxGlucoseTimeLabel;
    @FXML
    Label minTemperatureDateLabel, minTemperatureTimeLabel;
    @FXML
    Label minBloodPressureDateLabel, minBloodPressureTimeLabel;
    @FXML
    Label minGlucoseDateLabel, minGlucoseTimeLabel;
    @FXML
    Label averageTemperatureLabel, averageBloodPressureLabel, averageGlucoseLabel;
    @FXML
    Label amountTemperatureLabel, amountBloodPressureLabel, amountGlucoseLabel;
    @FXML
    AnchorPane testsPane, temperaturePane, bloodPressurePane, glucosePane;
    @FXML
    ImageView refreshView;
    @FXML
    Button backButton, temperatureButton, bloodPressureButton, glucoseButton;

    private PatientMenuPage patientMenuPage;
    private PatientMenuPageModel patientMenuPageModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        patientMenuPage = PatientMenuPage.getInstance();
        try {
            patientMenuPageModel = PatientMenuPageModel.getInstance();
            loadPatient();
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Actions();
        temperaturePane.setVisible(false);
        bloodPressurePane.setVisible(false);
        glucosePane.setVisible(false);
    }

    private void loadPatient() throws SQLException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        ResultSet resultSet = patientMenuPage.getResultSet();
        String details[] = new String[3];
        details[0] = resultSet.getString(2);
        details[1] = resultSet.getString(5);
        details[2] = resultSet.getString(8);
        idLabel.setText(patientMenuPage.getId());
        emailLabel.setText(details[0]);
        phoneLabel.setText(details[1]);
        String tests[] = details[2].split(" ");
        if (Arrays.asList(tests).contains("Temperature")) {
            temperaturePane.setVisible(true);
            patientMenuPageModel.setTemperature(true);
        }
        if (Arrays.asList(tests).contains("BloodPressure")) {
            bloodPressurePane.setVisible(true);
            patientMenuPageModel.setBloodPressure();
        }
        if (Arrays.asList(tests).contains("Glucose")) {
            glucosePane.setVisible(true);
            patientMenuPageModel.setGlucose(true);
        }
        String temperature, bloodPressure, glucose;
        if (temperaturePane.isVisible() && Integer.parseInt(resultSet.getString(12)) != 0) {
            temperature = resultSet.getString(11);//AVG
            averageTemperatureLabel.setText(temperature);
            temperature = resultSet.getString(9);
            minTemperatureValueLabel.setText(temperature.split(" ")[0]);
            minTemperatureDateLabel.setText(temperature.split(" ")[1]);
            minTemperatureTimeLabel.setText(temperature.split(" ")[2]);
            temperature = resultSet.getString(10);
            maxTemperatureValueLabel.setText(temperature.split(" ")[0]);
            maxTemperatureDateLabel.setText(temperature.split(" ")[1]);
            maxTemperatureTimeLabel.setText(temperature.split(" ")[2]);
            temperature = resultSet.getString(12);
            amountTemperatureLabel.setText(temperature);
        }
        if (bloodPressurePane.isVisible() && Integer.parseInt(resultSet.getString(17)) != 0) {
            bloodPressure = resultSet.getString(18);
            averageBloodPressureLabel.setText(bloodPressure);
            bloodPressure = resultSet.getString(15);
            minBloodPressureValueLabel.setText(bloodPressure.split(" ")[0]);
            minBloodPressureDateLabel.setText(bloodPressure.split(" ")[1]);
            minBloodPressureTimeLabel.setText(bloodPressure.split(" ")[2]);
            bloodPressure = resultSet.getString(16);
            maxBloodPressureValueLabel.setText(bloodPressure.split(" ")[0]);
            maxBloodPressureDateLabel.setText(bloodPressure.split(" ")[1]);
            maxBloodPressureTimeLabel.setText(bloodPressure.split(" ")[2]);
            bloodPressure = resultSet.getString(17);
            amountBloodPressureLabel.setText(bloodPressure);
        }
        if (glucosePane.isVisible() && Integer.parseInt(resultSet.getString(22)) != 0) {
            glucose = resultSet.getString(23);
            averageGlucoseLabel.setText(glucose);
            glucose = resultSet.getString(20);
            minGlucoseValueLabel.setText(glucose.split(" ")[0]);
            minGlucoseDateLabel.setText(glucose.split(" ")[1]);
            minGlucoseTimeLabel.setText(glucose.split(" ")[2]);
            glucose = resultSet.getString(21);
            maxGlucoseValueLabel.setText(glucose.split(" ")[0]);
            maxGlucoseDateLabel.setText(glucose.split(" ")[1]);
            maxGlucoseTimeLabel.setText(glucose.split(" ")[2]);
            glucose = resultSet.getString(22);
            amountGlucoseLabel.setText(glucose);
        }
    }

    private void Actions() {
        DoctorMenuPageController doctorMenuPageController = new DoctorMenuPageController();
        doctorMenuPageController.BackAction(backButton);
        refreshView.setOnMouseClicked(mouseEvent -> {
            try {
                loadPatient();
            } catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
                e.printStackTrace();
            }
        });
        backButton.setOnMouseClicked(mouseEvent -> {
            CreateUserData.getInstance().stop();
            LoginPage loginPage = new LoginPage();
            try {
                loginPage.start(LoginPage.getWindow());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        temperatureButton.setOnMouseClicked(mouseEvent -> {
            PatientData patientData = PatientData.getInstance();
            try {
                patientData.setId(patientMenuPage.getId());
                CreateUserData.getInstance().stop();
                patientData.createScreen();
                LoginPage.getWindow().setScene(patientData.getScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
