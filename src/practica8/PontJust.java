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
        lk.lock();
        try {
            if (dins > 0 && sentitActual != sentit || esperant[1-sentit] > 0) {
                esperant[sentit]++;
                cuaEspera[sentit].awaitUninterruptibly();
            }
            else {
                dins++;
            }
            sentitActual = sentit;
        }
        finally {
            lk.unlock();
        }
    }
    
    @Override
    public void sortir () {
        lk.lock();
        try {
            dins--;
            if (dins == 0 && esperant[1-sentitActual] > 0) {
                sentitActual = 1 - sentitActual;
                dins = esperant[sentitActual];
                esperant[sentitActual] = 0;
                cuaEspera[sentitActual].signalAll();
            }
        }
        finally {
            lk.unlock();
        }
    }
}
