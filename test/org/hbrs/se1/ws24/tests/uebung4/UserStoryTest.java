package org.hbrs.se1.ws24.tests.uebung4;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.Container;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.PersistenceStrategyStream;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.UserStory;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

public class UserStoryTest {

    private UserStory story;

    @BeforeEach
    void setUp() throws PersistenceException {
        Container.INSTANCE.setPersistenceStrategy(new PersistenceStrategyStream<>());
        Container.INSTANCE.clear();

        UserStory.synchronisiereIDQueue(Container.INSTANCE.getCurrentList());
        story = new UserStory("Test", "Kriterium", "Projekt", (byte) 2, (byte) 4, (byte) 3, (byte) 2, "Beschreibung");
    }

    @Test
    void testUserStoryCreation() {
        assertEquals(1, story.getId());
        assertEquals("Test", story.getTitel());
        assertEquals("Kriterium", story.getKriterium());
        assertEquals("Projekt", story.getProjekt());
        assertEquals(2, story.getAufwand_rel());
        assertEquals(4, story.getMehrwert_rel());
        assertEquals(3, story.getStrafe_rel());
        assertEquals(2, story.getRisiko_rel());
    }

    @Test
    void testPrioBerechnung() {
        double expectedPrio = (4.0 + 3.0) / (2.0 + 2.0);
        assertEquals(expectedPrio, story.getPrio(), 0.001);
    }

    @Test
    void testCompareTo() {
        UserStory story2 = new UserStory("Test2", "test2", "test", (byte) 2, (byte) 4, (byte) 3, (byte) 2, "Beschreibung");
        assertTrue(story.compareTo(story2) < 0); // hoehere Prio sollte kleiner sein
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
