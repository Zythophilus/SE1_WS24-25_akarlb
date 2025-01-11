package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.ContainerException;

/**
 * Grundlegendes Interface fuer alle Befehle dieser Anwendung
 */
public interface Befehl {
    void ausfuehren(String[] parameter) throws PersistenceException, ContainerException;
}
