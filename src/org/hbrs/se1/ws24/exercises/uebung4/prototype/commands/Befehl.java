package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;

import org.hbrs.se1.ws24.exercises.uebung3.persistence.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.ContainerException;

public interface Befehl {

    void ausfuehren(String[] parameter) throws ContainerException, PersistenceException;
}
