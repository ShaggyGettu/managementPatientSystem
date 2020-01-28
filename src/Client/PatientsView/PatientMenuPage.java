package Client.PatientsView;

import Client.Login.LoginModel;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static javafx.fxml.FXMLLoader.load;

public class PatientMenuPage {
    private Parent parent;
    private Scene scene;
    private static PatientMenuPage patientMenuPage;
    private String id;
    private ResultSet resultSet;

    private PatientMenuPage(){

    }

    public static PatientMenuPage getInstance(){
        if (patientMenuPage == null)
            patientMenuPage = new PatientMenuPage();
        return patientMenuPage;
    }

    public void createScene(ResultSet rs) throws IOException {
        resultSet = rs;
        parent = load(getClass().getResource("PatientMenu/PatientMenuPageView.fxml"));
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

    public ResultSet getResultSet() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        resultSet = LoginModel.getLoginModel().login(resultSet.getString(2), resultSet.getString(3), "Patient");
        return resultSet;
    }
}
