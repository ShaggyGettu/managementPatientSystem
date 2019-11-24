package Client.DoctorsView.DoctorsMenu;

import Client.DataTypes.Patient;
import Client.DoctorsView.DoctorRegister1.RegisterPage1;
import Client.DoctorsView.PatientScreen.PatientScreen;
import Client.Login.LoginPage;
import animatefx.animation.FadeIn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class DoctorMenuPageController implements Initializable {
    @FXML
    Button backButton;
    @FXML
    TableView<Patient> patientsTable;
    @FXML
    TableColumn<Patient, Integer> idColumn;
    @FXML
    TableColumn<Patient, String> nameColumn;
    @FXML
    TableColumn<Patient, String> emailColumn;
    @FXML
    TableColumn<Patient, String> phoneColumn;
    @FXML
    TableColumn<Patient, Button> showColumn;
    @FXML
    TableColumn<Patient, Button> deleteColumn;
    @FXML
    Button patientsButton;
    @FXML
    Label titleLabel;
    @FXML
    private AnchorPane titlePane;
    @FXML
    private Button addPatientButton;

    private Map<String ,String > colorSet;
    private DoctorsMenuPage doctorsMenuPage;
    private ObservableList<Patient> patients;
    private RegisterPage1 registerPage1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Actions();
        initColor();
        doctorsMenuPage = DoctorsMenuPage.getInstance();
        registerPage1 = RegisterPage1.getInstance();
        try {
            loadPatients();
        } catch (ClassNotFoundException | SQLException | InstantiationException | IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void initColor() {
        colorSet = new HashMap<>();
        colorSet.put("blue", "#642EFE");
    }

    private void loadPatients() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {
        DoctorMenuPageModel doctorMenuPageModel = DoctorMenuPageModel.getDoctorMenuPageModel();
        ResultSet resultSet = doctorMenuPageModel.getPatients(doctorsMenuPage.getId());
        patients = FXCollections.observableArrayList();
        while (resultSet.next()){
            patients.add(new Patient(resultSet.getString(1), resultSet.getString(2),resultSet.getString(4),doctorsMenuPage.getId(),resultSet.getString(6)));
        }
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        showColumn.setCellValueFactory(new PropertyValueFactory<>("show"));
        deleteColumn.setCellValueFactory(new PropertyValueFactory<>("delete"));
        patientsTable.setItems(patients);
        if(patients != null) {
            for (Patient patient : patients) {
                patient.getShow().setOnMouseClicked(mouseEvent -> {
                    PatientScreen patientScreen = null;
                    try {
                        patientScreen = PatientScreen.getInstance();
                        patientScreen.setPatient(patient);
                    } catch (ClassNotFoundException | SQLException | IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                    try {
                        patientScreen.createScreen();
                        LoginPage.getWindow().setScene(patientScreen.getScene());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
            titleLabel.setText("Patients");
            titlePane.styleProperty().set("-fx-background-color:" + colorSet.get("blue"));
            titlePane.setVisible(true);
            new FadeIn(titlePane).play();
    }

    private void Actions(){
        backButton.setOnMouseClicked(mouseEvent -> {
            LoginPage loginPage = new LoginPage();
            try {
                loginPage.start(LoginPage.getWindow());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        patientsButton.setOnMouseClicked(mouseEvent -> {
            try {
                loadPatients();
            } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException | IOException e) {
                e.printStackTrace();
            }
        });
        addPatientButton.setOnMouseClicked(mouseEvent -> {
            try {
                registerPage1.emptyFields();
                registerPage1.createScreen();
                LoginPage.getWindow().setScene(registerPage1.getScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
