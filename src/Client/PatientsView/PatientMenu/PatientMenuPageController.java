package Client.PatientsView.PatientMenu;

import Client.DataTypes.Point;
import Client.DataTypes.Temperature;
import Client.DoctorsView.DoctorsMenu.DoctorMenuPageController;
import Client.Login.LoginPage;
import Client.PatientsView.PatientMenuPage;
import Server.CreateUserData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.*;

public class PatientMenuPageController implements Initializable {
    @FXML
    Label nameLabel, idLabel, emailLabel, phoneLabel;
    @FXML
    Label temperatureLabel, bloodPressureLabel, glucoseLabel;
    @FXML
    Label maxTemperatureValueLabel, maxBloodPressureValueLabel, maxGlucoseValueLabel;
    @FXML
    Label minTemperatureValueLabel, minBloodPressureValueLabel, minGlucoseValueLabel;
    @FXML
    Label maxTemperatureDateLabel, maxTemperatureTimeLabel;
    @FXML
    Label maxBloodPressureDateLabel, maxBloodPressureTimeLabel;
    @FXML
    Label maxGlucoseDateLabel, maxGlucoseTimeLabel;
    @FXML
    Label minTemperatureDateLabel, minTemperatureTimeLabel;
    @FXML
    Label minBloodPressureDateLabel, minBloodPressureTimeLabel;
    @FXML
    Label minGlucoseDateLabel, minGlucoseTimeLabel;
    @FXML
    Label averageTemperatureLabel, averageBloodPressureLabel, averageGlucoseLabel;
    @FXML
    Label amountTemperatureLabel, amountBloodPressureLabel, amountGlucoseLabel;
    @FXML
    AnchorPane testsPane, temperaturePane, bloodPressurePane, glucosePane;
    @FXML
    ImageView refreshView;
    @FXML
    Button backButton, temperatureButton, bloodPressureButton, glucoseButton;

