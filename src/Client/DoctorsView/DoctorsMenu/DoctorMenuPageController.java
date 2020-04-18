package Client.DoctorsView.DoctorsMenu;

import Client.DataTypes.Patient;
import Client.DataTypes.Warning;
import Client.DoctorsView.DoctorRegister1.RegisterPage1;
import Client.Login.LoginPage;
import Client.ManagerView.ManagerMenuPageController;
import animatefx.animation.FadeIn;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DoctorMenuPageController implements Initializable {
    private static CheckWarn checkWarn;
    @FXML
    Button backButton, addPatientButton, warningsButton, patientsButton;
    @FXML
    ImageView refreshView;
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
        checkWarn = new CheckWarn();
        checkWarn.setDaemon(true);
        checkWarn.start();
        checkWarn.setPriority(7);
    }

    public class CheckWarn extends Thread{
        boolean flag = true;

        @Override
        public void run() {
            Runnable runnable = () -> {
                try {
                    showLiveWarn();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            };
            while (flag) {
                try {
                    System.out.println(this.getId() + " 1");
                    doctorMenuPageModel = DoctorMenuPageModel.getDoctorMenuPageModel();
                    ResultSet resultSet = doctorMenuPageModel.getWarning(doctorsMenuPage.getId());
                    String warnings = resultSet.getString(1);
                    if (warnings.endsWith("end\n") || warnings.equals("")) {
                        sleep(10000);
                        continue;
                    }
                    System.out.println("new Warning");
                    Platform.runLater(() -> Platform.runLater(runnable));
                    sleep(10000);
                } catch (ClassNotFoundException | SQLException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        void stopThread(){
            flag = false;
        }
    }

    private void showLiveWarn() throws SQLException, ClassNotFoundException {
        doctorMenuPageModel = DoctorMenuPageModel.getDoctorMenuPageModel();
        ResultSet resultSet = doctorMenuPageModel.getWarning(doctorsMenuPage.getId());
        String warnings = resultSet.getString(1);
        int length = warnings.split("end\n").length;
        warnings = warnings.split("end\n")[length - 1];
        String warningsArr[] = warnings.split("\n");
        for (String warning:warningsArr) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            String patientName = doctorMenuPageModel.getPatientName(warning.split(" ")[0]);
            String patientPhone = doctorMenuPageModel.getPatientPhone(warning.split(" ")[0]);
            alert.setTitle("Critical patient index");
            alert.setHeaderText("Patient " + patientName + " has " + warning.split(" ")[2] + " " + warning.split(" ")[1] + ", You can call her: " + patientPhone);
            alert.showAndWait();
        }
        doctorMenuPageModel.endWarnings(doctorsMenuPage.getId());
    }

    private void showDoctorDetails() throws SQLException, ClassNotFoundException {
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
            patients.add(new Patient(resultSet.getString(1), resultSet.getString(2),resultSet.getString(5),doctorsMenuPage.getId(),resultSet.getString(4)));
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
                checkWarn.stopThread();
                ManagerMenuPageController.showPatient(patient);
            });
            patient.getDelete().setOnMouseClicked(mouseEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Patient");
                alert.setHeaderText("Warning, You are about to delete patient from the system");
                alert.setContentText("If you sure you want to delete this patient press Yes");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    try {
                        doctorMenuPageModel.deletePatient(patient.getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        loadPatients();
                    } catch (ClassNotFoundException | SQLException | IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
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
            warnings.add(new Warning(warning[0], doctorMenuPageModel.getPatientName(warning[0]), warning[1], warning[2], warning[3] + " " + warning[4]));
        }
        idWarningColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
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
            checkWarn.stopThread();
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
        refreshView.setOnMouseClicked(mouseEvent ->{
            checkWarn.stopThread();
            checkWarn = new CheckWarn();
            checkWarn.start();
            checkWarn.setPriority(3);
        });
    }

    public void BackAction(Button backButton1) {
        backButton1.setOnMouseClicked(mouseEvent -> {
            if (backButton1 == this.backButton)
                checkWarn.stopThread();
            LoginPage loginPage = new LoginPage();
            try {
                loginPage.start(LoginPage.getWindow());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
