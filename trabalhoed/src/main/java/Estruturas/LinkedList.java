package Estruturas;

import Estruturas.Exceptions.NoSuchElementException;
import java.util.Iterator;

public class LinkedList<T> {
    LinearNode<T> head;

    public void add(T data) {
        LinearNode<T> newNode = new LinearNode<>(data);

        if (head == null) {
            head = newNode;
        } else {
            LinearNode<T> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
    }

    public void remove(T data) {
        if (head == null)
            return;

        if (head.getElement() == data) {
            head = head.getNext();
            return;
        }

        LinearNode<T> current = head;
        LinearNode<T> previous = null;

        while (current != null && current.getElement() != data) {
            previous = current;
            current = current.getNext();
        }

        if (current != null) {
            previous.setNext(current.getNext());
        }
    }

    public void print(LinearNode<T> node) {
        if (node == null) {
            return;
        }

        System.out.print(node.getElement() + " ");

        print(node.getNext());
    }

    public void printList() {
        print(head);
    }

    public boolean isEmpty() {
        return head == null;
    }

    // PESQUISA LINEAR
    public int linearSearch(T data) {
        LinearNode<T> current = head;
        int count = 0;
        while (current != null) {
            if (current.getElement().equals(data)) {
                return count;
            }
            count++;
            current = current.getNext();
        }
        return -1;
    }

    public void linearSearchCount(T data) {
        int count = linearSearch(data);
        if (count == -1) {
            System.out.println("Elemento não encontrado");
        } else {
            System.out.println("Elemento encontrado na posição " + count);
        }
    }

    // PESQUISA BINÁRIA
    public int binarySearch(T data) {
        LinearNode<T> current = head;
        int count = 0;
        while (current != null) {
            if (current.getElement().equals(data)) {
                return count;
            }
            count++;
            current = current.getNext();
        }
        return -1;
    }

    public void binarySearchCount(T data) {
        int count = binarySearch(data);
        if (count == -1) {
            System.out.println("Elemento não encontrado");
        } else {
            System.out.println("Elemento encontrado na posição " + count);
        }
    }

    public Iterator<T> iterator() {
        return new LinkedIterator<T>(head, size());
    }

    public int size() {
        int count = 0;
        LinearNode<T> current = head;
        while (current != null) {
            count++;
            current = current.getNext();
        }
        return count;
    }

}
