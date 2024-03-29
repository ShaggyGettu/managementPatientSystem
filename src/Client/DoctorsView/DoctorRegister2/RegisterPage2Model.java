package Client.DoctorsView.DoctorRegister2;

import Client.Login.LoginModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class RegisterPage2Model {
    private LoginModel loginModel;
    private static RegisterPage2Model registerPage2Model;

    private RegisterPage2Model() throws ClassNotFoundException, SQLException {
        loginModel = LoginModel.getLoginModel();
    }

    static RegisterPage2Model getRegisterPage2Model() throws ClassNotFoundException, SQLException, IllegalAccessException {
        if(registerPage2Model == null)
            registerPage2Model = new RegisterPage2Model();
        return registerPage2Model;
    }


    void addPatient(String id, String email, String password, String phone, String doctor, String name, String background, String tests, int periodTime) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        if (!UserExist(id)) {
            PreparedStatement preparedStatement;
            Calendar calendar = Calendar.getInstance();
            String formatter = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
            String sql = "Insert into patients (id, email, password, phone, doctor, name, diseaes, tests, periodTimeRepeat, lastTest) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = loginModel.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, doctor);
            preparedStatement.setString(6, name);
            preparedStatement.setString(7, background);
            preparedStatement.setString(8, tests);
            preparedStatement.setString(9, formatter + " " + calendar.get(Calendar.YEAR) + " " + String.valueOf(periodTime) + "\n");
            LocalDateTime localDate = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            preparedStatement.setString(10, dtf.format(localDate));
            preparedStatement.execute();
            sql = "CREATE TABLE hospital.s" + id + " (test varchar(50) DEFAULT NULL , year varchar(50) DEFAULT NULL ," +
                    " January TEXT DEFAULT NULL ," +
                    " February TEXT DEFAULT NULL ," +
                    " March TEXT DEFAULT NULL ," +
                    " April TEXT DEFAULT NULL ," +
                    " May TEXT DEFAULT NULL ," +
                    " June TEXT DEFAULT NULL ," +
                    " July TEXT DEFAULT NULL ," +
                    " August TEXT DEFAULT NULL ," +
                    " September TEXT DEFAULT NULL ," +
                    " October TEXT DEFAULT NULL ," +
                    " November TEXT DEFAULT NULL ," +
                    " December TEXT DEFAULT NULL ) ENGINE = InnoDB;";
            preparedStatement = loginModel.getConnection().prepareStatement(sql);
            preparedStatement.execute();
        }
    }

    public static boolean isYearExist(String id, LoginModel loginModel, String year) throws SQLException {
        String sql = "SELECT * FROM hospital.s" + id + " WHERE year = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, year);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public static void AddCurrentYear(String id, LoginModel loginModel, String year) throws SQLException {
        if (isYearExist(id, loginModel, year))
            return;
        String sql = "INSERT INTO hospital.s" + id + "(test, year) VALUES ('T', ?)";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, year);
        preparedStatement.execute();
        sql = "INSERT INTO hospital.s" + id + "(test, year) VALUES ('B', ?)";
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, year);
        preparedStatement.execute();
        sql = "INSERT INTO hospital.s" + id + "(test, year) VALUES ('G', ?)";
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, year);
        preparedStatement.execute();
    }

    private boolean UserExist(String id) throws SQLException {
        PreparedStatement preparedStatement;
        String sql = "SELECT * FROM patients WHERE id = ?";
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    void setCriticalMinTemp(String text, String id) throws SQLException {
        String sql = "Update patients SET criticalMinTemperature = ? WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, text);
        preparedStatement.setString(2, id);
        preparedStatement.execute();
    }

    void setCriticalMaxTemp(String text, String id) throws SQLException {
        String sql = "Update patients SET criticalMaxTemperature = ? WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, text);
        preparedStatement.setString(2, id);
        preparedStatement.execute();
    }

    void setCriticalBP(String text, String id) throws SQLException {
        String sql = "Update patients SET criticalBloodPressure = ? WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, text);
        preparedStatement.setString(2, id);
        preparedStatement.execute();
    }

    void setCriticalMinGlu(String text, String id) throws SQLException {
        String sql = "Update patients SET criticalMinGlucose = ? WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, text);
        preparedStatement.setString(2, id);
        preparedStatement.execute();
    }

    void setCriticalMaxGlu(String text, String id) throws SQLException {
        String sql = "Update patients SET criticalMaxGlucose = ? WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, text);
        preparedStatement.setString(2, id);
        preparedStatement.execute();
    }
}
