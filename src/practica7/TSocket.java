
package practica7;

import ast.protocols.tcp.TCPSegment;

import ast.logging.Log;
import ast.util.CircularQueue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Connection oriented Protocol Control Block.
 *
 * Each instance of TSocket maintains all the status of an endpoint.
 * 
 * Interface for application layer defines methods for passive/active opening and for closing the connection.
 * Interface lower layer defines methods for processing of received segments and for sending of segments.
 * We assume an ideal lower layer with no losses and no errors in packets.
 *
 * State diagram:<pre>
                              +---------+
                              |  CLOSED |-------------
                              +---------+             \
                           LISTEN  |                   \
                           ------  |                    | CONNECT
                                   V                    | -------
                              +---------+               | snd SYN
                              |  LISTEN |               |
                              +---------+          +----------+
                                   |               | SYN_SENT |
                                   |               +----------+
                         rcv SYN   |                    |
                         -------   |                    | rcv SYN
                         snd SYN   |                    | -------
                                   |                    |
                                   V                   /
                              +---------+             /
                              |  ESTAB  |<------------
                              +---------+
                       CLOSE    |     |    rcv FIN
                      -------   |     |    -------
 +---------+          snd FIN  /       \                    +---------+
 |  FIN    |<-----------------           ------------------>|  CLOSE  |
 |  WAIT   |------------------           -------------------|  WAIT   |
 +---------+          rcv FIN  \       /   CLOSE            +---------+
                      -------   |      |  -------
                                |      |  snd FIN 
                                V      V
                              +----------+
                              |  CLOSED  |
                              +----------+
 * </pre>
 *
 * @author AST's teachers
 */
public class TSocket {
    public static Log log = Protocol.log;

    protected Protocol proto;
    protected Lock lk;
    protected Condition appCV;

    protected int localPort;
    protected int remotePort;

    protected int state;
    protected CircularQueue<TSocket> acceptQueue;

    // States of FSM:
    protected final static int CLOSED = 0,
                               LISTEN = 1,
                               SYN_SENT = 2,
                               ESTABLISHED = 3,
                               FIN_WAIT = 4,
                               CLOSE_WAIT = 5;

    /**
     * Create an endpoint bound to the local IP address and the given TCP port.
     * The local IP address is determined by the networking system.
     * @param ch
     */
    protected TSocket(Protocol p, int localPort) {
        lk = new ReentrantLock();
        appCV = lk.newCondition();
        proto = p;
        this.localPort = localPort;
        state = CLOSED;
    }

    /**
     * Passive open
     */
    protected void listen() {
        lk.lock();
        try {
            log.debug("%s->listen()", this);
            acceptQueue = new CircularQueue<TSocket>(5);
            state = LISTEN;
            proto.addListenTSocket(this);
            logDebugState();
        }
        finally {
            lk.unlock();
        }
    }

    public TSocket accept() {
        lk.lock();
        try {
            log.debug("%1$s->accept()", this);
            while (acceptQueue.empty()) {
                appCV.awaitUninterruptibly();
            }
            TSocket accepted = acceptQueue.get();
            log.debug("%1$s->accepted", this);       
            return accepted;
        }
        finally {
            lk.unlock();
        }
    }

    /**
     * Active open
     */
    protected void connect(int remPort) {
        lk.lock();
        try {
            log.debug("%s->connect(%d)", this, remPort);
            remotePort = remPort;
            proto.addActiveTSocket(this);
            log.debug("Added socket %s to active sockets", this);
            // Create and send a SYN segment.
            TCPSegment syn = new TCPSegment();
            syn.setSourcePort(localPort);
            syn.setDestinationPort(remotePort);
            syn.setSyn(true);
            sendSegment(syn);
            this.state = SYN_SENT;
            logDebugState();
            // Wait for ESTABLISHED state.
            while (this.state != ESTABLISHED) {
                appCV.awaitUninterruptibly();
            }
        }
        finally {
            lk.unlock();
        }
    }

