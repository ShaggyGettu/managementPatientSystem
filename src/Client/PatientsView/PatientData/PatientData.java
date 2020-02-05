package Client.PatientsView.PatientData;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class PatientData {
    private String id;
    private Parent parent;
    private Scene scene;
    private static PatientData patientData;

    private PatientData(){
    }

    public static PatientData getInstance(){
        if (patientData == null)
            patientData = new PatientData();
        return patientData;
    }

    public void createScreen() throws IOException {
        parent = FXMLLoader.load(PatientData.class.getResource("PatientDataView.fxml"));
        scene = new Scene(parent);
    }

    public Scene getScene() {
        return scene;
    }

    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
}
