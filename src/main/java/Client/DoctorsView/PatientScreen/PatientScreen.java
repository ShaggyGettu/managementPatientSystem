package Client.DoctorsView.PatientScreen;

import Client.DataTypes.Patient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientScreen {
    private static PatientScreen patientScreen;
    private Parent parent;
    private Scene scene;
    private Patient patient;
    private PatientScreenModel patientScreenModel;

    private PatientScreen() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        patientScreenModel = PatientScreenModel.getInstance();
    }

    public static PatientScreen getInstance() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        if (patientScreen == null)
            patientScreen = new PatientScreen();
        return patientScreen;
    }

    public void createScreen() throws IOException {
        parent = FXMLLoader.load(getClass().getResource("PatientScreenView.fxml"));
        scene = new Scene(parent);
    }

    public Scene getScene(){
        return scene;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) throws SQLException {
        this.patient = patient;
        String details[] = patientScreenModel.findPatient(patient.getId());
        patient.setBackground(details[0]);
        String tests[] = details[1].split(" ");
        patient.setTests(tests);
    }
}
