package Client.Login;

import Client.DoctorsView.DoctorsMenu.DoctorsMenuPage;
import Client.PatientsView.PatientMenuPage;
import Server.CreateUserData;
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
    public AnchorPane mainPane;
    @FXML
    public ComboBox<String> roleComboBox;
    @FXML
    public Label usernameLabel;
    @FXML
    public Label passwordLabel;
    @FXML
    public TextField emailTextField;
    @FXML
    public PasswordField passwordTextField;
    @FXML
    public Button loginButton;
    @FXML
    public Label errorEmailLabel;
    @FXML
    public Label errorRoleLabel;
    @FXML
    private Label errorDetailsLabel;

    private ObservableList<String> list = FXCollections.observableArrayList("Patient", "Doctor");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        /*Formatter formatter = new Formatter();
        formatter.format("%tB", Calendar.getInstance());
        System.out.println(formatter);*/
        try {
            loginModel = LoginModel.getLoginModel();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
        roleComboBox.setPromptText("Patient/Doctor");
        roleComboBox.setItems(list);
        if (loginModel.isConnected())
            System.out.println("Connected");
        else System.out.println("Not Connected");
        Actions();
    }

    private void loginButtonOnClick() throws SQLException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        String role = roleComboBox.getSelectionModel().getSelectedItem();
        String id;
        if(email.isBlank()){
            new FadeIn(emailTextField).play();
            emailTextField.requestFocus();
            errorRoleLabel.setVisible(false);
            errorEmailLabel.setVisible(false);
            return;
        }
        if(!checkEmail(email)){
            errorEmailLabel.setVisible(true);
            new FadeIn(emailTextField).play();
            emailTextField.requestFocus();
            errorRoleLabel.setVisible(false);
            return;
        }
        if (password.isBlank()){
            passwordTextField.requestFocus();
            errorEmailLabel.setVisible(false);
            new FadeIn(passwordTextField).play();
            errorRoleLabel.setVisible(false);
            return;
        }
        if (!validatePassword(password)){
            new FadeIn(passwordTextField).play();
            passwordTextField.requestFocus();
            errorRoleLabel.setText("Please enter valid password");
            errorRoleLabel.setVisible(true);
            errorEmailLabel.setVisible(false);
            return;
        }
        if(roleComboBox.getSelectionModel().getSelectedItem()==null){
            errorRoleLabel.setVisible(true);
            new FadeIn(roleComboBox).play();
            errorEmailLabel.setVisible(false);
            roleComboBox.requestFocus();
            errorRoleLabel.setText("Please enter your role patient/doctor");
            return;
        }
        errorRoleLabel.setVisible(false);
        errorEmailLabel.setVisible(false);
        ResultSet resultSet = loginModel.login(email, password, role);
        if (resultSet != null) {
            id = resultSet.getString("id");
            if(role.equals("Doctor")) {
                DoctorsMenuPage doctorsMenuPage = DoctorsMenuPage.getInstance();
                doctorsMenuPage.setId(id);
                doctorsMenuPage.createScreen();
                LoginPage.getWindow().setScene(doctorsMenuPage.getScene());
            }
            if (role.equals("Patient")){
                PatientMenuPage patientMenuPage = PatientMenuPage.getInstance();
                patientMenuPage.setId(id);
                patientMenuPage.createScene();
                LoginPage.getWindow().setScene(patientMenuPage.getScene());
                String periodTimeRepeat = resultSet.getString("periodTimeRepeat");
                String s[] = periodTimeRepeat.split("\n");
                periodTimeRepeat = s[s.length - 1].split(" ")[2];
                String lastTest = resultSet.getString("lastTest");
                System.out.println(id);
                Thread thread = new Thread(CreateUserData.getInstance(periodTimeRepeat, lastTest, id), id);
                thread.start();
            }
        }
        else {
            errorDetailsLabel.setVisible(true);
            emailTextField.requestFocus();
        }
    }

    private boolean checkEmail(String email){
        //https://www.geeksforgeeks.org/check-email-address-valid-not-java/
        errorDetailsLabel.setVisible(false);
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
         if(!pat.matcher(email).matches()&&!email.isBlank()) {
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
                loginButtonOnClick();
            } catch (SQLException | IOException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
                e.printStackTrace();
            }
        });
        LoginPage.getWindow().setOnCloseRequest(windowEvent -> System.exit(0));
    }

}
