package org.hbrs.se1.ws24.exercises.uebung4.prototype.main;


import org.hbrs.se1.ws24.exercises.uebung4.prototype.control.Anwendung;

public class Main {

    /**
     * Main zum mainen der main()
     */
    public static void main(String[] args) {
        try {
            Anwendung.INSTANCE.starten();
        } finally {
            System.exit(0);
        }
    }

}
