package Client.PatientsView.PatientMenu;

import Client.Login.LoginModel;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static javafx.fxml.FXMLLoader.load;

public class PatientMenuPage {
    private Scene scene;
    private static PatientMenuPage patientMenuPage;
    private String id;
    private int backScreen;

    private PatientMenuPage(){

    }

    public static PatientMenuPage getInstance(){
        if (patientMenuPage == null)
            patientMenuPage = new PatientMenuPage();
        return patientMenuPage;
    }

    public void createScreen(String id) throws IOException {
        this.id = id;
        Parent parent = load(getClass().getResource("PatientMenuPageView.fxml"));
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

    public ResultSet getResultSet() throws SQLException, ClassNotFoundException {
        return LoginModel.getLoginModel().getUser(id);
    }

    int getBackScreen() {
        return backScreen;
    }

    public void setBackScreen(int backScreen) {
        this.backScreen = backScreen;
    }
}
