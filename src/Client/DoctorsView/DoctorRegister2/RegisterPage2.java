package Client.DoctorsView.DoctorRegister2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class RegisterPage2 {
    private static RegisterPage2 registerPage2;
    private Scene scene;
    private String fields[];

    private RegisterPage2(){

    }

    public static RegisterPage2 getInstance(){
        if(registerPage2 == null)
            registerPage2 = new RegisterPage2();
        return registerPage2;
    }

    public void createScreen() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("RegisterPage2View.fxml"));
        scene = new Scene(parent);
    }

    public Scene getScene(){
        return scene;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public String[] getFields(){
        return fields;
    }
}
