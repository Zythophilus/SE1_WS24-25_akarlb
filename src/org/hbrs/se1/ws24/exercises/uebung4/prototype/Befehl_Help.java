package org.hbrs.se1.ws24.exercises.uebung4.prototype;

public class Befehl_Help implements Befehl {
    @Override
    public void ausfuehren(String[] parameter) {
        System.out.println("Befehl   |   Erläuterung");
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("enter      Neue User Story eingeben");
        System.out.println("enter -p   Neue User Story eingeben mit Parametern in der Form:\n" +
                "           enter -p [Titel] [Kriterium] [Projekt] [Aufwand] [Mehrwert] [Strafe] [Risiko]\n" +
                "           (Bei fehlerhaften Eingabe der Parameter wird automatisch 'enter' ausgeführt)");
        System.out.println("store      User Stories speichern");
        System.out.println("load       User Stories laden");
        System.out.println("dump       Sortierte Ausgabe aller User Stories");
        System.out.println("dump -p    Sortierte Ausgabe aller User Stories nach Projekt in der Form:\n" +
                "           dump -p [Projekt]");
        System.out.println("exit       Programm beenden");
        System.out.println("help       Diese Hilfe anzeigen");
    }
}
