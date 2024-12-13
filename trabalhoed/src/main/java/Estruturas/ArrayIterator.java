package Estruturas;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * ArrayIterator represents an array implementation of an iterator.
 * 
 * @param <T> the element type
 * @see Iterator
 */
public class ArrayIterator<T> implements Iterator<T> {
    private int count; 
    private int current; 
    private T[] items; 

    /**
     * Sets up this iterator using the specified items and size.
     *
     * @param collection the array of elements to iterate over.
     * @param size       the number of elements in the collection.
     */
    public ArrayIterator(T[] collection, int size) {
        items = collection;
        count = size;
        current = 0;
    }

    /**
     * Returns true if this iterator has at least one more element to deliver
     *        in the iteration.
     *
     * @return true if there are more elements, false otherwise.
     */
    public boolean hasNext() {
        return (current < count);
    }

    /**
     * Returns the next element in the iteration. If there are no more
     * elements, a NoSuchElementException is thrown.
     *
     * @return the next element in the iteration.
     */
    public T next() {
        if (!hasNext())
            throw new NoSuchElementException();
        current++;
        return items[current - 1];
    }

    /**
     * The remove operation is not supported in this collection.
     */
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
