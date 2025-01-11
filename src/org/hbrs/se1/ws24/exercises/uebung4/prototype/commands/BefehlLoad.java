package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.Container;

public class BefehlLoad implements Befehl {
    @Override
    public void ausfuehren(String[] parameter) throws PersistenceException {
        Container.INSTANCE.load();
        System.out.println("Erfolgreich geladen.");

    }
}
