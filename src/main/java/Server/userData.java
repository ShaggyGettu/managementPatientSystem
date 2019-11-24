package Server;

import Client.DataTypes.BloodPressure;
import Client.DataTypes.Glucose;
import Client.DataTypes.Temperature;
import Client.PatientsView.PatientMenu.PatientMenuPageModel;

import java.sql.SQLException;
import java.util.Calendar;

public class userData implements Runnable{
    @Override
    public void run() {
        PatientMenuPageModel patientMenuPageModel = null;
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.SECOND)!=0)
            calendar = Calendar.getInstance();
        String id = Thread.currentThread().getName();
        Temperature temperature = new Temperature();
        System.out.println("Temperature: " + temperature.getCelsius());
        Glucose glucose = new Glucose();
        System.out.println("Glucose: " + glucose.getGlucose());
        BloodPressure bloodPressure = new BloodPressure();
        System.out.println("Blood Pressure: " + bloodPressure.toString());
        try {
            patientMenuPageModel = PatientMenuPageModel.getInstance();
            patientMenuPageModel.temperature(String.valueOf(temperature.getCelsius()),id);
            patientMenuPageModel.glucose(String.valueOf(glucose.getGlucose()),id);
            patientMenuPageModel.bloodPressure(bloodPressure.toString(), id);
        } catch (ClassNotFoundException | SQLException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }


    }
}
