package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.ContainerException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.FalscherParameterException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.NoMatchingEntryException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.Container;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.UserStory;

import java.util.List;

public class BefehlAnalyze implements Befehl {

    private AnalyzeStrategie strategie; // Wird intern gesetzt je nach Parameter

    public BefehlAnalyze() {
        this.strategie = new AnalyzeStandard(); // Default-Strategie
    }

    @Override
    public void ausfuehren(String[] parameter) throws ContainerException, PersistenceException {
        if (parameter.length == 0 ) throw new FalscherParameterException("Gebe bitte eine gueltige User Story ID oder '-all' an");
        try {
            if (parameter[0].equalsIgnoreCase("-all")) {
                List<UserStory> currentList = Container.INSTANCE.getCurrentList();
                double sumRatings = 0.0;

                for (UserStory userStory : currentList){
                    AnalyzeErgebnis ergebnis = strategie.analyze(userStory);
                    sumRatings += ergebnis.getRating();
                }

                double finalRating = sumRatings / currentList.size();
                String note = finalRating < 16 ? "ungenügend" :
                        finalRating < 45 ? "mangelhaft" :
                                finalRating < 60 ? "ausreichend" :
                                        finalRating < 80 ? "befriedigend" :
                                                finalRating < 96 ? "gut" :
                                                        "sehr gut";

                System.out.printf("Ihre %d User Stories haben durchschnittlich folgende Qualität:\n%.2f%% (%s)\n", currentList.size(), finalRating, note);
            } else if (Container.INSTANCE.containsId(Integer.parseInt(parameter[0]))) {
                if (parameter.length == 1) {
                    int id = Integer.parseInt(parameter[0]);
                    AnalyzeErgebnis ergebnis = strategie.analyze(Container.INSTANCE.getUserStory(id));
                    double rating = ergebnis.getRating();
                    String note = rating < 16 ? "ungenügend" :
                            rating < 45 ? "mangelhaft" :
                                    rating < 60 ? "ausreichend" :
                                            rating < 80 ? "befriedigend" :
                                                    rating < 96 ? "gut" :
                                                            "sehr gut";

                    System.out.printf("Die User Story mit der ID %d hat folgende Qualität:\n%.2f%% (%s)\n", id, rating, note);

                } else if (parameter[1] != null && parameter[1].equalsIgnoreCase("-details")) {

                    this.strategie = new AnalyzeDetailed();

                    if (parameter.length == 2) {
                        int id = Integer.parseInt(parameter[0]);
                        AnalyzeErgebnis ergebnis = strategie.analyze(Container.INSTANCE.getUserStory(id));
                        double rating = ergebnis.getRating();
                        List<String> details = ergebnis.getDetails();
                        String note = rating < 16 ? "ungenügend" :
                                rating < 45 ? "mangelhaft" :
                                        rating < 60 ? "ausreichend" :
                                                rating < 80 ? "befriedigend" :
                                                        rating < 96 ? "gut" :
                                                                "sehr gut";

                        System.out.printf("Die User Story mit der ID %d hat folgende Qualität:\n%.2f%% (%s)\n\nDetails:\n", id, rating, note);
                        for (String detail : details) {
                            System.out.println(detail);
                        }

                    } else if (parameter[2] != null && parameter[2].equalsIgnoreCase("-hints")) {
                        int id = Integer.parseInt(parameter[0]);
                        AnalyzeErgebnis ergebnis = strategie.analyze(Container.INSTANCE.getUserStory(id));
                        double rating = ergebnis.getRating();
                        List<String> details = ergebnis.getDetails();
                        List<String> hinweise = ergebnis.getHinweise();
                        String note = rating < 16 ? "ungenügend" :
                                rating < 45 ? "mangelhaft" :
                                        rating < 60 ? "ausreichend" :
                                                rating < 80 ? "befriedigend" :
                                                        rating < 96 ? "gut" :
                                                                "sehr gut";

                        System.out.printf("Die User Story mit der ID %d hat folgende Qualität:\n%.2f%% (%s)\n\nDetails:\n", id, rating, note);
                        for (String detail : details) {
                            System.out.println(detail);
                        }
                        System.out.println("\nHints:");
                        for (String hinweis : hinweise) {
                            System.out.println(hinweis);
                        }
                    }

                } else if(parameter[1].equalsIgnoreCase("-hints") && parameter.length == 2) {
                    this.strategie = new AnalyzeDetailed();
                    int id = Integer.parseInt(parameter[0]);
                    AnalyzeErgebnis ergebnis = strategie.analyze(Container.INSTANCE.getUserStory(id));
                    double rating = ergebnis.getRating();
                    List<String> hinweise = ergebnis.getHinweise();
                        String note = rating < 16 ? "ungenügend" :
                                rating < 45 ? "mangelhaft" :
                                        rating < 60 ? "ausreichend" :
                                                rating < 80 ? "befriedigend" :
                                                        rating < 96 ? "gut" :
                                                                "sehr gut";

                    System.out.printf("Die User Story mit der ID %d hat folgende Qualität:\n%.2f%% (%s)\n\nHints:\n", id, rating, note);
                    for (String hinweis : hinweise) {
                        System.out.println(hinweis);
                    }
                        System.out.println(); // ?

                } else {
                    throw new FalscherParameterException("Bitte geben sie gueltige Parameter an und beachten Sie die Reihenfolge!\nSiehe 'help'.");

                }
            } else {
                throw new NoMatchingEntryException("Keine User Story mit der ID " + parameter[0] + " gefunden.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Bitte gebe eine gueltige User Story ID an!");
        }
    }
}
