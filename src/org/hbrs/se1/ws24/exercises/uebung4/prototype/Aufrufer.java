package org.hbrs.se1.ws24.exercises.uebung4.prototype;

import org.hbrs.se1.ws24.exercises.uebung3.persistence.PersistenceException;

import java.util.HashMap;
import java.util.Map;

/**
 * Verwaltung und Aufrufung der bekannten Befehle
 */
public class Aufrufer {

    // Befehlsregister
    private final Map<String, Befehl> befehle = new HashMap<>();

    // Aufnahme eines Befehls in das Register
    public void nehmeBefehlAuf(String name, Befehl befehl) {
        befehle.put(name, befehl);
    }

    // Befehlsaufrufung
    public void aufrufen(String name, String[] parameter) throws ContainerException, PersistenceException, UnbekannterBefehlException {
        Befehl befehl = befehle.get(name);
        if (befehl != null) {
            befehl.ausfuehren(parameter);
        } else {
            throw new UnbekannterBefehlException("Befehl nicht bekannt");
        }
    }
}
