package practica1.CircularQ;

import java.util.Iterator;

public class TestCQ {

    public static void main(String[] args) {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        CircularQueue cq = new CircularQueue(10);
        Iterator<Integer> it = cq.iterator();
        for (int i = 0; i <= 9; i++) {
            cq.put(i);
        }
        while (it.hasNext()) {
            Integer next = it.next();
            System.out.print(next);
            System.out.print(',');
        }
        System.out.print('\n');
    }
}
