package org.hbrs.se1.ws24.exercises.uebung4.prototype;

import org.hbrs.se1.ws24.exercises.uebung3.persistence.PersistenceException;

public class Befehl_Exit implements Befehl {
    @Override
    public void ausfuehren(String[] parameter) throws ContainerException, PersistenceException {
        Anwendung.INSTANCE.beenden();
    }
}
