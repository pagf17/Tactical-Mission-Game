package Estruturas;

import java.util.Iterator;
import Estruturas.Exceptions.*;
import Estruturas.Interfaces.ListADT;

public class AbstractList<T> implements ListADT<T>, Iterable<T> {

    private final int DEFAULT_CAPACITY = 100;
    private final int NOT_FOUND = -1;

    protected int modCount;
    protected int rear;
    protected T[] list;

    public AbstractList() {
        rear = 0;
        list = (T[]) (new Object[DEFAULT_CAPACITY]);
        modCount = 0;
    }

    public AbstractList(int initialCapacity) {
        rear = 0;
        list = (T[]) (new Object[initialCapacity]);
    }

    public int size() {
        return this.rear;
    }

    public boolean isEmpty() {
        return (rear == 0);
    }

    @Override
    public T first() {
        return list[0];
    }

    @Override
    public T last() {
        return list[rear - 1];
    }

    @Override
    public boolean contains(T target) {
        return (find(target) != NOT_FOUND);
    }

    public Iterator<T> iterator() {
        return new MyIterator();
    }

    @Override
    public String toString() {

        String result = "";

        for (int scan = 0; scan < rear; scan++) {
            result = result + list[scan].toString() + "\n";
        }
        return result;
    }

    @Override
    public T removeFirst() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("A lista está vazia.");
        }

        T result = list[0]; 
        for (int scan = 0; scan < rear - 1; scan++) {
            list[scan] = list[scan + 1]; 
        }

        list[rear - 1] = null; 
        rear--; 
        modCount++; 

        return result; 
    }

    @Override
    public T removeLast() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("A lista está vazia.");
        }

        rear--;
        T result = list[rear]; 
        list[rear] = null; 
        modCount++;

        return result; 
    }

    @Override
    public T remove(T element) throws ElementNotFoundException {

        T result;
        int index = find(element);

        if (index == NOT_FOUND)
            throw new ElementNotFoundException("ArrayList");

        result = list[index];
        rear--;

        for (int scan = index; scan < rear; scan++)
            list[scan] = list[scan + 1];

        list[rear] = null;
        return result;
    }

    private int find(T target) {
        int scan = 0;
        int result = NOT_FOUND;

        if (!isEmpty())
            while (result == NOT_FOUND && scan < rear)
                if (target.equals(list[scan]))
                    result = scan;
                else
                    scan++;

        return result;
    }

    protected void expandCapacity() {

        T[] newStack = (T[]) (new Object[list.length * 2]);

        for (int i = 0; i < list.length; i++) {
            newStack[i] = list[i];
        }

        list = newStack;
    }

    private class MyIterator implements Iterator<T> {

        private boolean okToRemove;
        private int current, expectedModCount;

        public MyIterator() {
            okToRemove = false;
            expectedModCount = modCount;
            current = 0;
        }

        @Override
        public boolean hasNext() {

            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return (current < rear);
        }

        @Override
        public T next() {

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            this.okToRemove = true;
            ++this.current;
            return list[current - 1];
        }

        @Override
        public void remove() {

            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (!okToRemove) {
                throw new IllegalStateException("Remove não pode ser chamado antes de next.");
            }

            this.okToRemove = false;
            --this.current;

            try {
                AbstractList.this.remove(list[this.current]);
            } catch (ElementNotFoundException ex) {
                System.out.println("Elemento não encontrado");
            }

            ++this.expectedModCount;
        }
    }
}
