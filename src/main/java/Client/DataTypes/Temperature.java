package Client.DataTypes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Random;

public class Temperature {
    private static HashMap<Double,Integer> represent;
    private double celsius;
    private double fahrenheit;

    public Temperature(){
        Random random = new Random();
        double number  = new BigDecimal(random.nextDouble() * 100).setScale(1, RoundingMode.HALF_UP).doubleValue();;
        // Probability of getting heat higher than 38.7
        if(number>=0 && number<=0.5){
            celsius = new BigDecimal(random.nextDouble()*3.3 + 38.7).setScale(1, RoundingMode.HALF_UP).doubleValue();
        }

        // Probability of getting heat between 36-37
        else if(number>0.5&&number<=30) {
            celsius = new BigDecimal(random.nextDouble() + 36).setScale(1, RoundingMode.HALF_UP).doubleValue();
        }

        // Probability of getting heat between 37-38.7 is 550000/1000000
        else if(number>30&&number<99.5) {
            celsius = new BigDecimal(random.nextDouble()*1.7 + 37).setScale(1, RoundingMode.HALF_UP).doubleValue();
        }
        // Probability of getting heat between 35-36 is 149998/1000000
        else if(number>=99.5&&number<=100){
            celsius = new BigDecimal(random.nextDouble() + 35).setScale(1, RoundingMode.HALF_UP).doubleValue();;
        }
        fahrenheit = (double) (celsius*1.8 + 32);
    }

    public static void initialize(){
        represent = new HashMap<>();
        int i = 1;
        for (double z = 35.0;z<=42;z = z + 0.1){
            z = new BigDecimal(z).setScale(1, RoundingMode.HALF_UP).doubleValue();
            represent.put(z,i++);
        }
    }

    public int getValue(){
        return represent.get(celsius);
    }

    public double getCelsius(){
        return celsius;
    }

    public double getFahrenheit(){
        return fahrenheit;
    }

    public static int getValue(String temperature){
        return represent.get(Double.valueOf(temperature));
    }
    public static void main(String args[]) throws InterruptedException {
        Temperature.initialize();
        Temperature temperature = new Temperature();
    }

    @Override
    public String toString() {
        return String.format("%.1f",celsius);
    }
}
