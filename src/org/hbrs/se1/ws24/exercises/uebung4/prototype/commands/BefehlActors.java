package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.ContainerException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.FalscherParameterException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.NoMatchingEntryException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.PersistenceException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.Container;

import java.util.List;

public class BefehlActors implements Befehl {
    @Override
    public void ausfuehren(String[] parameter) throws ContainerException, PersistenceException {
        List<String> actors = Container.INSTANCE.getActorsList();
        if (actors.isEmpty()) throw new NoMatchingEntryException("no actors given");
        if (parameter.length == 2 && parameter[0].equalsIgnoreCase("-rm")) {
            if (!Container.INSTANCE.knownActor(parameter[1])) throw new NoMatchingEntryException("actor not known");
            Container.INSTANCE.deleteActor(parameter[1]);
            System.out.println("actor successfully removed");
        } else if (parameter.length == 0) {
            for (String actor : actors) {
                System.out.println(actor);
            }
        } else {
            throw new FalscherParameterException("Ungültige parameter! 'help' für Liste gültiger Parameter");
        }
    }
}
