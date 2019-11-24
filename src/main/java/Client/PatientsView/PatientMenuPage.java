package Client.PatientsView;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class PatientMenuPage {
    private Parent parent;
    private Scene scene;
    private static PatientMenuPage patientMenuPage;
    private String id;

    private PatientMenuPage(){

    }

    public static PatientMenuPage getInstance(){
        if (patientMenuPage == null)
            patientMenuPage = new PatientMenuPage();
        return patientMenuPage;
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
