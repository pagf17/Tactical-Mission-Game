package Estruturas;

import Estruturas.Exceptions.ElementNotFoundException;
import Estruturas.Interfaces.UnorderedListADT;

public class ArrayUnorderedList<T> extends AbstractList<T> implements UnorderedListADT<T> {
    
        public ArrayUnorderedList() {
            super();
        }
    
        public ArrayUnorderedList(int initialCapacity) {
            super(initialCapacity);
        }
    
        @Override
        public void addToFront(T element) {
            if (size() == list.length) {
                expandCapacity();
            }
    
            for (int i = rear; i > 0; i--) {
                list[i] = list[i - 1];
            }
    
            list[0] = element;
            rear++;
        }
    
        @Override
        public void addToRear(T element) {
            if (size() == list.length) {
                expandCapacity();
            }
    
            list[rear] = element;
            rear++;
        }
    
        @Override
    public void addAfter(T element, T target) throws ElementNotFoundException{
        
        int scan = 0;
        
        if (size() == list.length) {
            expandCapacity(); 
        }
        
        while (scan < rear && !target.equals(list[scan])){ 
            scan++;
        }     
        
        if (scan == rear) {
            throw new ElementNotFoundException("UnorderedList");
        }
	
        scan++;
        
        for (int shift=rear; shift > scan; shift--) {
            list[shift] = list[shift-1];
        }
        
        list[scan] = element;
        rear++;
        ++modCount;
    }
    
        @Override
        public String toString() {
            String result = "";
    
            for (int i = 0; i < rear; i++) {
                result += list[i].toString() + "\n";
            }
    
            return result;
        }
}
