package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.UserStory;

/**
 * Strategie-Interface, um flexibel neue Analyse-Strategien zu definieren
 */
public interface AnalyzeStrategie {
    AnalyzeErgebnis analyze(UserStory story);
}
