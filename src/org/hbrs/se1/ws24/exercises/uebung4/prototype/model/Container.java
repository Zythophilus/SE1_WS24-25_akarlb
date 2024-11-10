package org.hbrs.se1.ws24.exercises.uebung4.prototype.model;

import org.hbrs.se1.ws24.exercises.uebung2.ConcreteMember;
import org.hbrs.se1.ws24.exercises.uebung3.persistence.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung3.persistence.PersistenceStrategy;
import org.hbrs.se1.ws24.exercises.uebung3.persistence.PersistenceStrategyStream;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.ContainerException;

import java.util.*;

/**
 * Klasse zum Management sowie zur Eingabe und Ausgabe von User-Stories.
 */

public enum Container {

	INSTANCE;

	// Interne ArrayList zur Abspeicherung der Objekte vom Type UserStory
	private List<UserStory> liste;

	private PersistenceStrategy<UserStory> strategy;


	// URL der Datei, in der die Objekte gespeichert werden, nicht verwendet da Speicherort in der Strategie gesetzt
	final static String LOCATION = "allStories.ser";

	Container() {
		liste = new ArrayList<>();
		this.strategy = new PersistenceStrategyStream<>();
	}

	/*
	 * Methode zum Speichern der Liste. Es wird die komplette Liste
	 * inklusive ihrer gespeicherten UserStory-Objekte gespeichert.
	 * 
	 */
	public void store() throws PersistenceException {
		if (strategy == null) throw new PersistenceException(PersistenceException.ExceptionType.NoStrategyIsSet, "Es wurde keine Persistance-Strategy gesetzt");
		strategy.save(liste);
	}

	/*
	 * Methode zum Laden der Liste. Es wird die komplette Liste
	 * inklusive ihrer gespeicherten UserStory-Objekte geladen.
	 * 
	 */
	public void load() throws PersistenceException {
		if (strategy == null) throw new PersistenceException(PersistenceException.ExceptionType.NoStrategyIsSet, "Es wurde keine Persistance-Strategy gesetzt");
		clear();
		liste = strategy.load();
		UserStory.synchronisiereIDQueue(liste);
	}

	/* Löschen aller User Stories in der Liste */
	// 'public' nur aus Testzwecken
	public void clear() {
		Iterator<UserStory> iterator = liste.iterator();
		while (iterator.hasNext()) {
			UserStory userStory = iterator.next();
			iterator.remove();
			ConcreteMember.freeID(userStory.getId());
		}
	}

	/* Methode zum Hinzufügen einer User Story und Prüfung auf Duplikate */
	public void addUserStory ( UserStory userStory ) throws ContainerException {

		for (UserStory story : liste) {
			if (userStory.getId().equals(story.getId())) throw new ContainerException(userStory.getId());
		}

		liste.add(userStory);
	}


	public boolean deleteUserStory (UserStory userStory) {
		for (UserStory storyStored : liste) {
			if(storyStored.getId().equals(userStory.getId())) {
				liste.remove(storyStored);
				//UserStory.freeID(storyStored.getId()); macht überhaupt sinn
				return true;
			}
		}
		return false; // besser exception schreiben UserStoryNotFoundException auch für getUserStory
	}

	/* Prüft, ob eine UserStory bereits vorhanden ist */
	private boolean contains( UserStory userStory) {
		/*int ID = userStory.getId();
		for ( UserStory userStory1 : liste) {
			if ( userStory1.getId() == ID ) {
				return true;
			}
		}
		return false;*/
		return liste.contains(userStory);

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

	/* Liefert eine bestimmte UserStory zurück */
	private UserStory getUserStory(int id) {
		for ( UserStory userStory : liste) {
			if (id == userStory.getId() ){
				return userStory;
			}
		}
		return null;
	}

	public void setPersistenceStrategy(PersistenceStrategy<UserStory> strategy) {
		this.strategy = strategy;
	}

}
