package practica2.P0CZ.Monitor;

import static java.lang.Thread.sleep;

public class TestSumCZ {

    public static void main(String[] args) throws InterruptedException {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        
        MonitorCZ m = new MonitorCZ();
        
        CounterThreadCZ th1 = new CounterThreadCZ(m);
        th1.start();
        
        CounterThreadCZ th2 = new CounterThreadCZ(m);
        th2.start();
        
        th1.join();
        th2.join();
        System.out.println(m.getX());
        
    }
}
