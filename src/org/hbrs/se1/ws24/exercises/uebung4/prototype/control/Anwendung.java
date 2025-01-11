package org.hbrs.se1.ws24.exercises.uebung4.prototype.control;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.commands.*;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.*;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.Container;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Anwendungslogik und Ablauf des Programms
 */
public enum Anwendung {

    INSTANCE;

    private final Aufrufer aufrufer;
    private Scanner sc; // nicht 'final' aus Testzwecken
    private boolean beendet;

    Anwendung() {
        this.aufrufer= new Aufrufer();
        this.sc = new Scanner(System.in);
        initialisiereBefehle();
        beendet = false;
    }



    /* Aufnahme der vorhandenen Befehle in die HashMap des Befehls-Aufrufers */
    private void initialisiereBefehle() {
        aufrufer.nehmeBefehlAuf("analyze", new BefehlAnalyze());
        aufrufer.nehmeBefehlAuf("enter", new BefehlEnter(sc));
        aufrufer.nehmeBefehlAuf("store", new BefehlStore());
        aufrufer.nehmeBefehlAuf("load", new BefehlLoad());
        aufrufer.nehmeBefehlAuf("dump", new BefehlDump());
        aufrufer.nehmeBefehlAuf("help", new BefehlHelp());
        aufrufer.nehmeBefehlAuf("exit", new BefehlExit());
        aufrufer.nehmeBefehlAuf("actors", new BefehlActors());
        aufrufer.nehmeBefehlAuf("addElement", new BefehlAddElement(sc));
        aufrufer.nehmeBefehlAuf("remove", new BefehlRemove());
    }

    /* Programmstart */
    public void starten() throws ContainerException {
        hallo();
        ladenAufforderung();
        Container.INSTANCE.initialisiereContainer(); // Initialisierung aus Testzwecken

        while (!beendet) {
            try {
                eingabe();
            } catch (Exception e) {
                fehlerbehandlung(e);
            }
        }
    }

    /* Begrüßungsfunktion um den Start der Anwendung zu signalisieren */
    private void hallo() {
        System.out.println(
                "╔══════════════════════════════════════╗\n" +
                "║     _    _  _____ __  __ _    _      ║\n" +
                "║    | |  | |/ ____|  \\/  | |  | |     ║\n" +
                "║    | |  | | (___ | \\  / | |  | |     ║\n" +
                "║    | |  | |\\___ \\| |\\/| | |  | |     ║\n" +
                "║    | |__| |____) | |  | | |__| |     ║\n" +
                "║     \\____/|_____/|_|  |_|\\____/      ║\n" +
                "║                                      ║\n" +
                "║    User Story Management Ultimate    ║\n" +
                "║         [Version 0.2.1]              ║\n" +
                "╚══════════════════════════════════════╝\n");
        System.out.println("Willkommen! Geben Sie 'help' ein für eine Liste der Befehle.");
    }

    /* Eingabefunktion für die Nutzereingabe */
    private void eingabe() throws ContainerException, PersistenceException {
        if (beendet) return;

        System.out.print("> ");
        String eingabe = sc.nextLine().trim();

        // leere Eingabe
        if (eingabe.isEmpty()) {
            return;
        }

        // Zerlegung der Eingabe in seine Bestandteile
        String[] eingabeTeile = eingabe.split("\\s+", 2);
        String befehlName;
        String[] parameter;

        if (eingabeTeile.length == 1) { // Befehl ohne Parameter
            befehlName = eingabeTeile[0]; // erster Teil ist immer der Befehl
            parameter = new String[0];
        } else { // Befehl mit Parametern
            befehlName = eingabeTeile[0]; // s.o.
            parameter = eingabeTeile[1].split("\\s+");
        }
        // Befehlsaufrufung
        aufrufer.aufrufen(befehlName, parameter);
    }


    /* Geordnetes Beenden der Anwendung */
    public void beenden() {
        try {
            speichernAufforderung();
        } catch (ContainerException | PersistenceException e) {
            fehlerbehandlung(e);
        } finally {
            sc.close();
            beendet = true;
            System.out.println("Programm wurde beendet.");
        }
    }

    /* Mögliche Änderungen vor dem Beenden des Programms zu speichern */
    private void speichernAufforderung() throws ContainerException, PersistenceException {
        System.out.println("Möchten Sie mögliche Änderungen speichern? (j/n)");
        System.out.print("> ");
        String input = sc.nextLine().trim().toLowerCase();
        if (input.equals("j")) {
            aufrufer.aufrufen("store", null);
        }
    }

    /* Mögliche Datensätze vor dem Start des Programms zu laden */
    private void ladenAufforderung()  {
        System.out.println("Möchten Sie gegebenenfalls vorhandene Datensätze laden? (j/n)");
        System.out.print("> ");
        String input = sc.nextLine().trim().toLowerCase();
        if (input.equals("j")) {
            try {
                aufrufer.aufrufen("load", null);
            } catch (PersistenceException | ContainerException e) {
                fehlerbehandlung(e);
            }
        }

    }

    /*
    Zentrale Fehlerklasse zur Verwaltung und Behandlung der auftretenden Exceptions
     */
    private void fehlerbehandlung(Exception e) {
        if (e instanceof UnbekannterBefehlException) {
            System.out.println(e.getMessage());
        } else if (e instanceof PersistenceException && ((PersistenceException)e).getExceptionType().equals(PersistenceException.ExceptionType.NoStoredListAvailable)) {
            System.out.println("Es sind noch keine User Stories zum Laden verfügbar.");
        } else if (e instanceof FalscherParameterException || e instanceof NoMatchingEntryException) {
            System.out.println(e.getMessage());
        } else if (e instanceof MaxUndoException || e instanceof MaxRedoException) {
            System.out.println(e.getMessage());
        } else if (e instanceof ContainerException) {
            e.printStackTrace();
        }
        else {
            // debug
            System.err.println(e.getMessage());
            System.err.println(e.getCause());
            System.err.println("Error: " + Arrays.toString(e.getStackTrace()));
            System.out.println();
        }
    }
}
