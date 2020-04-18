package Client.DataTypes;

import java.util.Random;

public class Glucose {
    private int glucose;

    public Glucose(){
        Random random = new Random();
        float number = random.nextFloat() * 100;
        if (number>=0&&number<=0.01){
            glucose = random.nextInt(20) + 40;
        }
        else if (number>0.01&&number<=50){
            glucose = random.nextInt(21) + 80;
        }
        else if (number>50&&number<99.5){
            glucose = random.nextInt(15) + 101;
        }
        else if (number>=99.99&&number<=100){
            glucose = random.nextInt(185) + 116;
        }
    }

    public Glucose(int glucose){
        this.glucose = glucose;
    }

    public int getGlucose() {
        return glucose;
    }

    @Override
    public String toString() {
        return "glucose = " + String.valueOf(glucose);
    }
}
