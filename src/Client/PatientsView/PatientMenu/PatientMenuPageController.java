package Client.PatientsView.PatientMenu;

import Client.DataTypes.Point;
import Client.DataTypes.Temperature;
import Client.DataTypes.Value;
import Client.DoctorsView.DoctorsMenu.DoctorMenuPageController;
import Client.Login.LoginPage;
import Client.PatientsView.PatientMenuPage;
import Server.CreateUserData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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
    private AnchorPane showDataPage;
    private ComboBox<String> months;
    private ComboBox<String> tests;
    private ComboBox<String> days;
    private Button showButton;
    private Label label;
    private AnchorPane graphPane;
    private AnchorPane settingsPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        patientMenuPage = PatientMenuPage.getInstance();
        try {
            patientMenuPageModel = PatientMenuPageModel.getInstance();
            loadPatient();
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Actions();
        temperaturePane.setVisible(false);
        bloodPressurePane.setVisible(false);
        glucosePane.setVisible(false);
    }

    private void initializeShowData() {
        axisX = new CategoryAxis();
        axisY = new NumberAxis();
        lineChart = new LineChart<>(axisX, axisY);
        dataStage= new Stage();
        dataStage.setTitle("Show data");
        dataStage.setResizable(false);
        dataStage.initModality(Modality.WINDOW_MODAL);
        dataStage.setWidth(800);
        dataStage.setHeight(600);
        showDataPage = new AnchorPane();
        showDataPage.setPrefWidth(800);
        showDataPage.setLayoutX(0);
        showDataPage.setLayoutY(0);
        graphPane = new AnchorPane();
        graphPane.setLayoutY(0);
        graphPane.setLayoutX(0);
        graphPane.setPrefWidth(800);
        graphPane.getChildren().addAll(lineChart);
        showDataPage.getChildren().addAll(graphPane);
        settingsPane = new AnchorPane();
        settingsPane.setLayoutY(500);
        settingsPane.setLayoutX(50);
        settingsPane.setPrefHeight(50);
        settingsPane.setPrefWidth(750);
        years = new ComboBox<>();
        tests = new ComboBox<>();
        months = new ComboBox<>();
        days = new ComboBox<>();
        showButton = new Button("Show");
        label = new Label("Details missing");
        series = new XYChart.Series<>();
        months.setPrefSize(150,20);
        months.setLayoutX(0);
        years.setPrefSize(100, 20);
        years.setLayoutX(150);
        tests.setPrefSize(150, 20);
        tests.setLayoutX(250);
        days.setPrefSize(150, 20);
        days.setLayoutX(400);
        showButton.setPrefSize(100, 20);
        showButton.setLayoutX(550);
        label.setPrefSize(70, 20);
        label.setLayoutX(650);
        ObservableList<String> strings = FXCollections.observableArrayList();
        strings.add("Temperature");
        strings.add("Blood Pressure");
        strings.add("Glucose");
        tests.setItems(strings);
        strings = FXCollections.observableArrayList();
        strings.add("All month average");
        for (int i = 1;i<=31;i++) {
            strings.add(String.valueOf(i));
        }
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
        settingsPane.getChildren().addAll(months, years, tests, days, showButton, label);
        showDataPage.getChildren().addAll(settingsPane);
        label.setVisible(false);
        dataStage.setScene(new Scene(showDataPage));
        showDataPage.setVisible(true);
        axisX.setVisible(false);
        axisY.setVisible(false);
        lineChart.setVisible(false);
        years.setVisible(true);
        months.setVisible(true);
        days.setVisible(true);
        tests.setVisible(true);
        axisX.setTickLabelRotation(90);
        dataStage.show();
        showGraphActions();
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
        temperatureButton.setOnMouseClicked(mouseEvent -> {
            initializeShowData();
        });
    }

    private void showGraphActions(){
        dataStage.setOnCloseRequest(windowEvent -> {
            dataStage = null;
            showDataPage = null;
            axisY = null;
            axisX = null;
            lineChart = null;
            series = null;
        });
        showButton.setOnMouseClicked(mouseEvent ->{
            Object object = years.getSelectionModel().getSelectedItem();
            if (object == null){
                label.setVisible(true);
                return;
            }
            object = months.getSelectionModel().getSelectedItem();
            if (object == null){
                label.setVisible(true);
                return;
            }
            object = tests.getSelectionModel().getSelectedItem();
            if (object == null){
                label.setVisible(true);
                return;
            }
            object = days.getSelectionModel().getSelectedItem();
            if (object == null){
                label.setVisible(true);
                return;
            }
            label.setVisible(false);
            int currentYear = years.getSelectionModel().getSelectedItem();;
            String month = months.getSelectionModel().getSelectedItem();
            String test = tests.getSelectionModel().getSelectedItem();
            String id = patientMenuPage.getId();
            String day = days.getSelectionModel().getSelectedItem();
            int intDay = 0;
            if (test.equals("Temperature")){
                axisY = new NumberAxis(35,40,0.1);
            }
            else if (test.equals("Glucose"))
                axisY = new NumberAxis(60,200, 10);
            else if (test.equals("Blood Pressure"))
                axisY = new NumberAxis(0, 221, 5);
            graphPane.getChildren().clear();
            axisX = new CategoryAxis();
            series = null;
            lineChart = new LineChart<>(axisX, axisY);
            graphPane.getChildren().addAll(lineChart);
            series = new XYChart.Series<>();
            lineChart.getData().addAll(series);
            lineChart.setLegendVisible(false);
            lineChart.setPadding(new Insets(30));
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
            if (test.equals("Blood Pressure")){
                ArrayList<Value> points = new ArrayList<>();
                ArrayList<String> values = new ArrayList<>(Arrays.asList(result.split(",")));
                for (String string:values) {
                    String value[] = string.split(" ");
                    int d = Integer.valueOf(value[1]);
                    int large = Integer.parseInt(value[0].split("|")[0]);
                    int small = Integer.parseInt(value[0].split("|")[1]);
                    points.add(new Value(new Point(large, small), d));
                    ArrayList<Point> avgDay  = null;
                    ArrayList<Value> days = null;

                }
            }
            ArrayList<Point> points = new ArrayList<>();
            assert result != null;
            ArrayList<String> values = new ArrayList<>(Arrays.asList(result.split(",")));
            for (String string:values){
                String value[] = string.split(" ");
                int d = Integer.valueOf(value[1]);
                if (test.equals("Temperature")) {
                    points.add(new Point(Integer.valueOf(value[0]), Temperature.getCelsiusNumber(d)));
                }
                else if (test.equals("Glucose"))
                    points.add(new Point(Integer.valueOf(value[0]), d));
            }
            points.sort(Comparator.comparing(Point::getIx));
            ArrayList<Point> avgDay  = null;
            ArrayList<Value> days = null;
            try {
                if (intDay == 0)
                    avgDay = BuildAverageDayInMonth(points, id, month,currentYear,test);
                else
                    days = BuildDayInMonth(id, points, month, currentYear, intDay,test);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            lineChart.setTitle(day + " " + month + " " + currentYear + " " + test);
            if (intDay == 0) {
                lineChart.setTitle("Average " + test + " Month: " + month);
                assert avgDay != null;
                System.out.println(avgDay.size());
                for (Point point : avgDay) {
                    if (test.equals("Temperature"))
                        series.getData().add(new XYChart.Data(String.valueOf(point.getIx()), point.getDy()));
                    else if (test.equals("Glucose")) {
                        series.getData().add(new XYChart.Data(String.valueOf(point.getIx()), point.getIy()));
                        System.out.println(point);
                    }
                }
            }
            else {
                assert days != null;
                System.out.println(days.size());
                for (Value value:days) {
                    if (test.equals("Temperature"))
                        series.getData().add(new XYChart.Data(value.getDate(), value.getDoubleNumber()));
                    else if (test.equals("Glucose"))
                        series.getData().add(new XYChart.Data<>(value.getDate(), value.getIntNumber()));
                }
            }
            showDataPage.setVisible(true);
            axisX.setVisible(true);
            axisY.setVisible(true);
            lineChart.setVisible(true);
            years.setVisible(true);
            months.setVisible(true);
            tests.setVisible(true);
            lineChart.setVisible(true);
            for (XYChart.Data<String, Number> data:series.getData()){
                Tooltip.install(data.getNode(), new Tooltip(data.getXValue() + "," + data.getYValue()));
            }
        });
    }

    private ArrayList<Value> BuildDayInMonth(String id, ArrayList<Point> points, String monthName, int year, int day, String test) throws SQLException {
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
                if (test.equals("Temperature"))
                    values.add(new Value(String.valueOf(string), points.get(place).getDy()));
                else if (test.equals("Glucose")) {
                    int n = points.get(place).getIy();
                    values.add(new Value(String.valueOf(string), n));
                }
            }
            place++;
            calendar1.set(year, month, day, 0, 0, 0);
        }
        return values;
    }

    private ArrayList<Point> BuildAverageDayInMonth(ArrayList<Point> points, String id, String monthName, Integer year, String test) throws SQLException {
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
                if (test.equals("Temperature"))
                    avg += points.get(place).getDy();
                else if (test.equals("Glucose"))
                    avg += points.get(place).getIy();
                place++;
                if (place<points.size())
                    number = points.get(place).getIx();
                else
                    break;
            }
            avg = new BigDecimal(avg/amount).setScale(1, RoundingMode.HALF_UP).doubleValue();
            //System.exit(10);
            if (avg !=0.0) {
                if (test.equals("Temperature"))
                    avgDay.add(new Point(i + 1, avg));
                else if (test.equals("Glucose"))
                    avgDay.add(new Point(i + 1, (int) avg));
            }
            else{
                if (test.equals("Temperature"))
                    avgDay.add(new Point(i + 1, avg));
                else if (test.equals("Glucose"))
                    avgDay.add(new Point(i + 1, (int) avg));
            }
            avg = 0.0;
            amount = 0;
        }
        avgDay.sort(Comparator.comparing(Point::getIx));
        return avgDay;
    }

    private String showGraph(int year, String test, String month) throws SQLException {
        return patientMenuPageModel.getPatientData(patientMenuPage.getId(), year, test, month);
    }
}
