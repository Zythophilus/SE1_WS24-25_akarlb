package org.hbrs.se1.ws24.exercises.uebung4.prototype.commands;


import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.Container;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.FalscherParameterException;
import org.hbrs.se1.ws24.exercises.uebung4.prototype.model.UserStoryView;

public class Befehl_Dump implements Befehl {
    @Override
    public void ausfuehren(String[] parameter) throws FalscherParameterException {
        if (parameter.length == 0 ) {
            System.out.println(UserStoryView.dump(Container.INSTANCE.getCurrentList()));
            return;
        }
        if (parameter[0].equalsIgnoreCase("-p")) {
            if (parameter.length == 1) throw new FalscherParameterException("Bitte geben Sie einen Projektnamen an.");
            System.out.println(UserStoryView.dumpProjekt(Container.INSTANCE.getCurrentList(), parameter[1]));
        } else {
            throw new FalscherParameterException("Ungültiger Parameter!");
        }
    }
}
