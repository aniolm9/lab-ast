package practica8;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author aniol
 */
public class PontJust implements Pont {
    Lock lk;
    Condition[] cuaEspera;
    int[] esperant;
    int sentitActual, dins;
    
    public PontJust () {
        lk = new ReentrantLock();
        cuaEspera = new Condition[2];
        cuaEspera[0] = lk.newCondition();
        cuaEspera[1] = lk.newCondition();
        esperant = new int[2];
    }
    
    @Override
    public void entrar (int sentit) {
    
    }
    
    @Override
    public void sortir () {
        
    }
    
}
