package Client.DataTypes;

import java.util.Random;

public class BloodPressure {
    private int smallBloodPressure;
    private int largeBloodPressure;
    private int status;

    public BloodPressure(){
        Random random = new Random();
        float number = random.nextFloat()* 100;
        if(number>=0&&number<50){//Optimal blood pressure
            largeBloodPressure = random.nextInt(20) + 100;
            smallBloodPressure = random.nextInt(20) + 60;
            status = 1;
        }
        else if(number>=50&&number<70){//Normal blood pressure
            largeBloodPressure = random.nextInt(10) + 120;
            smallBloodPressure = random.nextInt(5) + 80;
            status = 2;
        }
        else if(number>=70&&number<97.5) {//Border blood pressure
            largeBloodPressure = random.nextInt(10) + 130;
            smallBloodPressure = random.nextInt(5) + 85;
            status = 3;
        }
        else if(number>=97.5&&number<99.5) {//Hypertension grade 1
            largeBloodPressure = random.nextInt(30) + 140;
            smallBloodPressure = random.nextInt(20) + 90;
            status = 4;
        }
        else if(number>=99.5&&number<=100) {//Hypertension grade 3
            largeBloodPressure = random.nextInt(41) + 170;
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
        return String .valueOf(largeBloodPressure) + ',' + smallBloodPressure;
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
