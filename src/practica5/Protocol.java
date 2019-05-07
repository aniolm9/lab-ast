
package practica5;

// define imports

import ast.logging.Log;
import ast.logging.LogFactory;
import ast.protocols.tcp.TCPSegment;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import utils.FDuplexChannel;


/**
 *
 * @author upcnet
 */
public class Protocol {
    public static Log log = LogFactory.getLog(Protocol.class);

    protected ArrayList<TSocket> sockets;
    protected Thread task;
    protected Lock lk;
    protected FDuplexChannel.Peer net;

    public Protocol(FDuplexChannel.Peer ch) {
        sockets = new ArrayList<TSocket>();
        task = new Thread(new ReceiverTask());
        task.start();
        lk = new ReentrantLock();
        net = ch;
    }

    public TSocket openWith(int localPort, int remotePort) {
        lk.lock();
        try {
            TSocket ts = new TSocket(this, localPort, remotePort);
            sockets.add(ts);
            return ts;
        } finally {
            lk.unlock();
        }
    }

    protected void ipInput(TCPSegment segment) {
        TSocket s = this.getMatchingTSocket(segment.getDestinationPort(), segment.getSourcePort());
        if (s != null) s.processReceivedSegment(segment);
    }

    protected TSocket getMatchingTSocket(int localPort, int remotePort) {
        lk.lock();
        try {
            TSocket socket = null;
            for (TSocket s : sockets) {
                if (s.localPort == localPort && s.remotePort == remotePort) {
                    socket = s;
                    break;
                }
            }
            return socket;
        } finally {
            lk.unlock();
        }
    }

    class ReceiverTask implements Runnable {
        public void run() {
            while (true) {
                TCPSegment rseg = net.receive();
                ipInput(rseg);
            }
        }
    }


}
