package Client.DataTypes;

import java.util.Random;

public class Temperature {
    private float celsius;
    private float fahrenheit;

    public Temperature(){
        Random random = new Random();
        float number = random.nextFloat() * 100;
        // Probability of getting heat higher than 38.7
        if(number>=0 && number<=0.5){
            celsius = (float) (random.nextFloat()*3.3 + 38.7);
        }

        // Probability of getting heat between 36-37
        else if(number>0.5&&number<=30) {
            celsius = random.nextFloat() + 36;
        }

        // Probability of getting heat between 37-38.7 is 550000/1000000
        else if(number>30&&number<99.5) {
            celsius = (float) (random.nextFloat()*(1.7) + 37);
        }
        // Probability of getting heat between 35-36 is 149998/1000000
        else if(number>=99.5&&number<=100){
            celsius = random.nextFloat() + 35;
        }
        fahrenheit = (float) (celsius*1.8 + 32);
    }

    public float getCelsius(){
        return celsius;
    }
    public float getFahrenheit(){
        return fahrenheit;
    }

    public static void main(String args[]) throws InterruptedException {
        Temperature temperature = new Temperature();
        while (temperature.celsius<38.0)
        {
            System.out.println(temperature.celsius);
            Thread.sleep(100);
            temperature = new Temperature();
        }
        System.out.println(temperature.celsius);
    }

    @Override
    public String toString() {
        return String.format("%.1f",celsius);
    }
}
