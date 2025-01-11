package org.hbrs.se1.ws24.tests.uebung4;

import static org.junit.jupiter.api.Assertions.*;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.control.Aufrufer;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.UnbekannterBefehlException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class AufruferTest {
    private Aufrufer aufrufer;

    @BeforeEach
    void setUp() {
        aufrufer = new Aufrufer();
    }

    @Test
    void testBefehlRegistrierung()  {
        aufrufer.nehmeBefehlAuf("test", (parameter) -> {
        });

        assertDoesNotThrow(() -> aufrufer.aufrufen("test", new String[]{}));
    }

    @Test
    void testUnbekannterBefehl() {
        assertThrows(UnbekannterBefehlException.class, () ->
                aufrufer.aufrufen("unbekannt", new String[]{})
        );
    }

}
