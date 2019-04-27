package practica2.P0CZ.Monitor;

import java.util.concurrent.locks.ReentrantLock;

public class MonitorCZ {

    private int x = 0;
    ReentrantLock lock = new ReentrantLock();

    public void inc() {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        lock.lock();
        x = x + 1;
        lock.unlock();
    }

    public int getX() {
        lock.lock();
        try {
            return x;
        } finally {
            lock.unlock();
        }
    }

}
