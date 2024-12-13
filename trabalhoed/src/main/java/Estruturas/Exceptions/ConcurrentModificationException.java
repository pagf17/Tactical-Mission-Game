package Estruturas.Exceptions;

/**
 * ConcurrentModificationException
 */
public class ConcurrentModificationException extends RuntimeException {
    public ConcurrentModificationException() {
        super("Concurrent modification detected.");
    }
    
}
