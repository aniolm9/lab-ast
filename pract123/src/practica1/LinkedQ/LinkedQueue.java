package practica1.LinkedQ;

import java.util.Iterator;
import utils.Queue;

public class LinkedQueue<E> implements Queue<E> {
    private final Node[] queue;
    private final int N;
    
    public LinkedQueue(int N) {
        this.N = N;
        queue = (Node[]) (new Object[N]);
    }
    
    @Override
    public int size() {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        return this.N;
    }

    @Override
    public int free() {
        throw new UnsupportedOperationException("Not applies");
    }

    @Override
    public boolean hasFree(int n) {
        throw new RuntimeException("Aquest mètode s'ha de completar...");
    }

    @Override
    public boolean empty() {
        throw new RuntimeException("Aquest mètode s'ha de completar...");
    }

    @Override
    public boolean full() {
        throw new RuntimeException("Aquest mètode s'ha de completar...");
    }

    @Override
    public E peekFirst() {
        throw new RuntimeException("Aquest mètode s'ha de completar...");
    }

    @Override
    public E peekLast() {
        throw new RuntimeException("Aquest mètode s'ha de completar...");
    }

    @Override
    public E get() {
        throw new RuntimeException("Aquest mètode s'ha de completar...");
    }

    @Override
    public void put(E value) {
        throw new RuntimeException("Aquest mètode s'ha de completar...");
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    class MyIterator implements Iterator {

        //Completar

        @Override
        public boolean hasNext() {
            throw new RuntimeException("Aquest mètode s'ha de completar...");
        }

        @Override
        public E next() {
            throw new RuntimeException("Aquest mètode s'ha de completar...");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }
}
