package Estruturas;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedIterator<T> implements Iterator<T>{
   private int count;  
   private LinearNode<T> current;  
    
   /**
    * Sets up this iterator using the specified items.
    *
    * @param collection the collection the iterator will move over.
    * @param size the integer size of the collection
    */
   public LinkedIterator (LinearNode<T> collection, int size) {
      current = collection;
      count = size;
   }
 
   /**
    * Returns true if this iterator has at least one more element to deliver
    */
   public boolean hasNext() {
      return (current!= null);
   }
 
   /**
    * Returns the next element in the iteration.
    */
   public T next() {
      if (!hasNext()) 
         throw new NoSuchElementException();
 
      T result = current.getElement();
      current = current.getNext();
      return result;
   }
 
   /**
    * The remove operation is not supported in this collection.
    */
   public void remove() throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
   }
}
