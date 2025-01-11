package org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions;

public class UnbekannterBefehlException extends RuntimeException {
    public UnbekannterBefehlException() {
        super("Ungültiger Befehl. Bitte geben Sie 'help' ein für eine Liste aller verfügbaren Befehle.");
    }
}
