package practica8.client;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aniol
 */
public class CotxeJust implements Runnable {
    PontJustStub p;
    int sentit;
    
    public CotxeJust(PontJustStub p, int sentit) {
        this.p = p;
        this.sentit = sentit;
    }
    
    @Override
    public void run() {
        p.entrar(sentit);
        System.out.println("Fiuuuuu.");
        // Wait 5 seconds.
        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Cotxe.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        p.sortir();
        System.out.println("Fooouuum.");
        p.close();
        //System.out.println("He tancat.");
    }
}
