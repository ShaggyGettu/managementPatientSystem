package Client.DoctorsView.DoctorRegister2;

import Client.DoctorsView.DoctorRegister1.RegisterPage1;
import Client.DoctorsView.DoctorsMenu.DoctorsMenuPage;
import Client.Login.LoginPage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterPage2Controller implements Initializable {
    @FXML
    TextArea backgroundTextArea;
    @FXML
    CheckBox temperatureCheckBox, bloodPressureCheckBox, glucoseCheckBox;
    @FXML
    Button exitButton, backButton, finishButton;

    private RegisterPage2Model registerPage2Model;
    private DoctorsMenuPage doctorMenuPage;
    private RegisterPage1 registerPage1;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Actions();
        doctorMenuPage = DoctorsMenuPage.getInstance();
        registerPage1 = RegisterPage1.getInstance();
        try {
            registerPage2Model = RegisterPage2Model.getRegisterPage2Model();
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void Actions() {
        exitButton.setOnMouseClicked(mouseEvent -> LoginPage.getWindow().setScene(doctorMenuPage.getScene()));
        backButton.setOnMouseClicked(mouseEvent -> LoginPage.getWindow().setScene(registerPage1.getScene()));
        finishButton.setOnMouseClicked(mouseEvent -> {
            try {
                addPatient();
            } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void addPatient() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {
        String background = backgroundTextArea.getText();
        String tests = "";
        if (temperatureCheckBox.isSelected())
            tests = "Temperature ";
        if (bloodPressureCheckBox.isSelected())
            tests += "BloodPressure ";
        if (glucoseCheckBox.isSelected())
            tests += "Glucose";
        registerPage2Model.addPatient(registerPage1.getFields()[0], registerPage1.getFields()[1], registerPage1.getFields()[2], registerPage1.getFields()[4], doctorMenuPage.getId(), registerPage1.getFields()[3], background, tests);
            doctorMenuPage.createScreen();
        LoginPage.getWindow().setScene(doctorMenuPage.getScene());
    }
}
