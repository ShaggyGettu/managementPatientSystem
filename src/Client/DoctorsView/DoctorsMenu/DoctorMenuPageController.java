package Client.DoctorsView.DoctorsMenu;

import Client.DataTypes.Patient;
import Client.DataTypes.Warning;
import Client.DoctorsView.DoctorRegister1.RegisterPage1;
import Client.DoctorsView.PatientScreen.PatientScreen;
import Client.Login.LoginPage;
import animatefx.animation.FadeIn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DoctorMenuPageController implements Initializable {
    @FXML
    Button backButton, addPatientButton, warningsButton, patientsButton;
    @FXML
    TableView<Patient> patientsTable;
    @FXML
    TableColumn<Patient, Integer> idPatientColumn;
    @FXML
    TableColumn<Patient, String> namePatientColumn;
    @FXML
    TableColumn<Patient, String> emailPatientColumn;
    @FXML
    TableColumn<Patient, String> phonePatientColumn;
    @FXML
    TableColumn<Patient, Button> showPatientColumn;
    @FXML
    TableColumn<Patient, Button> deletePatientColumn;
    @FXML
    TableView<Warning> warningsTable;
    @FXML
    TableColumn<Warning, String> idWarningColumn;
    @FXML
    TableColumn<Warning, String> testWarningColumn;
    @FXML
    TableColumn<Warning, String> valueWarningColumn;
    @FXML
    TableColumn<Warning, String> dateWarningColumn;
    @FXML
    TableColumn<Warning, Button> deleteWarningColumn;
    @FXML
    Label titleLabel, doctorIdLabel, doctorNameLabel, doctorEmailLabel;
    @FXML
    private AnchorPane titlePane;

    private Map<String ,String > colorSet;
    private DoctorsMenuPage doctorsMenuPage;
    private RegisterPage1 registerPage1;
    private DoctorMenuPageModel doctorMenuPageModel;
    private static boolean entered;
    private ObservableList<Warning> warnings;

    public static void setEntered() {
        entered = false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Actions();
        initColor();
        doctorsMenuPage = DoctorsMenuPage.getInstance();
        registerPage1 = RegisterPage1.getInstance();
        try {
            showDoctorDetails();
            if (!entered) {
                showAlertWarnings();
                entered = true;
            }
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void showDoctorDetails() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        doctorMenuPageModel = DoctorMenuPageModel.getDoctorMenuPageModel();
        ResultSet resultSet = doctorMenuPageModel.getDoctorDetails(doctorsMenuPage.getId());
        doctorIdLabel.setText(resultSet.getString("id"));
        doctorNameLabel.setText(resultSet.getString("name"));
        doctorEmailLabel.setText(resultSet.getString("email"));
    }

    private void showAlertWarnings() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        doctorMenuPageModel = DoctorMenuPageModel.getDoctorMenuPageModel();
        ResultSet resultSet = doctorMenuPageModel.getWarning(doctorsMenuPage.getId());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning data from patients");
        alert.setHeaderText(null);
        String warnings = resultSet.getString(1);
        if (warnings.endsWith("end\n") || warnings.equals("")){
            alert.setContentText("You don't have a warning information from your patients");
            alert.showAndWait();
        }
        else {
            int length = warnings.split("end\n").length;
            warnings = warnings.split("end\n")[length - 1];
            alert.setHeaderText("Warning data");
            Label label = new Label("All the warning data from your patients:");
            TextArea textArea = new TextArea(warnings);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);
            GridPane gridPane = new GridPane();
            gridPane.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(label, 0, 0);
            gridPane.add(textArea, 0, 1);
            alert.getDialogPane().setExpandableContent(gridPane);
            alert.show();
            doctorMenuPageModel.endWarnings(doctorsMenuPage.getId());
        }
    }

    private void initColor() {
        colorSet = new HashMap<>();
        colorSet.put("blue", "#642EFE");
    }

    private void loadPatients() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        warningsTable.setVisible(false);
        patientsTable.setVisible(true);
        doctorMenuPageModel = DoctorMenuPageModel.getDoctorMenuPageModel();
        ResultSet resultSet = doctorMenuPageModel.getPatients(doctorsMenuPage.getId());
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        while (resultSet.next()){
            patients.add(new Patient(resultSet.getString(1), resultSet.getString(2),resultSet.getString(4),doctorsMenuPage.getId(),resultSet.getString(6)));
        }
        idPatientColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        namePatientColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailPatientColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phonePatientColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        showPatientColumn.setCellValueFactory(new PropertyValueFactory<>("show"));
        deletePatientColumn.setCellValueFactory(new PropertyValueFactory<>("delete"));
        patientsTable.setItems(patients);
        for (Patient patient : patients) {
            patient.getShow().setOnMouseClicked(mouseEvent -> {
                PatientScreen patientScreen = null;
                try {
                    patientScreen = PatientScreen.getInstance();
                    patientScreen.setPatients(patient);
                } catch (ClassNotFoundException | SQLException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
                try {
                    assert patientScreen != null;
                    patientScreen.createScreen();
                    LoginPage.getWindow().setScene(patientScreen.getScene());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        titleLabel.setText("Patients");
        titlePane.styleProperty().set("-fx-background-color:" + colorSet.get("blue"));
        titlePane.setVisible(true);
        new FadeIn(titlePane).play();
    }

    private void loadWarnings() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        Warning.setCount();
        patientsTable.setVisible(false);
        warningsTable.setVisible(true);
        titleLabel.setText("Warnings");
        titlePane.styleProperty().set("-fx-background-color:" + colorSet.get("blue"));
        titlePane.setVisible(true);
        new FadeIn(titlePane).play();
        doctorMenuPageModel = DoctorMenuPageModel.getDoctorMenuPageModel();
        ResultSet resultSet = doctorMenuPageModel.getWarning(doctorsMenuPage.getId());
        if (resultSet.getString(1).equals(""))
            return;
        warnings = FXCollections.observableArrayList();
        ArrayList<String> warningsString = new ArrayList<>(Arrays.asList(resultSet.getString(1).split("\n")));
        for (String string:warningsString){
            if (string.equals("end"))
                continue;
            String warning[] = string.split(" ");
            warnings.add(new Warning(warning[0], warning[1], warning[2], warning[3] + " " + warning[4]));
        }
        idWarningColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        testWarningColumn.setCellValueFactory(new PropertyValueFactory<>("test"));
        valueWarningColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        dateWarningColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        deleteWarningColumn.setCellValueFactory(new PropertyValueFactory<>("delete"));
        warningsTable.setItems(warnings);
        for (Warning warning : warnings) {
            warning.getDelete().setOnMouseClicked(mouseEvent -> {
                int place = warning.getWarningId();
                System.out.println(place);
                warnings.remove(warning);
                StringBuilder warnings1 = new StringBuilder();
                int i = 1;
                for (String string:warningsString) {
                    if (i != place)
                        warnings1.append(string).append("\n");
                    i++;
                }
                try {
                    doctorMenuPageModel.updateWarnings(doctorsMenuPage.getId(), String.valueOf(warnings1));
                    loadWarnings();
                } catch (SQLException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void Actions(){
        BackAction(backButton);
        patientsButton.setOnMouseClicked(mouseEvent -> {
            try {
                loadPatients();
            } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
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
        warningsButton.setOnMouseClicked(mouseEvent -> {
            try {
                loadWarnings();
            } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public void BackAction(Button backButton1) {
        backButton1.setOnMouseClicked(mouseEvent -> {
            LoginPage loginPage = new LoginPage();
            try {
                loginPage.start(LoginPage.getWindow());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
