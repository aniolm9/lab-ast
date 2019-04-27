package practica1.CircularQ;

import java.util.Iterator;
import utils.Queue;

public class CircularQueue<E> implements Queue<E> {

    private final E[] queue;
    private final int N;
    private int head;
    private int tail;
    private int nelem;

    public CircularQueue(int N) {
        this.N = N;
        queue = (E[]) (new Object[N]);
    }

    @Override
    public int size() {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        return this.N;
    }

    @Override
    public int free() {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        return (this.N - this.nelem);
    }

    @Override
    public boolean hasFree(int n) {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        return this.nelem < this.N;
    }

    @Override
    public boolean empty() {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        return this.nelem == 0;
    }

    @Override
    public boolean full() {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        return this.nelem == this.N;
    }

    @Override
    public E peekFirst() {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        if (empty()) throw new IllegalArgumentException("You are trying to acess an empty array");
        return this.queue[this.head];
    }

    @Override
    public E peekLast() {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        if (empty()) throw new IllegalArgumentException("You are trying to acess an empty array");
        return this.queue[(N+this.tail-1) % N];
    }

    @Override
    public E get() {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        if (empty()) throw new IllegalArgumentException("You are trying to acess an empty array");
        this.head = (this.head + 1) % N;
        this.nelem--;
        return this.queue[(N+this.head-1) % N];
    }

    @Override
    public void put(E e) {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        if (this.full()) throw new IllegalArgumentException("You are trying to acess a full array");
        this.queue[this.tail] = e;
        this.tail = (this.tail + 1) % this.N;
        this.nelem++;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();

    }

    class MyIterator implements Iterator {
        private int position = head;
          
        @Override
        public boolean hasNext() {
            //throw new RuntimeException("Aquest mètode s'ha de completar...");
            return this.position < size();
            
        }

        @Override
        public E next() {
            //throw new RuntimeException("Aquest mètode s'ha de completar...");
            if (this.hasNext()) {
                return queue[(this.position++) % N];
            }
            else {
                return null;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }
}
