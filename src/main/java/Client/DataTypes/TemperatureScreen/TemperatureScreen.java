package Client.DataTypes.TemperatureScreen;

import Client.PatientsView.PatientMenuPage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class TemperatureScreen {
    private Parent parent;
    private Scene scene;
    private static TemperatureScreen temperatureScreen;
    private String id;

    private TemperatureScreen(){

    }

    public static TemperatureScreen getInstance(){
        if (temperatureScreen == null)
            temperatureScreen = new TemperatureScreen();
        return temperatureScreen;
    }

    public void createScene() throws IOException {
        parent = FXMLLoader.load(getClass().getResource("PatientMenu/PatientMenuPageView.fxml"));
        scene = new Scene(parent);
    }

    public Scene getScene(){
        return scene;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
