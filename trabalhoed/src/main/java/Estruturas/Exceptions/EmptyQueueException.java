package Estruturas.Exceptions;

/**
 * EmptyQueueException
 */
public class EmptyQueueException extends RuntimeException{
        public EmptyQueueException(String message) {
            super(message);
        }
}
