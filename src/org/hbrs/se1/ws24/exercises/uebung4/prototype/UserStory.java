package org.hbrs.se1.ws24.exercises.uebung4.prototype;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class UserStory implements Serializable, Comparable<UserStory> {

    private String titel;
    private String kriterium;
    private Integer id;
    private String projekt;
    private double prio;
    private byte risiko_rel;
    private byte aufwand_rel;
    private byte mehrwert_rel;
    private byte strafe_rel;

    private static Integer maxID = 1;

    private static volatile Queue<Integer> idQueue = new LinkedList<>();

    private static synchronized Integer getNextID () {
        return !idQueue.isEmpty() ? idQueue.remove() : maxID++;
    }

    public static synchronized void freeID (Integer id) { idQueue.offer(id); }

    public static synchronized void synchronisiereIDQueue(List<UserStory> stories) {
        idQueue.clear();

        if (stories.isEmpty()) {
            maxID = 1;
            return;
        }

        List<Integer> vorhandeneIDs = stories.stream()
                .map(story -> story.getId())
                .sorted()
                .toList();

        maxID = vorhandeneIDs.get(vorhandeneIDs.size() - 1) + 1;

        Integer freieID = 1;
        for (Integer id : vorhandeneIDs) {
            while (freieID < id) {
                idQueue.add(freieID);
                freieID++;
            }
            freieID = id + 1;
        }
    }


    public String getKriterium() {
        return kriterium;
    }

    public void setKriterium(String kriterium) {
        this.kriterium = kriterium;
    }

    public byte getMehrwert_rel() {
        return mehrwert_rel;
    }

    public void setMehrwert_rel(byte mehrwert_rel) {
        this.mehrwert_rel = mehrwert_rel;
    }

    public byte getStrafe_rel() {
        return strafe_rel;
    }

    public void setStrafe_rel(byte strafe_rel) {
        this.strafe_rel = strafe_rel;
    }

    public byte getRisiko_rel() {
        return risiko_rel;
    }

    public void setRisiko_rel(byte risiko_rel) {
        this.risiko_rel = risiko_rel;
    }

    public byte getAufwand_rel() {
        return aufwand_rel;
    }

    public void setAufwand_rel(byte aufwand_rel) {
        this.aufwand_rel = aufwand_rel;
    }

    public String getProjekt() {
            return projekt;
    }

    public void setProjekt(String project) {
            this.projekt = project;
    }


    public UserStory(String titel, String kriterium, String projekt, byte aufwand, byte mehrwert, byte strafe, byte risiko) {
        this.id = getNextID();
        this.titel = titel;
        this.kriterium = kriterium;
        this.projekt = projekt;
        this.aufwand_rel = aufwand;
        this.mehrwert_rel = mehrwert;
        this.strafe_rel = strafe;
        this.risiko_rel = risiko;
        this.prio = calcPrio();
    }

    public double getPrio() {
            return prio;
    }

    public void setPrio(double prio) {
        this.prio = prio;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public int compareTo(UserStory story) {
        return this.id - story.getId();
    }


    /* Berechnet Priorität nach Gloger(2013) */
    public double calcPrio() {
        return ((double) (mehrwert_rel + strafe_rel)) / (aufwand_rel + risiko_rel);
    }

}




