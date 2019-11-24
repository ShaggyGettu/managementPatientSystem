package Client.DoctorsView.DoctorsMenu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.SQLException;

public class DoctorsMenuPage {
    private Parent root;
    private  Scene scene;
    private static DoctorsMenuPage doctorsMenuPage;
    private String id;

    private DoctorsMenuPage() {
    }
    public static DoctorsMenuPage getInstance() {
        if(doctorsMenuPage==null) {
            doctorsMenuPage = new DoctorsMenuPage();
        }
        return doctorsMenuPage;
    }

    public void createScreen() throws IOException{
        root = FXMLLoader.load(DoctorsMenuPage.class.getResource("DoctorMenuPageView.fxml"));
        scene = new Scene(root);
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
