package practica1.LinkedQ;

import java.util.Iterator;

public class TestLQ {

    public static void main(String[] args) {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        LinkedQueue queue = new LinkedQueue(10);
        Iterator<Integer> it = queue.iterator();
        for (int i = 0; i <= 9; i++) {
            queue.put(i);
            //System.out.println(i);
        }
        while (it.hasNext()) {
            Integer next = it.next();
            System.out.print(next);
            System.out.print(',');
        }
        System.out.print('\n');
        
        System.out.println("Removing item " + queue.get() + "...");
        System.out.println("Done.");
        
        it = queue.iterator();
        while (it.hasNext()) {
            Integer next = it.next();
            System.out.print(next);
            System.out.print(',');
        }
        System.out.print('\n');
        
    }
}
