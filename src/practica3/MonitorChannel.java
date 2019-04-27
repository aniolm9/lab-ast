package practica3;

import ast.protocols.tcp.TCPSegment;
import java.lang.Math;

public class MonitorChannel extends practica2.Protocol.MonitorChannel {
    private double lossRatio;
    
    public MonitorChannel(int N, double lossRatio) {
        super(N);
        this.lossRatio = lossRatio;
    }

    /*@Override
    public void send(TCPSegment seg) {
        throw new RuntimeException("Aquest mètode s'ha de completar...");
    }*/
    @Override
    public void send(TCPSegment seg) {
        double n = Math.random();
        //System.out.println("lossratio=" + lossRatio);
        //System.out.println("n=" + n);
        if (n >= this.lossRatio) {
            l.lock();
            try {
                while (cq.full()) {
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
    }
    
    @Override
    public int getMSS() {
        return 5;
    }

}
