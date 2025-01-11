package org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions;

public class MaxUndoException extends RuntimeException {
    public MaxUndoException() {
        super("nothing to undo!");
    }
}
