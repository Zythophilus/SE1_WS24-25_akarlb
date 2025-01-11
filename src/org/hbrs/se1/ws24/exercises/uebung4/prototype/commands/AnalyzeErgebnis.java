package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;

import java.util.List;

/**
 * Klasse, worauf sich die Ergebnisse aller Analyse-Strategien abbilden lassen
 */
public class AnalyzeErgebnis {

    private double rating;
    private List<String> details;
    private List<String> hinweise;

    public AnalyzeErgebnis(double rating, List<String> details, List<String> hinweise) {
        this.rating = rating;
        this.details = details;
        this.hinweise = hinweise;
    }

    public AnalyzeErgebnis(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public List<String> getDetails() {
        return details;
    }

    public List<String> getHinweise() {
        return hinweise;
    }
}
