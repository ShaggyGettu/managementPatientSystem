package Client.PatientsView.PatientMenu;

import Client.Login.LoginModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PatientMenuPageModel {
    private LoginModel loginModel;
    private static PatientMenuPageModel patientMenuPageModel;

    private PatientMenuPageModel() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        loginModel = LoginModel.getLoginModel();
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

    public void temperature(String temperature, String id) throws SQLException {
        String sql = "SELECT temperatureMin FROM patients WHERE id = ?";
        float temper = Float.valueOf(temperature);
        //System.out.println("temper: " + temper);
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            //System.out.println("temperatureMin before inserting: " + resultSet.getString(1));
            if (resultSet.getString(1) == null || temper < Float.valueOf(resultSet.getString(1).split(" ")[0]))
                changeTemperature(1, temperature, id);
        }
        sql = "SELECT temperatureMax FROM patients WHERE id = ?";
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            //System.out.println("temperatureMax before inserting: " + resultSet.getString(1));

            if (resultSet.getString(1) == null || temper > Float.valueOf(resultSet.getString(1).split(" ")[0]))
                changeTemperature(2, temperature, id);
        }
        sql = "SELECT temperatureAmount, temperatureAvg FROM patients WHERE id = ?";
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            //System.out.println("temperatureAmount before inserting: " + resultSet.getString(1));
            //System.out.println("temepreatureAvg before inserting: " + resultSet.getString(2));
            int amount = Integer.valueOf(resultSet.getString(1));
            float avg = Float.valueOf(resultSet.getString(2));
            //System.out.println("avg: " + avg);
            avg *= amount;
            avg = avg + temper;
            temperature = String.valueOf(avg / (amount + 1));
            changeAvgTemperature(temperature, id, amount);
        }
    }

    private void changeAvgTemperature(String temperature, String id, int amount) throws SQLException {
        String sql = "UPDATE patients SET temperatureAmount = ?, temperatureAvg = ? WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, String .valueOf(amount + 1));
        preparedStatement.setString(2, temperature);
        preparedStatement.setString(3, id);
        preparedStatement.execute();

    }

    private void changeTemperature(int status, String temperature, String id) throws SQLException {
        String sql = null;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        temperature += (" " + formatter.format(calendar.getTime()));
        System.out.println(id + ": " + temperature);
        if(status == 1)
            sql = "UPDATE patients SET temperatureMin = ? WHERE id = ?";
        else if (status == 2)
            sql = "UPDATE patients SET temperatureMax = ? WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, temperature);
        preparedStatement.setString(2, id);
        preparedStatement.execute();
    }

    public void bloodPressure(String bloodPressure, String id) throws SQLException {
        String sql = "SELECT bloodPressureMin, bloodPressureMax, bloodPressureAmount, bloodPressureAvg From patients WHERE id = ?";
        String bloodPressureMin, bloodPressureMax, bloodPressureAmount, bloodPressureAvg;
        int sBP = Integer.valueOf(bloodPressure.split(",")[0].substring(0,2));
        int lBP = Integer.valueOf(bloodPressure.split(",")[1].substring(0,2));
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            bloodPressureMin = resultSet.getString(1);
            bloodPressureMax = resultSet.getString(2);
            bloodPressureAmount = resultSet.getString(3);
            bloodPressureAvg = resultSet.getString(4);
            if (bloodPressureMin == null)
                changeBloodPressure(1, bloodPressure, id);
            else {
                int sBPN = Integer.valueOf(bloodPressureMin.split(",")[0]);
                int lBPN = Integer.valueOf(bloodPressureMin.split(",")[1].split(" ")[0]);
                if (sBP < sBPN && lBP < lBPN)
                    changeBloodPressure(1, bloodPressure, id);
                if ((sBP + lBP) / 2 < (sBPN + lBPN) / 2)
                    changeBloodPressure(1, bloodPressure, id);
            }
            if (bloodPressureMax == null)
                changeBloodPressure(2, bloodPressure, id);
            else {
                int sBPN = Integer.valueOf(bloodPressureMax.split(",")[0]);
                int lBPN = Integer.valueOf(bloodPressureMax.split(",")[1].split(" ")[0]);
                if (sBP > sBPN && lBP > lBPN)
                    changeBloodPressure(2, bloodPressure, id);
                if ((sBP + lBP) / 2 > (sBPN + lBPN) / 2)
                    changeBloodPressure(2, bloodPressure, id);
            }
            int amount = Integer.valueOf(bloodPressureAmount);
            int sAvg = Integer.valueOf(bloodPressureAvg.split(",")[0]);
            int lAvg = Integer.valueOf(bloodPressureAvg.split(",")[1].split(" ")[0]);
            lAvg *= amount;
            lAvg += lBP;
            lAvg /= (amount + 1);
            sAvg *= amount;
            sAvg += sBP;
            sAvg /= (amount + 1);
            changeAvgBloodPressure(id, amount, sAvg + "," + lAvg);
        }
    }

    private void changeAvgBloodPressure(String id, int amount, String avg) throws SQLException {
        //System.out.println(avg);
        String sql = "UPDATE patients SET bloodPressureAmount = ?, bloodPressureAvg = ? WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, String.valueOf(amount + 1));
        preparedStatement.setString(2, avg);
        preparedStatement.setString(3, id);
        preparedStatement.execute();
    }

    private void changeBloodPressure(int status, String bloodPressure, String id) throws SQLException {
        String sql = null;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        bloodPressure += (" " + formatter.format(calendar.getTime()));
        System.out.println(id + ": " + bloodPressure);
        if (status == 1)
        sql = "UPDATE patients SET bloodPressureMin = ? WHERE id = ?";
        else if (status == 2)
            sql = "UPDATE patients SET bloodPressureMax = ? WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, bloodPressure);
        preparedStatement.setString(2, id);
        preparedStatement.execute();
    }

    public void glucose(String glucose, String id) throws SQLException {
        String sql = "SELECT glucoseMin, glucoseMax, glucoseAmount, glucoseAvg FROM patients WHERE id = ?";
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
            if (glucoseMin == null || glucos<Integer.valueOf(glucoseMin.split(" ")[0]))
                changeGlucose(1, glucose, id);
            if (glucoseMax == null || glucos>Integer.valueOf(glucoseMax.split(" ")[0]))
                changeGlucose(2, glucose, id);
            int amount = Integer.valueOf(glucoseAmount);
            float avg = Float.valueOf(glucoseAvg);
            avg *= amount;
            avg += glucos;
            avg /= (amount + 1);
            changeAvgGlucose(amount, avg, id);
        }
    }

    private void changeAvgGlucose(int amount, float glucose, String id) throws SQLException {
        String sql = "UPDATE patients SET glucoseAmount = ?, glucoseAvg = ? WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, String.valueOf(amount + 1));
        preparedStatement.setString(2, String.valueOf(glucose));
        preparedStatement.setString(3, id);
        preparedStatement.execute();
    }

    private void changeGlucose(int status, String glucose, String id) throws SQLException {
        String sql = null;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        glucose += (" " + formatter.format(calendar.getTime()));
        System.out.println(id + ": " + glucose);
        if (status == 1)
            sql = "UPDATE patients SET glucoseMin = ? WHERE id = ?";
        if (status == 2)
            sql = "UPDATE patients SET glucoseMax = ? where id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, glucose);
        preparedStatement.setString(2, id);
        preparedStatement.execute();
    }

    public static void main(String args[]) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        PatientMenuPageModel pageModel = new PatientMenuPageModel();
    }
}
