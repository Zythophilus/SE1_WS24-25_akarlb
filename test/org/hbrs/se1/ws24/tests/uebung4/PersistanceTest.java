package org.hbrs.se1.ws24.tests.uebung4;

import static org.junit.jupiter.api.Assertions.*;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.*;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.ContainerException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class PersistanceTest {

    private Container container;
    private PersistenceStrategyStream<UserStory, String> strategy;
    private PersistenceStrategyMongoDB<UserStory, String> mongostrategy;
    private MongoDBControllerInterface mongoDBController;

    @BeforeEach
    void setUp() throws PersistenceException {
        container = Container.INSTANCE;
        strategy = new PersistenceStrategyStream<>();
        container.setPersistenceStrategy(strategy);
        container.clear();
    }

    @Test
    void testSaveAndLoad() throws ContainerException, PersistenceException {
        UserStory story = new UserStory("Test", "Kriterium", "Projekt", (byte) 3, (byte) 4, (byte) 5, (byte) 2, "Beschreibung");
        container.addUserStory(story);
        container.addActor("Akteur");

        container.store();
        container.clear();
        List<String> actorsCleared = container.getActorsList();
        List<UserStory> cleared = container.getCurrentList();
        assertEquals(0, cleared.size());
        assertFalse(container.containsId(story.getId()));
        assertEquals(0, actorsCleared.size());
        assertFalse(container.knownActor("Akteur"));
        container.load();

        List<UserStory> loaded = container.getCurrentList();
        assertEquals(1, loaded.size());
        assertEquals(story.getId(), loaded.get(0).getId());
    }

    @Test
    void testInvalidLocation() {
        strategy.setLocation("/invalid/path/test.ser");

        assertThrows(PersistenceException.class, () ->
                container.store()
        );
    }

    @Test
    void testNullStrategy() {
        container.setPersistenceStrategy(null);

        assertThrows(PersistenceException.class, () ->
                container.store()
        );

        container.setPersistenceStrategy(strategy); // fuer tear-down
    }

    @Test
    void testStrategySwitch() throws ContainerException, PersistenceException {
        mongostrategy = new PersistenceStrategyMongoDB<>();
        container.setPersistenceStrategy(mongostrategy);
        container.load();
        container.clear();

        // save und load
        UserStory story = new UserStory("Test", "Kriterium", "Projekt", (byte) 3, (byte) 4, (byte) 5, (byte) 2, "Beschreibung");
        container.addUserStory(story);
        container.addActor("Akteur");

        container.store();
        container.clear();
        List<String> actorsCleared = container.getActorsList();
        List<UserStory> cleared = container.getCurrentList();
        assertEquals(0, cleared.size());
        assertFalse(container.containsId(story.getId()));
        assertEquals(0, actorsCleared.size());
        assertFalse(container.knownActor("Akteur"));
        container.load();

        List<UserStory> loaded = container.getCurrentList();
        List<String> actorsLoaded = container.getActorsList();
        assertEquals(1, loaded.size());
        assertEquals(story.getId(), loaded.get(0).getId());
        assertEquals(1, actorsLoaded.size());
        assertTrue(container.knownActor("Akteur"));

        //mongostrategy.clearDB(); löscht alle meine daten

    }

    @AfterEach
    void tearDown() throws PersistenceException {
        Container.INSTANCE.clear();
        UserStory.synchronisiereIDQueue(Container.INSTANCE.getCurrentList());
        File f = new File("objects.ser");
        if (f.exists()) {
            f.delete();
        }
        strategy = null;
        container.setPersistenceStrategy(strategy);
    }
}
