package Estruturas;

import Estruturas.Exceptions.ElementNotFoundException;
import Estruturas.Interfaces.UnorderedListADT;

public class LinkedUnorderedList<T> extends LinkedList<T> implements UnorderedListADT<T> {

    /**
     * addToFront adds the specified element to the front of the list
     * 
     * @param element the element to be added to the front of the list
     */
    @Override
    public void addToFront(T element) {
        LinearNode<T> newNode = new LinearNode<>(element);
        newNode.setNext(head);
        head = newNode;
    }

    /**
     * addToRear adds the specified element to the rear of the list
     * 
     * @param element the element to be added to the rear of the list
     */
    @Override
    public void addToRear(T element) {
        LinearNode<T> newNode = new LinearNode<>(element);
        if (isEmpty()) {
            head = newNode;
        } else {
            LinearNode<T> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
    }

    /**
     * addAfter adds the specified element after the specified target
     * 
     * @param element the element to be added after the target
     */
    @Override
    public void addAfter(T element, T target) throws ElementNotFoundException {
        LinearNode<T> current = head;
        boolean found = false;
        while (current != null && !found) {
            if (current.getElement().equals(target)) {
                found = true;
            } else {
                current = current.getNext();
            }
        }
        if (!found) {
            throw new ElementNotFoundException("LinkedUnorderedList");
        }
        LinearNode<T> newNode = new LinearNode<>(element);
        newNode.setNext(current.getNext());
        current.setNext(newNode);
    }

}
