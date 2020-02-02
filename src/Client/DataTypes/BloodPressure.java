package Client.DataTypes;

import java.util.ArrayList;
import java.util.Random;

public class BloodPressure {
    private int smallBloodPressure;
    private int largeBloodPressure;
    private int status;
    private static ArrayList<BloodPressure> represent;

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

    public BloodPressure(Point point){
        largeBloodPressure = point.getIx();
        smallBloodPressure = point.getIy();
    }
    public BloodPressure(int i, int z) {
        largeBloodPressure = i;
        smallBloodPressure = z;
    }

    public static void initialize(){
        represent = new ArrayList<>();
        int i = 100;
        int z= 60;
        int place= 0;
        while (i <= 210 || z <= 139){
            represent.add(place, new BloodPressure(i, z));
            if (place %2 == 0) {
                i++;
                place++;
            }
            else {
                z++;
                place++;
            }
        }
    }

    public static int getPresentation(BloodPressure bloodPressure){
        for (int i = 0;i<represent.size();i++){
            if (bloodPressure.equals(represent.get(i)))
                return i;
        }
        return -1;
    }

    public static BloodPressure getValue(int i){
        return represent.get(i);
    }

    public boolean equals(BloodPressure bp) {
        return this.smallBloodPressure == bp.smallBloodPressure && this.largeBloodPressure == bp.largeBloodPressure;
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
        BloodPressure.initialize();
        System.out.println(represent.size());
    }
}
