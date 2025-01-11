package org.hbrs.se1.ws24.tests.uebung4;

import static org.junit.jupiter.api.Assertions.*;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.commands.*;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.ContainerException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.FalscherParameterException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.NoMatchingEntryException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.*;
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
    private PersistenceStrategyStream<UserStory, String> strategy;

    @BeforeEach
    void setUp() throws PersistenceException {
        container = Container.INSTANCE;
        strategy = new PersistenceStrategyStream<>();
        container.setPersistenceStrategy(strategy);
        container.clear();
    }

    @Test
    void testBefehlEnter() throws ContainerException {
        // richtige Eingabe
        String simulierteEingabe= String.join("\n",
                "Test",
                "Kriterium",
                "Projekt",
                "3",
                "4",
                "2",
                "1",
                "Beschreibung"
        );

        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulierteEingabe.getBytes());
        Scanner scanner = new Scanner(inputStream);

        BefehlEnter befehl = new BefehlEnter(scanner);
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

        // falsche Eingabe
        simulierteEingabe = String.join("\n",
                "",
                "Test",
                "Kriterium",
                "",
                "Projekt",
                "0",
                "3",
                "4",
                "-6",
                "2",
                "728",
                "5",
                "\n" // Beschreibung ist optional
        );

        inputStream = new ByteArrayInputStream(simulierteEingabe.getBytes());
        scanner = new Scanner(inputStream);

        befehl = new BefehlEnter(scanner);
        befehl.ausfuehren(new String[]{});

    }

    @Test
    void testBefehlDump() throws ContainerException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        BefehlDump befehl = new BefehlDump();

        // Test 1: dump
        UserStory story1 = new UserStory("Test1", "Kriterium1", "Projekt1",
                (byte)3, (byte)4, (byte)5, (byte)2, "Beschreibung1");
        UserStory story2 = new UserStory("Test2", "Kriterium2", "Projekt2",
                (byte)2, (byte)3, (byte)4, (byte)1, "Beschreibung2");
        container.addUserStory(story1);
        container.addUserStory(story2);

        befehl.ausfuehren(new String[]{});
        String output = outContent.toString();

        assertTrue(output.contains("Priorität"));
        assertTrue(output.contains("ID"));
        assertTrue(output.contains("Titel"));
        assertTrue(output.contains("Test1"));
        assertTrue(output.contains("Test2"));

        outContent.reset();

        // Test 2: dump -p
        befehl.ausfuehren(new String[]{"-p", "Projekt1"});
        output = outContent.toString();

        assertTrue(output.contains("Test1"));
        assertFalse(output.contains("Test2"));
        assertTrue(output.contains("Projekt = Projekt1"));

        outContent.reset();

        // Test 3: dump [ID]
        befehl.ausfuehren(new String[]{String.valueOf(story1.getId())});
        output = outContent.toString();

        assertTrue(output.contains("ID: " + story1.getId()));
        assertTrue(output.contains("Beschreibung:"));
        assertTrue(output.contains("Akzeptanzkriterium:"));
        assertTrue(output.contains("Priorität:"));
        assertTrue(output.contains("Projekt:"));

        outContent.reset();

        // Test 4: Text als ID
        assertThrows(NoMatchingEntryException.class, () -> {
            befehl.ausfuehren(new String[]{"-p", "NonExistingProject"});
        });

        // Test 5: ohne Projektnamen
        assertThrows(FalscherParameterException.class, () -> {
            befehl.ausfuehren(new String[]{"-p"});
        });

        // Test 6: nicht vorhandene ID
        assertThrows(NoMatchingEntryException.class, () -> {
            befehl.ausfuehren(new String[]{"999"});
        });

        // Test 7: falsche Parameter
        assertThrows(FalscherParameterException.class, () -> {
            befehl.ausfuehren(new String[]{"-invalid"});
        });

        // Test 8: langen Text formatieren
        UserStory storyLongText = new UserStory(
                "Test3",
                "This is a very long acceptance criterion that should be wrapped properly when displayed in the console output",
                "Projekt3",
                (byte)3, (byte)4, (byte)5, (byte)2,
                "This is a very long description that should be wrapped properly when displayed in the console output"
        );
        container.addUserStory(storyLongText);

        befehl.ausfuehren(new String[]{String.valueOf(storyLongText.getId())});
        output = outContent.toString();

        // Textformatierung
        assertTrue(output.contains("\n"));
        assertTrue(output.lines().anyMatch(line -> line.length() <= 50)); // Check if lines are wrapped at 50 chars

        // Zurücksetzen der Systemausgabe
        System.setOut(System.out);
    }


    @Test
    void testBefehlStore() throws ContainerException, PersistenceException {
        container.addUserStory(new UserStory("Test", "Kriterium", "Projekt", (byte) 3, (byte) 4, (byte) 5, (byte) 2, "Beschreibung"));

        BefehlStore befehl = new BefehlStore();
        befehl.ausfuehren(new String[]{});

        // Überprüfen, ob Datei erstellt wurde
        File f = new File("objects.ser");
        assertTrue(f.exists());
    }

    @Test
    void testBefehlLoad() throws PersistenceException, ContainerException {

        // Speichern einer Liste als Vorbereitung zum Laden
        container.addUserStory(new UserStory("Test", "Kriterium", "Projekt", (byte) 3, (byte) 4, (byte) 5, (byte) 2, "Beschreibung"));
        BefehlStore befehlSetUp = new BefehlStore();
        befehlSetUp.ausfuehren(new String[]{});

        // eigentliches Laden
        BefehlLoad befehl = new BefehlLoad();
        befehl.ausfuehren(new String[]{});

        // Überprüfen der geladenen Daten
        assertNotNull(Container.INSTANCE.getCurrentList());
    }

    @Test
    void testBefehlHelp() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        BefehlHelp befehl = new BefehlHelp();
        befehl.ausfuehren(new String[]{});

        assertTrue(outContent.toString().contains("Befehl     |   Erläuterung"));

        System.setOut(System.out);
    }

    @Test
    void testBefehlRemove() throws ContainerException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Test 1: vorhandene UserStory entfernen
        UserStory story = new UserStory("Test", "Kriterium", "Projekt",
                (byte)3, (byte)4, (byte)2, (byte)1, "Beschreibung");
        container.addUserStory(story);
        int storyId = story.getId();

        BefehlRemove befehl = new BefehlRemove();
        befehl.ausfuehren(new String[]{String.valueOf(storyId)});

        assertTrue(outContent.toString().contains("User Story mit ID " + storyId + " erfolgreich gelöscht!"));
        assertEquals(0, container.size());

        outContent.reset();

        // Test 2: nicht vorhandene UserStory entfernen
        int nonExistingId = 999;
        BefehlRemove finalBefehl = new BefehlRemove();
        assertThrows(NoMatchingEntryException.class, () -> {
            finalBefehl.ausfuehren(new String[]{String.valueOf(nonExistingId)});
        });

        // Test 3: undo
        befehl.undo();
        assertEquals(1, container.size());
        UserStory restoredStory = container.getUserStory(storyId);
        assertEquals(story.getTitel(), restoredStory.getTitel());
        assertEquals(story.getBeschreibung(), restoredStory.getBeschreibung());

        // Test 4: redo
        befehl.redo();
        assertEquals(0, container.size());
        assertThrows(NoMatchingEntryException.class, () -> {
            container.getUserStory(storyId);
        });

        // Test 5: kopieren von Befehlen
        UserStory story2 = new UserStory("Test2", "Kriterium2", "Projekt2",
                (byte)2, (byte)3, (byte)1, (byte)4, "Beschreibung2");
        container.addUserStory(story2);
        int story2Id = story2.getId();

        BefehlRemove befehl2 = new BefehlRemove();
        befehl2.ausfuehren(new String[]{String.valueOf(story2Id)});

        Undoable copiedCommand = befehl2.copy();
        container.addUserStory(story2);

        ((BefehlRemove)copiedCommand).ausfuehren(new String[]{String.valueOf(story2Id)});
        assertEquals(0, container.size());

        System.setOut(System.out);
    }

    @Test
    void testBefehlActors() throws ContainerException, PersistenceException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        BefehlActors befehl = new BefehlActors();

        // Test 1: leere Akteuren-Liste
        assertThrows(NoMatchingEntryException.class, () -> {
            befehl.ausfuehren(new String[]{});
        });


        // Test 2: Akteure anzeigen lassen
        Container.INSTANCE.getActorsList().add("Akteur1");
        Container.INSTANCE.getActorsList().add("Akteur2");
        befehl.ausfuehren(new String[]{});
        String output = outContent.toString();
        assertTrue(output.contains("Akteur1"));
        assertTrue(output.contains("Akteur2"));

        outContent.reset();

        // Test 3: vorhandenen Akteur entfernen
        befehl.ausfuehren(new String[]{"-rm", "Akteur1"});
        output = outContent.toString();
        assertTrue(output.contains("actor successfully removed"));
        assertFalse(Container.INSTANCE.getActorsList().contains("Akteur1"));

        outContent.reset();

        // Test 4: nicht vorhandenen Akteur löschen
        assertThrows(NoMatchingEntryException.class, () -> {
            befehl.ausfuehren(new String[]{"-rm", "NichtAkteur"});
        });

        // Test 5: falsche Parameter
        assertThrows(FalscherParameterException.class, () -> {
            befehl.ausfuehren(new String[]{"falscheParameter"});
        });

        System.setOut(System.out);
    }

    @Test
    void testBefehlAddElement() throws ContainerException, PersistenceException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Test 1: Akteur registrieren
        String[] actorParams = {"-actor", "NeuerAkteur"};
        BefehlAddElement befehl = new BefehlAddElement(new Scanner(System.in));
        befehl.ausfuehren(actorParams);

        assertTrue(outContent.toString().contains("Der Akteur 'NeuerAkteur' wurde im System registriert!"));
        assertTrue(Container.INSTANCE.getActorsList().contains("NeuerAkteur"));

        outContent.reset();

        // Test 2: Mehrwert hinzufuegen
        UserStory testStory = new UserStory("Test", "Kriterium", "Projekt",
                (byte)3, (byte)4, (byte)2, (byte)1,
                "Als Benutzer möchte ich Feature X damit ich Y machen kann");
        container.addUserStory(testStory);

        String simulatedInput = "um effizienter zu arbeiten\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        befehl = new BefehlAddElement(scanner);
        befehl.ausfuehren(new String[]{String.valueOf(testStory.getId()), "-value"});

        assertTrue(outContent.toString().contains("Der neue Mehrwert wurde erfolgreich zur User Story hinzugefuegt!"));

        outContent.reset();

        // Test 3: Beschreibung hinzufuegen
        simulatedInput = "Neue Beschreibung\n";
        inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        scanner = new Scanner(inputStream);

        befehl = new BefehlAddElement(scanner);
        befehl.ausfuehren(new String[]{String.valueOf(testStory.getId()), "-spec"});

        assertTrue(outContent.toString().contains("Die neue Beschreibung wurde erfolgreich zur User Story hinzugefuegt!"));
        assertEquals("Neue Beschreibung", testStory.getBeschreibung());

        // Test 4: ungueltige Parameter
        BefehlAddElement finalBefehl = befehl;
        assertThrows(FalscherParameterException.class, () -> {
            finalBefehl.ausfuehren(new String[]{});
        });

        BefehlAddElement finalBefehl1 = befehl;
        assertThrows(FalscherParameterException.class, () -> {
            finalBefehl1.ausfuehren(new String[]{"-actor"});
        });

        // Test 5: Mehrwert einer nicht vorhandenen UserStory hinzufuegen
        BefehlAddElement finalBefehl2 = befehl;
        assertThrows(NoMatchingEntryException.class, () -> {
            finalBefehl2.ausfuehren(new String[]{"999", "-value"});
        });


        // Test 6: undo und redo fuer -actor
        BefehlAddElement actorBefehl = new BefehlAddElement(new Scanner(System.in));
        actorBefehl.ausfuehren(new String[]{"-actor", "TestActor"});

        // undo
        actorBefehl.undo();
        assertFalse(Container.INSTANCE.getActorsList().contains("TestActor"));

        // redo
        actorBefehl.redo();
        assertTrue(Container.INSTANCE.getActorsList().contains("TestActor"));

        // Test 7: undo und redo fuer -spec
        String originalSpec = "Original Beschreibung";
        String newSpec = "Neue Beschreibung";
        UserStory undoTestStory = new UserStory("Test7", "Kriterium", "Projekt",
                (byte)3, (byte)4, (byte)2, (byte)1, originalSpec);
        container.addUserStory(undoTestStory);

        simulatedInput = newSpec + "\n";
        inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        scanner = new Scanner(inputStream);

        BefehlAddElement descBefehl = new BefehlAddElement(scanner);
        descBefehl.ausfuehren(new String[]{String.valueOf(undoTestStory.getId()), "-spec"});

        // undo
        descBefehl.undo();
        assertEquals(originalSpec, undoTestStory.getBeschreibung());

        // redo
        descBefehl.redo();
        assertEquals(newSpec, undoTestStory.getBeschreibung());

        System.setOut(System.out);
    }

    @Test
    void testBefehlAnalyze() throws ContainerException, PersistenceException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        BefehlAnalyze befehl = new BefehlAnalyze();

        // Test 1: analyze [ID]
        UserStory goodStory = new UserStory("Test", "Kriterium", "Projekt",
                (byte)3, (byte)4, (byte)2, (byte)1,
                "Als Benutzer möchte ich Feature X damit ich effizienter arbeiten kann");
        Container.INSTANCE.addUserStory(goodStory);
        Container.INSTANCE.addActor("Benutzer");

        befehl.ausfuehren(new String[]{String.valueOf(goodStory.getId())});
        String output = outContent.toString();
        assertTrue(output.contains("Die User Story mit der ID " + goodStory.getId() + " hat folgende Qualität:"));

        outContent.reset();

        // Test 2: analyze -details
        befehl.ausfuehren(new String[]{String.valueOf(goodStory.getId()), "-details"});
        output = outContent.toString();
        assertTrue(output.contains("Details:"));

        outContent.reset();

        // Test 3: analyze -hints
        befehl.ausfuehren(new String[]{String.valueOf(goodStory.getId()), "-hints"});
        output = outContent.toString();
        assertTrue(output.contains("Hints:"));

        outContent.reset();

        // Test 4: analyze -details -hints
        befehl.ausfuehren(new String[]{String.valueOf(goodStory.getId()), "-details", "-hints"});
        output = outContent.toString();
        assertTrue(output.contains("Details:"));
        assertTrue(output.contains("Hints:"));

        outContent.reset();

        // Test 5: analyze -all
        UserStory secondStory = new UserStory("Test2", "Kriterium", "Projekt",
                (byte)3, (byte)4, (byte)2, (byte)1,
                "Als Admin möchte ich Feature Y damit ich besser verwalten kann");
        Container.INSTANCE.addUserStory(secondStory);
        Container.INSTANCE.addActor("Admin");

        befehl.ausfuehren(new String[]{"-all"});
        output = outContent.toString();
        assertTrue(output.contains("User Stories haben durchschnittlich folgende Qualität:"));

        outContent.reset();

        // Test 6: ungueltige Parameter
        assertThrows(FalscherParameterException.class, () -> {
            befehl.ausfuehren(new String[]{});
        });

        // Test 7: mit nicht vorhandener UserStory
        assertThrows(NoMatchingEntryException.class, () -> {
            befehl.ausfuehren(new String[]{"999"});
        });

        // Test 8: schlechte UserStory
        UserStory poorStory1 = new UserStory("Test3", "Kriterium", "Projekt",
                (byte)3, (byte)4, (byte)2, (byte)1,
                "Kurz aber lang genug"); // Too short, no actor, no value
        Container.INSTANCE.addUserStory(poorStory1);

        befehl.ausfuehren(new String[]{String.valueOf(poorStory1.getId()), "-details", "-hints"});
        output = outContent.toString();
        assertTrue(output.contains("Beschreibung ist zu kurz"));
        assertTrue(output.contains("Kein Akteur ersichtlich"));


        UserStory poorStory2 = new UserStory("Test4", "Kriterium", "Projekt",
                (byte)3, (byte)4, (byte)2, (byte)1,
                "Zu kurz");
        Container.INSTANCE.addUserStory(poorStory2);
        befehl.ausfuehren(new String[]{String.valueOf(poorStory2.getId()), "-details", "-hints"});
        output = outContent.toString();
        assertTrue(output.contains("Keine aussagekräftige Beschreibung vorhanden"));
        assertTrue(output.contains("Fügen sie eine aussagekräftige Beschreibung hinzu!"));

        // Test 9: falsche flag
        assertThrows(FalscherParameterException.class, () -> {
            befehl.ausfuehren(new String[]{String.valueOf(goodStory.getId()), "-flag"});
        });

        System.setOut(System.out);
    }

    @Test
    void testAnalyzeStrategies() throws ContainerException {
        UserStory story = new UserStory("Test", "Kriterium", "Projekt",
                (byte)3, (byte)4, (byte)2, (byte)1,
                "Als Benutzer möchte ich Feature X damit ich effizienter arbeiten kann");
        Container.INSTANCE.addActor("Benutzer");

        // Test Standard
        AnalyzeStrategie standardStrategy = new AnalyzeStandard();
        AnalyzeErgebnis standardResult = standardStrategy.analyze(story);
        assertTrue(standardResult.getRating() > 0);

        // Test Detailed
        AnalyzeStrategie detailedStrategy = new AnalyzeDetailed();
        AnalyzeErgebnis detailedResult = detailedStrategy.analyze(story);
        assertTrue(detailedResult.getRating() > 0);
        assertFalse(detailedResult.getDetails().isEmpty());
        System.out.println(detailedResult.getHinweise());
        assertTrue(detailedResult.getHinweise().isEmpty());
    }

    @AfterEach
    void tearDown() throws PersistenceException {
        Container.INSTANCE.clear();
        UserStory.synchronisiereIDQueue(Container.INSTANCE.getCurrentList());
        File f = new File("objects.ser");
        if (f.exists()) {
            f.delete();
        }
    }
}
