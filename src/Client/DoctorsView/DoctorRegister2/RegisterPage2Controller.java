package Client.DoctorsView.DoctorRegister2;

import Client.DoctorsView.DoctorRegister1.RegisterPage1;
import Client.DoctorsView.DoctorsMenu.DoctorsMenuPage;
import Client.Login.LoginPage;
import Client.ManagerView.ManagerMenuPage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterPage2Controller implements Initializable {
    @FXML
    TextArea backgroundTextArea;
    @FXML
    TextField periodTime, criticalMaxTemp, criticalMaxGlu, criticalMinTemp, criticalBP, criticalMinGlu;
    @FXML
    CheckBox temperatureCheckBox, bloodPressureCheckBox, glucoseCheckBox;
    @FXML
    Button exitButton, backButton, finishButton;

    private RegisterPage2Model registerPage2Model;
    private DoctorsMenuPage doctorMenuPage = null;
    private ManagerMenuPage managerMenuPage;
    private RegisterPage1 registerPage1;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Actions();
        periodTime.setPromptText("Default period time repeat is 3 hours");
        if (DoctorsMenuPage.isExist())
            doctorMenuPage = DoctorsMenuPage.getInstance();
        else if (ManagerMenuPage.isExist())
            managerMenuPage = ManagerMenuPage.getManagerMenuPage();
        registerPage1 = RegisterPage1.getInstance();
        try {
            registerPage2Model = RegisterPage2Model.getRegisterPage2Model();
        } catch (ClassNotFoundException | SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void Actions() {
        exitButton.setOnMouseClicked(mouseEvent -> {
            if (managerMenuPage != null)
                LoginPage.getWindow().setScene(managerMenuPage.getScene());
            if (doctorMenuPage != null)
                LoginPage.getWindow().setScene(doctorMenuPage.getScene());
        });
        backButton.setOnMouseClicked(mouseEvent -> LoginPage.getWindow().setScene(registerPage1.getScene()));
        finishButton.setOnMouseClicked(mouseEvent -> {
            try {
                addPatient();
            } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException | IOException e) {
                e.printStackTrace();
            }
        });
        setCheckBoxVisible(temperatureCheckBox, criticalMinTemp, criticalMaxTemp);
        setCheckBoxVisible(bloodPressureCheckBox, criticalBP);
        setCheckBoxVisible(glucoseCheckBox, criticalMinGlu, criticalMaxGlu);
    }

    private void setCheckBoxVisible(CheckBox bloodPressureCheckBox, TextField criticalBP) {
        bloodPressureCheckBox.setOnMouseClicked(mouseEvent ->{
            if (bloodPressureCheckBox.isSelected())
                criticalBP.setVisible(true);
            else {
                criticalBP.setText("170,105");
                criticalBP.setVisible(false);
            }
        });
    }

    private void setCheckBoxVisible(CheckBox glucoseCheckBox, TextField criticalMinGlu, TextField criticalMaxGlu) {
        glucoseCheckBox.setOnMouseClicked(mouseEvent -> {
            if (glucoseCheckBox.isSelected()){
                criticalMinGlu.setVisible(true);
                criticalMaxGlu.setVisible(true);
            }
            else {
                if (glucoseCheckBox == this.glucoseCheckBox) {
                    criticalMaxGlu.setText("160");
                    criticalMinGlu.setText("65");
                }
                if (glucoseCheckBox == this.temperatureCheckBox){
                    criticalMaxGlu.setText("38.8");
                    criticalMinGlu.setText("35.4");
                }
                criticalMinGlu.setVisible(false);
                criticalMaxGlu.setVisible(false);
            }
        });
    }

    private void addPatient() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {
        String background = backgroundTextArea.getText();
        String tests = "";
        int intPeriodTime;
        if (periodTime.getText().equals("")|| periodTime.getText().contains("."))
            intPeriodTime = 3;
        else
            intPeriodTime = Integer.parseInt(periodTime.getText());
        if (temperatureCheckBox.isSelected()) {
            tests = "Temperature ";
            if (!criticalMinTemp.getText().equals(""))
                registerPage2Model.setCriticalMinTemp(criticalMinTemp.getText(), registerPage1.getFields()[0]);
            if (!criticalMaxTemp.getText().equals(""))
                registerPage2Model.setCriticalMaxTemp(criticalMaxTemp.getText(), registerPage1.getFields()[0]);
        }
        if (bloodPressureCheckBox.isSelected()) {
            tests += "BloodPressure ";
            if (!criticalBP.getText().equals(""))
                registerPage2Model.setCriticalBP(criticalBP.getText(), registerPage1.getFields()[0]);
        }
        if (glucoseCheckBox.isSelected()) {
            tests += "Glucose";
            if (!criticalMinGlu.getText().equals(""))
                registerPage2Model.setCriticalMinGlu(criticalMinGlu.getText(), registerPage1.getFields()[0]);
            if (!criticalMaxGlu.getText().equals(""))
                registerPage2Model.setCriticalMaxGlu(criticalMaxGlu.getText(), registerPage1.getFields()[0]);
        }
        if (doctorMenuPage != null) {
            registerPage2Model.addPatient(registerPage1.getFields()[0], registerPage1.getFields()[1], registerPage1.getFields()[2], registerPage1.getFields()[4], doctorMenuPage.getId(), registerPage1.getFields()[3], background, tests, intPeriodTime);
            doctorMenuPage.createScreen();
            LoginPage.getWindow().setScene(doctorMenuPage.getScene());
        }
        else if (managerMenuPage != null){
            registerPage2Model.addPatient(registerPage1.getFields()[0], registerPage1.getFields()[1], registerPage1.getFields()[2], registerPage1.getFields()[4], managerMenuPage.getSelectedDoctor(), registerPage1.getFields()[3], background, tests, intPeriodTime);
            managerMenuPage.createScene();
            LoginPage.getWindow().setScene(managerMenuPage.getScene());
        }
    }
}
