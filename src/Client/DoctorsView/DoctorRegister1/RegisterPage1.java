package Client.DoctorsView.DoctorRegister1;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class RegisterPage1 {

    private static RegisterPage1 registerPage1;
    private Scene scene;

    private String[] fields;

    private RegisterPage1(){
        fields = new String[5];
    }

    public static RegisterPage1 getInstance() {
        if(registerPage1 == null)
            registerPage1 = new RegisterPage1();
        return registerPage1;
    }

    public void createScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RegisterPage1View.fxml"));
        scene = new Scene(root);
    }

    public Scene getScene() {
        return scene;
    }

    public String[] getFields() {
        return fields;
    }

    public void emptyFields(){
        fields[0] = "";
        fields[1] = "";
        fields[2] = "";
        fields[3] = "";
        fields[4] = "";
    }
}
