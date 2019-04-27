package ast.practica4;

// declareu imports
...

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
	
      //...
	  
      //treu aquesta sentencia en completar el codi:
      return null;
	  
    } finally {
      lk.unlock();
    }
  }

  protected void ipInput(TCPSegment segment) {
  
    //...
	
  }

  protected TSocketRecv getMatchingTSocket(int localPort, int remotePort) {
    lk.lock();
    try {
	
      //...
	  
      return null;
    } finally {
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