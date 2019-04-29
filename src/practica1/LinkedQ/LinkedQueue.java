package practica1.LinkedQ;

import java.util.Iterator;
import utils.Queue;

public class LinkedQueue<E> implements Queue<E> {
    private final Node<E>[] queue;
    private final int N;
    private int nelem; 
    private int first; 
    private int last; 
    
    
    public LinkedQueue(int N) {
        this.N = N;
        queue = (Node[]) (new Node[N]);
    }
    
    @Override
    public int size() {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        return this.nelem;
    }

    @Override
    public int free() {
        //throw new UnsupportedOperationException("Not applies");
        return N-nelem; 
    }

    @Override
    public boolean hasFree(int n) {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        return nelem < N;
    }

    @Override
    public boolean empty() {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        return nelem == 0; 
    }

    @Override
    public boolean full() {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        return nelem == N; 
    }

    @Override
    public E peekFirst() {
       //throw new RuntimeException("Aquest mètode s'ha de completar...");
       if (empty()) throw new IllegalArgumentException("You are trying to acess an empty array");
       return this.queue[first].getValue();
    }

    @Override
    public E peekLast() {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        if (empty()) throw new IllegalArgumentException("You are trying to acess an empty array");
        return this.queue[last-1].getValue();
    }

    @Override
    public E get() {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        if (empty()) throw new IllegalArgumentException("You are trying to acess an empty array");
        first++;
        nelem--;
        this.queue[last-1].setNext(this.queue[first]);
        return this.queue[first-1].getValue();
    }

    @Override
    public void put(E value) {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        if (full()) throw new IllegalArgumentException("You are trying to acess an empty array");
        this.queue[last] = new Node();
        this.queue[last].setValue(value);
        this.queue[last].setNext(this.queue[first]);
        nelem++;
        last++;
        
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    class MyIterator implements Iterator {

        //Completar
        private int position = first; 

        @Override
        public boolean hasNext() {
            //throw new RuntimeException("Aquest mètode s'ha de completar...");
            return this.position < size();
        }

        @Override
        public E next() {
            //throw new RuntimeException("Aquest mètode s'ha de completar...");
            if (hasNext()) {
                return queue[position++].getValue();
            }
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }
}
