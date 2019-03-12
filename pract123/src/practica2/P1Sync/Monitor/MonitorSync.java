package practica2.P1Sync.Monitor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MonitorSync {

    private final int N;
    private Lock l;
    private Condition[] queue;
    private int current;

    public MonitorSync(int N) {
        this.N = N;
        this.l = new ReentrantLock();
        this.queue = new Condition[N];
        for (int i = 0; i < N; i++) {
            queue[i] = l.newCondition();
        }
    }

    public void waitForTurn(int id) {
        l.lock();
        current = id;
        l.unlock();
    }
    
    public void transferTurn() {
        l.lock();
        try {
            queue[current].await();
        }
        catch (InterruptedException ex) {
            Logger.getLogger(MonitorSync.class.getName()).log(Level.SEVERE, null, ex);
        }
        queue[current+1].signal();
        l.unlock();
        
    }
}
