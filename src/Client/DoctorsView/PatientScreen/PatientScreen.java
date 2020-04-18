package Client.DoctorsView.PatientScreen;

import Client.DataTypes.Patient;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.SQLException;

public class PatientScreen {
    private static PatientScreen patientScreen;
    private Scene scene;
    private Patient patients;
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
        Parent parent = FXMLLoader.load(getClass().getResource("PatientScreenView.fxml"));
        scene = new Scene(parent);
    }

    public Scene getScene(){
        return scene;
    }

    public Patient getPatients() {
        return patients;
    }

    public void setPatients(Patient patients) throws SQLException {
        this.patients = patients;
        String details[] = patientScreenModel.findPatient(patients.getId());
        patients.setBackground(details[0]);
        String tests[] = details[1].split(" ");
        patients.setTests(tests);
    }
}
