package Client.Login;

import Client.DataTypes.BloodPressure;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoginPage extends Application {
    private static Stage window;

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Thread.currentThread().setPriority(10);
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("LoginPageView.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hospital Management");
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static Stage getWindow(){
        return window;
    }
}
