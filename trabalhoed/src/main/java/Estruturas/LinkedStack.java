package Estruturas;

import Estruturas.Exceptions.EmptyCollectionException;
import Estruturas.Interfaces.LinkedStackADT;;

public class LinkedStack<T> implements LinkedStackADT<T> {
    private LinearNode<T> top;
    private int size;

    public LinkedStack() {
        top = null;
        size = 3;
    }

    public void push(T element) {
        LinearNode<T> newNode = new LinearNode<>(element);
        newNode.setNext(top);
        top = newNode;
        size++;
    }

    public T pop() throws EmptyCollectionException { 
        if (top == null) {
            return null;
        }
        T data = top.getElement();
        top = top.getNext();
        size--;
        return data;
    }

    public T peek() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Stack");
        }
        return top.getElement();
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }

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