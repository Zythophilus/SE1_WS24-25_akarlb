package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.ContainerException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.FalscherParameterException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.NoMatchingEntryException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.Container;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.UserStory;

public class BefehlRemove implements Undoable {
    private UserStory removedStory;
    private int id;

    public BefehlRemove() {
    }

    private BefehlRemove(UserStory story) {
        this.removedStory = story;
        this.id = story.getId();
    }

    @Override
    public void ausfuehren(String[] parameter) throws ContainerException {
        try {
            if (parameter.length == 1) {
                id = Integer.parseInt(parameter[0]);
                if (Container.INSTANCE.containsId(id)) {
                    removedStory = Container.INSTANCE.getUserStory(id);
                    Container.INSTANCE.deleteUserStory(removedStory);
                    System.out.println("User Story mit ID " + id + " erfolgreich gelöscht!");
                } else {
                    throw new NoMatchingEntryException("Keine User Story mit der ID " + parameter[0]  +  " vorhanden.");
                }

            } else {
                throw new FalscherParameterException("Bitte geben Sie nur eine gueltige User Story ID an!");
            }

        } catch (NumberFormatException e) {
            throw new NoMatchingEntryException("Keine User Story mit der ID " + parameter[0] + " vorhanden.");
        }
    }

    @Override
    public void undo() throws ContainerException {
        if (removedStory != null) {
            Container.INSTANCE.addUserStory(removedStory);
        }
    }

    @Override
    public void redo() throws ContainerException {
        if (removedStory != null) {
            Container.INSTANCE.deleteUserStory(removedStory);
        }
    }

    @Override
    public Undoable copy() {
        return new BefehlRemove(removedStory);
    }

    public UserStory getRemovedStory() {
        return removedStory;
    }
}
