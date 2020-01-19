package Client.DoctorsView.DoctorRegister2;

import Client.Login.LoginModel;
import com.mysql.cj.log.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Formatter;

public class RegisterPage2Model {
    private LoginModel loginModel;
    private static RegisterPage2Model registerPage2Model;

    private RegisterPage2Model() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        loginModel = LoginModel.getLoginModel();
    }

    public static RegisterPage2Model getRegisterPage2Model() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        if(registerPage2Model == null)
            registerPage2Model = new RegisterPage2Model();
        return registerPage2Model;
    }


    public void addPatient(String id, String email, String password, String phone, String doctor, String name, String background, String tests) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        loginModel.connect();
        if (!UserExist(id)) {
            PreparedStatement preparedStatement;
            String sql = "Insert into patients (id, email, password, phone, doctor, name, diseaes, tests, lastTest) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = loginModel.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, doctor);
            preparedStatement.setString(6, name);
            preparedStatement.setString(7, background);
            preparedStatement.setString(8, tests);
            LocalDateTime localDate = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            preparedStatement.setString(9, dtf.format(localDate));
            preparedStatement.execute();
            sql = "CREATE TABLE hospital.s" + id + " (test varchar(50) DEFAULT NULL , year varchar(50) DEFAULT NULL ," +
                    " January varchar(250) DEFAULT NULL ," +
                    " Febraury varchar(250) DEFAULT NULL ," +
                    " March varchar(250) DEFAULT NULL ," +
                    " April varchar(250) DEFAULT NULL ," +
                    " May varchar(250) DEFAULT NULL ," +
                    " June varchar(250) DEFAULT NULL ," +
                    " July varchar(250) DEFAULT NULL ," +
                    " August varchar(250) DEFAULT NULL ," +
                    " September varchar(250) DEFAULT NULL ," +
                    " October varchar(250) DEFAULT NULL ," +
                    " November varchar(250) DEFAULT NULL ," +
                    " December varchar(250) DEFAULT NULL ) ENGINE = InnoDB;";
            preparedStatement = loginModel.getConnection().prepareStatement(sql);
            preparedStatement.execute();
            AddCurrentYear(id, loginModel);
        }
    }

    public static void AddCurrentYear(String id, LoginModel loginModel) throws SQLException {
        String sql = "INSERT INTO hospital.s" + id + "(test, year) VALUES ('T', ?)";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        preparedStatement.execute();
        sql = "INSERT INTO hospital.s" + id + "(test, year) VALUES ('B', ?)";
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        preparedStatement.execute();
        sql = "INSERT INTO hospital.s" + id + "(test, year) VALUES ('G', ?)";
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        preparedStatement.execute();
    }

    private boolean UserExist(String id) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        loginModel.connect();
        PreparedStatement preparedStatement;
        String sql = "SELECT * FROM patients WHERE id = ?";
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
}
