package Server;

import Client.Login.LoginModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class createData implements Runnable{

    @Override
    public void run() {
        LoginModel loginModel;
        try {
            loginModel = LoginModel.getLoginModel();
            ResultSet resultSet = loginModel.Users();
            //Thread for every user in our system
            resultSet.next();
            while (resultSet.next()){
                Thread t1 = new Thread(new userData(), resultSet.getString(1));
                t1.start();
            }
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

