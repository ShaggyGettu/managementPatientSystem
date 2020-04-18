package Client.ManagerView;

import Client.DataTypes.Doctor;
import Client.DataTypes.Patient;
import Client.DoctorsView.DoctorRegister1.RegisterPage1;
import Client.DoctorsView.DoctorsMenu.DoctorMenuPageController;
import Client.DoctorsView.PatientScreen.PatientScreen;
import Client.Login.LoginPage;
import Client.ManagerView.DoctorRegistration.DoctorRegistration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManagerMenuPageController implements Initializable {

    public AnchorPane titlePane;
    public Label titleLabel;
    @FXML
    Button addPatientButton, addDoctorButton, patientsListButton, doctorListButton, backButton;
    @FXML
    ImageView refreshView;
    @FXML
    Label managerNameLabel, managerIdLabel, managerEmailLabel;
    @FXML
    TableView<Patient> patientsTable;
    @FXML
    TableView<Doctor> doctorsTable;
    @FXML
    TableColumn<Patient, String> patientIdColumn;
    @FXML
    TableColumn<Patient, String> patientNameColumn;
    @FXML
    TableColumn<Patient, String> pDoctorIdColumn;
    @FXML
    TableColumn<Patient, String> pDoctorNameColumn;
    @FXML
    TableColumn<Patient, String> patientEmailColumn;
    @FXML
    TableColumn<Patient, String> patientPhoneColumn;
    @FXML
    TableColumn<Patient, Button> showPatientColumn;
    @FXML
    TableColumn<Patient, Button> deletePatientColumn;
    @FXML
    TableColumn<Doctor, String> doctorIdColumn;
    @FXML
    TableColumn<Doctor, String> doctorNameColumn;
    @FXML
    TableColumn<Doctor, String> doctorEmailColumn;
    @FXML
    TableColumn<Doctor, Button> deleteDoctorColumn;

    private ManagerMenuPageModel managerMenuPageModel;
    private ManagerMenuPage managerMenuPage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        managerMenuPage = ManagerMenuPage.getManagerMenuPage();
        try {
            managerMenuPageModel = ManagerMenuPageModel.getModel();
            String string[] = managerMenuPageModel.managerDetails(managerMenuPage.getId());
            managerIdLabel.setText(managerMenuPage.getId());
            managerEmailLabel.setText(string[0]);
            managerNameLabel.setText(string[1]);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        Actions();
    }

    private void Actions() {
        addPatientButton.setOnMouseClicked(mouseEvent -> {
            try {
                addPatient();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        addDoctorButton.setOnMouseClicked(mouseEvent -> {
            try {
                addDoctor();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        patientsListButton.setOnMouseClicked(mouseEvent -> {
            try {
                showPatients();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        doctorListButton.setOnMouseClicked(mouseEvent -> {
            try {
                showDoctors();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        DoctorMenuPageController doctorMenuPageController = new DoctorMenuPageController();
        doctorMenuPageController.BackAction(backButton);
    }

    private void showDoctors() throws SQLException {
        doctorsTable.setVisible(true);
        patientsTable.setVisible(false);
        ResultSet resultSet = managerMenuPageModel.getDoctors();
        ObservableList<Doctor> doctors = FXCollections.observableArrayList();
        while (resultSet.next())
            doctors.add(new Doctor(resultSet.getString(2), resultSet.getString(3), resultSet.getString(1)));
        doctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        doctorEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        deleteDoctorColumn.setCellValueFactory(new PropertyValueFactory<>("delete"));
        doctorsTable.setItems(doctors);
        for (Doctor doctor : doctors) {
            doctor.getDelete().setOnMouseClicked(mouseEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Doctor");
                alert.setHeaderText("Warning, You are about to delete doctor from the system");
                alert.setContentText("If you sure you want to delete this doctor press Yes");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    try {
                        managerMenuPageModel.deleteDoctor(doctor.getId());
                        showDoctors();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void showPatients() throws SQLException, ClassNotFoundException {
        patientsTable.setVisible(true);
        doctorsTable.setVisible(false);
        ResultSet resultSet = managerMenuPageModel.getPatients();
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        while (resultSet.next())
            patients.add(new Patient(resultSet.getString(1), resultSet.getString(2), resultSet.getString(5), resultSet.getString(6), resultSet.getString(4)));
        patientsTable.setItems(patients);
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        pDoctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        pDoctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        patientEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        patientPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        showPatientColumn.setCellValueFactory(new PropertyValueFactory<>("show"));
        deletePatientColumn.setCellValueFactory(new PropertyValueFactory<>("delete"));
        for (Patient patient : patients) {
            patient.getShow().setOnMouseClicked(mouseEvent -> showPatient(patient));
            patient.getDelete().setOnMouseClicked(mouseEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Patient");
                alert.setHeaderText("Warning, You are about to delete patient from the system");
                alert.setContentText("If you sure you want to delete this patient press Yes");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    try {
                        managerMenuPageModel.deletePatient(patient.getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        showPatients();
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                    }
                }

            });
        }
    }

    public static void showPatient(Patient patient) {
        PatientScreen patientScreen;
        try {
            patientScreen = PatientScreen.getInstance();
            patientScreen.setPatients(patient);
            patientScreen.createScreen();
            LoginPage.getWindow().setScene(patientScreen.getScene());
        } catch (IOException | IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void addDoctor() throws IOException {
        DoctorRegistration doctorRegistration = DoctorRegistration.getDoctorRegistration();
        doctorRegistration.createScene();
        LoginPage.getWindow().setScene(doctorRegistration.getScene());
    }

    private void addPatient() throws SQLException {
        ResultSet resultSet = managerMenuPageModel.getDoctors();
        List<String> choices = new ArrayList<>();
        while (resultSet.next())
            choices.add(resultSet.getString(3) + " " + resultSet.getString(1));
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(choices.get(0), choices);
        choiceDialog.setTitle("Choose doctor");
        choiceDialog.setHeaderText("Choose doctor for the new patient");
        choiceDialog.setContentText("Doctor: ");
        Optional<String> result = choiceDialog.showAndWait();
        result.ifPresent(s -> {
            s = s.split(" ")[2];
            managerMenuPage.setSelectedDoctor(s);
            try {
                RegisterPage1 registerPage1 = RegisterPage1.getInstance();
                registerPage1.emptyFields();
                registerPage1.createScreen();
                LoginPage.getWindow().setScene(registerPage1.getScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
