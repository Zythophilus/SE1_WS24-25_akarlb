package org.hbrs.se1.ws24.exercises.uebung10;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyPrettyStack<T> implements Stapel<T>, Iterable<T> {

    private T[] stapel;
    private int topIndex;
    private final int maxSize;

    public MyPrettyStack(int size) {
        this.maxSize = size;
        this.stapel = (T[]) new Object[size];
        this.topIndex = -1;
    }

    @Override
    public T top() {
        if (isEmpty()) {
            throw new IllegalStateException("Stapel ist leer!");
        }
        return stapel[topIndex];
    }

    @Override
    public void push(T obj) {
        if (topIndex >= maxSize - 1) {
            throw new IllegalStateException("Stapel ist voll!");
        }
        if (obj == null) throw new NullPointerException();
        stapel[++topIndex] = obj;
    }

    @Override
    public void pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stapel ist leer!");
        }
        topIndex--;
    }

    @Override
    public boolean isEmpty() {
        return topIndex == -1;
    }

    public boolean isFull() {
        return topIndex == maxSize - 1;
    }

    public int size() {
        return topIndex + 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i <= topIndex; i++) {
            sb.append(stapel[i]);
            if (i < topIndex) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new StapelIterator();
    }

    private class StapelIterator implements Iterator<T> {
        private int zeiger = 0;

        @Override
        public boolean hasNext() {
            return zeiger <= topIndex;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return stapel[zeiger++];
        }
    }
}
