package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.Container;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.UserStory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Vanilla Analyse
 */
public class AnalyzeStandard implements AnalyzeStrategie {
    @Override
    public AnalyzeErgebnis analyze(UserStory story) {
        double rating = 0.0;
        String beschreibung = story.getBeschreibung();

        // 1. Prueft, ob überhaupt eine Beschreibung existiert (>10 Zeichen)
        if (beschreibung.length() > 10) {
            rating += 30;

            // 2. Prueft Laenge der Beschreibung
            // UserStory sollte zwischen 50 und 200 Zeichen sein
            if (beschreibung.length() >= 50 && beschreibung.length() <= 200) {
                rating += 20;
            }

            // 3. Akteur pruefen
            Pattern actorPattern = Pattern.compile("[Aa]ls +([A-Za-z]+),?"); // "A/als [A-Z]kteur(,)...
            Matcher actorMatcher = actorPattern.matcher(beschreibung);
            while (actorMatcher.find()) {
                String actor = actorMatcher.group(1);
                if (Container.INSTANCE.knownActor(actor)) {
                    rating += 20;
                    break; // Nur einmal den Bonus
                }
            }

            // 4. prueft Mehrwert
            String[] mehrwertMarker = {"damit", "um", "weil", "Um", "Damit", "Weil"};
            for (String marker : mehrwertMarker) {
                int index = beschreibung.toLowerCase().indexOf(marker);
                if (index != -1) {
                    // nach Marker sollten mind. 20 Zeichen kommen
                    String mehrwertText = beschreibung.substring(index);
                    if (mehrwertText.length() >= 20) {
                        rating += 30;
                        break;
                    }
                }
            }
        }
        return new AnalyzeErgebnis(rating);
    }
}
