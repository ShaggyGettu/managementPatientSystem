package Client.DataTypes;

import java.util.Random;

public class Glucose {
    private int glucose;

    public Glucose(){
        Random random = new Random();
        int number = random.nextInt(1000);
        if (number>=0&&number<150){
            glucose = random.nextInt(20) + 60;
        }
        else if (number>=150&&number<300){
            glucose = random.nextInt(21) + 80;
        }
        else if (number>=300&&number<700){
            glucose = random.nextInt(25) + 101;
        }
        else if (number>=700&&number<1000){
            glucose = random.nextInt(75) + 126;
        }
    }

    public int getGlucose() {
        return glucose;
    }

    @Override
    public String toString() {
        return "glucose = " + String.valueOf(glucose);
    }

    public static void main(String args[]){
        Glucose glucose = new Glucose();
        for (int i=0;i<10;i++){
            System.out.println(glucose);
            glucose = new Glucose();
        }
        System.out.println(glucose);
    }
}
