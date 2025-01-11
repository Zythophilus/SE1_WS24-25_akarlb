package org.hbrs.se1.ws24.exercises.uebung4.prototype.model;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.NoMatchingEntryException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * View-Klasse für die Darstellung der internen User Story Verwaltung
 */
public class UserStoryView {

    public static String dump(List<UserStory> liste) {
        return Stream.concat(
                Stream.of(
                        String.format("%-10s | %-5s | %-50s | %-100s | %-15s", "Priorität", "ID", "Titel", "Akzeptanzkriterium", "Projekt"),
                        "-".repeat(205)
                ),
                liste.stream()
                        .map(p -> String.format("%-10.2f | %-5d | %-50s | %-100s | %-15s",
                                p.getPrio(),
                                p.getId(),
                                p.getTitel(),
                                p.getKriterium(),
                                p.getProjekt()
                        ))
        ).collect(Collectors.joining("\n"));
    }

    public static String dumpProjekt(List<UserStory> liste, String projekt) {
        if (liste.stream().noneMatch(p -> p.getProjekt().equals(projekt))) {
            throw new NoMatchingEntryException("Es wurden keine User Stories für das Projekt '" + projekt + "' gefunden.");
        }
        return Stream.concat(
                Stream.of(
                        String.format("%-10s | %-5s | %-50s | %-100s | %-15s", "Priorität", "ID", "Titel", "Akzeptanzkriterium", "Projekt = " + projekt),
                        "-".repeat(205)
                ),
                liste.stream()
                        .filter(p -> p.getProjekt().equalsIgnoreCase(projekt))
                        .map(p -> String.format("%-10.2f | %-5d | %-50s | %-100s | %-15s",
                                p.getPrio(),
                                p.getId(),
                                p.getTitel(),
                                p.getKriterium(),
                                p.getProjekt()
                        ))
        ).collect(Collectors.joining("\n"));
    }

    public static String show(UserStory story) {

        final int MAXIMALE_TEXTBREITE = 50;

        String wrappedDescription = wrapText(story.getBeschreibung(), MAXIMALE_TEXTBREITE);
        String kriteriumFormatiert = wrapText(story.getKriterium(), MAXIMALE_TEXTBREITE);

        return String.format("""
            --------------------------------------------------
            ID: %d - %s
            
            Beschreibung:
            %s
            
            Akzeptanzkriterium:
            %s
            
            Priorität: %.2f
            Projekt: %s
            -------------------------------------------------""",
                story.getId(),
                story.getTitel(),
                wrappedDescription,
                kriteriumFormatiert,
                story.getPrio(),
                story.getProjekt()
        );
    }

    // Hilfsmethode
    private static String wrapText(String text, int width) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        StringBuilder wrapped = new StringBuilder();
        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            if (line.length() + word.length() > width) {
                wrapped.append(line.toString().trim()).append("\n");
                line = new StringBuilder();
            }
            line.append(word).append(" ");
        }

        if (line.length() > 0) {
            wrapped.append(line.toString().trim());
        }

        return wrapped.toString();
    }


}
