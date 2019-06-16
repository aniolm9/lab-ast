package practica8;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author aniol
 */
public class Pont {
    int dins;
    boolean sentitActual;
    Lock lk;
    Condition espera;
    
    public Pont() {
        dins = 0;
        sentitActual = false;
        lk = new ReentrantLock();
        espera = lk.newCondition();
    }
    
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