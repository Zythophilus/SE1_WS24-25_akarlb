package org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions;

public class MaxRedoException extends RuntimeException {
    public MaxRedoException() {
        super("nothing to redo!");
    }
}
