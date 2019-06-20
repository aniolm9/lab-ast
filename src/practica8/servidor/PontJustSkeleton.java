package practica8.servidor;

import java.net.ServerSocket;
import java.net.Socket;
import practica8.Comms;
import practica8.PontJust;

/**
 *
 * @author aniol
 */
public class PontJustSkeleton {
    public static void main (String[] args) {
        try {
            ServerSocket ss = new ServerSocket(Comms.PORT);
            PontJust p = new PontJust();
            while (true) {
                System.out.println("Esperant connexions...");
                Socket sc = ss.accept();
                new Thread(new PontJustWorker(sc, p)).start();
            }
        }
        catch (Exception e) {}
    }
}
