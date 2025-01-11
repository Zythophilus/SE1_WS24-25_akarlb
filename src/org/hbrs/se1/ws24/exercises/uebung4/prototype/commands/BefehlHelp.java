package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;

public class BefehlHelp implements Befehl {
    @Override
    public void ausfuehren(String[] parameter) {
        System.out.println("Befehl     |   Erläuterung\n" +
                "------------------------------------------------------------------------------------------\n" +
                "actors             Ausgabe aller registrierten Akteure\n" +
                "-rm [Akteur]       Löschen eines regstrierten Akteurs in der Form:\n" +
                "                   actors -rm [Akteur]\n" +
                "addElement\n" +
                "-actor             Registrierung von Akteuren in der Form:\n" +
                "                   addElement -actor [Akteur]\n" +
                "-spec              Hinzufuegen einer schriftlichen Beschreibung zu einer User Story in der Form:\n" +
                "                   addElement [ID] -spec\n" +
                "-value             Hinzufuegen eines schriftlichen Mehrwerts zu einer User Story in der Form:\n" +
                "                   addElement [ID] -value\n" +
                "analyze\n" +
                "-all               Durchschnittliche Bewertung aller User Stories anzeigen\n" +
                "[ID]               Bewertung einer einzelnen User Story anzeigen in der Form:\n" +
                "                   analyze [ID]\n" +
                "-details           Bewertung mit Details zu den Defiziten einer User Story anzeigen in der Form:\n" +
                "                   analyze [ID] -details\n" +
                "-hints             Bewertung mit Hinweisen zur Optimierung einer User Story anzeigen in der Form:\n" +
                "                   analyze [ID] -hints\n" +
                "-details -hints    Bewertung mit Details kombiniert mit Hinweisen einer User Sotry anzeigen in der Form:\n" +
                "                   analyze [ID] -details -hints\n" +
                "dump               Sortierte Ausgabe aller User Stories\n" +
                "[ID]               Ausgabe einer User Story mit Beschreibung in der Form:\n" +
                "                   dump [ID]\n" +
                "-p                 Alle User Stories sortiert nach Projekt ausgeben in der Form:\n" +
                "                   dump -p [Projekt]\n" +
                "enter              Neue User Story eingeben\n" +
                "-p                 Neue User Story eingeben direkt mit Parametern in der Form:\n" +
                "                   enter -p [Titel] [Kriterium] [Projekt] [Aufwand] [Mehrwert] [Strafe] [Risiko]\n" +
                "                   (Bei fehlerhaften Eingabe der Parameter wird automatisch 'enter' ausgeführt)\n" +
                "exit               Programm beenden\n" +
                "help               Diese Hilfe anzeigen\n" +
                "load               User Stories laden\n" +
                "redo               Fuehrt den letzten Befehl wieder aus, der mit 'undo' rueckgaengig gemacht wurde\n" +
                "remove             Entfernt die angegebene User Story in der Form:\n" +
                "                   remove [ID]\n" +
                "store              User Stories speichern\n" +
                "undo               Macht den letzten 'addElement/'enter'/'remove' Befehl rueckgaengig");
    }
}
