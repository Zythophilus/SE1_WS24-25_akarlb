package org.hbrs.se1.ws24.exercises.uebung4.prototype.model;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.ContainerException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.NoMatchingEntryException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.PersistenceException;

import java.util.*;

/**
 * Klasse zum Management sowie zur Eingabe und Ausgabe von User-Stories.
 */
public enum Container {

	INSTANCE;

	// Interne ArrayList zur Abspeicherung der Objekte vom Type UserStory
	private List<UserStory> liste;

	private PersistenceStrategy<UserStory, String> strategy;

	private List<String> actors;


	// URL der Datei, in der die Objekte gespeichert werden, nicht verwendet da Speicherort in der Strategie gesetzt
	final static String LOCATION = "allStories.ser";

	Container() {
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.OFF); // um die nervige Nachricht zu unterdruecken...
		this.liste = new ArrayList<>();
		//this.strategy = new PersistenceStrategyStream<>();
		this.strategy = new PersistenceStrategyMongoDB<>();
		this.actors = new ArrayList<>();
	}

	/*
	 * Methode zum Speichern der Liste. Es wird die komplette Liste
	 * inklusive ihrer gespeicherten UserStory-Objekte gespeichert.
	 * 
	 */
	public void store() throws PersistenceException {
		if (strategy == null) throw new PersistenceException(PersistenceException.ExceptionType.NoStrategyIsSet, "Es wurde keine Persistance-Strategy gesetzt!");
		strategy.save(liste, actors);
	}

	/*
	 * Methode zum Laden der Liste. Es wird die komplette Liste
	 * inklusive ihrer gespeicherten UserStory-Objekte geladen.
	 * 
	 */
	public void load() throws PersistenceException {
		if (strategy == null) throw new PersistenceException(PersistenceException.ExceptionType.NoStrategyIsSet, "Es wurde keine Persistance-Strategy gesetzt!");
		liste = strategy.load();
		actors = strategy.loadActors();
		if (liste.isEmpty() && actors.isEmpty()) System.out.println("Es wurden noch keine Daten hinterlegt.");
		UserStory.synchronisiereIDQueue(liste);
	}

	/* Löschen aller User Stories in der Liste */
	// 'public' nur aus Testzwecken
	public void clear() throws PersistenceException {
		strategy.clear(liste, actors);
	}

	/* Methode zum Hinzufügen einer User Story und Prüfung auf Duplikate */
	public void addUserStory ( UserStory userStory ) throws ContainerException {
		for (UserStory story : liste) {
			if (userStory.equals(story)) throw new ContainerException(userStory.getId());
		}
		if (containsId(userStory.getId())) { // ungleiche UserStory aber mit einer alten (wieder vergebenen) ID
			userStory.setId(UserStory.getNextID()); // bekommt hier wieder eine neue, freie ID
		}
		liste.add(userStory);
	}

	public void addActor(String actor) throws ContainerException {
		if (actor == null || actor.trim().isEmpty()) {
			throw new NoMatchingEntryException("Ungültiger Akteur!");
		}
		for (String akteur : actors) {
			if (akteur.equalsIgnoreCase(actor)) {
				throw new ContainerException(actor);
			}
		}
		actors.add(actor.substring(0, 1).toUpperCase() + actor.substring(1)); // Akteure werden großgeschrieben
	}

	public boolean knownActor(String actor) {
		for (String knownActor : actors) {
			if (actor.equalsIgnoreCase(knownActor)) return true;
		}
		return false;
	}

	public void deleteActor(String name) {
		if (name == null || name.trim().isEmpty()) {
			return;
		}
		for (String actor : actors) {
			if (name.equalsIgnoreCase(actor)) {
				actors.remove(actor);
				return;
			}
		}
	}

	public void deleteUserStory (UserStory userStory) {
		for (UserStory storyStored : liste) {
			if(storyStored.getId().equals(userStory.getId())) {
				liste.remove(storyStored);
				UserStory.freeID(storyStored.getId());
				return;
			}
		}
	}

	/* Prüft, ob eine UserStory bereits vorhanden ist */
	public boolean containsId(Integer id) {
		for (UserStory storyStored : liste) {
			if(storyStored.getId().equals(id)) {
				return true;
			}
		}
		return false;

	}

	/* Ermittlung der Anzahl von internen User Stories */
	public int size() {
		return liste.size();
	}

	/* Methode zur Rückgabe der aktuellen Liste mit Stories */
	public List<UserStory> getCurrentList() {
		Collections.sort(liste);
		return liste;
	}

	public List<String> getActorsList() {
		Collections.sort(actors);
		return actors;
	}

	/* Liefert eine bestimmte UserStory zurück */
	public UserStory getUserStory(int id) {
		for ( UserStory userStory : liste) {
			if (id == userStory.getId() ){
				return userStory;
			}
		}
		throw new NoMatchingEntryException("Keine User Story mit der ID " + id + " vorhanden.");
	}

	public void setPersistenceStrategy(PersistenceStrategy<UserStory, String> strategy) {
		this.strategy = strategy;
	}

	public void initialisiereContainer() throws ContainerException {
		UserStory story1 = new UserStory("Jongleur", "jonglieren", "Zirkus", (byte) 2, (byte) 4, (byte) 1, (byte) 3, "Als Akteur möchte ich Akten jonglieren, um als Jongleur zu agieren");
		addUserStory(story1);
		UserStory story2 = new UserStory("Mensch", "Dasein", "Existenz", (byte) 2, (byte) 4, (byte) 4, (byte) 3, "Als Mensch möchte ich sein");
		addUserStory(story2);
		UserStory story3 = new UserStory("UserStory", "existieren", "Existenz", (byte) 1, (byte) 4, (byte) 4, (byte) 1, "Als User Story will ich existieren, um für Testzwecke zu dienen");
		addUserStory(story3);
		UserStory story4 = new UserStory("Verkehrt", "kehrtVer", "Zirkus", (byte) 4, (byte) 3, (byte) 2, (byte) 1, "um die Richtung zu ändern, möchte ich rückwärts gehen können, als Mensch");
		addUserStory(story4);
		UserStory story5 = new UserStory("Void", "Nichts", "Leere", (byte) 1, (byte) 1, (byte) 1, (byte) 4, "");
		addUserStory(story5);
		UserStory story6 = new UserStory("NummerEins", "Top", "Existenz", (byte) 1, (byte) 3, (byte) 3, (byte) 4, "Als #1 möchte ich sagen können, ich war Nummer 1");
		addUserStory(story6);
		UserStory story7 = new UserStory("Akteur", "Sein", "Existenz", (byte) 3, (byte) 3, (byte) 1, (byte) 4, "Ich will sein, um Akteur zu werden");
		addUserStory(story7);
		UserStory story8 = new UserStory("Kurz", "-", "Kürze", (byte) 1, (byte) 1, (byte) 1, (byte) 1, "zu kurz");
		addUserStory(story8);
		UserStory story9 = new UserStory("Lang", "………", "Länge", (byte) 4, (byte) 4, (byte) 4, (byte) 1, "zu lang zu lang zu lang zu lang zu lang zu lang zu lang zu lang");
		addUserStory(story9);
	}

}
