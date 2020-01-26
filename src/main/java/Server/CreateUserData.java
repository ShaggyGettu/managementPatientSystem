package Server;

import Client.DataTypes.BloodPressure;
import Client.DataTypes.Glucose;
import Client.DataTypes.Temperature;
import Client.DoctorsView.DoctorRegister2.RegisterPage2Model;
import Client.Login.LoginModel;
import Client.PatientsView.PatientMenu.PatientMenuPageModel;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateUserData implements Runnable{
    private String id;
    private int periodTimeRepeat;
    private String lastTest;
    private boolean exit;
    private static CreateUserData createUserData;
    private Calendar runningCal;
    private PatientMenuPageModel patientMenuPageModel;

    private CreateUserData(String periodTimeRepeat, String lastTest, String id) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        this.periodTimeRepeat = Integer.parseInt(periodTimeRepeat);
        this.lastTest = lastTest;
        exit = true;
        this.id = id;
        String year = lastTest.split("/")[2].split(" ")[0];
        if (!RegisterPage2Model.isYearExist(id, LoginModel.getLoginModel(), year))
            RegisterPage2Model.AddCurrentYear(id, LoginModel.getLoginModel(), year);
        runningCal = Calendar.getInstance();

    }

    public static CreateUserData getInstance(String periodTimeRepeat, String lastTest, String id) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        if (createUserData == null){
            createUserData = new CreateUserData(periodTimeRepeat, lastTest, id);
        }
        return createUserData;
    }

    public static CreateUserData getInstance() {
        return createUserData;
    }

    @Override
    public void run() {
        while (exit) {
            try {
                patientMenuPageModel = PatientMenuPageModel.getInstance();
            } catch (ClassNotFoundException | SQLException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            if (Calendar.getInstance().get(Calendar.MONTH) != runningCal.get(Calendar.MONTH)){
                String s = null;
                try {
                    s = patientMenuPageModel.getPeriodTimeRepeat(id).getString(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                assert s != null;
                String s1[] = s.split("\n");
                periodTimeRepeat = Integer.parseInt(s1[s1.length - 1].split(" ")[2]);
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date lastT = null;
            try {
                lastT = simpleDateFormat.parse(lastTest);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert lastT != null;
            Calendar lastPlusPeriod = Calendar.getInstance();
            lastPlusPeriod.setTime(lastT);
            Calendar last = Calendar.getInstance();
            last.setTime(lastT);
            lastPlusPeriod.add(Calendar.MINUTE, periodTimeRepeat);
            if(lastPlusPeriod.before(Calendar.getInstance())) {
                try {
                    if (last.get(Calendar.YEAR) != lastPlusPeriod.get(Calendar.YEAR))
                        RegisterPage2Model.AddCurrentYear(id, LoginModel.getLoginModel(), String.valueOf(lastPlusPeriod.get(Calendar.YEAR)));
                    Temperature temperature = new Temperature();
                    Glucose glucose = new Glucose();
                    BloodPressure bloodPressure = new BloodPressure();
                    if (patientMenuPageModel.isTemperature())
                        patientMenuPageModel.temperature(lastPlusPeriod.getTime(), temperature.toString(), id, periodTimeRepeat);
                    if (patientMenuPageModel.isGlucose())
                        patientMenuPageModel.glucose(lastPlusPeriod.getTime(), String.valueOf(glucose.getGlucose()), id, periodTimeRepeat);
                    if (patientMenuPageModel.isBloodPressure())
                        patientMenuPageModel.bloodPressure(lastPlusPeriod.getTime(), bloodPressure.toString(), id, periodTimeRepeat);
                } catch (ClassNotFoundException | SQLException | IllegalAccessException | InstantiationException e){
                    e.printStackTrace();
                }
            }
            try {
                lastTest = PatientMenuPageModel.getInstance().getLastTest(id);
            } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        exit = false;
        createUserData = null;
    }
}
