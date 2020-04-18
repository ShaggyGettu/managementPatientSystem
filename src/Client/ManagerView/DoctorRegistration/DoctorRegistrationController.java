package Client.ManagerView.DoctorRegistration;

import Client.DoctorsView.DoctorRegister1.RegisterPage1Controller;
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

public class DoctorRegistrationController implements Initializable {
    public Label idLabel;
    public Label emailLabel;
    public Label passwordLabel;
    public Label nameLabel;
    @FXML
    AnchorPane mainPane;
    @FXML
    TextField idTextField, emailTextField, passwordTextField, nameTextField;
    @FXML
    Button addButton, exitButton;
    @FXML
    Label idErrorLabel, emailErrorLabel, passwordErrorLabel, nameErrorLabel;

    private Node selectedItem;
    private DoctorRegistration doctorRegistration;
    private DoctorRegistrationModel model;
    private ManagerMenuPage menuPage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuPage = ManagerMenuPage.getManagerMenuPage();
        doctorRegistration = DoctorRegistration.getDoctorRegistration();
        try {
            model = DoctorRegistrationModel.getModel();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        Actions();

    }

    private void Actions() {
        addButton.setOnAction(actionEvent -> {
            try {
                addDoctor();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
        exitButton.setOnMouseClicked(mouseEvent -> LoginPage.getWindow().setScene(menuPage.getScene()));
        idTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equals("TAB")){
                emailErrorLabel.setVisible(false);
                if(!checkId()){
                    idErrorLabel.setText("Please enter valid id.");
                    idErrorLabel.setVisible(true);
                }
                else {
                    idErrorLabel.setVisible(false);
                }
            }
        });
        emailTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equals("TAB")){
                passwordErrorLabel.setVisible(false);
                if (!checkEmail()){
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
                if (!checkPassword())
                    passwordErrorLabel.setVisible(true);
                else
                    passwordErrorLabel.setVisible(false);
            }
        });
        nameTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equals("TAB")){
                if (!checkName())
                    nameErrorLabel.setVisible(true);
                else
                    nameErrorLabel.setVisible(false);
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
        mainPane.setOnMouseMoved(mouseEvent -> selectedItem = doctorRegistration.getScene().focusOwnerProperty().get());
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
            if (!checkEmail()) {
                emailErrorLabel.setText("Please enter valid email address.");
                emailErrorLabel.setVisible(true);
            }
            else
                emailErrorLabel.setVisible(false);
        }
        else if (node == passwordTextField) {
            if (!checkPassword())
                passwordErrorLabel.setVisible(true);
            else
                passwordErrorLabel.setVisible(false);
        }
        else if (node == nameTextField) {
            if (!checkName())
                nameErrorLabel.setVisible(true);
            else
                nameErrorLabel.setVisible(false);
        }
    }

    private boolean checkName() {
        RegisterPage1Controller controller = new RegisterPage1Controller();
        return controller.checkName(nameTextField, nameErrorLabel);
    }

    private boolean checkId() {
        if (idTextField.getText().equals(""))
            return true;
        if(idTextField.getText().length()!=9)
            return false;
        return idTextField.getText().chars().allMatch(Character::isDigit);
    }

    private boolean checkEmail() {
        RegisterPage1Controller controller = new RegisterPage1Controller();
        return controller.checkEmail(emailTextField);
    }

    private boolean checkPassword() {
        RegisterPage1Controller controller = new RegisterPage1Controller();
        return controller.checkPassword(passwordTextField);
    }

    private void addDoctor() throws SQLException, IOException {
        String id = idTextField.getText(), email = emailTextField.getText(), password = passwordTextField.getText();
        String name = nameTextField.getText();

        if(!checkId()){
            idErrorLabel.setText("Please enter valid id.");
            idErrorLabel.setVisible(true);
            idTextField.requestFocus();
            new FadeIn(idTextField).play();
            return;
        }
        if (id.equals("")){
            idTextField.requestFocus();
            new FadeIn(idTextField).play();
            return;
        }
        if(!checkEmail()){
            emailErrorLabel.setText("Please enter valid email address.");
            emailErrorLabel.setVisible(true);
            emailTextField.requestFocus();
            new FadeIn(emailTextField).play();
            return;
        }
        if (email.equals("")){
            emailTextField.requestFocus();
            new FadeIn(emailTextField).play();
            return;
        }
        if(!checkPassword()){
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
        if (!checkName()){
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
        if (model.idExists(id)){
            idErrorLabel.setVisible(true);
            idErrorLabel.setText("id already exists in the system");
            idTextField.requestFocus();
            new FadeIn(idTextField).play();
            return;
        }
        if(model.emailExists(email)){
            emailErrorLabel.setVisible(true);
            emailErrorLabel.setText("email already exists in the system");
            emailTextField.requestFocus();
            new FadeIn(emailTextField).play();
            return;
        }

        model.addDoctor(id, email, password, name);
        menuPage.createScene();
        LoginPage.getWindow().setScene(menuPage.getScene());
    }
}
