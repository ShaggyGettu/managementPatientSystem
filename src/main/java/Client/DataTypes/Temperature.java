package Client.DataTypes;

import java.util.Random;

public class Temperature {
    private float celsius;
    private float fahrenheit;

    public Temperature(){
        Random random = new Random();
        int number = random.nextInt(1000000);
        // Probability of getting heat higher than 39.5 is 1/1000000
        if(number==0){
            celsius = (float) (random.nextFloat()*2.5 + 39.5);
        }

        // Probability of getting heat between 36-37 is 300000/1000000
        else if(number>0&&number<300002) {
            celsius = random.nextFloat() + 36;
        }

        // Probability of getting heat between 37-39.5 is 550000/1000000
        else if(number>300001&&number<850003) {
            celsius = (float) (random.nextFloat()*(1.5) + 37);
        }
        // Probability of getting heat between 35-36 is 149998/1000000
        else if(number>850002&&number<1000000){
            celsius = random.nextFloat() + 35;
        }

        // Probability of getting heat lower than 35 is 1/1000000
        else if(number==1000000){
            celsius = random.nextFloat() + 34;
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
        return String.format("%.2f",celsius);
    }
}
