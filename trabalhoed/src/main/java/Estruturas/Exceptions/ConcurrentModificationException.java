package Estruturas.Exceptions;

public class ConcurrentModificationException extends RuntimeException {
    public ConcurrentModificationException() {
        super("Concurrent modification detected.");
    }
    
}
