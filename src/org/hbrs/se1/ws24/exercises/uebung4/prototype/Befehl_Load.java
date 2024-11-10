package org.hbrs.se1.ws24.exercises.uebung4.prototype;

import org.hbrs.se1.ws24.exercises.uebung3.persistence.PersistenceException;

public class Befehl_Load implements Befehl {
    @Override
    public void ausfuehren(String[] parameter) throws PersistenceException {
        Container.INSTANCE.load();
        System.out.println("Erfolgreich geladen.");

    }
}
