package practica8.client;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aniol
 */
public class Cotxe implements Runnable {
    PontStub p;
    boolean sentit;
    
    public Cotxe(PontStub p, boolean sentit) {
        this.p = p;
        this.sentit = sentit;
    }
    
    @Override
    public void run() {
        p.entrar(sentit);
        System.out.println("He entrat.");
        // Wait 5 seconds.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Cotxe.class.getName()).log(Level.SEVERE, null, ex);
        }
        p.sortir();
        System.out.println("He sortit.");
        p.close();
        System.out.println("Tancat.");
    }
    
}
