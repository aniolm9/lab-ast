package practica4;

// declareu imports

import ast.protocols.tcp.TCPSegment;
import practica1.CircularQ.CircularQueue;


/**
 * Socket for receiving endpoint.
 *
 * @author upcnet
 */
public class TSocketRecv extends TSocketBase {

    protected CircularQueue<TCPSegment> rcvQueue;
    protected int rcvSegConsumedBytes;

    /**
     * Create an endpoint bound to the local IP address and the given TCP port.
     * The local IP address is determined by the networking system.
     * @param ch
     */
    protected TSocketRecv(ProtocolRecv p, int localPort, int remotePort) {
        super(p, localPort, remotePort);
        rcvQueue = new CircularQueue<TCPSegment>(20);
        rcvSegConsumedBytes = 0;
    }

    /**
     * Places received data in buf
     */
    public int receiveData(byte[] buf, int offset, int length) {
        int n = 0;
        lk.lock();
        try {
            //throw new RuntimeException("Aquest mètode s'ha de completar...");
            //System.out.println("receiveData length: " + length);
            while (rcvQueue.empty()) {
                appCV.await();
            }
            while (n < length && !rcvQueue.empty()) {
                n += consumeSegment(buf, offset+n, length-n);
                //System.out.println(n);
            }
        } finally {
            lk.unlock();
            return n;
        }
    }

    protected int consumeSegment(byte[] buf, int offset, int length) {
        TCPSegment seg = rcvQueue.peekFirst();
        // getCnd data from seg and copy to receiveData's buffer
        int n = seg.getDataLength() - rcvSegConsumedBytes;
        if (n > length) {
            // receiveData's buffer is small. Consume a fragment of the received segment
            n = length;
        }
        // n == min(length, seg.getDataLength() - rcvSegConsumedBytes)
        System.arraycopy(seg.getData(), seg.getDataOffset() + rcvSegConsumedBytes, buf, offset, n);
        rcvSegConsumedBytes += n;
        if (rcvSegConsumedBytes == seg.getDataLength()) {
            // seg is totally consumed. Remove from rcvQueue
            rcvQueue.get();
            rcvSegConsumedBytes = 0;
        }
        return n;
    }

    /**
     * TCPSegment arrival.
     *
     * @param rseg segment of received packet
     */
    public void processReceivedSegment(TCPSegment rseg) {
        lk.lock();
        try {
            //throw new RuntimeException("Aquest mètode s'ha de completar...");
            if (!rcvQueue.full()) {
                rcvQueue.put(rseg);
                appCV.signalAll();
            }
        } finally {
            lk.unlock();
        }
    }
}




