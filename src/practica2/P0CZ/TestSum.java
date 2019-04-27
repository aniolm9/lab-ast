package practica2.P0CZ;

import static java.lang.Thread.sleep;

public class TestSum {

    public static void main(String[] args) throws InterruptedException {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        
        /*CounterThread th1 = new CounterThread();
        th1.start();
        
        CounterThread th2 = new CounterThread();
        th2.start();*/
        
        CounterThread_fail th3 = new CounterThread_fail();
        th3.start();
        
        CounterThread_fail th4 = new CounterThread_fail();
        th4.start();
        
        //th1.join();
        //th2.join();
        th3.join();
        th4.join();
        //System.out.println(CounterThread.x);
        System.out.println(CounterThread_fail.x);
    }
}

