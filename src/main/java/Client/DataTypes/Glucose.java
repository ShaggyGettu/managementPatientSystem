package Client.DataTypes;

import java.util.Random;

public class Glucose {
    private int glucose;

    public Glucose(){
        Random random = new Random();
        float number = random.nextFloat() * 100;
        if (number>=0&&number<=0.5){
            glucose = random.nextInt(20) + 60;
        }
        else if (number>0.5&&number<=50){
            glucose = random.nextInt(21) + 80;
        }
        else if (number>50&&number<99.5){
            glucose = random.nextInt(15) + 101;
        }
        else if (number>=99.5&&number<=100){
            glucose = random.nextInt(85) + 116;
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
