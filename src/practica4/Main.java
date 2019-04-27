
package practica4;

// declareu imports
import utils.Channel;
import practica2.Protocol.MonitorChannel; 

public class Main {

    public static void main(String[] args){
            Channel c = new MonitorChannel(20);

            ProtocolRecv proto1 = new ProtocolRecv(c);
            new Thread(new Host1(proto1)).start();

            ProtocolSend proto2 = new ProtocolSend(c);
            new Thread(new Host2(proto2)).start();
    }
}


class Host1 implements Runnable {

    public static final int PORT = 10;

    protected ProtocolRecv proto;

    public Host1(ProtocolRecv proto) {
        this.proto = proto;
    }

    @Override
    public void run() {
      //arranca dos fils receptors, cadascun amb el seu socket de recepcio
      //fes servir els ports apropiats
      TSocketRecv r1 = proto.openForInput(PORT, Host2.PORT1);
      TSocketRecv r2 = proto.openForInput(PORT, Host2.PORT2);
      
      Thread th1 = new Thread(new Receiver(r1));
      Thread th2 = new Thread(new Receiver(r2));
      
      th1.start();
      th2.start();
      
    }
}


class Host2 implements Runnable {

    public static final int PORT1 = 10;
    public static final int PORT2 = 50;

    protected ProtocolSend proto;
    
    public Host2(ProtocolSend proto) {
        this.proto = proto;
    }
    
    public void run() {
      //arranca dos fils emissors, cadascun amb el seu socket de transmissio
      //fes servir els ports apropiats
      //...
      TSocketSend s1 = proto.openForOutput(PORT1, Host1.PORT);
      TSocketSend s2 = proto.openForOutput(PORT2, Host1.PORT);
      
      Thread th3 = new Thread(new Sender(s1));
      Thread th4 = new Thread(new Sender(s2));
      
      th3.start();
      th4.start();
    }
    
}


