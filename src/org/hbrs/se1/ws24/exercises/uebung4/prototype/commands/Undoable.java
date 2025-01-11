package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.ContainerException;

/**
 * Interface fue Befehle, die mit undo() rueckgaengig gemacht werden koennen
 */
public interface Undoable extends Befehl {
    void undo() throws ContainerException, PersistenceException;
    void redo() throws ContainerException;
    Undoable copy();
}
