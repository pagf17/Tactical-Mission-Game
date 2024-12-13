package Estruturas;

import java.util.Iterator;

import Estruturas.Exceptions.ElementNotFoundException;
import Estruturas.Exceptions.EmptyCollectionException;
import Estruturas.Interfaces.ListADT;

/**
 * ArrayList represents an array implementation of a list.
 * 
 * @param <T> the class of the objects in the list
 */
public class ArrayList<T> implements ListADT<T> {
    /**
     * Default capacity of the list.
     */
    protected final int DEFAULT_CAPACITY = 50;
    private final int NOT_FOUND = -1;
    /**
     * Rear of the list.
     */
    protected int rear;
    /**
     * List of elements.
     */
    protected T[] list;

    /**
     * Creates an empty list using the default capacity.
     */
    public ArrayList() {
        rear = 0;
        list = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * Creates an empty list using the specified capacity.
     * 
     * @param initialCapacity the initial size of the list
     */
    public ArrayList(int initialCapacity) {
        rear = 0;
        list = (T[]) (new Object[initialCapacity]);
    }

    /**
     * Adds the specified element to the rear of this list, expanding the capacity of the list array if necessary.
     * 
     * @param element the element to be added to the list
     */
    public void add(T element) {
        if (rear == list.length) {
            expandCapacity();
        }

        list[rear] = element;
        rear++;
    }

    /**
     * Removes and returns the last element from the list.
     */
    public T removeLast() throws EmptyCollectionException {
        T result;

        if (isEmpty())
            throw new EmptyCollectionException("list");

        rear--;
        result = list[rear];
        list[rear] = null;

        return result;
    }

    /**
     * Removes and returns the first element from the list.
     */
    public T removeFirst() throws EmptyCollectionException {
        if (isEmpty())
            throw new EmptyCollectionException("list");

        T result = list[0];
        rear--;
        /** shift the elements */
        for (int scan = 0; scan < rear; scan++)
            list[scan] = list[scan + 1];

        list[rear] = null;

        return result;
    }

    /**
     * Removes and returns the specified element from the list.
     */
    public T remove(T element) {
        T result;
        int index = find(element);

        if (index == NOT_FOUND)
            throw new ElementNotFoundException("list");

        result = list[index];
        rear--;
        /** shift the appropriate elements */
        for (int scan = index; scan < rear; scan++)
            list[scan] = list[scan + 1];

        list[rear] = null;

        return result;
    }

    /**
     * Returns a reference to the first element in the list.
     */
    public T first() throws EmptyCollectionException {
        if (isEmpty())
            throw new EmptyCollectionException("list");

        return list[0];
    }

    /**
     * Returns a reference to the last element in the list.
     */
    public T last() throws EmptyCollectionException {
        if (isEmpty())
            throw new EmptyCollectionException("list");

        return list[rear - 1];
    }

    /**
     * Returns true if this list contains the specified target element.
     */
    public boolean contains(T target) {
        return (find(target) != NOT_FOUND);
    }

    /**
     * Returns the index of the specified target element, or -1 if it is not found in the list.
     */
    private int find(T target) {
        int scan = 0, result = NOT_FOUND;
        boolean found = false;

        if (!isEmpty())
            while (!found && scan < rear)
                if (target.equals(list[scan])) {
                    found = true;
                } else {
                    scan++;
                }
        if (found) {
            result = scan;
        }
        return result;
    }

    /**
     * Returns true if this list is empty and false otherwise.
     */
    public boolean isEmpty() {
        return (rear == 0);
    }

    /**
     * Returns the number of elements in this list.
     */
    public int size() {
        return rear;
    }

    /**
     * Returns an iterator for the elements in this list.
     */
    public Iterator<T> iterator() {
        return new ArrayIterator<T>(list, rear);
    }

    /**
     * Returns a string representation of this list.
     */
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int scan = 0; scan < rear; scan++)
            result.append(list[scan].toString()).append("\n");

        return result.toString();
    }

    /**
     * Creates a new array to store the contents of this list with twice the capacity of the old one.
     */
    protected void expandCapacity() {
        T[] larger = (T[]) (new Object[list.length * 2]);

        for (int scan = 0; scan < list.length; scan++)
            larger[scan] = list[scan];

        list = larger;
    }

    /**
     * Returns the element at the specified index.
     * @param index the index of the element to return
     * @return the element at the specified index
     */
    public T get(int index) {
        return list[index];
    }
}