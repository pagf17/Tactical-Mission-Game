package Estruturas;

import Estruturas.Exceptions.EmptyCollectionException;
import Estruturas.Interfaces.LinkedStackADT;;

public class LinkedStack<T> implements LinkedStackADT<T> {
    private static final int DEFAULT_CAPACITY = 100;
    private LinearNode<T> top;
    private int size;
    private int maxSize;

    /**
     * Creates an empty stack.
     */
    public LinkedStack() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Creates an empty stack with the specified capacity.
     * 
     * @param maxSize the initial size of the stack
     */
    public LinkedStack(int maxSize) {
        top = null;
        size = 0;
        this.maxSize = maxSize;
    }

    /**
     * Adds the specified element to the top of this stack.
     */
    public void push(T element) {
        if (size == maxSize) {
            throw new IllegalStateException("Stack is full");
        }
        LinearNode<T> newNode = new LinearNode<>(element);
        newNode.setNext(top);
        top = newNode;
        size++;
    }

    /**
     * Removes the element at the top of this stack and returns a reference to it.
     * 
     * @return the element at the top of this stack
     */
    public T pop() throws EmptyCollectionException { 
        if (top == null) {
            return null;
        }
        T data = top.getElement();
        top = top.getNext();
        size--;
        return data;
    }

    /**
     * Returns a reference to the element at the top of this stack.
     * 
     * @return a reference to the element at the top of this stack
     */
    public T peek() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Stack");
        }
        return top.getElement();
    }

    /**
     * Returns true if this stack is empty and false otherwise.
     * 
     * @return true if this stack is empty
     */
    public boolean isEmpty() {
        return top == null;
    }

    /**
     * Returns true if this stack is full and false otherwise.
     * 
     * @return true if this stack is full
     */
    public boolean isFull() {
        return size == maxSize;
    }

    /**
     * Returns the number of elements in this stack.
     * 
     * @return the number of elements in this stack
     */
    public int size() {
        return size;
    }

    /**
     * Returns a string representation of this stack.
     * 
     * @return a string representation of this stack
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        LinearNode<T> current = top;
        while (current != null) {
            result.append(current.getElement()).append("\n");
            current = current.getNext();
        }
        return result.toString();
    }
}