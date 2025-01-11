package org.hbrs.se1.ws24.exercises.uebung4.prototype.model;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.PersistenceException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PersistenceStrategyMongoDB<E, Z> implements PersistenceStrategy<E, Z> {

    private MongoDBControllerInterface mongoController;

    public PersistenceStrategyMongoDB() {
        this.mongoController = new MongoDBControllerImpl();
    }

    @Override
    public void save(List<E> member, List<Z> actors) throws PersistenceException {
        try {
            mongoController.openConnection();
            mongoController.clearUserStories();
            for (E item : member) {
                if (item instanceof UserStory) {
                    mongoController.insertUserStory((UserStory) item);
                } else {
                    throw new PersistenceException(PersistenceException.ExceptionType.ImplementationNotAvailable,
                            "Cannot save object of type " + item.getClass().getName() + " as it does not match UserStory");
                }
            }
            mongoController.clearActors();
            for (Z actor : actors) {
                if (actor instanceof String) {
                    mongoController.insertActor((String) actor);
                } else {
                    throw new PersistenceException(
                            PersistenceException.ExceptionType.ImplementationNotAvailable,
                            "Cannot save object of type " + actor.getClass().getName() + " as it does not match String"
                    );
                }
            }
        } catch (IllegalStateException e) {
            throw new PersistenceException(PersistenceException.ExceptionType.ConnectionNotAvailable, "Connection to MongoDB could not be established.");
        } finally {
            mongoController.closeConnection();
        }
    }

    @Override
    public List<E> load() throws PersistenceException {
        try {
            mongoController.openConnection();
            ArrayList<UserStory> userStories = mongoController.listUserStories();
            return (List<E>) userStories;
        } catch (IllegalStateException e) {
            throw new PersistenceException(PersistenceException.ExceptionType.ConnectionNotAvailable, "Connection to MongoDB could not be established.");
        } finally {
            mongoController.closeConnection();
        }
    }

    @Override
    public List<Z> loadActors() throws PersistenceException {
        try {
            mongoController.openConnection();
            ArrayList<String> actors = mongoController.listActors();
            return (List<Z>) actors;
        } catch (IllegalStateException e) {
            throw new PersistenceException(
                    PersistenceException.ExceptionType.ConnectionNotAvailable,
                    "Connection to MongoDB could not be established."
            );
        } finally {
            mongoController.closeConnection();
        }
    }

    // Testzwecke
    public void clearDB() {
        try {
            mongoController.openConnection();
            mongoController.clearUserStories();
            mongoController.clearActors();
        } finally {
            mongoController.closeConnection();
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
}
