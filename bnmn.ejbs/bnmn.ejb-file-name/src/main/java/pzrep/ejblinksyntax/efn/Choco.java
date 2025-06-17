package pzrep.ejblinksyntax.efn;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import pzrep.ejblinksyntax.api.Cat;
import pzrep.ejblinksyntax.api.Creature;

@Stateless
public class Choco implements Cat {
    @EJB(beanName = "pzrep.ejb-bean-name-module-name-bnmn.ejb-base-1.0-SNAPSHOT.jar#Ant")
    private Creature creature;

    @Override
    public String color() {
        return "brown (file-name#)";
    }

    @Override
    public String creatureName() {
        return creature.name();
    }
}
