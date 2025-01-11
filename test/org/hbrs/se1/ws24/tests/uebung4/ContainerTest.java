package org.hbrs.se1.ws24.tests.uebung4;

import static org.junit.jupiter.api.Assertions.*;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.Container;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.ContainerException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.PersistenceStrategyStream;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.UserStory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;


public class ContainerTest {

    private Container container;

    @BeforeEach
    void setUp() throws PersistenceException {
        container = Container.INSTANCE;
        container.setPersistenceStrategy(new PersistenceStrategyStream<>());
        container.clear();
    }

    @Test
    void testAddUserStory() throws ContainerException {
        UserStory story = new UserStory("Test", "test", "test", (byte) 5, (byte) 4, (byte) 3, (byte) 2, "Beschreibung");
        container.addUserStory(story);
        assertEquals(1, container.size());
    }

    @Test
    void testDuplicateUserStory() throws ContainerException {
        UserStory story1 = new UserStory("Test", "test", "test", (byte) 5, (byte) 4, (byte) 3, (byte) 2, "Beschreibung");
        UserStory story2 = new UserStory("Test2", "test2", "test", (byte) 2, (byte) 4, (byte) 3, (byte) 2, "Beschreibung2");
        container.addUserStory(story2);
        container.addUserStory(story1);
        assertEquals(2, container.size());
    }


    @Test
    void testPersistence() throws ContainerException, PersistenceException {
        UserStory story = new UserStory("Test", "test", "test", (byte) 5, (byte) 4, (byte) 3, (byte) 2, "Beschreibung");
        container.addUserStory(story);
        container.store();

        container.clear();
        assertEquals(0, container.size());

        container.load();
        assertEquals(1, container.size());
    }

    @AfterEach
    void tearDown() throws PersistenceException {
        Container.INSTANCE.clear();
        File f = new File("objects.ser");
        if (f.exists()) {
            f.delete();
        }
    }
}
