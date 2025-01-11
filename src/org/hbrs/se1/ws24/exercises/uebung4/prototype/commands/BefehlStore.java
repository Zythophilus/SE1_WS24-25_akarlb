package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.Container;

public class BefehlStore implements Befehl {
    @Override
    public void ausfuehren(String[] parameter) throws PersistenceException {
        Container.INSTANCE.store();
        System.out.println("Erfolgreich gespeichert.");
    }
}
