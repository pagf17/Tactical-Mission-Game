package Estruturas;

import Estruturas.Exceptions.EmptyQueueException;
import Estruturas.Interfaces.QueueADT;

public class LinkedQueue<T> implements QueueADT<T> {

    private LinearNode<T> front; 
    private LinearNode<T> rear; 
    private int size;

    public LinkedQueue() {
        front = null;
        rear = null;
        size = 0;
    }

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

    // metodo dequeue
    // Remove o elemento do inicio da fila
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

    // metodo first
    // Devolve o primeiro elemento da fila 
    @Override
    public T first() {
        if (isEmpty()) {
            throw new EmptyQueueException("queue");
        }
        return front.getElement();
    }

    // metodo isEmpty
    // Verifica se a fila esta vazia
    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    // metodo size
    // Devolve o tamanho da queue
    @Override
    public int size() {
        return size;
    }

    // metodo toString  
    // Devolve uma String com todos os elementos presentes na queue
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