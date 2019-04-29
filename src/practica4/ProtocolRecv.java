package practica4;

// declareu imports
import ast.protocols.tcp.TCPSegment;
import java.util.ArrayList;
import utils.Channel;

/**
 *
 * @author upcnet
 */
public class ProtocolRecv extends ProtocolBase {

    protected Thread task;
    protected ArrayList<TSocketRecv> sockets;

    public ProtocolRecv(Channel ch) {
        super(ch);
        sockets = new ArrayList<TSocketRecv>();
        task = new Thread(new ReceiverTask());
        task.start();
    }

    public TSocketRecv openForInput(int localPort, int remotePort) {
        lk.lock();
        try {
            TSocketRecv s = new TSocketRecv(this, localPort, remotePort);
            sockets.add(s);
            return s;
        }
        finally {
            lk.unlock();
        }
    }

    protected void ipInput(TCPSegment segment) {
        TSocketRecv s = this.getMatchingTSocket(segment.getDestinationPort(), segment.getSourcePort());
        //System.out.println(s);
        if (s != null) s.processReceivedSegment(segment);
    }

    protected TSocketRecv getMatchingTSocket(int localPort, int remotePort) {
        lk.lock();
        TSocketRecv socket = null;
        try {
            for (TSocketRecv s : sockets) {
                if (s.localPort == localPort && s.remotePort == remotePort)
                    socket = s;
            }
            return socket;
        }
        finally {
            lk.unlock();
        }
    }

    class ReceiverTask implements Runnable {
        public void run() {
            while (true) {
                TCPSegment rseg = channel.receive();
                ipInput(rseg);
            }
        }
    }
}
