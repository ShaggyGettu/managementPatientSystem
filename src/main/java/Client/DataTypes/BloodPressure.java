package Client.DataTypes;

import java.util.Random;

public class BloodPressure {
    private int smallBloodPressure;
    private int largeBloodPressure;
    private int status;

    public BloodPressure(){
        Random random = new Random();
        int number = random.nextInt(10000);
        if(number>=0&&number<500){//Optimal blood pressure
            largeBloodPressure = random.nextInt(20) + 100;
            smallBloodPressure = random.nextInt(20) + 60;
            status = 1;
        }
        else if(number>=500&&number<2000){//Normal blood pressure
            largeBloodPressure = random.nextInt(10) + 120;
            smallBloodPressure = random.nextInt(5) + 80;
            status = 2;
        }
        else if(number>=2000&&number<4000) {//Border blood pressure
            largeBloodPressure = random.nextInt(10) + 130;
            smallBloodPressure = random.nextInt(5) + 85;
            status = 3;
        }
        else if(number>=4000&&number<6500) {//Hypertension grade 1
            largeBloodPressure = random.nextInt(20) + 140;
            smallBloodPressure = random.nextInt(10) + 90;
            status = 4;
        }
        else if(number>=6500&&number<9000) {//Hypertension grade 2
            largeBloodPressure = random.nextInt(20) + 160;
            smallBloodPressure = random.nextInt(10) + 100;
            status = 5;
        }
        else if(number>=9000&&number<10000) {//Hypertension grade 3
            largeBloodPressure = random.nextInt(40) + 180;
            smallBloodPressure = random.nextInt(30) + 110;
            status = 6;
        }
    }

    public int getLargeBloodPressure() {
        return largeBloodPressure;
    }

    public int getSmallBloodPressure() {
        return smallBloodPressure;
    }

    @Override
    public String toString() {
        return String .valueOf(largeBloodPressure) + ',' + smallBloodPressure + " status: " + status;
    }

    public static void main(String args[]){
        BloodPressure bloodPressure = new BloodPressure();
        while (bloodPressure.status != 6){
            System.out.println(bloodPressure);
            bloodPressure = new BloodPressure();
        }
        System.out.println(bloodPressure);
    }
}
