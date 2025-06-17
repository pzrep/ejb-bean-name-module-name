package pzrep.ejblinksyntax.ebn;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import pzrep.ejblinksyntax.api.Cat;
import pzrep.ejblinksyntax.api.Creature;

@Stateless
public class Choco implements Cat {
    @EJB(lookup = "java:app/Base/Ant")
    private Creature creature;

    @Override
    public String color() {
        return "brown (java:app/<module-name>)";
    }

    @Override
    public String creatureName() {
        return creature.name();
    }
}
