package Server;

import Client.DataTypes.BloodPressure;
import Client.DataTypes.Glucose;
import Client.DataTypes.Temperature;
import Client.PatientsView.PatientMenu.PatientMenuPageModel;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class userData implements Runnable{
    private static final int PeriodTimeRepeat = 10;

    @Override
    public void run() {
        while (true) {
            PatientMenuPageModel patientMenuPageModel = null;
            Calendar calendar = Calendar.getInstance();
            if(calendar.get(Calendar.SECOND) % PeriodTimeRepeat == 0) {
                String id = Thread.currentThread().getName();
                Temperature temperature = new Temperature();
                Glucose glucose = new Glucose();
                BloodPressure bloodPressure = new BloodPressure();
                try {
                    patientMenuPageModel = PatientMenuPageModel.getInstance();
                    patientMenuPageModel.temperature(String.valueOf(temperature.getCelsius()), id);
                    patientMenuPageModel.glucose(String.valueOf(glucose.getGlucose()), id);
                    patientMenuPageModel.bloodPressure(bloodPressure.toString(), id);
                } catch (ClassNotFoundException | SQLException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }
}
