package practica1.Protocol;

import practica1.CircularQ.CircularQueue;
import ast.protocols.tcp.TCPSegment;
import utils.Channel;

public class QueueChannel implements Channel {

    private int N;
    private CircularQueue cq;

    public QueueChannel(int N) {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        this.N = N;
        this.cq = new CircularQueue(N);
    }

    @Override
    public void send(TCPSegment s) {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        cq.put(s);
    }

    @Override
    public TCPSegment receive() {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        return (TCPSegment) cq.get();
    }

    @Override
    public int getMSS() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