    public void close() {
        lk.lock();
        try {
            log.debug("%s->close()", this);
            switch (state) {
                case ESTABLISHED: {
                    TCPSegment fin = new TCPSegment();
                    fin.setSourcePort(localPort);
                    fin.setDestinationPort(remotePort);
                    fin.setFin(true);
                    this.sendSegment(fin);
                    this.state = FIN_WAIT;
                    break;
                }
                // Preguntar si cal fer FIN i si s'han de fer els dos remove.
                case CLOSE_WAIT: {
                    TCPSegment fin = new TCPSegment();
                    fin.setSourcePort(localPort);
                    fin.setDestinationPort(remotePort);
                    fin.setFin(true);
                    this.sendSegment(fin);
                    this.state = CLOSED;
                    proto.removeActiveTSocket(this);
                    //proto.removeListenTSocket(this);
                    break;
                }
                default:
                    log.error("%s->close: connection does not exist", this);
            }
        }
        finally {
            lk.unlock();
        }
    }


    /**
     * Segment arrival.
     * @param rseg segment of received packet
     */
    protected void processReceivedSegment(TCPSegment rseg) {
        lk.lock();
        try {
            log.debug("Received segment.");
            switch (state) {
                case LISTEN: {
                    if (rseg.isSyn()) {
                        log.debug("SYN Segment received in LISTEN state.");
                        // create a new TSocket for new connection and set it to ESTABLISHED state
                        // also set local and remote ports
                        TSocket newConnectionSocket = new TSocket(proto, rseg.getDestinationPort());
                        newConnectionSocket.remotePort = rseg.getSourcePort();
                        newConnectionSocket.state = ESTABLISHED;
                        proto.addActiveTSocket(newConnectionSocket);
                        //System.out.println(newConnectionSocket);

                        // prepare this TSocket to accept the newly created TSocket
                        acceptQueue.put(newConnectionSocket);
                        // acceptQueue isn't empty anymore, we should signal accept()
                        appCV.signalAll();

                        // from the new TSocket send SYN segment for new connection 
                        TCPSegment syn = new TCPSegment();
                        syn.setSourcePort(newConnectionSocket.localPort);
                        syn.setDestinationPort(newConnectionSocket.remotePort);
                        syn.setSyn(true);
                        newConnectionSocket.sendSegment(syn);
                    }
                    logDebugState();
                    break;
                }
                case SYN_SENT: {
                    if (rseg.isSyn()) {
                        // Change the state to ESTABLISHED and signal connect().
                        this.state = ESTABLISHED;
                        appCV.signalAll();
                        logDebugState();
                    }
                    logDebugState();
                    break;
                }
                case ESTABLISHED: {
                    if (rseg.isFin()) {
                        this.state = CLOSE_WAIT;
                    }
                    logDebugState();
                    break;
                }
                case FIN_WAIT: {
                    if (rseg.isFin()) {
                        this.state = CLOSED;
                        proto.removeActiveTSocket(this);
                        //proto.removeListenTSocket(this);
                    }
                    logDebugState();
                    break;
                }
                case CLOSE_WAIT: {
                    // Process segment text
                    if (rseg.getDataLength() > 0) {
                        /*if (state == ESTABLISHED || state == FIN_WAIT) {
                            // Here should go the segment's data processing
                        } else {
                            // This should not occur, since a FIN has been received from the
                            // remote side.  Ignore the segment text.
                        }*/
                        // This should not occur, since a FIN has been received from the
                        // remote side.  Ignore the segment text.
                    }
                    logDebugState();
                    break;
                }
            }
        }
        finally {
            lk.unlock();
        }
    }

    protected void sendSegment(TCPSegment segment) {
        log.debug("%s->sendSegment(%s)", this, segment);
		proto.channel.send(segment);
    }

    protected void logDebugState() {
        if (log.debugEnabled()) {
            log.debug("%s=> state: %s", this, stateToString());
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(proto.channel.getAddr()).append("/{local=").append(localPort);
        buf.append(",remote=").append(remotePort).append("}");
        return buf.toString(); 
    }

    public String stateToString() {
        String sst;
        switch (state) {
            case CLOSED:      sst = "CLOSED"; break;
            case LISTEN:      sst = "LISTEN"; break;
            case SYN_SENT:    sst = "SYN_SENT"; break;
            case ESTABLISHED: sst = "ESTABLISHED"; break;
            case FIN_WAIT:    sst = "FIN_WAIT"; break;
            case CLOSE_WAIT:  sst = "CLOSE_WAIT"; break;
            default: sst = "?";
        }
        return sst;
    }

}

