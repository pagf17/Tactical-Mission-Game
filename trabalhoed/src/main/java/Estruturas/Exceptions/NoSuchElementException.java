package Estruturas.Exceptions;

/**
 * NoSuchElementException
 * 
 * @see RuntimeException
 */
public class NoSuchElementException extends RuntimeException {
    public NoSuchElementException() {
        super("No such element found.");
    }
}
