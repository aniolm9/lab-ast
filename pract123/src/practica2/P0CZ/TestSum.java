package practica2.P0CZ;

import static java.lang.Thread.sleep;

public class TestSum {

    public static void main(String[] args) throws InterruptedException {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        
        CounterThread th1 = new CounterThread();
        th1.start();
        
        CounterThread th2 = new CounterThread();
        th2.start();
        
        sleep(100);
        System.out.println(CounterThread.x);
    }
}