    private PatientMenuPage patientMenuPage;
    private PatientMenuPageModel patientMenuPageModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Actions();
        temperaturePane.setVisible(false);
        bloodPressurePane.setVisible(false);
        glucosePane.setVisible(false);
        patientMenuPage = PatientMenuPage.getInstance();
        try {
            patientMenuPageModel = PatientMenuPageModel.getInstance();
            loadPatient();
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void loadPatient() throws SQLException {
        String details[] = patientMenuPageModel.getPatient(patientMenuPage.getId());
        idLabel.setText(patientMenuPage.getId());
        emailLabel.setText(details[0]);
        phoneLabel.setText(details[1]);
        String tests[] = details[2].split(" ");
        if (Arrays.asList(tests).contains("Temperature")) {
            temperaturePane.setVisible(true);
            patientMenuPageModel.setTemperature(true);
        }
        if (Arrays.asList(tests).contains("BloodPressure")) {
            bloodPressurePane.setVisible(true);
            patientMenuPageModel.setBloodPressure();
        }
        if (Arrays.asList(tests).contains("Glucose")) {
            glucosePane.setVisible(true);
            patientMenuPageModel.setGlucose(true);
        }
        ResultSet resultSet = patientMenuPageModel.getData(patientMenuPage.getId());
        String temperature, bloodPressure, glucose;
        if (temperaturePane.isVisible() && Integer.parseInt(resultSet.getString(4)) != 0) {
            temperature = resultSet.getString(1);//AVG
            averageTemperatureLabel.setText(temperature);
            temperature = resultSet.getString(2);
            minTemperatureValueLabel.setText(temperature.split(" ")[0]);
            minTemperatureDateLabel.setText(temperature.split(" ")[1]);
            minTemperatureTimeLabel.setText(temperature.split(" ")[2]);
            temperature = resultSet.getString(3);
            maxTemperatureValueLabel.setText(temperature.split(" ")[0]);
            maxTemperatureDateLabel.setText(temperature.split(" ")[1]);
            maxTemperatureTimeLabel.setText(temperature.split(" ")[2]);
            temperature = resultSet.getString(4);
            amountTemperatureLabel.setText(temperature);
        }
        if (bloodPressurePane.isVisible() && Integer.parseInt(resultSet.getString(8)) != 0) {
            bloodPressure = resultSet.getString(5);
            averageBloodPressureLabel.setText(bloodPressure);
            bloodPressure = resultSet.getString(6);
            minBloodPressureValueLabel.setText(bloodPressure.split(" ")[0]);
            minBloodPressureDateLabel.setText(bloodPressure.split(" ")[1]);
            minBloodPressureTimeLabel.setText(bloodPressure.split(" ")[2]);
            bloodPressure = resultSet.getString(7);
            maxBloodPressureValueLabel.setText(bloodPressure.split(" ")[0]);
            maxBloodPressureDateLabel.setText(bloodPressure.split(" ")[1]);
            maxBloodPressureTimeLabel.setText(bloodPressure.split(" ")[2]);
            bloodPressure = resultSet.getString(8);
            amountBloodPressureLabel.setText(bloodPressure);
        }
        if (glucosePane.isVisible() && Integer.parseInt(resultSet.getString(12)) != 0) {
            glucose = resultSet.getString(9);
            averageGlucoseLabel.setText(glucose);
            glucose = resultSet.getString(10);
            minGlucoseValueLabel.setText(glucose.split(" ")[0]);
            minGlucoseDateLabel.setText(glucose.split(" ")[1]);
            minGlucoseTimeLabel.setText(glucose.split(" ")[2]);
            glucose = resultSet.getString(11);
            maxGlucoseValueLabel.setText(glucose.split(" ")[0]);
            maxGlucoseDateLabel.setText(glucose.split(" ")[1]);
            maxGlucoseTimeLabel.setText(glucose.split(" ")[2]);
            glucose = resultSet.getString(12);
            amountGlucoseLabel.setText(glucose);
        }
    }

    private void Actions() {
        DoctorMenuPageController doctorMenuPageController = new DoctorMenuPageController();
        doctorMenuPageController.BackAction(backButton);
        refreshView.setOnMouseClicked(mouseEvent -> {
            try {
                loadPatient();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        backButton.setOnMouseClicked(mouseEvent -> {
            CreateUserData.getInstance().stop();
            LoginPage loginPage = new LoginPage();
            try {
                loginPage.start(LoginPage.getWindow());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        temperatureButton.setOnMouseClicked(mouseEvent ->{
            Dialog dialog = new Dialog();
            GridPane gridPane = new GridPane();
            dialog.initStyle(StageStyle.UTILITY);
            ComboBox<Integer> years = new ComboBox<>();
            ComboBox<String> tests = new ComboBox<>();
            ObservableList<String> strings = FXCollections.observableArrayList();
            strings.add("Temperature");
            strings.add("Blood Pressure");
            strings.add("Glucose");
            ComboBox<String> months = new ComboBox<>();
            ObservableList<String> strings1 = FXCollections.observableArrayList();
            for (int i = 0;i<12;i++){
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, i);
                strings1.add(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US));
            }
            months.setItems(strings1);
            tests.setItems(strings);
            int year = 0;
            try {
                year = patientMenuPageModel.getMinYear(patientMenuPage.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ObservableList<Integer> list = FXCollections.observableArrayList();
            for (;year<=Calendar.getInstance().get(Calendar.YEAR);year++)
                list.add(year);
            years.setItems(list);
            Button button = new Button("Show");
            gridPane.add(months, 0, 0);
            gridPane.add(years, 1, 0);
            gridPane.add(tests, 2, 0);
            gridPane.add(button, 3, 0);
            dialog.getDialogPane().setContent(gridPane);
            dialog.setHeight(500);
            dialog.setWidth(600);
            dialog.show();
            button.setOnMouseClicked(mouseEvent1 ->{
                String result = null;
                System.out.println(tests.getSelectionModel().getSelectedItem());
                try {
                    result = showGraph(years.getSelectionModel().getSelectedItem(), tests.getSelectionModel().getSelectedItem(), months.getSelectionModel().getSelectedItem());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ArrayList<Point> points = new ArrayList<>();
                assert result != null;
                ArrayList<String> values = new ArrayList<>(Arrays.asList(result.split(",")));
                for (String string:values){
                    String value[] = string.split(" ");
                    double d = Temperature.getCelsiusNumber(Integer.valueOf(value[1]));
                    points.add(new Point(Integer.valueOf(value[0]), d));
                }
                points.sort(Comparator.comparing(Point::getIx));
                ArrayList<Point> avgDay  = null;
                try {
                    avgDay = BuildAverageDayInMonth(points, patientMenuPage.getId(), months.getSelectionModel().getSelectedItem(), years.getSelectionModel().getSelectedItem());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                XYChart.Series series = new XYChart.Series<Number,Number>();
                NumberAxis axisX = new NumberAxis();
                NumberAxis axisY = new NumberAxis();
                axisX.setLabel("Date");
                axisY.setLabel("Temperature");
                LineChart<Number, Number> lineChart = new LineChart<>(axisX, axisY);
                lineChart.setTitle("Temperature date " + years.getSelectionModel().getSelectedItem());
                for (Point point:avgDay){
                    System.out.println(point);
                    series.getData().add(new XYChart.Data<Number, Number>(point.getIx(), point.getIy()));
                }
                lineChart.getData().add(series);
                gridPane.add(lineChart, 0,3);
            });
        });
    }

    private ArrayList<Point> BuildAverageDayInMonth(ArrayList<Point> points, String id, String monthName, Integer year) throws SQLException {
        String s[] = patientMenuPageModel.getPeriodTimeRepeat(id).split("\n");
        int period = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Month.valueOf(monthName.toUpperCase()).getValue(),1);
        Calendar calendar1 = Calendar.getInstance();
        System.out.println(Arrays.toString(points.toArray()));
        for (String string:s){
            String s1[] = string.split(" ");
            calendar1.set(Calendar.YEAR, Integer.parseInt(s1[1]));
            int month = Month.valueOf(s1[0].toUpperCase()).getValue();
            calendar1.set(Calendar.MONTH, month);
            calendar1.set(Calendar.DAY_OF_MONTH, 1);
            if (calendar.before(calendar1))
                period = Integer.valueOf(s1[2]);
            else
                break;
        }
        ArrayList<Point> avgDay = new ArrayList<>();
        calendar = Calendar.getInstance();
        int month = Month.valueOf(monthName.toUpperCase()).getValue() - 1;
        calendar.set(year, month, 1);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        double avg = 0.0;
        int place = 0;
        int amount = 0;
        for (int i = 0;i<=maxDay;i++){
            int number = points.get(place).getIx();
            while (i*24*60<=number*period&&number*period<=((i + 1)*24*60)){
                amount++;
                avg += points.get(place).getDy();
                place++;
                if (place<points.size())
                    number = points.get(place).getIx();
                else
                    break;
            }
            avg = new BigDecimal(avg).setScale(1, RoundingMode.HALF_UP).doubleValue();
            if (avg !=0.0)
                avgDay.add(new Point(i + 1, avg /amount));
            else
                avgDay.add(new Point(i + 1,avg));
            avg = 0.0;
            amount = 0;
            //System.exit(2);
        }
        avgDay.sort(Comparator.comparing(Point::getIx));
        return avgDay;
    }

    private String showGraph(int year, String test, String month) throws SQLException {
        return patientMenuPageModel.getPatientData(patientMenuPage.getId(), year, test, month);
    }
}
