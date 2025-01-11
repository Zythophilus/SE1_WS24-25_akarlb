package org.hbrs.se1.ws24.exercises.uebung4.prototype.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class UserStory implements Serializable, Comparable<UserStory> {

    private String titel;
    private String beschreibung;
    private String kriterium;
    private Integer id;
    private String projekt;
    private double prio;
    private byte risiko_rel;
    private byte aufwand_rel;
    private byte mehrwert_rel;
    private byte strafe_rel;
    public static final byte MAX_TITEL = 50;
    public static final byte MAX_KRITERIUM = 100;
    public static final byte MAX_PROJEKT = 15;
    public static final byte MAX_AUFWAND = 5;
    public static final byte MAX_MEHRWERT = 5;
    public static final byte MAX_STRAFE = 5;
    public static final byte MAX_RISIKO = 5;


    private static Integer maxID = 1;

    private static volatile Queue<Integer> idQueue = new LinkedList<>();

    public UserStory() {
    }

    static synchronized Integer getNextID () {
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

    public byte getMehrwert_rel() {
        return mehrwert_rel;
    }

    public byte getStrafe_rel() {
        return strafe_rel;
    }

    public byte getRisiko_rel() {
        return risiko_rel;
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

    public UserStory(String titel, String kriterium, String projekt, byte aufwand, byte mehrwert, byte strafe, byte risiko, String beschreibung) {
        this.id = getNextID();
        this.beschreibung = beschreibung;
        this.titel = titel;
        this.kriterium = kriterium;
        this.projekt = projekt;
        this.aufwand_rel = aufwand;
        this.mehrwert_rel = mehrwert;
        this.strafe_rel = strafe;
        this.risiko_rel = risiko;
        this.prio = calcPrio();
    }

    public UserStory(String titel, String kriterium, String projekt, byte aufwand, byte mehrwert, byte strafe, byte risiko, String beschreibung, Integer id) {
        this.beschreibung = beschreibung;
        this.id = id;
        maxID = Math.max(maxID, id + 1);
        this.titel = titel;
        this.kriterium = kriterium;
        this.projekt = projekt;
        this.aufwand_rel = aufwand;
        this.mehrwert_rel = mehrwert;
        this.strafe_rel = strafe;
        this.risiko_rel = risiko;
        this.prio = calcPrio();
    }

    public UserStory(Integer id, String titel, String kriterium, String projekt, Integer mehrwert, Integer strafe, Integer aufwand, Integer risk, Double prio, String beschreibung) {
        this.id = id;
        maxID = Math.max(maxID, id + 1);
        this.titel = titel;
        this.kriterium = kriterium;
        this.mehrwert_rel = Byte.parseByte(String.valueOf(mehrwert));
        this.strafe_rel = Byte.parseByte(String.valueOf(strafe));
        this.aufwand_rel = Byte.parseByte(String.valueOf(aufwand));
        this.risiko_rel = Byte.parseByte(String.valueOf(risk));
        this.projekt = projekt;
        this.prio = prio;
        this.beschreibung = beschreibung;
    }


    public double getPrio() {
            return prio;
    }

    public String getTitel() {
        return titel;
    }

    public Integer getId() {
        return id;
    }


    @Override
    public int compareTo(UserStory story) {
        return this.id - story.getId();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStory userStory = (UserStory) o;
        //return Objects.equals(userStory.id, this.id);
        return aufwand_rel == userStory.aufwand_rel && mehrwert_rel == userStory.mehrwert_rel && risiko_rel == userStory.risiko_rel && strafe_rel == userStory.strafe_rel && Double.compare(prio, userStory.prio) == 0 && Objects.equals(titel, userStory.titel) && Objects.equals(projekt, userStory.projekt) && Objects.equals(kriterium, userStory.kriterium);
    }


    /* Berechnet Priorität nach Gloger(2013) */
    public double calcPrio() {
        return ((double) (mehrwert_rel + strafe_rel)) / (aufwand_rel + risiko_rel);
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public void setId(int id) {
        this.id = id;

    }

    @Override
    public String toString() {
        return "UserStory{" +
                "titel='" + titel + '\'' +
                ", aufwand=" + aufwand_rel +
                ", id=" + id +
                ", mehrwert=" + mehrwert_rel +
                ", risk=" + risiko_rel +
                ", strafe=" + strafe_rel +
                ", prio=" + prio +
                ", beschreibung=" + beschreibung +
                ", kriterium=" + kriterium +
                ", project='" + projekt + '\'' +
                '}';
    }
}




