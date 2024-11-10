package org.hbrs.se1.ws24.exercises.uebung4.prototype;

import org.hbrs.se1.ws24.exercises.uebung3.persistence.PersistenceException;

public interface Befehl {

    void ausfuehren(String[] parameter) throws ContainerException, PersistenceException;
}
