package Server;

import java.util.Calendar;

public class userData implements Runnable{
    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.SECOND));
        while (calendar.get(Calendar.SECOND)!=0);
        System.out.println(calendar.get(Calendar.SECOND));
        String id = Thread.currentThread().getName();


    }
}
