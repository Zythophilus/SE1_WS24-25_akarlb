package org.hbrs.se1.ws24.exercises.uebung4.prototype;

import org.hbrs.se1.ws24.exercises.uebung3.persistence.PersistenceException;

/**
 * Zentrale Fehlerklasse zur Verwaltung und Behandlung der auftretenden Fehler
 */

public class Fehlerverwalter {

    public void fehlerbehandlung(Exception e) {
        if (e instanceof UnbekannterBefehlException) {
            System.out.println("Ungültiger Befehl. Bitte geben Sie 'help' ein für eine Liste aller verfügbaren Befehle.");
        } else if (e instanceof PersistenceException && ((PersistenceException)e).getExceptionType().equals(PersistenceException.ExceptionType.NoStoredListAvailable)) {
            System.out.println("Es sind noch keine User Stories zum Laden verfügbar.");
        } else if (e instanceof FalscherParameterException || e instanceof NoMatchingEntryException) {
            System.out.println(e.getMessage());
        } else {
            System.err.println("Error: " + e.getMessage());
            System.out.println();
        }
    }
}
