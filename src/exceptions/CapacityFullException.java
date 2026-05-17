package exceptions;

public class CapacityFullException extends Exception {
    public CapacityFullException(String message) {
        super(message);
    }
}