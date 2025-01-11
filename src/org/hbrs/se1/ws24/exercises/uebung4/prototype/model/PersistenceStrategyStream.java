package org.hbrs.se1.ws24.exercises.uebung4.prototype.model;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.PersistenceException;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class PersistenceStrategyStream<E, Z> implements PersistenceStrategy<E, Z> {

    // URL of file, in which the objects are stored
    private String location = "objects.ser";

    private String locationActors = "actors.ser";

    // Backdoor method used only for testing purposes, if the location should be changed in a Unit-Test
    // Example: Location is a directory (Streams do not like directories, so try this out ;-)!
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    /**
     * Method for saving a list of Member-objects to a disk (HDD)
     */
    public void save(List<E> member, List<Z> actors) throws PersistenceException {
        try (FileOutputStream fos = new FileOutputStream(location);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(member);
        } catch (IOException e) {
            throw new PersistenceException(PersistenceException.ExceptionType.SavingFailed, "Couldn't save Object");
        }
        try (FileOutputStream fos = new FileOutputStream(locationActors);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(actors);
        } catch (IOException e) {
            throw new PersistenceException(PersistenceException.ExceptionType.SavingFailed, "Couldn't save actor");
        }
    }

    @Override
    public List<Z> loadActors() throws PersistenceException {
        List<Z> newList;
        try {
            FileInputStream fis = new FileInputStream(locationActors);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                newList = (List<Z>) obj;
            } else {
                throw new PersistenceException(
                        PersistenceException.ExceptionType.LoadingFailed,
                        "Loaded object is not a List"
                );
            }
            fis.close();
            ois.close();
            return newList;
        } catch (FileNotFoundException e) {
            throw new PersistenceException(
                    PersistenceException.ExceptionType.NoStoredListAvailable,
                    "No List available to load"
            );
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenceException(
                    PersistenceException.ExceptionType.LoadingFailed,
                    "Couldn't load Object"
            );
        }
    }

    @Override
    public void clear(List<E> stories, List<Z> actors) throws PersistenceException {
        Iterator<E> iterator = stories.iterator();
        while (iterator.hasNext()) {
            UserStory userStory = (UserStory) iterator.next();
            UserStory.freeID(userStory.getId());
            iterator.remove();
        }
        Iterator<Z> iterator2 = actors.iterator();
        while (iterator2.hasNext()) {
            iterator2.next();
            iterator2.remove();
        }
    }



    @Override
    /**
     * Method for loading a list of Member-objects from a disk (HDD)
     * Some coding examples come for free :-)
     * Take also a look at the import statements above ;-!
     */
    public List<E> load() throws PersistenceException {
        List<E> newListe;
        try {
            FileInputStream fis = new FileInputStream(location);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                newListe = (List<E>) obj;
            } else {
                throw new PersistenceException(PersistenceException.ExceptionType.LoadingFailed, "Loaded object is not a List");
            }
            fis.close();
            ois.close();
            return newListe;
        } catch (FileNotFoundException e) {
            throw new PersistenceException(PersistenceException.ExceptionType.NoStoredListAvailable, "No List available to load");
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenceException(PersistenceException.ExceptionType.LoadingFailed, "Couldn't load Object");
        }
    }
}
