package practica8.client;

/**
 *
 * @author aniol
 */
public class Transit {
    public static void main (String args[]) {
        PontStub p = new PontStub();
        boolean sentit = true;
        int threads = 1;
        for (int i = 0; i < threads; i++) {
            sentit = !sentit;
            new Thread(new Cotxe(p, sentit)).start();
        }
       // p.close();
    }
}
