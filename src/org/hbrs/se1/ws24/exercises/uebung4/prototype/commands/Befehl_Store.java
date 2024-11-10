package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;

import org.hbrs.se1.ws24.exercises.uebung3.persistence.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.Container;

public class Befehl_Store implements Befehl {

    @Override
    public void ausfuehren(String[] parameter) throws PersistenceException {
        Container.INSTANCE.store();
        System.out.println("Erfolgreich gespeichert.");
    }
}
