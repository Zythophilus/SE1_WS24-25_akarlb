package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.Container;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.ContainerException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.UserStory;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Befehl_Enter implements Befehl {

    private final Scanner sc;


    public Befehl_Enter(Scanner scanner) {
        this.sc = scanner;
    }

    @Override
    public void ausfuehren(String[] parameter) throws ContainerException {
        String titel, kriterium, projekt, eingabe;
        titel = kriterium = projekt = "";
        byte aufwand, mehrwert, strafe, risiko, byte_eingabe;
        aufwand = mehrwert = strafe = risiko = 0;
        try {
            if(parameter[0] != null && parameter[0].equalsIgnoreCase("-p")) {
                titel = parameter[1];
                kriterium = parameter[2];
                projekt = parameter[3];
                aufwand = Byte.parseByte(parameter[4]);
                mehrwert = Byte.parseByte(parameter[5]);
                strafe = Byte.parseByte(parameter[6]);
                risiko = Byte.parseByte(parameter[7]);
                if ((titel.length() < 51 && !titel.isEmpty())
                        && (kriterium.length() < 71 && !kriterium.isEmpty())
                        && (projekt.length() < 16 && !projekt.isEmpty())
                        && !(aufwand < 0 || aufwand > 5)
                        && !(mehrwert < 0 || mehrwert > 5)
                        && !(strafe < 0 || strafe > 5)
                        && !(risiko < 0 || risiko > 5)
                ) {
                    UserStory story_neu = new UserStory(titel, kriterium, projekt, aufwand, mehrwert, strafe, risiko);
                    Container.INSTANCE.addUserStory(story_neu);
                    System.out.println("User Story erfolgreich angelegt!");
                    return;
                }
            }
        } catch (Exception e) {
                titel = kriterium = projekt = "";
                aufwand = mehrwert = strafe = risiko = 0;
        }
        System.out.println();
        System.out.println("-----User Story Eingabe-----");
        System.out.println("---['exit' zum Abbrechen]---");
        System.out.println();

        System.out.println("Bitte nennen Sie einen Titel für die User Story:");
        while (titel.isEmpty()) {
            eingabe = sc.nextLine().trim();
            if (eingabe.equalsIgnoreCase("exit")) {
                return;
            } else if (eingabe.isEmpty() || eingabe.length() > 50) {
                System.out.println("Mindestens 1 bis maximal 50 Zeichen erlaubt!");
                System.out.println("['exit' zum Abbrechen]");
            } else {
                titel = eingabe;
            }
        }

        System.out.println("Bitte nennen Sie das Akzeptanzkriterium:");
        while (kriterium.isEmpty()) {
            eingabe = sc.nextLine().trim();
            if (eingabe.equalsIgnoreCase("exit")) {
                return;
            } else if (eingabe.isEmpty() || eingabe.length() > 70) {
                System.out.println("Mindestens 1 bis maximal 70 Zeichen erlaubt!");
                System.out.println("['exit' zum Abbrechen]");
            } else {
                kriterium = eingabe;
            }
        }

        System.out.println("Welchem Projekt soll die User Story zugeordnet werden?");
        while (projekt.isEmpty()) {
            eingabe = sc.nextLine().trim();
            if (eingabe.equalsIgnoreCase("exit")) {
                return;
            } else if (eingabe.isEmpty() || eingabe.length() > 15) {
                System.out.println("Mindestens 1 bis maximal 15 Zeichen erlaubt!");
                System.out.println("['exit' zum Abbrechen]");
            } else {
                projekt = eingabe;
            }
        }

        System.out.println("Bitte nennen sie den relativen Aufwand (1-5):");
        while (aufwand == 0) {
            try {
                eingabe = sc.nextLine().trim();
                byte_eingabe = Byte.parseByte(eingabe);
                if (byte_eingabe < 0 || byte_eingabe > 5) {
                    throw new InputMismatchException();
                } else {
                    aufwand = byte_eingabe;
                }
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Bitte nur Ganzzahlen zwischen 1 und 5!");
                System.out.println("['exit' zum Abbrechen]");

            }
        }

        System.out.println("Bitte nennen sie den relativen Mehrwert (1-5):");
        while (mehrwert == 0) {
            try {
                eingabe = sc.nextLine().trim();
                byte_eingabe = Byte.parseByte(eingabe);
                if (byte_eingabe < 0 || byte_eingabe > 5) {
                    throw new InputMismatchException();
                } else {
                    mehrwert = byte_eingabe;
                }
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Bitte nur Ganzzahlen zwischen 1 und 5!");
                System.out.println("['exit' zum Abbrechen]");

            }
        }

        System.out.println("Bitte nennen sie die relative Strafe (1-5):");
        while (strafe == 0) {
            try {
                eingabe = sc.nextLine().trim();
                byte_eingabe = Byte.parseByte(eingabe);
                if (byte_eingabe < 0 || byte_eingabe > 5) {
                    throw new InputMismatchException();
                } else {
                    strafe = byte_eingabe;
                }
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Bitte nur Ganzzahlen zwischen 1 und 5!");
                System.out.println("['exit' zum Abbrechen]");

            }
        }

        System.out.println("Bitte nennen sie das relative Risiko (1-5):");
        while (risiko == 0) {
            try {
                eingabe = sc.nextLine().trim();
                byte_eingabe = Byte.parseByte(eingabe);
                if (byte_eingabe < 0 || byte_eingabe > 5) {
                    throw new InputMismatchException();
                } else {
                    risiko = byte_eingabe;
                }
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Bitte nur Ganzzahlen zwischen 1 und 5!");
                System.out.println("['exit' zum Abbrechen]");

            }
        }

        UserStory story_neu = new UserStory(titel, kriterium, projekt, aufwand, mehrwert, strafe, risiko);
        Container.INSTANCE.addUserStory(story_neu);
        System.out.println("User Story erfolgreich angelegt!");
    }
}
