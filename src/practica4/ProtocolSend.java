package ast.practica4;

// declareu imports
...

/**
 *
 * @author upcnet
 */
public class ProtocolSend extends ProtocolBase {

  protected ArrayList<TSocketSend> sockets;

  public ProtocolSend(Channel ch) {
    super(ch);
    sockets = new ArrayList<TSocketSend>();
  }

  public TSocketSend openForOutput(int localPort, int remotePort) {
    lk.lock();
    try {

      //...
	  
      //treu aquesta sentencia en completar el codi:
      return null;
	  
    } finally {
      lk.unlock();
    }
  }
}
