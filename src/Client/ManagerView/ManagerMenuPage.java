package Client.ManagerView;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class ManagerMenuPage {
    private Scene scene;
    private static ManagerMenuPage managerMenuPage;
    private String id;
    private String selectedDoctorId;

    public static ManagerMenuPage getManagerMenuPage() {
        if (managerMenuPage == null)
            managerMenuPage = new ManagerMenuPage();
        return managerMenuPage;
    }

    public static boolean isExist() {
        return managerMenuPage != null;
    }

    public void createScene() throws IOException {
        Parent root = FXMLLoader.load(ManagerMenuPage.class.getResource("ManagerMenuPageView.fxml"));
        scene = new Scene(root);
    }

    public Scene getScene() {
        return scene;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelectedDoctor() {
        return selectedDoctorId;
    }

    void setSelectedDoctor(String result) {
        selectedDoctorId = result;
    }
}