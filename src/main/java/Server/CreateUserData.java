package Server;

import Client.DataTypes.BloodPressure;
import Client.DataTypes.Glucose;
import Client.DataTypes.Temperature;
import Client.PatientsView.PatientMenu.PatientMenuPageModel;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CreateUserData implements Runnable{
    private int periodTimeRepeat;
    private String lastTest;

    public CreateUserData(String periodTimeRepeat, String lastTest) {
        this.periodTimeRepeat = Integer.parseInt(periodTimeRepeat);
        this.lastTest = lastTest;
    }

    @Override
    public void run() {
        while (true) {
            PatientMenuPageModel patientMenuPageModel;
            LocalDateTime localDate = LocalDateTime.now();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            String now = dtf.format(localDate);
            Date lastT = null;
            Date nowT = null;
            long diff = 0;
            try {
                nowT = simpleDateFormat.parse(now);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (lastTest != null) {
                try {
                    lastT = simpleDateFormat.parse(lastTest);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                assert nowT != null;
                assert lastT != null;
                diff = nowT.getTime() - lastT.getTime();
                diff = diff / (60 * 60 * 1000);
            }
            if(diff>periodTimeRepeat) {
                String id = Thread.currentThread().getName();
                try {
                    Temperature temperature = new Temperature();
                    Glucose glucose = new Glucose();
                    BloodPressure bloodPressure = new BloodPressure();
                    patientMenuPageModel = PatientMenuPageModel.getInstance();
                    patientMenuPageModel.temperature(nowT, temperature.toString(), id);
                    patientMenuPageModel.glucose(nowT, String.valueOf(glucose.getGlucose()), id);
                    patientMenuPageModel.bloodPressure(nowT, bloodPressure.toString(), id);
                } catch (ClassNotFoundException | SQLException | IllegalAccessException | InstantiationException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
