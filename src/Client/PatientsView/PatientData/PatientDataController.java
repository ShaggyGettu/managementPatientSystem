package Client.PatientsView.PatientData;

import Client.DataTypes.BloodPressure;
import Client.DataTypes.Point;
import Client.DataTypes.Temperature;
import Client.DataTypes.Value;
import Client.Login.LoginPage;
import Client.PatientsView.PatientMenu.PatientMenuPageModel;
import Client.PatientsView.PatientMenu.PatientMenuPage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.*;

public class PatientDataController implements Initializable {
    private XYChart.Series<String, Number> series;
    public NumberAxis axisYT, axisYB, axisYG;
    @FXML
    private CategoryAxis axisXT, axisXB, axisXG;
    @FXML
    private LineChart<String, Number> lineChartT, lineChartB, lineChartG;
    @FXML
    private ComboBox<Integer> years;
    @FXML
    private ComboBox<String> months, tests, days;
    @FXML
    private Button showButton, backButton;
    @FXML
    private Label missingLabel;
    private PatientMenuPageModel patientMenuPageModel;
    private PatientMenuPage patientMenuPage;
    private PatientData patientData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            patientMenuPageModel = PatientMenuPageModel.getInstance();
            patientData = PatientData.getInstance();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        patientMenuPage = PatientMenuPage.getInstance();
        initializeShowData();
        Actions();
    }

    private void initializeShowData() {
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
        axisXT.setTickLabelRotation(90);
        axisXB.setTickLabelRotation(90);
        axisXG.setTickLabelRotation(90);
        lineChartT.setLegendVisible(false);
        lineChartT.setPadding(new Insets(30));
        lineChartB.setLegendVisible(false);
        lineChartB.setPadding(new Insets(30));
        lineChartG.setLegendVisible(false);
        lineChartG.setPadding(new Insets(30));
        lineChartT.setVisible(false);
        lineChartB.setVisible(false);
        lineChartG.setVisible(false);
        missingLabel.setVisible(false);
        axisYT.setLabel(" Temperature value");
        axisYB.setLabel(" Blood pressure value");
        axisYB.setVisible(false);
        axisYG.setLabel(" Glucose value");
        series = new XYChart.Series<>();
        lineChartT.getData().add(series);
        lineChartB.getData().add(series);
        lineChartG.getData().add(series);
    }

    private void Actions() {
        showButton.setOnMouseClicked(mouseEvent -> {
            Object object = years.getSelectionModel().getSelectedItem();
            if (object == null) {
                missingLabel.setVisible(true);
                return;
            }
            object = months.getSelectionModel().getSelectedItem();
            if (object == null) {
                missingLabel.setVisible(true);
                return;
            }
            object = tests.getSelectionModel().getSelectedItem();
            if (object == null) {
                missingLabel.setVisible(true);
                return;
            }
            object = days.getSelectionModel().getSelectedItem();
            if (object == null) {
                missingLabel.setVisible(true);
                return;
            }
            missingLabel.setVisible(false);
            int currentYear = years.getSelectionModel().getSelectedItem();
            String month = months.getSelectionModel().getSelectedItem();
            String test = tests.getSelectionModel().getSelectedItem();
            String day = days.getSelectionModel().getSelectedItem();
            int intDay = 0;
            lineChartT.getData().clear();
            lineChartB.getData().clear();
            lineChartG.getData().clear();
            series = new XYChart.Series<>();
            if (!day.equals("All month average"))
                intDay = Integer.parseInt(day);
            String result = null;
            try {
                result = showGraph(currentYear, test, month);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ArrayList<Object> points = new ArrayList<>();
            assert result != null;
            ArrayList<String> values = new ArrayList<>(Arrays.asList(result.split(",")));
            for (String value2 : values) {
                String value[] = value2.split(" ");
                switch (test) {
                    case "Blood Pressure":
                        int large = Integer.parseInt(value[1].split("-")[0]);
                        int small = Integer.parseInt(value[1].split("-")[1]);
                        Value value1 = new Value(new Point(large, small), Integer.valueOf(value[0]));
                        points.add(value1);
                        break;
                    case "Temperature":
                        points.add(new Point(Integer.valueOf(value[0]), Temperature.getCelsiusNumber(Integer.valueOf(value[1]))));
                        break;
                    case "Glucose":
                        points.add(new Point(Integer.valueOf(value[0]), Integer.valueOf(value[1])));
                        break;
                }
            }
            ArrayList<Point> avgDay = null;
            ArrayList<Value> days = null;
            if (intDay == 0)
                avgDay = BuildAverageDayInMonth(points, month, currentYear, test);
            else
                days = BuildDayInMonth(points, month, currentYear, intDay, test);
            lineChartT.setTitle(day + " " + month + " " + currentYear + " " + test);
            lineChartB.setTitle(day + " " + month + " " + currentYear + " " + test);
            lineChartG.setTitle(day + " " + month + " " + currentYear + " " + test);
            if (intDay == 0) {
                switch (test) {
                    case "Temperature":
                        lineChartT.setTitle("Average " + test + " Month: " + month);
                        break;
                    case "Blood Pressure":
                        lineChartB.setTitle("Average " + test + " Month: " + month);
                        break;
                    case "Glucose":
                        lineChartG.setTitle("Average " + test + " Month: " + month);
                }
                assert avgDay != null;
                for (Point point : avgDay) {
                    switch (test) {
                        case "Temperature":
                            series.getData().add(new XYChart.Data<>(String.valueOf(point.getIx()), point.getDy()));
                            break;
                        case "Glucose":
                            series.getData().add(new XYChart.Data<>(String.valueOf(point.getIx()), point.getIy()));
                            break;
                        case "Blood Pressure":
                            series.getData().add(new XYChart.Data<>(String.valueOf(point.getIx()), point.getIy()));
                            break;
                    }
                }
            } else {
                assert days != null;
                System.out.println(days.size());
                for (Value value : days) {
                    switch (test) {
                        case "Temperature":
                            series.getData().add(new XYChart.Data<>(value.getDate(), value.getDoubleNumber()));
                            break;
                        case "Glucose":
                            series.getData().add(new XYChart.Data<>(value.getDate(), value.getIntNumber()));
                            break;
                        case "Blood Pressure":
                            series.getData().add(new XYChart.Data<>(value.getDate(), value.getIntNumber()));
                            break;
                    }
                }
            }
            switch (test) {
                case "Temperature":
                    lineChartT.getData().addAll(series);
                    lineChartT.setVisible(true);
                    lineChartB.setVisible(false);
                    lineChartG.setVisible(false);
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        Tooltip.install(data.getNode(), new Tooltip(data.getXValue() + "," + data.getYValue()));
                    }
                    break;
                case "Blood Pressure":
                    lineChartB.getData().addAll(series);
                    lineChartT.setVisible(false);
                    lineChartB.setVisible(true);
                    lineChartG.setVisible(false);
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        if ((Integer) data.getYValue() != -1)
                            Tooltip.install(data.getNode(), new Tooltip(data.getXValue() + "," + BloodPressure.getValue((Integer) data.getYValue()).toString()));
                        else
                            Tooltip.install(data.getNode(), new Tooltip(data.getXValue() + ",0"));
                    }
                    break;
                case "Glucose":
                    lineChartG.getData().addAll(series);
                    lineChartT.setVisible(false);
                    lineChartB.setVisible(false);
                    lineChartG.setVisible(true);
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        Tooltip.install(data.getNode(), new Tooltip(data.getXValue() + "," + data.getYValue()));
                    }
                    break;
            }
        });
        backButton.setOnMouseClicked(mouseEvent -> {
            LoginPage.getWindow().setScene(patientMenuPage.getScene());
            String periodTimeRepeat;
            ResultSet resultSet;
            try {
                resultSet = patientMenuPage.getResultSet();
                periodTimeRepeat = resultSet.getString(26);
                String s[] = periodTimeRepeat.split("\n");
                periodTimeRepeat = s[s.length - 1].split(" ")[2];
                int period = Integer.valueOf(periodTimeRepeat);
                String lastTest = resultSet.getString(27);
                Thread thread = new Thread(CreateUserData.getInstance(period, lastTest, patientMenuPage.getId()), patientMenuPage.getId());
                thread.start();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private ArrayList<Value> BuildDayInMonth(ArrayList<Object> points, String monthName, int year, int day, String test) {
        Calendar calendar = Calendar.getInstance();
        int month = Month.valueOf(monthName.toUpperCase()).getValue() - 1;
        calendar.set(year, month, day,0,0,0);
        ArrayList<Value> values = new ArrayList<>();
        int place = 0;
        int number;
        String string;
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(year, month, day,0,0,0);
        if (test.equals("Blood Pressure")){
            ArrayList<Value> points1 = new ArrayList<>();
            for (Object object:points)
                points1.add((Value)object);
            points1.sort(Comparator.comparing(Value::getIntNumber));
            while (place<points1.size()){
                Value value = points1.get(place);
                number = value.getIntNumber();
                calendar1.add(Calendar.HOUR_OF_DAY, number);
                if (calendar.get(Calendar.DAY_OF_MONTH) == calendar1.get(Calendar.DAY_OF_MONTH)) {
                    if (calendar1.get(Calendar.HOUR_OF_DAY) < 10)
                        string = "0" + String.valueOf(calendar1.get(Calendar.HOUR_OF_DAY));
                    else
                        string = String.valueOf(calendar1.get(Calendar.HOUR_OF_DAY));
                    string += ":";
                    if (calendar1.get(Calendar.MINUTE) < 10)
                        string += ("0" + calendar.get(Calendar.MINUTE));
                    else
                        string += calendar1.get(Calendar.MINUTE);
                    BloodPressure bloodPressure = new BloodPressure(value.getPoint());
                    values.add(new Value(String.valueOf(string), BloodPressure.getPresentation(bloodPressure)));
                }
                place++;
                calendar1.set(year, month, day, 0, 0, 0);
            }
            return values;
        }
        ArrayList<Point> points1 = new ArrayList<>();
        for (Object object:points)
            points1.add((Point)object);
        points1.sort(Comparator.comparing(Point::getIx));
        while (place < points1.size()) {
            Point point = points1.get(place);
            number = point.getIx();
            calendar1.add(Calendar.HOUR_OF_DAY, number);
            if (calendar.get(Calendar.DAY_OF_MONTH) == calendar1.get(Calendar.DAY_OF_MONTH)) {
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
                    values.add(new Value(String.valueOf(string), point.getDy()));
                else if (test.equals("Glucose")) {
                    int n = point.getIy();
                    values.add(new Value(String.valueOf(string), n));
                }
            }
            place++;
            calendar1.set(year, month, day, 0, 0, 0);
        }
        return values;
    }

    private ArrayList<Point> BuildAverageDayInMonth(ArrayList<Object> points, String monthName, Integer year, String test) {
        Calendar calendar = Calendar.getInstance();
        int month = Month.valueOf(monthName.toUpperCase()).getValue() - 1;
        calendar.set(year, month,1);
        ArrayList<Point> avgDay = new ArrayList<>();
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int place = 0;
        int amount = 0;
        if (test.equals("Blood Pressure")){
            ArrayList<Value> points1 = new ArrayList<>();
            for (Object object:points){
                points1.add((Value)object);
            }
            points1.sort(Comparator.comparing(Value::getIntNumber));
            double avgX = 0.0;
            double avgY = 0.0;
            for (int i = 0;i<=maxDay;i++){
                Value value = points1.get(place);
                int number = value.getIntNumber();
                while ((i * 23 <= number) && (number <= (i + 1) * 23)){
                    int x = value.getPoint().getIx();
                    int y = value.getPoint().getIy();
                    amount++;
                    avgX += x;
                    avgY += y;
                    place++;
                    if (place<points1.size()) {
                        value = points1.get(place);
                        number = value.getIntNumber();
                    }
                    else
                        break;
                }
                avgX /= amount;
                avgY /= amount;
                int bp = -1;
                if (amount != 0)
                    bp = BloodPressure.getPresentation(new BloodPressure((int)avgX, (int)avgY));
                avgDay.add(new Point((i + 1), bp));
                avgX = 0.0;
                avgY = 0.0;
                amount = 0;
            }
            return avgDay;
        }
        ArrayList<Point> points1 = new ArrayList<>();
        for (Object object:points)
            points1.add((Point)object);
        points1.sort(Comparator.comparing(Point::getIx));
        double avg = 0.0;
        for (int i = 0;i<=maxDay;i++){
            Point point = points1.get(place);
            int number = point.getIx();
            while ((i * 23 <= number) && (number <= ((i + 1) * 23))){
                amount++;
                if (test.equals("Temperature"))
                    avg += point.getDy();
                else if (test.equals("Glucose"))
                    avg += point.getIy();
                place++;
                if (place<points1.size()) {
                    point = points1.get(place);
                    number = point.getIx();
                }
                else
                    break;
            }
            if (avg != 0.0)
                avg = new BigDecimal(avg/amount).setScale(1, RoundingMode.HALF_UP).doubleValue();
            if (test.equals("Temperature"))
                avgDay.add(new Point(i + 1, avg));
            else if (test.equals("Glucose"))
                avgDay.add(new Point(i + 1, (int) avg));
            avg = 0.0;
            amount = 0;
        }
        avgDay.sort(Comparator.comparing(Point::getIx));
        return avgDay;
    }

    private String showGraph(int year, String test, String month) throws SQLException {
        return patientMenuPageModel.getPatientData(patientData.getId(), year, test, month);
    }

}
