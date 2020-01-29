package Client.PatientsView.PatientMenu;

import Client.DataTypes.Point;
import Client.DataTypes.Temperature;
import Client.DataTypes.Value;
import Client.DoctorsView.DoctorsMenu.DoctorMenuPageController;
import Client.Login.LoginPage;
import Client.PatientsView.PatientMenuPage;
import Server.CreateUserData;
import com.sun.javafx.charts.Legend;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private XYChart.Series<String, Number> series;
    private NumberAxis axisY = null;
    private CategoryAxis axisX;
    private LineChart<String, Number> lineChart = null;
    private Stage dataStage;
    private ComboBox<Integer> years;
    private GridPane gridPane;
    private ComboBox<String> months;
    private ComboBox<String> tests;
    private ComboBox<String> days;
    private Button showButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataStage= new Stage();
        gridPane = new GridPane();
        years = new ComboBox<>();
        tests = new ComboBox<>();
        months = new ComboBox<>();
        days = new ComboBox<>();
        showButton = new Button("Show");
        series = new XYChart.Series<>();
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
        initializeShowData();
    }

    private void initializeShowData() {
        dataStage.setTitle("Show data");
        dataStage.setResizable(false);
        dataStage.initModality(Modality.WINDOW_MODAL);
        dataStage.setWidth(1000);
        dataStage.setHeight(400);
        gridPane.setPrefWidth(1000);
        gridPane.setPrefHeight(400);
        ObservableList<String> strings = FXCollections.observableArrayList();
        strings.add("Temperature");
        strings.add("Blood Pressure");
        strings.add("Glucose");
        tests.setItems(strings);
        strings = FXCollections.observableArrayList();
        strings.add("All month average");
        for (int i = 1;i<=31;i++)
            strings.add(String.valueOf(i));
        days.setItems(strings);
        strings = FXCollections.observableArrayList();
        for (int i = 0;i<12;i++){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, i);
            strings.add(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US));
        }
        months.setItems(strings);
        ObservableList<Integer> list = FXCollections.observableArrayList();
        int year = 0;
        try {
            year = patientMenuPageModel.getMinYear(patientMenuPage.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (;year<=Calendar.getInstance().get(Calendar.YEAR);year++)
            list.add(year);
        years.setItems(list);
        gridPane.add(months, 1, 0);
        gridPane.add(years, 2, 0);
        gridPane.add(tests, 3, 0);
        gridPane.add(days, 4, 0);
        gridPane.add(showButton, 5, 0);
        dataStage.setScene(new Scene(gridPane));
    }

    private void loadPatient() throws SQLException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        ResultSet resultSet = patientMenuPage.getResultSet();
        String details[] = new String[3];
        details[0] = resultSet.getString(2);
        details[1] = resultSet.getString(5);
        details[2] = resultSet.getString(8);
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
        //ResultSet resultSet = patientMenuPageModel.getData(patientMenuPage.getId());
        String temperature, bloodPressure, glucose;
        if (temperaturePane.isVisible() && Integer.parseInt(resultSet.getString(12)) != 0) {
            temperature = resultSet.getString(11);//AVG
            averageTemperatureLabel.setText(temperature);
            temperature = resultSet.getString(9);
            minTemperatureValueLabel.setText(temperature.split(" ")[0]);
            minTemperatureDateLabel.setText(temperature.split(" ")[1]);
            minTemperatureTimeLabel.setText(temperature.split(" ")[2]);
            temperature = resultSet.getString(10);
            maxTemperatureValueLabel.setText(temperature.split(" ")[0]);
            maxTemperatureDateLabel.setText(temperature.split(" ")[1]);
            maxTemperatureTimeLabel.setText(temperature.split(" ")[2]);
            temperature = resultSet.getString(12);
            amountTemperatureLabel.setText(temperature);
        }
        if (bloodPressurePane.isVisible() && Integer.parseInt(resultSet.getString(17)) != 0) {
            bloodPressure = resultSet.getString(18);
            averageBloodPressureLabel.setText(bloodPressure);
            bloodPressure = resultSet.getString(15);
            minBloodPressureValueLabel.setText(bloodPressure.split(" ")[0]);
            minBloodPressureDateLabel.setText(bloodPressure.split(" ")[1]);
            minBloodPressureTimeLabel.setText(bloodPressure.split(" ")[2]);
            bloodPressure = resultSet.getString(16);
            maxBloodPressureValueLabel.setText(bloodPressure.split(" ")[0]);
            maxBloodPressureDateLabel.setText(bloodPressure.split(" ")[1]);
            maxBloodPressureTimeLabel.setText(bloodPressure.split(" ")[2]);
            bloodPressure = resultSet.getString(17);
            amountBloodPressureLabel.setText(bloodPressure);
        }
        if (glucosePane.isVisible() && Integer.parseInt(resultSet.getString(22)) != 0) {
            glucose = resultSet.getString(23);
            averageGlucoseLabel.setText(glucose);
            glucose = resultSet.getString(20);
            minGlucoseValueLabel.setText(glucose.split(" ")[0]);
            minGlucoseDateLabel.setText(glucose.split(" ")[1]);
            minGlucoseTimeLabel.setText(glucose.split(" ")[2]);
            glucose = resultSet.getString(21);
            maxGlucoseValueLabel.setText(glucose.split(" ")[0]);
            maxGlucoseDateLabel.setText(glucose.split(" ")[1]);
            maxGlucoseTimeLabel.setText(glucose.split(" ")[2]);
            glucose = resultSet.getString(22);
            amountGlucoseLabel.setText(glucose);
        }
    }

    private void Actions() {
        DoctorMenuPageController doctorMenuPageController = new DoctorMenuPageController();
        doctorMenuPageController.BackAction(backButton);
        refreshView.setOnMouseClicked(mouseEvent -> {
            try {
                loadPatient();
            } catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
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
        temperatureButton.setOnMouseClicked(mouseEvent -> dataStage.show());
        showButton.setOnMouseClicked(mouseEvent ->{
            series.getData().clear();
            gridPane.getChildren().remove(lineChart);
            series = new XYChart.Series<>();
            axisX = new CategoryAxis();
            axisY = new NumberAxis();
            lineChart = new LineChart<>(axisX, axisY);
            axisX.setTickLabelRotation(90);
            lineChart.setLegendVisible(false);
            lineChart.setVisible(false);
            lineChart.setPadding(new Insets(30));
            gridPane.add(lineChart,0, 5);
            int currentYear = years.getSelectionModel().getSelectedItem();
            String month = months.getSelectionModel().getSelectedItem();
            String test = tests.getSelectionModel().getSelectedItem();
            String id = patientMenuPage.getId();
            String day = days.getSelectionModel().getSelectedItem();
            int intDay = 0;
            if (!day.equals("All month average"))
                intDay = Integer.parseInt(day);
            String result = null;
            axisX.setLabel("Date");
            axisY.setLabel(test + " value");
            try {
                result = showGraph(currentYear, test, month);
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
            ArrayList<Value> days = null;
            try {
                if (intDay == 0)
                    avgDay = BuildAverageDayInMonth(points, id, month,currentYear);
                else
                    days = BuildDayInMonth(id, points, month, currentYear, intDay);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            lineChart.setPrefWidth(1000);
            lineChart.setTitle(day + " " + month + " " + currentYear + " " + test);
            if (intDay == 0) {
                lineChart.setTitle("Average " + test + " Month: " + month);
                assert avgDay != null;
                System.out.println(avgDay.size());
                for (Point point : avgDay)
                    series.getData().add(new XYChart.Data(String.valueOf(point.getIx()), point.getDy()));
            }
            else {
                    System.out.println(days.size());
                for (Value value:days)
                    series.getData().add(new XYChart.Data(value.getDate(), value.getDoubleNumber()));
            }
            lineChart.getData().addAll(series);
            lineChart.setVisible(true);
        });
    }

    private ArrayList<Value> BuildDayInMonth(String id, ArrayList<Point> points, String monthName, int year, int day) throws SQLException {
        Calendar calendar = Calendar.getInstance();
        int month = Month.valueOf(monthName.toUpperCase()).getValue() - 1;
        calendar.set(year, month, day,0,0,0);
        int period = patientMenuPageModel.getPeriodTime(id, calendar);
        ArrayList<Value> values = new ArrayList<>();
        int place = 0;
        int number;
        String string = "";
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(year, month, day,0,0,0);
        while (place < points.size()) {
            number = points.get(place).getIx()*period;
            calendar1.add(Calendar.MINUTE, number);
            if (calendar.get(Calendar.DAY_OF_MONTH) == calendar1.get(Calendar.DAY_OF_MONTH)){
                if (calendar1.get(Calendar.HOUR_OF_DAY) < 10)
                    string = "0" + String.valueOf(calendar1.get(Calendar.HOUR_OF_DAY));
                else
                    string = String.valueOf(calendar1.get(Calendar.HOUR_OF_DAY));
                string += ":";
                if (calendar1.get(Calendar.MINUTE) < 10)
                    string += ("0" + calendar.get(Calendar.MINUTE));
                else
                    string += calendar1.get(Calendar.MINUTE);
                values.add(new Value(String.valueOf(string), points.get(place).getDy()));
            }
            place++;
            calendar1.set(year, month, day, 0, 0, 0);
        }
        return values;
    }

    private ArrayList<Point> BuildAverageDayInMonth(ArrayList<Point> points, String id, String monthName, Integer year) throws SQLException {
        Calendar calendar = Calendar.getInstance();
        int month = Month.valueOf(monthName.toUpperCase()).getValue() - 1;
        calendar.set(year, month,1);
        int period = patientMenuPageModel.getPeriodTime(id, calendar);
        ArrayList<Point> avgDay = new ArrayList<>();
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
            avg = new BigDecimal(avg/amount).setScale(1, RoundingMode.HALF_UP).doubleValue();
            //System.exit(10);
            if (avg !=0.0)
                avgDay.add(new Point(i + 1, avg));
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
