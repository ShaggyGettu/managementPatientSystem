package Server;

import Client.DoctorsView.DoctorsMenu.DoctorMenuPageModel;
import Client.Login.LoginModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UpdateData {
    private Queries queries;
    private LoginModel loginModel;

    private UpdateData() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        queries = new Queries();
        ResultSet patients= queries.getAllPatients();
        while (patients.next()){
            ResultSet dataTable = queries.getPatientDataTable(patients.getString(1));
            dataTable.next();
            dataTable.absolute(1);
            while (dataTable.absolute(dataTable.getRow())){
                Calendar calendar = Calendar.getInstance();
                int year = Integer.parseInt(dataTable.getString("year"));
                for (int i = 0;i<12;i++) {
                    calendar.set(year, i, 1);
                    String month = calendar.getDisplayName(2, 2, Locale.US);
                    String stringValues = dataTable.getString(month);
                    HashMap<Float, Float> numericValues = new HashMap<>();
                    if (stringValues != null){
                        ArrayList<String> values = new ArrayList<>(Arrays.asList(stringValues.split(",")));
                        if (dataTable.getString("test").equals("T") || dataTable.getString("test").equals("G")){
                            values.remove(values.size() - 2);
                            for (String string:values){
                                String value[] = string.split(" ");
                                numericValues.put(Float.valueOf(value[0]), Float.valueOf(value[1]));
                            }
                        }
                    }
                }
                dataTable.absolute(dataTable.getRow() + 1);
            }
        }

    }



    public static void main(String args[]) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        new UpdateData();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.NOVEMBER, 25, 20, 12);
    }
}
