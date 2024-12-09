package Estruturas;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The {@code ArrayIterator} class implements the Iterator interface for an
 * array of elements.
 */
public class ArrayIterator<T> implements Iterator<T> {

    private int count; // the number of elements in the collection
    private int current; // the current position in the iteration
    private T[] items; // the array of items

    /**
     * @brief Sets up this iterator using the specified items and size.
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
     * @brief Returns true if this iterator has at least one more element to deliver
     *        in the iteration.
     *
     * @return true if there are more elements, false otherwise.
     */
    public boolean hasNext() {
        return (current < count);
    }

    /**
     * @brief Returns the next element in the iteration. If there are no more
     *        elements, a NoSuchElementException is thrown.
     *
     * @return the next element in the iteration.
     * @throws NoSuchElementException if there are no more elements in the
     *                                iteration.
     */
    public T next() {
        if (!hasNext())
            throw new NoSuchElementException();

        current++;

        return items[current - 1];
    }

    /**
     * @brief The remove operation is not supported in this collection.
     *
     * @throws UnsupportedOperationException if the remove operation is called.
     */
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
