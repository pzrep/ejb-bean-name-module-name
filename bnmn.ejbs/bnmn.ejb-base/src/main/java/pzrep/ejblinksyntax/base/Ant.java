package pzrep.ejblinksyntax.base;

import jakarta.ejb.Stateless;
import pzrep.ejblinksyntax.api.Creature;

@Stateless
public class Ant implements Creature {
    @Override
    public String name() {
        return "Z";
    }
}
