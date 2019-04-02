package practica2.Protocol;

import ast.protocols.tcp.TCPSegment;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import practica1.CircularQ.CircularQueue;
import utils.Channel;

public class MonitorChannel implements Channel {

    protected int N;
    protected Lock l;
    protected Condition rQueue, sQueue;
    protected CircularQueue<TCPSegment> cq;

    public MonitorChannel(int N) {
        this.N = N;
        this.l = new ReentrantLock();
        this.rQueue = l.newCondition();
        this.sQueue = l.newCondition();
        this.cq = new CircularQueue(N);
    }

    @Override
    public void send(TCPSegment seg) {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        l.lock();
        try {
            while(cq.full()) {
                sQueue.await();
            }
            cq.put(seg);
            rQueue.signalAll();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            l.unlock();
        }
    }

    @Override
    public TCPSegment receive() {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        TCPSegment tmp = null;
        l.lock();
        try {
            while (cq.empty()) {
                rQueue.await();
            }
            tmp = cq.get();
            sQueue.signalAll();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            l.unlock();
            return tmp;
        }
    }

    @Override
    public int getMSS() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

}
