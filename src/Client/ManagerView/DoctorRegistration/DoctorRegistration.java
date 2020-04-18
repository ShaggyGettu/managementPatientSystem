package Client.ManagerView.DoctorRegistration;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class DoctorRegistration {
    private Scene scene;
    private static DoctorRegistration doctorRegistration;

    public static DoctorRegistration getDoctorRegistration(){
        if (doctorRegistration == null)
            doctorRegistration = new DoctorRegistration();
        return doctorRegistration;
    }

    public void createScene() throws IOException {
        Parent root = FXMLLoader.load(DoctorRegistration.class.getResource("DoctorRegistrationView.fxml"));
        scene = new Scene(root);
    }

    public Scene getScene() {
        return scene;
    }
}
