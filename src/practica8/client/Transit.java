package practica8.client;

import java.util.ArrayList;

/**
 *
 * @author aniol
 */
public class Transit {
    public static void main (String args[]) {
        boolean sentit = true;
        int threads = 2;
        ArrayList<Thread> threadsQueue = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            sentit = !sentit;
            Thread th = new Thread(new Cotxe(new PontStub(), sentit));
            threadsQueue.add(th);
            th.start();
        }
        for (Thread th : threadsQueue) {
            try {
                th.join();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}