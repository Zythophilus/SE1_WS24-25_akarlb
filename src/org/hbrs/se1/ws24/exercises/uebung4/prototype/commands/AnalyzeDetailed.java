package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.Container;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.UserStory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

public class AnalyzeDetailed implements AnalyzeStrategie {
    @Override
    public AnalyzeErgebnis analyze(UserStory story) {
        double rating = 0.0;
        List<String> details = new ArrayList<>();
        List<String> hinweise = new ArrayList<>();
        String beschreibung = story.getBeschreibung();


        // 1. Prueft, ob überhaupt eine Beschreibung existiert (>10 Zeichen)
        if (beschreibung.length() > 10) {
            rating += 30;

            // 2. Prueft Laenge der Beschreibung
            // UserStory sollte zwischen 50 und 200 Zeichen sein
            if (beschreibung.length() <= 50) {
                details.add("Beschreibung ist zu kurz (-20%)");
                hinweise.add("Ergänzen Sie die Beschreibung!");

            } else if (beschreibung.length() > 200) {
                details.add("Beschreibung ist zu lang (-20%)");
                hinweise.add("Kerzen Sie die Beschreibung!");
            } else {
                rating += 20;
            }

            // 3. Akteur pruefen
            boolean akteurGefunden = false;
            Pattern akteurMuster = Pattern.compile("[Aa]ls +([A-Za-z]+),?"); // "A/als [A-Z]kteur(,)...
            Matcher akteurSucher = akteurMuster.matcher(beschreibung);
            while (akteurSucher.find()) {
                String actor = akteurSucher.group(1);
                if (Container.INSTANCE.knownActor(actor)) {
                    rating += 20;
                    akteurGefunden = true;
                    break; // Bonus nur einmal
                } else {
                    akteurGefunden = true;
                    details.add(format("Akteur („%s“) ist nicht bekannt (-20%%)", actor));
                    hinweise.add("Registrieren sie einen neuen Akteur!");
                    break;
                }
            }
            if (!akteurGefunden) {
                details.add("Kein Akteur ersichtlich (-20%)");
                hinweise.add("Ergänzen Sie einen Akteur!");
            }

            // 4. prueft Mehrwert
            boolean mehrwertGefunden = false;
            String[] mehrwertMarker = {"damit", "um", "weil", "Um", "Damit", "Weil"};
            for (String marker : mehrwertMarker) {
                int index = beschreibung.toLowerCase().indexOf(marker);
                if (index != -1) {
                    // nach Marker sollten mind. 20 Zeichen kommen
                    String mehrwertText = beschreibung.substring(index);
                    if (mehrwertText.length() >= 20) {
                        rating += 30;
                        mehrwertGefunden = true;
                        break;
                    }
                }
            }
            if (!mehrwertGefunden) {
                details.add("Kein schriftlicher Mehrwert zu erkennen (-30%)");
                hinweise.add("Fügen sie einen schriftlichen Mehrwert hinzu!");
            }
            if (rating == 100.0) details.add("Alles ok"); // alles jut

        } else {
            details.add("Keine aussagekräftige Beschreibung vorhanden (-100%)");
            hinweise.add("Fügen sie eine aussagekräftige Beschreibung hinzu!");
        }
        return new AnalyzeErgebnis(rating, details, hinweise);
    }
}
