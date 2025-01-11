package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;


import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.NoMatchingEntryException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.Container;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.FalscherParameterException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.UserStoryView;

public class BefehlDump implements Befehl {
    @Override
    public void ausfuehren(String[] parameter) throws FalscherParameterException {
        try {
            if (parameter.length == 0 ) {
                System.out.println(UserStoryView.dump(Container.INSTANCE.getCurrentList()));
            } else if (parameter[0].equalsIgnoreCase("-p")) {
                if (parameter.length == 1) throw new FalscherParameterException("Bitte geben Sie einen Projektnamen an.");
                System.out.println(UserStoryView.dumpProjekt(Container.INSTANCE.getCurrentList(), parameter[1]));
            } else if (Container.INSTANCE.containsId(Integer.parseInt(parameter[0]))) {
                System.out.println(UserStoryView.show(Container.INSTANCE.getUserStory(Integer.parseInt(parameter[0]))));
            } else if(parameter.length == 1 && !Container.INSTANCE.containsId(Integer.parseInt(parameter[0]))) {
                throw new NoMatchingEntryException("Es gibt keine User Story mit der ID " + parameter[0]);
            }
            else {
                throw new FalscherParameterException("Ungültiger Parameter!");
            }

        } catch (NumberFormatException e) {
            throw new FalscherParameterException("Ungültiger Parameter!");
        }
    }
}
