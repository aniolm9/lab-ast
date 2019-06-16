package practica8;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author aniol
 */
public class PontInjust implements Pont {
    int dins;
    boolean sentitActual;
    Lock lk;
    Condition espera;
    
    public PontInjust() {
        dins = 0;
        sentitActual = false;
        lk = new ReentrantLock();
        espera = lk.newCondition();
    }
    
    @Override
    public void entrar(boolean sentit) {
        lk.lock();
        try {
            while (sentit != sentitActual && dins > 0) {
                espera.awaitUninterruptibly();
            }
            sentitActual = sentit;
            dins++;
        }
        finally {
            lk.unlock();
        }
    }
    
    @Override
    public void sortir() {
        lk.lock();
        try {
            dins--;
            if (dins == 0) espera.signalAll();
        }
        finally {
            lk.unlock();
        }
    }
}