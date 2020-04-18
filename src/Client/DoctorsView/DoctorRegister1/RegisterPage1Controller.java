package Client.DoctorsView.DoctorRegister1;

import Client.DoctorsView.DoctorRegister2.RegisterPage2;
import Client.DoctorsView.DoctorsMenu.DoctorsMenuPage;
import Client.Login.LoginPage;
import Client.ManagerView.ManagerMenuPage;
import animatefx.animation.FadeIn;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPage1Controller implements Initializable {

    public Label idLabel;
    public Label emailLabel;
    public Label passwordLabel;
    public Label nameLabel;
    public Label phoneNumberLabel;
    @FXML
    AnchorPane mainPane;
    @FXML
    TextField idTextField, emailTextField, passwordTextField, nameTextField, phoneNumberTextField;
    @FXML
    Button nextButton, exitButton;
    @FXML
    Label idErrorLabel, emailErrorLabel, passwordErrorLabel, nameErrorLabel, phoneNumberErrorLabel;

    private DoctorsMenuPage doctorMenuPage;
    private ManagerMenuPage managerMenuPage;
    private RegisterPage1 registerPage1;
    private Node selectedItem;
    private RegisterPage1Model registerPage1Model;
    private RegisterPage2 registerPage2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (DoctorsMenuPage.isExist())
            doctorMenuPage = DoctorsMenuPage.getInstance();
        else if (ManagerMenuPage.isExist())
            managerMenuPage = ManagerMenuPage.getManagerMenuPage();
        registerPage1 = RegisterPage1.getInstance();
        registerPage2 = RegisterPage2.getInstance();
        try {
            registerPage1Model = RegisterPage1Model.getRegisterPage1Model();
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Actions();
        idTextField.setText(registerPage1.getFields()[0]);
        emailTextField.setText(registerPage1.getFields()[1]);
        passwordTextField.setText(registerPage1.getFields()[2]);
        nameTextField.setText(registerPage1.getFields()[3]);
        phoneNumberTextField.setText(registerPage1.getFields()[4]);
    }

    private void Actions() {
        nextButton.setOnAction(actionEvent-> {
            try {
                addPatient();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
        exitButton.setOnMouseClicked(mouseEvent -> {
            if (managerMenuPage != null)
                LoginPage.getWindow().setScene(managerMenuPage.getScene());
            else if (doctorMenuPage != null) {
                LoginPage.getWindow().setScene(doctorMenuPage.getScene());
            }
        });
        idTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equals("TAB")){
                emailErrorLabel.setVisible(false);
                if(!checkId()){
                    idErrorLabel.setText("Please enter valid id.");
                    idErrorLabel.setVisible(true);
                }
                else
                    idErrorLabel.setVisible(false);
            }
        });
        emailTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equals("TAB")){
                passwordErrorLabel.setVisible(false);
                if (!checkEmail(emailTextField)){
                    emailErrorLabel.setText("Please enter valid email address.");
                    emailErrorLabel.setVisible(true);
                }
                else
                    emailErrorLabel.setVisible(false);
            }
        });
        passwordTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equals("TAB")){
                nameErrorLabel.setVisible(false);
                if (!checkPassword(passwordTextField))
                    passwordErrorLabel.setVisible(true);
                else
                    passwordErrorLabel.setVisible(false);
            }
        });
        nameTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equals("TAB")){
                phoneNumberErrorLabel.setVisible(false);
                if (!checkName(nameTextField, nameErrorLabel))
                    nameErrorLabel.setVisible(true);
                else
                    nameErrorLabel.setVisible(false);
            }
        });
        phoneNumberTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equals("TAB")){
                if (!checkPhone())
                    phoneNumberErrorLabel.setVisible(true);
                else
                    phoneNumberErrorLabel.setVisible(false);
            }
        });
        exitButton.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equals("TAB"))
                idErrorLabel.setVisible(false);
        });
        idTextField.setOnMouseClicked(mouseEvent -> {
            idErrorLabel.setVisible(false);
            check(selectedItem);
        });
        emailTextField.setOnMouseClicked(mouseEvent -> {
            emailErrorLabel.setVisible(false);
            check(selectedItem);
        });
        passwordTextField.setOnMouseClicked(mouseEvent -> {
            passwordErrorLabel.setVisible(false);
            check(selectedItem);
        });
        nameTextField.setOnMouseClicked(mouseEvent -> {
            nameErrorLabel.setVisible(false);
            check(selectedItem);
        });
        phoneNumberTextField.setOnMouseClicked(mouseEvent -> {
            phoneNumberErrorLabel.setVisible(false);
            check(selectedItem);
        });
        mainPane.setOnMouseMoved(mouseEvent -> selectedItem = registerPage1.getScene().focusOwnerProperty().get());

    }

    private void check(Node node) {
        if(node == idTextField) {
            if (!checkId()) {
                idErrorLabel.setText("Please enter valid id.");
                idErrorLabel.setVisible(true);
            }
            else
                idErrorLabel.setVisible(false);
        }
        else if (node == emailTextField) {
            if (!checkEmail(emailTextField)) {
                emailErrorLabel.setText("Please enter valid email address.");
                emailErrorLabel.setVisible(true);
            }
            else
                emailErrorLabel.setVisible(false);
        }
        else if (node == passwordTextField) {
            if (!checkPassword(passwordTextField))
                passwordErrorLabel.setVisible(true);
            else
                passwordErrorLabel.setVisible(false);
        }
        else if (node == nameTextField) {
            if (!checkName(nameTextField, nameErrorLabel))
                nameErrorLabel.setVisible(true);
            else
                nameErrorLabel.setVisible(false);
        }
        else if (node == phoneNumberTextField) {
            if (!checkPhone())
                phoneNumberErrorLabel.setVisible(true);
            else
                phoneNumberErrorLabel.setVisible(false);
        }
    }

    private boolean checkPhone() {
        if (phoneNumberTextField.getText().equals(""))
            return true;
        if (!phoneNumberTextField.getText().chars().allMatch(Character::isDigit)){
            phoneNumberErrorLabel.setText("Please enter valid phone number.");
            return false;
        }
        if (phoneNumberTextField.getText().length()!=10){
            phoneNumberErrorLabel.setText("Please enter 10 digits phone number.");
            return false;
        }
        return true;
    }

    public boolean checkName(TextField textField, Label label) {
        if (textField.getText().equals(""))
            return true;
        if (textField.getText().split(" ").length != 2) {
            label.setText("Please enter first name and last name.");
            return false;
        }
        String fName = textField.getText().split(" ")[0], lName = textField.getText().split(" ")[1];
        if (!fName.chars().allMatch(Character::isLetter)||!lName.chars().allMatch(Character::isLetter)) {
            label.setText("Please enter valid name.");
            return false;
        }
        if (fName.length()<=2||lName.length()<=2) {
            label.setText("Please enter valid name.");
            return false;
        }
        return true;
    }

    public boolean checkPassword(TextField textField) {
        //https://www.java2novice.com/java-collections-and-util/regex/valid-password/
        if (textField.getText().equals(""))
            return true;
        String password = textField.getText();
        if (password.length()<8)
            return false;
        Pattern pswNamePtrn = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,15})");
        Matcher mtch = pswNamePtrn.matcher(password);
        return mtch.matches();
    }

    public boolean checkEmail(TextField textField) {
        //https://www.geeksforgeeks.org/check-email-address-valid-not-java/
        String email = textField.getText();
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches() || email.equals("");
    }

    private boolean checkId() {
        if (idTextField.getText().equals(""))
            return true;
        if(idTextField.getText().length()!=9)
            return false;
        return idTextField.getText().chars().allMatch(Character::isDigit);
    }

    private void addPatient() throws SQLException, IOException {
        String id = idTextField.getText(), email = emailTextField.getText(), password = passwordTextField.getText();
        String name = nameTextField.getText(), phone = phoneNumberTextField.getText();

        if(!checkId()){
            idErrorLabel.setVisible(true);
            idErrorLabel.setText("Please enter valid id.");
            idTextField.requestFocus();
            new FadeIn(idTextField).play();
            return;
        }
        if (id.equals("")){
            idTextField.requestFocus();
            new FadeIn(idTextField).play();
            return;
        }
        if(!checkEmail(emailTextField)){
            emailErrorLabel.setVisible(true);
            emailErrorLabel.setText("Please enter valid email address.");
            emailTextField.requestFocus();
            new FadeIn(emailTextField).play();
            return;
        }
        if (email.equals("")){
            emailTextField.requestFocus();
            new FadeIn(emailTextField).play();
            return;
        }
        if(!checkPassword(passwordTextField)){
            passwordErrorLabel.setVisible(true);
            passwordTextField.requestFocus();
            new FadeIn(passwordTextField).play();
            return;
        }
        if (password.equals("")){
            passwordTextField.requestFocus();
            new FadeIn(passwordTextField).play();
            return;
        }
        if (!checkName(nameTextField, nameErrorLabel)){
            nameErrorLabel.setVisible(true);
            nameTextField.requestFocus();
            new FadeIn(nameErrorLabel).play();
            return;
        }
        if (name.equals("")){
            nameTextField.requestFocus();
            new FadeIn(nameTextField).play();
            return;
        }
        if (!checkPhone()){
            phoneNumberErrorLabel.setVisible(true);
            phoneNumberTextField.requestFocus();
            new FadeIn(phoneNumberTextField).play();
            return;
        }
        if (phone.equals("")){
            phoneNumberTextField.requestFocus();
            new FadeIn(phoneNumberTextField).play();
            return;
        }
        if (registerPage1Model.idExists(id)){
            idErrorLabel.setText("id already exists in the system");
            idErrorLabel.setVisible(true);
            idTextField.requestFocus();
            new FadeIn(idTextField).play();
            return;
        }
        if(registerPage1Model.emailExists(email)){
            emailErrorLabel.setText("email already exists in the system");
            emailErrorLabel.setVisible(true);
            emailTextField.requestFocus();
            new FadeIn(emailTextField).play();
            return;
        }

        registerPage1.getFields()[0] = idTextField.getText();
        registerPage1.getFields()[1] = emailTextField.getText();
        registerPage1.getFields()[2] = passwordTextField.getText();
        registerPage1.getFields()[3] = nameTextField.getText();
        registerPage1.getFields()[4] = phoneNumberTextField.getText();
        registerPage2.createScreen();
        LoginPage.getWindow().setScene(registerPage2.getScene());
    }
}
