package org.hbrs.se1.ws24.exercises.uebung4.prototype.control;

import org.hbrs.se1.ws24.exercises.uebung3.persistence.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.commands.*;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.ContainerException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.Fehlerverwalter;

import java.util.Scanner;

/**
 * Anwendungslogik und Ablauf des Programms
 */
public enum Anwendung {

    INSTANCE;

    private final Aufrufer aufrufer;
    private  Scanner sc;
    private Fehlerverwalter fehlerverwalter; // nicht 'final' für aus Testzwecken
    private boolean beendet;

    Anwendung() {
        this.aufrufer= new Aufrufer();
        this.fehlerverwalter = new Fehlerverwalter();
        this.sc = new Scanner(System.in);
        initialisiereBefehle();
        beendet = false;
    }



    /* Aufnahme der vorhandenen Befehle in die HashMap des Befehls-Aufrufers */
    private void initialisiereBefehle() {
        aufrufer.nehmeBefehlAuf("enter", new Befehl_Enter(sc));
        aufrufer.nehmeBefehlAuf("store", new Befehl_Store());
        aufrufer.nehmeBefehlAuf("load", new Befehl_Load());
        aufrufer.nehmeBefehlAuf("dump", new Befehl_Dump());
        aufrufer.nehmeBefehlAuf("help", new Befehl_Help());
        aufrufer.nehmeBefehlAuf("exit", new Befehl_Exit());
    }

    /* Programmstart */
    public void starten() {
        hallo();

        while (!beendet) {
            try {
                eingabe();
            } catch (Exception e) {
                fehlerverwalter.fehlerbehandlung(e);
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
                "║         [Version 0.1.3]              ║\n" +
                "╚══════════════════════════════════════╝\n");
        System.out.println("Willkommen! Geben Sie 'help' ein für eine Liste der Befehle.");
    }

    /* Eingabefunktion für die Nutzereingabe */
    // 'public' nur für Testzwecke
    public void eingabe() throws ContainerException, PersistenceException {
        if (beendet) return;

        System.out.print("> ");
        String eingabe = sc.nextLine().trim();

        // leere Eingabe
        if (eingabe.isEmpty()) {
            return;
        }

        // Zerlegung der Eingabe in seine Teile
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

        // Befehlaufrufung
        aufrufer.aufrufen(befehlName, parameter);
    }


    /* Geordnetes Beenden der Anwendung */
    public void beenden() {
        try {
            speichernAufforderung();
        } catch (ContainerException | PersistenceException e) {
            fehlerverwalter.fehlerbehandlung(e);
        } finally {
            sc.close();
            beendet = true;
            System.out.println("Programm wurde beendet.");
        }
    }

    /* Mögliche Änderungen vor dem Beenden des Programms zu speichern */
    private void speichernAufforderung() throws ContainerException, PersistenceException {
        System.out.println("Möchten Sie mögliche Änderungen speichern? (j/n)");
        String input = sc.nextLine().trim().toLowerCase();
        if (input.equals("j")) {
            aufrufer.aufrufen("store", null);
        }
    }

    // nur zu Testzwecken
    public void setScanner(Scanner sc) {
        this.sc = sc;
    }
}
