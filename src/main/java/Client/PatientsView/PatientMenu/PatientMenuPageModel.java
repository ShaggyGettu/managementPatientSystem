package Client.PatientsView.PatientMenu;

import Client.Login.LoginModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;

public class PatientMenuPageModel {
    private LoginModel loginModel;
    private static PatientMenuPageModel patientMenuPageModel;
    private boolean isTemperature;
    private boolean isBloodPressure;
    private boolean isGlucose;

    private PatientMenuPageModel() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        loginModel = LoginModel.getLoginModel();
        isTemperature = false;
        isBloodPressure = false;
        isGlucose = false;
    }

    public static PatientMenuPageModel getInstance() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        if (patientMenuPageModel == null)
            patientMenuPageModel = new PatientMenuPageModel();
        return patientMenuPageModel;
    }

    public String[] getPatient(String id) throws SQLException {
        String sql = "SELECT email, phone, tests FROM patients WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            String details[] = new String[3];
            details[0] = resultSet.getString(1);
            details[1] = resultSet.getString(2);
            details[2] = resultSet.getString(3);
            preparedStatement.close();
            resultSet.close();
            return details;
        }
        return null;
    }

    public void temperature(Date nowT, String temperature, String id, int periodTimeRepeat) throws SQLException {
        addValue(nowT, temperature, id, "T", periodTimeRepeat);
        String sql = "SELECT temperatureMin,temperatureMax, temperatureAvg, temperatureAmount, doctor, criticalMinTemperature, criticalMaxTemperature FROM patients WHERE id = ?";
        float temper = Float.valueOf(temperature);
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String temperatureMin = resultSet.getString(1);
            String temperatureMax = resultSet.getString(2);
            String temperatureAvg = resultSet.getString(3);
            String temperatureAmount = resultSet.getString(4);
            String doctor = resultSet.getString(5);
            String criticalMinTemperature = resultSet.getString(6);
            String criticalMaxTemperature = resultSet.getString(7);
            if (temperatureMin == null) {
                changeTemperature(1, temperature, id,nowT);
                changeTemperature(2, temperature, id, nowT);
                changeAvgTemperature(temperature, id, Integer.parseInt(temperatureAmount));
            }
            else {
                if (temper < Float.valueOf(temperatureMin.split(" ")[0]))
                    changeTemperature(1, temperature, id, nowT);
                if (temper > Float.valueOf(temperatureMax.split(" ")[0]))
                    changeTemperature(2, temperature, id, nowT);
                int amount = Integer.valueOf(temperatureAmount);
                float avg = Float.valueOf(temperatureAvg);
                avg *= amount;
                avg = avg + temper;
                temperature = String.format("%.1f", (avg / (amount + 1)));
                changeAvgTemperature(temperature, id, amount);
            }
            if (temper<Float.valueOf(criticalMinTemperature) || temper>Float.valueOf(criticalMaxTemperature))
                addWarning(1, String.valueOf(temper), id, doctor, nowT);
        }
    }

    private void addValue(Date now, String temperature, String id, String test, int periodTimeRepeat) throws SQLException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        Formatter formatter = new Formatter();
        int minutesFromBeginningMonth = calendar.get(Calendar.MINUTE) + (calendar.get(Calendar.HOUR_OF_DAY)*60) + (calendar.get(Calendar.DAY_OF_MONTH)*24*60);
        temperature = ", " + Integer.valueOf(minutesFromBeginningMonth) + " " + temperature;
        formatter.format("%tB", calendar);
        String sql = "update s" + id + " set " + formatter + " = CONCAT(IFNULL(" + formatter + ", ''), ?) WHERE year = ? AND test = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, temperature);
        preparedStatement.setString(2, String.valueOf(calendar.get(Calendar.YEAR)));
        preparedStatement.setString(3, test);
        preparedStatement.execute();
        sql = "update patients set lastTest = ? WHERE id = ?";
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        preparedStatement.setString(1, simpleDateFormat.format(now));
        preparedStatement.setString(2, id);
        preparedStatement.execute();
    }

    public String getLastTest(String id) throws SQLException {
        String sql = "SELECT lastTest FROM patients WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString(1);
    }

    private void addWarning(int state, String temperature, String id, String doctor, Date nowT) throws SQLException {
        String sql = "update doctors set warnings = CONCAT(IFNULL(warnings, ''), ?) WHERE id = ?";
        String warning = id + " ";
        switch (state)
        {
            case 1:
                warning += "Temperature ";
                break;
            case 2:
                warning += "BloodPressure ";
                break;
            case 3:
                warning += "Glucose ";
                break;
        }
        warning += temperature;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowT);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        warning += (" " + formatter.format(calendar.getTime()));
        warning += "\n";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1,warning);
        preparedStatement.setString(2,doctor);
        preparedStatement.execute();
    }

    private void changeAvgTemperature(String temperature, String id, int amount) throws SQLException {
        String sql = "UPDATE patients SET temperatureAmount = ?, temperatureAvg = ? WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, String .valueOf(amount + 1));
        preparedStatement.setString(2, temperature);
        preparedStatement.setString(3, id);
        preparedStatement.execute();

    }

    private void changeTemperature(int status, String temperature, String id, Date nowT) throws SQLException {
        String sql = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowT);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        temperature += (" " + formatter.format(calendar.getTime()));
        if(status == 1)
            sql = "UPDATE patients SET temperatureMin = ? WHERE id = ?";
        else if (status == 2)
            sql = "UPDATE patients SET temperatureMax = ? WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, temperature);
        preparedStatement.setString(2, id);
        preparedStatement.execute();
    }

    public void bloodPressure(Date nowT, String bloodPressure, String id, int periodTimeRepeat) throws SQLException {
        addValue(nowT, bloodPressure, id, "B", periodTimeRepeat);
        String sql = "SELECT bloodPressureMin, bloodPressureMax, bloodPressureAmount, bloodPressureAvg, doctor, criticalBloodPressure From patients WHERE id = ?";
        String bloodPressureMin;
        String bloodPressureMax;
        String bloodPressureAmount;
        String bloodPressureAvg;
        int lBP = Integer.valueOf(bloodPressure.split(",")[0]);
        int sBP = Integer.valueOf(bloodPressure.split(",")[1]);
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            bloodPressureMin = resultSet.getString(1);
            bloodPressureMax = resultSet.getString(2);
            bloodPressureAmount = resultSet.getString(3);
            bloodPressureAvg = resultSet.getString(4);
            String doctor = resultSet.getString(5);
            String criticalBloodPressure = resultSet.getString(6);
            if (bloodPressureMin == null){
                changeBloodPressure(1, bloodPressure, id, nowT);
                changeBloodPressure(2, bloodPressure, id, nowT);
                changeAvgBloodPressure(id, Integer.parseInt(bloodPressureAmount), bloodPressure);
            }
            else {
                int lBPN = Integer.valueOf(bloodPressureMin.split(",")[0]);
                int sBPN = Integer.valueOf(bloodPressureMin.split(",")[1].split(" ")[0]);
                if (lBP < lBPN && sBP < sBPN)
                    changeBloodPressure(1, bloodPressure, id, nowT);
                else if ((lBP + sBP) / 2 < (lBPN + sBPN) / 2)
                    changeBloodPressure(1, bloodPressure, id, nowT);
                lBPN = Integer.valueOf(bloodPressureMax.split(",")[0]);
                sBPN = Integer.valueOf(bloodPressureMax.split(",")[1].split(" ")[0]);
                if (lBP > lBPN && sBP > sBPN)
                    changeBloodPressure(2, bloodPressure, id, nowT);
                else if ((lBP + sBP) / 2 > (lBPN + sBPN) / 2)
                    changeBloodPressure(2, bloodPressure, id, nowT);
                int amount = Integer.valueOf(bloodPressureAmount);
                int lAvg = Integer.valueOf(bloodPressureAvg.split(",")[0]);
                int sAvg = Integer.valueOf(bloodPressureAvg.split(",")[1].split(" ")[0]);
                sAvg *= amount;
                sAvg += sBP;
                sAvg /= (amount + 1);
                lAvg *= amount;
                lAvg += lBP;
                lAvg /= (amount + 1);
                changeAvgBloodPressure(id, amount, lAvg + "," + sAvg);
            }
            if (Integer.parseInt(criticalBloodPressure.split(",")[0])<=lBP || Integer.parseInt(criticalBloodPressure.split(",")[1])<=sBP)
                addWarning(2, bloodPressure, id, doctor, nowT);
        }
    }

    private void changeAvgBloodPressure(String id, int amount, String avg) throws SQLException {
        String sql = "UPDATE patients SET bloodPressureAmount = ?, bloodPressureAvg = ? WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, String.valueOf(amount + 1));
        preparedStatement.setString(2, avg);
        preparedStatement.setString(3, id);
        preparedStatement.execute();
    }

    private void changeBloodPressure(int status, String bloodPressure, String id, Date nowT) throws SQLException {
        String sql = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowT);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        bloodPressure += (" " + formatter.format(calendar.getTime()));
        if (status == 1)
        sql = "UPDATE patients SET bloodPressureMin = ? WHERE id = ?";
        else if (status == 2)
            sql = "UPDATE patients SET bloodPressureMax = ? WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, bloodPressure);
        preparedStatement.setString(2, id);
        preparedStatement.execute();
    }

    public void glucose(Date nowT, String glucose, String id, int periodTimeRepeat) throws SQLException {
        addValue(nowT, glucose, id, "G", periodTimeRepeat);
        String sql = "SELECT glucoseMin, glucoseMax, glucoseAmount, glucoseAvg, doctor, criticalMinGlucose, criticalMaxGlucose FROM patients WHERE id = ?";
        String glucoseMin, glucoseMax, glucoseAmount, glucoseAvg;
        int glucos = Integer.valueOf(glucose);
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            glucoseMin = resultSet.getString(1);
            glucoseMax = resultSet.getString(2);
            glucoseAmount = resultSet.getString(3);
            glucoseAvg = resultSet.getString(4);
            String doctor = resultSet.getString(5);
            String criticalMinGlucose = resultSet.getString(6);
            String criticalMaxGlucose = resultSet.getString(7);
            if (glucoseMin == null || glucos<Integer.valueOf(glucoseMin.split(" ")[0]))
                changeGlucose(1, glucose, id, nowT);
            if (glucoseMax == null || glucos>Integer.valueOf(glucoseMax.split(" ")[0]))
                changeGlucose(2, glucose, id, nowT);
            int amount = Integer.valueOf(glucoseAmount);
            float avg = Float.valueOf(glucoseAvg);
            avg *= amount;
            avg += glucos;
            avg /= (amount + 1);
            changeAvgGlucose(amount, (int) avg, id);
            if (glucos<Integer.valueOf(criticalMinGlucose) || glucos>Integer.valueOf(criticalMaxGlucose))
                addWarning(3, glucose, id, doctor, nowT);
        }
    }

    private void changeAvgGlucose(int amount, int glucose, String id) throws SQLException {
        String sql = "UPDATE patients SET glucoseAmount = ?, glucoseAvg = ? WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, String.valueOf(amount + 1));
        preparedStatement.setString(2, String.valueOf(glucose));
        preparedStatement.setString(3, id);
        preparedStatement.execute();
    }

    private void changeGlucose(int status, String glucose, String id, Date nowT) throws SQLException {
        String sql = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowT);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        glucose += (" " + formatter.format(calendar.getTime()));
        if (status == 1)
            sql = "UPDATE patients SET glucoseMin = ? WHERE id = ?";
        if (status == 2)
            sql = "UPDATE patients SET glucoseMax = ? where id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, glucose);
        preparedStatement.setString(2, id);
        preparedStatement.execute();
    }

    public static void main(String args[]) throws ParseException {
    }

    ResultSet getData(String id) throws SQLException {
        String sql = "SELECT temperatureAvg, temperatureMin, temperatureMax, temperatureAmount, " +
                "bloodPressureAvg, bloodPressureMin, bloodPressureMax, bloodPressureAmount, " +
                "glucoseAvg, glucoseMin, glucoseMax, glucoseAmount FROM patients WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet;
    }

    public boolean isTemperature() {
        return isTemperature;
    }

    public void setTemperature(boolean temperature) {
        isTemperature = temperature;
    }

    public boolean isBloodPressure() {
        return isBloodPressure;
    }

    public void setBloodPressure(boolean bloodPressure) {
        isBloodPressure = bloodPressure;
    }

    public boolean isGlucose() {
        return isGlucose;
    }

    public void setGlucose(boolean glucose) {
        isGlucose = glucose;
    }
}
