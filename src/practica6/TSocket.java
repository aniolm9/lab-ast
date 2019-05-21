
package practica6;

import ast.logging.Log;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ast.protocols.tcp.TCPSegment;
import ast.util.CircularQueue;
import ast.util.Timer;
import java.util.concurrent.TimeUnit;

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
    protected int rcvWindow;
    protected static final int SND_RTO = 500;
    protected Timer timerService;
    protected Timer.Task sndRtTimer;
    protected int sndNxt;   
    protected TCPSegment sndUnackedSegment;
    
    
    // Receiver variables:
    protected CircularQueue<TCPSegment> rcvQueue;
    protected int rcvSegConsumedBytes;
    protected int rcvNxt;        
   
    
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
        // init receiver variables
        rcvQueue = new CircularQueue<TCPSegment>(RCV_QUEUE_SIZE);
        rcvWindow = RCV_QUEUE_SIZE;
        timerService = new Timer();

        //Other necessary initializations
        this.sndUnackedSegment = null;
        this.sndNxt = 0;
    }


    // -------------  SENDER PART  ---------------
    public void sendData(byte[] data, int offset, int length) {
        lk.lock();
        try {
            log.debug("%s->sendData(length=%d)", this, length);
            while (length > 0) {
                while (sndUnackedSegment != null) {
                    appCV.await();
                }
                
                int dataLength = Math.min(length, sndMSS);
                if (rcvWindow == 0) dataLength = 1; // Ojo amb aixo
                sndUnackedSegment = this.segmentize(data, offset, dataLength);
                offset += dataLength;
                length -= dataLength;
                this.sendSegment(sndUnackedSegment);
                this.startRTO();
                System.out.println("Segment enviat.");
                sndNxt = (sndNxt + 1) % 2;
                appCV.signalAll();
            }
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
        segment.setSeqNum(sndNxt);
        return segment;
    }

    protected void sendSegment(TCPSegment segment) {
        log.debug("%s->sendSegment(%s)", this, segment);
        proto.net.send(segment);
    }

    protected void timeout() {
      lk.lock();
      try {
          if (sndUnackedSegment != null) {
            System.out.println("torno a enviar segment DADES: "+ sndUnackedSegment);
            sendSegment(sndUnackedSegment);
            startRTO();
          }
      }
      finally {
          lk.unlock();
      }
    }

    protected void startRTO() {
        if (sndRtTimer != null) {
            sndRtTimer.cancel();
      }
      sndRtTimer = timerService.startAfter(
        new Runnable() {
          @Override
          public void run() {
              timeout();
          }
        },
        SND_RTO, TimeUnit.MILLISECONDS);
    }

    protected void stopRTO() {
        if (sndRtTimer != null) {
            sndRtTimer.cancel();
        }
        sndRtTimer = null;
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
        }
        catch (Exception e) {
            e.printStackTrace();
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
        ack.setAckNum(rcvNxt);
        ack.setWindow(rcvQueue.free());
        ack.setDestinationPort(remotePort);
        ack.setSourcePort(localPort);
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
                if (rseg.getAckNum() == (sndUnackedSegment.getSeqNum()+1)%2) {
                    System.out.println("ACK rebut");
                    sndUnackedSegment = null;
                    sndNxt = rseg.getAckNum();
                    rcvWindow = rseg.getWindow();
                    System.out.println("Window: " + rcvWindow);
                }
                appCV.signalAll();
            }
            else if (rseg.getDataLength() > 0) {
                // Process segment data
                System.out.println("Segment rebut.");
                if (rcvQueue.full()) {
                    log.warn("%s->processReceivedSegment: no free space: %d lost bytes",
                                this, rseg.getDataLength());
                    return;
                }
                if (rseg.getSeqNum() != rcvNxt) {
                    sendAck();
                    return;
                }
                rcvQueue.put(rseg);
                rcvNxt = (rcvNxt + 1) % 2;
                sendAck();
                System.out.println("ACK enviat");
                appCV.signalAll();
            }
        } finally {
            lk.unlock();
        }
    }


    // -------------  LOG SUPPORT  ---------------
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(proto.net.getAddr()).append("/{local=").append(localPort);
        buf.append(",remote=").append(remotePort).append("}");
        return buf.toString(); 
    }


}
