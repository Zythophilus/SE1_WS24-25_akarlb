package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.Container;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.ContainerException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.UserStory;

import java.util.InputMismatchException;
import java.util.Scanner;

public class BefehlEnter implements Undoable {

    private final Scanner sc;


    private UserStory enteredStory;


    public BefehlEnter(Scanner scanner) {
        this.sc = scanner;
    }

    private BefehlEnter(UserStory story) {
        this.sc = null;
        this.enteredStory = story;
    }

    @Override
    public void ausfuehren(String[] parameter) throws ContainerException {
        String titel, kriterium, projekt, eingabe, beschreibung;
        titel = kriterium = projekt = beschreibung = "";
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

                StringBuilder baumeister = new StringBuilder();
                for (int i = 8; i < parameter.length; i++) {
                    baumeister.append(parameter[i]);
                    if (i < parameter.length - 1) {
                        baumeister.append(" ");
                    }
                }
                beschreibung = baumeister.toString();
                if ((!titel.isEmpty() && titel.length() < UserStory.MAX_TITEL)
                        && (!kriterium.isEmpty() && kriterium.length() < UserStory.MAX_KRITERIUM)
                        && (!projekt.isEmpty() && projekt.length() < UserStory.MAX_PROJEKT)
                        && !(aufwand < 0 || aufwand > UserStory.MAX_AUFWAND)
                        && !(mehrwert < 0 || mehrwert > UserStory.MAX_MEHRWERT)
                        && !(strafe < 0 || strafe > UserStory.MAX_STRAFE)
                        && !(risiko < 0 || risiko > UserStory.MAX_RISIKO)
                ) {
                    enteredStory = new UserStory(titel, kriterium, projekt, aufwand, mehrwert, strafe, risiko, beschreibung);
                    Container.INSTANCE.addUserStory(enteredStory);
                    System.out.println("User Story erfolgreich angelegt!");
                    return;
                }
            }
        } catch (ContainerException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
                titel = kriterium = projekt = "";
                beschreibung = null;
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
            } else if (eingabe.isEmpty() || eingabe.length() > UserStory.MAX_TITEL) {
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
            } else if (eingabe.isEmpty() || eingabe.length() > UserStory.MAX_KRITERIUM) {
                System.out.println("Mindestens 1 bis maximal 100 Zeichen erlaubt!");
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
            } else if (eingabe.isEmpty() || eingabe.length() > UserStory.MAX_PROJEKT) {
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
                if (byte_eingabe < 0 || byte_eingabe > UserStory.MAX_AUFWAND) {
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
                if (byte_eingabe < 0 || byte_eingabe > UserStory.MAX_MEHRWERT) {
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
                if (byte_eingabe < 0 || byte_eingabe > UserStory.MAX_STRAFE) {
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
                if (byte_eingabe < 0 || byte_eingabe > UserStory.MAX_RISIKO) {
                    throw new InputMismatchException();
                } else {
                    risiko = byte_eingabe;
                }
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Bitte nur Ganzzahlen zwischen 1 und 5!");
                System.out.println("['exit' zum Abbrechen]");

            }
        }

        System.out.println("Nun beschreiben Sie bitte Ihre User Story:");
        while (beschreibung == null) {
            eingabe = sc.nextLine().trim();
            if (eingabe.equalsIgnoreCase("exit")) {
                return;
            } else {
                beschreibung = eingabe;
            }
        }

        enteredStory = new UserStory(titel, kriterium, projekt, aufwand, mehrwert, strafe, risiko, beschreibung);
        Container.INSTANCE.addUserStory(enteredStory);
        System.out.println("User Story erfolgreich angelegt!");
    }

    public void undo() {
        Container.INSTANCE.deleteUserStory(enteredStory);

    }

    @Override
    public void redo() throws ContainerException {
        Container.INSTANCE.addUserStory(enteredStory);
    }

    @Override
    public Undoable copy() {
        return new BefehlEnter(enteredStory);
    }

    public UserStory getEnteredStory() {
        return enteredStory;
    }
}
