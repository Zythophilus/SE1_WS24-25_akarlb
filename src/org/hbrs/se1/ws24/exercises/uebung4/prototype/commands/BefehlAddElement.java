package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.NoMatchingEntryException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.ContainerException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.FalscherParameterException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.Container;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.UserStory;

import java.util.Scanner;

public class BefehlAddElement implements Undoable{

    private Scanner sc;

    public String getBeschreibungNeu() {
        return this.beschreibungNeu;
    }

    public String getActor() {
        return this.actor;
    }

    private String beschreibungNeu;
    private String beschreibungAlt;
    private String actor;

    private UserStory story;

    public BefehlAddElement(Scanner scanner) {
        this.sc = scanner;
    }

    private BefehlAddElement(UserStory story, String beschreibungNeu, String beschreibungAlt, String actor) {
        this.story = story;
        this.actor = actor;
        this.beschreibungNeu = beschreibungNeu;
        this.beschreibungAlt = beschreibungAlt;
    }

    @Override
    public void ausfuehren(String[] parameter) throws FalscherParameterException, ContainerException {
        try {
            if (parameter.length == 0) throw new FalscherParameterException("Bitte geben Sie einen gueltigen Parameter an.");
            if (parameter[0].equalsIgnoreCase("-actor")) {
                if (parameter.length == 1) throw new FalscherParameterException("Bitte geben Sie einen Akteur an.");
                actor = parameter[1];
                Container.INSTANCE.addActor(actor);
                System.out.println("Der Akteur '" + parameter[1] + "' wurde im System registriert!");
            } else if (Container.INSTANCE.containsId(Integer.parseInt(parameter[0]))) {
                if (parameter.length == 1) throw new FalscherParameterException("Bitte geben Sie einen gültigen Parameter an.");
                if (parameter[1].equalsIgnoreCase("-value")) {
                    String mehrwertNeu = "";
                    String eingabe;
                    story = Container.INSTANCE.getUserStory(Integer.parseInt(parameter[0]));
                    String beschreibung = story.getBeschreibung();

                    // Finde den Mehrwert-Block und markiere ihn mit []
                    String[] mehrwertMarker = {"damit", "um", "weil"};
                    String markierteBeschreibung = beschreibung;
                    for (String marker : mehrwertMarker) {
                        int index = beschreibung.toLowerCase().indexOf(marker);
                        if (index != -1) {
                            // Extrahiere den ursprünglichen Mehrwert-Text
                            String mehrwertText = beschreibung.substring(index);
                            // Ersetze den ursprünglichen Mehrwert mit der markierten Version
                            markierteBeschreibung = beschreibung.substring(0, index) + "[" + mehrwertText + "]";
                            break;
                        }
                    }
                    System.out.println("---------------------------------------------------------------------------");
                    System.out.println(markierteBeschreibung);
                    System.out.println("---------------------------------------------------------------------------");
                    System.out.println("[] -> wird ersetzt.");
                    System.out.println("Schreiben Sie nun bitte einen neuen Mehrwert:");
                    System.out.println("['exit' zum Abbrechen]");

                    while (mehrwertNeu.isEmpty()) {
                        eingabe = sc.nextLine().trim();
                        if (eingabe.equalsIgnoreCase("exit")) {
                            return;
                        } else {
                            mehrwertNeu = eingabe;
                        }
                    }

                    // Ersetze den alten Mehrwert mit dem neuen
                    for (String marker : mehrwertMarker) {
                        int index = beschreibung.toLowerCase().indexOf(marker);
                        if (index != -1) {
                            // Erstelle neue Beschreibung mit dem neuen Mehrwert
                            beschreibungNeu = beschreibung.substring(0, index) + mehrwertNeu;
                            beschreibungAlt = story.getBeschreibung();
                            story.setBeschreibung(beschreibungNeu);
                            System.out.println("Der neue Mehrwert wurde erfolgreich zur User Story hinzugefuegt!");
                            break;
                        }

                    }

                } else if (parameter[1].equalsIgnoreCase("-spec")) {
                    if (!Container.INSTANCE.containsId(Integer.parseInt(parameter[0]))) throw new FalscherParameterException("Bitte geben Sie eine gueltige User Story ID an.");
                    String eingabe = null;
                    story = Container.INSTANCE.getUserStory(Integer.parseInt(parameter[0]));
                    String beschreibung = story.getBeschreibung();

                    System.out.println("---------------------------------------------------------------------------");
                    System.out.println(beschreibung);
                    System.out.println("---------------------------------------------------------------------------");
                    System.out.println("Schreiben Sie nun bitte eine neue Beschreibung:");
                    System.out.println("['exit' zum Abbrechen]");

                    while (eingabe == null) {
                        eingabe = sc.nextLine().trim();
                        if (eingabe.equalsIgnoreCase("exit")) {
                            return;
                        } else {
                            beschreibungNeu = eingabe;
                        }
                    }
                    beschreibungAlt = story.getBeschreibung();
                    story.setBeschreibung(beschreibungNeu);
                    System.out.println("Die neue Beschreibung wurde erfolgreich zur User Story hinzugefuegt!");
                } else {
                    throw new FalscherParameterException("Ungültiger Parameter!");
                }
            } else {
                throw new NoMatchingEntryException("Es gibt keine User Story mit der ID " + parameter[0]);
            }

        } catch (NumberFormatException e) {
            throw new NoMatchingEntryException("Es gibt keine User Story mit der ID " + parameter[0]);
        }
    }

    @Override
    public void undo() {
        if (this.story != null) {
            Container.INSTANCE.getUserStory(story.getId()).setBeschreibung(beschreibungAlt);
        } else {
            Container.INSTANCE.deleteActor(actor);
        }
    }

    @Override
    public void redo() throws ContainerException {
        if (this.story != null) {
            Container.INSTANCE.getUserStory(story.getId()).setBeschreibung(beschreibungNeu);
        } else {
            Container.INSTANCE.addActor(actor);
        }

    }

    @Override
    public Undoable copy() {
        return new BefehlAddElement(story, beschreibungNeu, beschreibungAlt, actor);
    }
}
