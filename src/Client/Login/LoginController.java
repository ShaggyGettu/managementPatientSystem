package Client.Login;

import Client.DoctorsView.DoctorsMenu.DoctorsMenuPage;
import Client.ManagerView.ManagerMenuPage;
import Client.PatientsView.PatientMenu.PatientMenuPage;
import Client.PatientsView.PatientData.CreateUserData;
import animatefx.animation.FadeIn;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController implements Initializable {
    private LoginModel loginModel;

    @FXML
    private AnchorPane mainPane;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    public Label usernameLabel;
    @FXML
    public Label passwordLabel;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorEmailLabel;
    @FXML
    private Label errorRoleLabel;
    @FXML
    private Label errorDetailsLabel;

    private ObservableList<String> list = FXCollections.observableArrayList("Patient", "Doctor", "Manager");
    private String id, email, password, role;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            loginModel = LoginModel.getLoginModel();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        roleComboBox.setPromptText("Patient/Doctor/Manager");
        roleComboBox.setItems(list);
        if (loginModel.isConnected())
            System.out.println("Connected");
        else System.out.println("Not Connected");
        Actions();
    }

    private boolean loginButtonOnClick() {
        email = emailTextField.getText();
        password = passwordTextField.getText();
        role = roleComboBox.getSelectionModel().getSelectedItem();
        if(email.equals("")){
            new FadeIn(emailTextField).play();
            emailTextField.requestFocus();
            errorRoleLabel.setVisible(false);
            errorEmailLabel.setVisible(false);
            return false;
        }
        if(!checkEmail(email)){
            errorEmailLabel.setVisible(true);
            new FadeIn(emailTextField).play();
            emailTextField.requestFocus();
            errorRoleLabel.setVisible(false);
            return false;
        }
        if (password.equals("")){
            passwordTextField.requestFocus();
            errorEmailLabel.setVisible(false);
            new FadeIn(passwordTextField).play();
            errorRoleLabel.setVisible(false);
            return false;
        }
        if (!validatePassword(password)){
            new FadeIn(passwordTextField).play();
            passwordTextField.requestFocus();
            errorRoleLabel.setText("Please enter valid password");
            errorRoleLabel.setVisible(true);
            errorEmailLabel.setVisible(false);
            return false;
        }
        if(roleComboBox.getSelectionModel().getSelectedItem()==null){
            errorRoleLabel.setVisible(true);
            new FadeIn(roleComboBox).play();
            errorEmailLabel.setVisible(false);
            roleComboBox.requestFocus();
            errorRoleLabel.setText("Please enter your role patient/doctor");
            return false;
        }
        errorRoleLabel.setVisible(false);
        errorEmailLabel.setVisible(false);
        return true;
    }

    private boolean checkEmail(String email){
        //https://www.geeksforgeeks.org/check-email-address-valid-not-java/
        errorDetailsLabel.setVisible(false);
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
         if(!pat.matcher(email).matches()&&!email.equals("")) {
             errorEmailLabel.setVisible(true);
             return false;
         }
         errorEmailLabel.setVisible(false);
         return true;
    }

    private static boolean validatePassword(String password){
        //https://www.java2novice.com/java-collections-and-util/regex/valid-password/
        if (password.length()<8)
            return false;
        Pattern pswNamePtrn = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,15})");
        Matcher mtch = pswNamePtrn.matcher(password);
        return mtch.matches();
    }

    private void deleteError(){
        errorEmailLabel.setVisible(false);
        errorDetailsLabel.setVisible(false);
    }

    private void Actions(){
        //On key pressed TAB key
        emailTextField.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().toString().equals("TAB")){
                checkEmail(emailTextField.getText());
            }
        });
        loginButton.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().toString().equals("TAB"))
                deleteError();
        });
        emailTextField.setOnKeyTyped(keyEvent -> deleteError());
        emailTextField.setOnMouseClicked(mouseEvent -> deleteError());
        emailTextField.setOnMousePressed(mouseEvent -> deleteError());
        passwordTextField.setOnMouseClicked(mouseEvent -> checkEmail(emailTextField.getText()));
        mainPane.setOnMouseClicked(mouseEvent -> checkEmail(emailTextField.getText()));
        loginButton.setOnAction(actionEvent -> {
            try {
                boolean flag = loginButtonOnClick();
                if (flag)
                    login();
            } catch (SQLException | IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        LoginPage.getWindow().setOnCloseRequest(windowEvent -> System.exit(0));
    }

    private void login() throws SQLException, IOException, ClassNotFoundException {
        ResultSet resultSet = loginModel.login(email, password, role);
        if (resultSet != null) {
            id = resultSet.getString("id");
            if(role.equals("Doctor"))
                loadDoctorPage();
            if (role.equals("Patient"))
                loadPatientPage(resultSet);
            if (role.equals("Manager"))
                loadManagerPage();
        }
        else
            errorDetailsShow();
    }

    private void loadManagerPage() throws IOException {
        ManagerMenuPage managerMenuPage = ManagerMenuPage.getManagerMenuPage();
        managerMenuPage.setId(id);
        managerMenuPage.createScene();
        LoginPage.getWindow().setScene(managerMenuPage.getScene());
    }

    private void errorDetailsShow() {
        errorDetailsLabel.setVisible(true);
        emailTextField.requestFocus();
    }

    private void loadPatientPage(ResultSet resultSet) throws IOException, SQLException, ClassNotFoundException {
        PatientMenuPage patientMenuPage = PatientMenuPage.getInstance();
        patientMenuPage.setId(id);
        patientMenuPage.setBackScreen(0);
        patientMenuPage.createScreen(id);
        LoginPage.getWindow().setScene(patientMenuPage.getScene());
        String periodTimeRepeat = resultSet.getString(26);
        String s[] = periodTimeRepeat.split("\n");
        periodTimeRepeat = s[s.length - 1].split(" ")[2];
        int period = Integer.valueOf(periodTimeRepeat);
        String lastTest = resultSet.getString(27);
        Thread thread = new Thread(CreateUserData.getInstance(period, lastTest, id), id);
        thread.start();
    }

    private void loadDoctorPage() throws IOException {
        DoctorsMenuPage doctorsMenuPage = DoctorsMenuPage.getInstance();
        doctorsMenuPage.setId(id);
        doctorsMenuPage.createScreen();
        LoginPage.getWindow().setScene(doctorsMenuPage.getScene());
    }

}
