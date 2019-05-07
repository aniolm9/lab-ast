
package practica5;

import ast.logging.Log;
import ast.protocols.tcp.TCPSegment;
import ast.util.CircularQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author AST's professors
 */
public class TSocket {
    public static Log log = Protocol.log;
    protected static final int RCV_QUEUE_SIZE = 3;

    protected Protocol proto;
    protected Lock lk;
    protected Condition appCV;

    protected int localPort;
    protected int remotePort;
    
    // Sender variables:
    protected int sndMSS;       // Send maximum segment size
    protected boolean segmentAcknowledged; // segment not yet acknowledged ?
    protected int rcvWindow;

    // Receiver variables:
    protected CircularQueue<TCPSegment> rcvQueue;
    protected int rcvSegConsumedBytes;

    //Other atributes (sender or receiver)
    //...
    
    /**
     * Create an endpoint bound to the given TCP ports.
     */
    protected TSocket(Protocol p, int localPort, int remotePort) {
        lk = new ReentrantLock();
        appCV = lk.newCondition();
        proto = p;
        this.localPort = localPort;
        this.remotePort = remotePort;
        // init sender variables
        sndMSS = p.net.getMSS() - TCPSegment.HEADER_SIZE; // IP maximum message size - TCP header size
        segmentAcknowledged = false;
        // init receiver variables
        rcvQueue = new CircularQueue<TCPSegment>(RCV_QUEUE_SIZE);
        rcvSegConsumedBytes = 0;
        rcvWindow = RCV_QUEUE_SIZE;
        //Other necessary initializations
        //...
    }


    // -------------  SENDER PART  ---------------
    public void sendData(byte[] data, int offset, int length) {
        lk.lock();
        // Pendent implementar cas finestra 0.
        try {
            log.debug("%s->sendData(length=%d)", this, length);
            while (!segmentAcknowledged) {
                appCV.await();
            }
            int dataLength = length;
            while (rcvWindow > 0 && dataLength >= sndMSS) {
                this.sendSegment(this.segmentize(data, offset, sndMSS));
                //rcvWindow--;
                offset += sndMSS;
                dataLength -= sndMSS;
            }
            if (length > 0 && rcvWindow > 0) {
                this.sendSegment(this.segmentize(data, offset, dataLength));
                //rcvWindow--;
            }
            // for each segment to send
                // wait until the sender is not expecting an acknowledgement
                // create a data segment and send it
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        finally {
            lk.unlock();
        }
    }

    protected TCPSegment segmentize(byte[] data, int offset, int length) {
        byte[] data_copy = new byte[length];
        TCPSegment segment = new TCPSegment();
        System.arraycopy(data, offset, data_copy, 0, length);
        segment.setData(data_copy, 0, length);
        segment.setDestinationPort(remotePort);
        segment.setSourcePort(localPort);
        return segment;
    }

    protected void sendSegment(TCPSegment segment) {
        log.debug("%s->sendSegment(%s)", this, segment);
        proto.net.send(segment);
    }


    // -------------  RECEIVER PART  ---------------
    /**
     * Places received data in buf
     */
    public int receiveData(byte[] buf, int offset, int maxlen) {
        lk.lock();
        int n = 0;
        try {
            log.debug("%s->receiveData(maxlen=%d)", this, maxlen);
            // A completar per l'estudiant:
            while (rcvQueue.empty()) {
                appCV.await();
            }
            while (n < maxlen && !rcvQueue.empty()) {
                n += consumeSegment(buf, offset+n, maxlen-n);
            }
            // wait until there is a received segment
            // get data from the received segment
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        finally {
            lk.unlock();
            return n;
        }
    }

    protected int consumeSegment(byte[] buf, int offset, int length) {
        TCPSegment seg = rcvQueue.peekFirst();
        // get data from seg and copy to receiveData's buffer
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

    protected void sendAck() {
               // A completar per l'estudiant:
        TCPSegment ack = new TCPSegment();
        ack.setAck(true);
        //ack.setAckNum();
        ack.setDestinationPort(remotePort);
        ack.setSourcePort(localPort);
        //ack.setSeqNum();
        ack.setWindow(rcvWindow);
        this.sendSegment(ack);
    }


    // -------------  SEGMENT ARRIVAL  -------------
    /**
     * Segment arrival.
     * @param rseg segment of received packet
     */
    protected void processReceivedSegment(TCPSegment rseg) {
        lk.lock();
        try {
            // Check ACK
            if (rseg.isAck()) {
                // A completar per l'estudiant:
                ...
                //segmentAcknowledged = true;
                //appCV.signalAll();
                logDebugState();
            } else if (rseg.getDataLength() > 0) {
                // Process segment data
                if (rcvQueue.full()) {
                    log.warn("%s->processReceivedSegment: no free space: %d lost bytes",
                                this, rseg.getDataLength());
                    return;
                }
                // A completar per l'estudiant:
                ...
                logDebugState();
            }
        } finally {
            lk.unlock();
        }
    }


    // -------------  LOG SUPPORT  ---------------
    protected void logDebugState() {
        if (log.debugEnabled()) {
            log.debug("%s=> state: %s", this, stateToString());
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(proto.net.getAddr()).append("/{local=").append(localPort);
        buf.append(",remote=").append(remotePort).append("}");
        return buf.toString(); 
    }

    public String stateToString() {
        StringBuilder buf = new StringBuilder();
        buf.append("{sndIsUna=").append(sndIsUna);
        if (rcvSegment == null) {
            buf.append(",rcvSegment=null");
        } else {
            buf.append(",rcvSegment.dataLength=").append(rcvSegment.getDataLength());
            buf.append(",rcvSegConsumedBytes=").append(rcvSegConsumedBytes);
        }
        return buf.append("}").toString();
    }

}
