package practica2.P0CZ;

public class CounterThread extends Thread {

    public static int x;
    private final int I = 5000; // If I>1000 it will probably fail.

    @Override
    public void run() {
        for (int i = 0; i < I; i++) {
            x = x + 1;
        }
        //System.out.println(CounterThread.x);
    }
}
