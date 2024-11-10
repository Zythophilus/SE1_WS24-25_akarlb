package org.hbrs.se1.ws24.tests.uebung4;

import static org.junit.jupiter.api.Assertions.*;

import org.hbrs.se1.ws24.exercises.uebung3.persistence.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung3.persistence.PersistenceStrategyStream;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;




public class BefehlTest {

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
    void testBefehlEnter() throws ContainerException {
        //richtiger Input
        String simulierteEingabe= String.join("\n",
                "Test",
                "Kriterium",
                "Projekt",
                "3",
                "4",
                "2",
                "1"
        );

        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulierteEingabe.getBytes());
        Scanner scanner = new Scanner(inputStream);

        Befehl_Enter befehl = new Befehl_Enter(scanner);
        befehl.ausfuehren(new String[]{});

        assertEquals(1, container.size());
        UserStory addedStory = container.getCurrentList().get(0);
        assertEquals("Test", addedStory.getTitel());
        assertEquals("Kriterium", addedStory.getKriterium());
        assertEquals("Projekt", addedStory.getProjekt());
        assertEquals(3, addedStory.getAufwand_rel());
        assertEquals(4, addedStory.getMehrwert_rel());
        assertEquals(2, addedStory.getStrafe_rel());
        assertEquals(1, addedStory.getRisiko_rel());

        // falsche eingabe
        simulierteEingabe = String.join("\n",
                "",
                "Test",
                "Kriterium",
                "t".repeat(16),
                "Projekt",
                "0",
                "3",
                "4",
                "-6",
                "2",
                "728",
                "1"
        );

        inputStream = new ByteArrayInputStream(simulierteEingabe.getBytes());
        scanner = new Scanner(inputStream);

        befehl = new Befehl_Enter(scanner);
        befehl.ausfuehren(new String[]{});

    }

    @Test
    void testBefehlDump() throws ContainerException {

        container.addUserStory(new UserStory("Test", "Kriterium", "Projekt", (byte) 3, (byte) 4, (byte) 5, (byte) 2));

        // Umleiten der Systemausgabe
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Befehl_Dump befehl = new Befehl_Dump();
        befehl.ausfuehren(new String[]{});

        assertTrue(outContent.toString().contains("Test"));

        // Zurücksetzen der Systemausgabe
        System.setOut(System.out);
    }

    @Test
    void testBefehlStore() throws ContainerException, PersistenceException {
        container.addUserStory(new UserStory("Test", "Kriterium", "Projekt", (byte) 3, (byte) 4, (byte) 5, (byte) 2));

        Befehl_Store befehl = new Befehl_Store();
        befehl.ausfuehren(new String[]{});

        // Überprüfen, ob Datei erstellt wurde
        File f = new File("objects.ser");
        assertTrue(f.exists());
    }

    @Test
    void testBefehlLoad() throws PersistenceException, ContainerException {

        // Speichern einer Liste als Vorbereitung zum Laden
        container.addUserStory(new UserStory("Test", "Kriterium", "Projekt", (byte) 3, (byte) 4, (byte) 5, (byte) 2));
        Befehl_Store befehlSetUp = new Befehl_Store();
        befehlSetUp.ausfuehren(new String[]{});

        // eigentliches Laden
        Befehl_Load befehl = new Befehl_Load();
        befehl.ausfuehren(new String[]{});

        // Überprüfen der geladenen Daten
        assertNotNull(Container.INSTANCE.getCurrentList());
    }

    @Test
    void testBefehlHelp() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Befehl_Help befehl = new Befehl_Help();
        befehl.ausfuehren(new String[]{});

        assertTrue(outContent.toString().contains("Befehl   |   Erläuterung"));

        System.setOut(System.out);
    }

    @AfterEach
    void tearDown() {
        Container.INSTANCE.clear();
        UserStory.synchronisiereIDQueue(Container.INSTANCE.getCurrentList());
        File f = new File("objects.ser");
        if (f.exists()) {
            f.delete();
        }
    }
}
