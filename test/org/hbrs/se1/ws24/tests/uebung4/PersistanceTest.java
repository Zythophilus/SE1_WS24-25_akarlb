package org.hbrs.se1.ws24.tests.uebung4;

import static org.junit.jupiter.api.Assertions.*;

import org.hbrs.se1.ws24.exercises.uebung3.persistence.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung3.persistence.PersistenceStrategyStream;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.Container;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.ContainerException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.UserStory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class PersistanceTest {

    private Container container;
    private PersistenceStrategyStream<UserStory> strategy;

    @BeforeEach
    void setUp() {
        container = Container.INSTANCE;
        container.clear();
        strategy = new PersistenceStrategyStream<>();
        container.setPersistenceStrategy(strategy);
    }

    @Test
    void testSaveAndLoad() throws ContainerException, PersistenceException {
        UserStory story = new UserStory("Test", "Kriterium", "Projekt", (byte) 3, (byte) 4, (byte) 5, (byte) 2);
        container.addUserStory(story);

        container.store();
        container.clear();
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
    }

    @AfterEach
    void tearDown() {
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
