package Estruturas;

import java.util.Iterator;
import Estruturas.Exceptions.NoSuchElementException;
import Estruturas.Exceptions.EmptyCollectionException;
import Estruturas.Interfaces.ListADT;

public class LinkedList<T> implements ListADT<T>, Iterable<T> {
    LinearNode<T> head;
    
    /**
     * removeFirst removes the first element from the list.
     * 
     * @return the first element from the list
     */
    @Override
    public T removeFirst() {
        if (head == null) {
            return null;
        }

        T element = head.getElement();
        head = head.getNext();
        return element;
    }

    /**
     * removeLast removes the last element from the list.
     * 
     * @return the last element from the list
     */
    @Override
    public T removeLast() {
        if (head == null) {
            return null;
        }

        if (head.getNext() == null) {
            T element = head.getElement();
            head = null;
            return element;
        }

        LinearNode<T> current = head;
        LinearNode<T> previous = null;

        while (current.getNext() != null) {
            previous = current;
            current = current.getNext();
        }

        previous.setNext(null);
        return current.getElement();
    }

    /**
     * remove removes the specified element from the list.
     * 
     * @param element the element to be removed from the list
     * @return the element that was removed from the list
     */
    public T remove(T element) throws EmptyCollectionException, NoSuchElementException {
        if (head == null) {
            throw new EmptyCollectionException("The list is empty.");
        }

        if (head.getElement().equals(element)) {
            T result = head.getElement();
            head = head.getNext();
            return result;
        }

        LinearNode<T> current = head;
        LinearNode<T> previous = null;

        while (current != null && !current.getElement().equals(element)) {
            previous = current;
            current = current.getNext();
        }

        if (current == null) {
            throw new NoSuchElementException();
        }

        previous.setNext(current.getNext());
        return current.getElement();
    }
    
    /**
     * Returns a reference to the first element in this list.
     * 
     * @return a reference to the first element in this list
     */
    @Override
    public T first() {
        if (head == null) {
            return null;
        }
        return head.getElement();
    }

    /**
     * Returns a reference to the last element in this list.
     * 
     * @return a reference to the last element in this list
     */
    @Override
    public T last() {
        if (head == null) {
            return null;
        }

        LinearNode<T> current = head;
        while (current.getNext() != null) {
            current = current.getNext();
        }
        return current.getElement();
    }

    /**
     * Returns true if this list contains the specified target element.
     * 
     * @param data the target that is being sought in the list
     * @return true if the list contains this element
     */
    @Override
    public boolean contains(T data) {
        LinearNode<T> current = head;
        while (current != null) {
            if (current.getElement().equals(data)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    /**
     * Returns true if this list contains no elements.
     * 
     * @return true if this list contains no elements
     */
    public boolean isEmpty() {
        return head == null;
    }
    
    /**
     * Returns the number of elements in this list.
     * 
     * @return the integer representation of number of elements in this list
     */
    public int size() {
        int count = 0;
        LinearNode<T> current = head;
        while (current != null) {
            count++;
            current = current.getNext();
        }
        return count;
    }

    /**
     * Returns an iterator for the elements in this list.
     * 
     * @return an iterator over the elements in this list
     */
    @Override
    public Iterator<T> iterator() {
        return new LinkedIterator<T>(head, size());
    }

}
