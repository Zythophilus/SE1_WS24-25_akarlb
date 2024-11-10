package org.hbrs.se1.ws24.exercises.uebung4.prototype;

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
                        String.format("%-10s | %-5s | %-50s | %-70s | %-15s", "Priorität", "ID", "Titel", "Akzeptanzkriterium", "Projekt"),
                        "-".repeat(175)
                ),
                liste.stream()
                        .map(p -> String.format("%-10.2f | %-5d | %-50s | %-70s | %-15s",
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
                        String.format("%-10s | %-5s | %-50s | %-70s | %-15s", "Priorität", "ID", "Titel", "Akzeptanzkriterium", "Projekt = " + projekt),
                        "-".repeat(175)
                ),
                liste.stream()
                        .filter(p -> p.getProjekt().equalsIgnoreCase(projekt))
                        .map(p -> String.format("%-10.2f | %-5d | %-50s | %-70s | %-15s",
                                p.getPrio(),
                                p.getId(),
                                p.getTitel(),
                                p.getKriterium(),
                                p.getProjekt()
                        ))
        ).collect(Collectors.joining("\n"));
    }
}
