package Estruturas;

import Estruturas.Exceptions.EmptyQueueException;
import Estruturas.Interfaces.QueueADT;

public class LinkedQueue<T> implements QueueADT<T> {
    private LinearNode<T> front; 
    private LinearNode<T> rear; 
    private int size;

    /**
     * Creates an empty queue.
     */
    public LinkedQueue() {
        front = null;
        rear = null;
        size = 0;
    }

    /**
     * Adds the specified element to the rear of this queue.
     */
    @Override
    public void enqueue(T element) {
        LinearNode<T> newNode = new LinearNode<T>(element);
        if (isEmpty()) {
            front = newNode;
        } else {
            rear.setNext(newNode);
        }
        rear = newNode;
        size++;
    }

    /**
     * Removes the element at the front of this queue and returns a reference to it.
     */
    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new EmptyQueueException("queue");
        }
        T result = front.getElement();
        front = front.getNext();
        size--;
        if (isEmpty()) {
            rear = null;
        }
        return result;
    }

    /**
     * Returns a reference to the element at the front of this queue.
     */
    @Override
    public T first() {
        if (isEmpty()) {
            throw new EmptyQueueException("queue");
        }
        return front.getElement();
    }

    /**
     * Returns true if this queue is empty and false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Returns the number of elements in this queue.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns a string representation of this queue.
     */
    @Override
    public String toString() {
        String result = "";
        LinearNode<T> current = front;
        while (current != null) {
            result += current.getElement() + "\n";
            current = current.getNext();
        }
        return result;
    }

}