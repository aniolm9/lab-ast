package practica8.servidor;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import practica8.PontInjust;
import practica8.Comms;

/**
 *
 * @author aniol
 */
public class PontWorker implements Runnable {
    Socket sc;
    PontInjust p;
    
    public PontWorker(Socket sc, PontInjust p) {
        this.sc = sc;
        this.p = p;
    }
    
    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(sc.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(sc.getInputStream());
            
            while (true) {
                int solicitud;
                try {
                    solicitud = ois.readInt();
                    //System.out.println("Sol: " + solicitud);
                }
                catch (EOFException eof) {
                    break;
                }
                switch (solicitud) {
                    case (Comms.ENTRAR): {
                        int sentit = ois.readInt();
                        p.entrar(sentit);
                        oos.writeInt(Comms.OK);
                        oos.flush();
                        break;
                    }
                    case (Comms.SORTIR): {
                        p.sortir();
                        oos.writeInt(Comms.OK);
                        oos.flush();
                        break;
                    }
                    case (Comms.FIN): {
                        sc.close();
                        System.out.println("Ha sortit un client.");
                        return;
                    }
                    default:
                        break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
