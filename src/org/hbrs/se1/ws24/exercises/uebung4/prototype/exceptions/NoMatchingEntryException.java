package org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions;

public class NoMatchingEntryException extends RuntimeException {
    public NoMatchingEntryException(String message) {
        super(message);
    }
}
