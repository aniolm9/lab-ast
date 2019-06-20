package practica8.client;

import java.util.ArrayList;

/**
 *
 * @author aniol
 */
public class TransitJust {
    public static void main (String args[]) {
        int sentit;
        int threads = 5;
        ArrayList<Thread> threadsQueue = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            sentit = (int)((Math.random()*10)%2);
            Thread th = new Thread(new CotxeJust(new PontJustStub(), sentit));
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
