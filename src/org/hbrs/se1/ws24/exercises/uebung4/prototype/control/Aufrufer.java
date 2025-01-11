package org.hbrs.se1.ws24.exercises.uebung4.prototype.control;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.commands.*;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.ContainerException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.MaxRedoException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.MaxUndoException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.UnbekannterBefehlException;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Verwaltung und Aufrufung der bekannten Befehle
 */
public class Aufrufer {

    // Befehlsregister
    private final Map<String, Befehl> befehle;
    // undo bzw. redo-Stacks
    private final Stack<Undoable> undoStack;
    private final Stack<Undoable> redoStack;

    public Aufrufer() {
        this.befehle = new HashMap<>();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        nehmeBefehlAuf("undo", new BefehlUndo());  // interne meta-befehle
        nehmeBefehlAuf("redo", new BefehlRedo());
    }

    // Aufnahme eines Befehls in das Register
    // public aus Testzwecken
    public void nehmeBefehlAuf(String name, Befehl befehl) {
        befehle.put(name, befehl);
    }

    // Befehlsaufrufung
    // public nur aus Testzwecken
    public void aufrufen(String name, String[] parameter) throws ContainerException, PersistenceException, UnbekannterBefehlException {
        Befehl befehl = befehle.get(name);
        if (befehl != null) {
            befehl.ausfuehren(parameter);
            if (befehl instanceof Undoable &&
                    ((befehl instanceof BefehlEnter && ((BefehlEnter) befehl).getEnteredStory() != null) ||
                            (befehl instanceof BefehlAddElement && (((BefehlAddElement) befehl).getBeschreibungNeu() != null
                                    || (((BefehlAddElement) befehl).getActor() != null))) ||
                                    (befehl instanceof BefehlRemove && ((BefehlRemove)befehl).getRemovedStory() != null))) {
                undoStack.push(((Undoable) befehl).copy());
            }
        } else {
            throw new UnbekannterBefehlException();
        }
    }

    // als interner Meta-Befehl direkt im Aufrufer integriert
    private class BefehlUndo implements Befehl {
        @Override
        public void ausfuehren(String[] parameter) throws ContainerException, PersistenceException, MaxUndoException {
            try {
                Undoable befehl = undoStack.pop();
                befehl.undo();
                redoStack.push(befehl);
            } catch (EmptyStackException e) {
                throw new MaxUndoException();
            }
        }
    }

    // dito
    private class BefehlRedo implements Befehl {
        @Override
        public void ausfuehren(String[] parameter) throws ContainerException, MaxRedoException {
            try {
                Undoable befehl = redoStack.pop();
                befehl.redo();
                undoStack.push(befehl);
            } catch(EmptyStackException e) {
                throw new MaxRedoException();
            }
        }
    }
}
