package practica2.P1Sync.Monitor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MonitorSync {
    private final int N;
    private Lock l;
    private int currentID;
    private Condition c;

    public MonitorSync(int N) {
        this.N = N;
        this.l = new ReentrantLock();
        this.c = l.newCondition();
        this.currentID = 0;
    }

    public void waitForTurn(int id) {
        l.lock();
        try {
            while(this.currentID != id) {
                c.await();
            }
        }
        catch (InterruptedException ex) {
            Logger.getLogger(MonitorSync.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            l.unlock();
        }
    }

    public void transferTurn() {
        l.lock();
        this.currentID = (this.currentID+1) % N; 
        c.signalAll();
        l.unlock();
    }
}
