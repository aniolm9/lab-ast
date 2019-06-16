package practica8.servidor;

import practica8.Communication;
import java.net.ServerSocket;
import java.net.Socket;
import practica8.PontInjust;

/**
 *
 * @author aniol
 */
public class PontSkeleton {
    public static void main(String args[]) {
        try {
            ServerSocket ss = new ServerSocket(Communication.PORT);
            PontInjust p = new PontInjust();
            
            while (true) {
                System.out.println("Esperant connexions de clients...");
                Socket sc = ss.accept();
                // Start a new worker.
                /*PontWorker worker = new PontWorker(sc, p);
                Thread th = new Thread(worker);
                th.start();*/
                System.out.println("Ha entrat un client.");
                new Thread(new PontWorker(sc, p)).start();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
