package org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions;

public class UnbekannterBefehlException extends RuntimeException {
    public UnbekannterBefehlException(String message) {
        super(message);
    }
}
