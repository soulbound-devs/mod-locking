package net.vakror.mod_locking.locking;

import java.util.HashMap;
import java.util.Map;

public class Restriction {
    protected Map<Type, Boolean> restrictions = new HashMap<Type, Boolean>();
    
    public Restriction set(Type type, boolean restrictions) {
        this.restrictions.put(type, restrictions);
        return this;
    }

    public Map<Type, Boolean> getRestrictions() {
        return restrictions;
    }

    public boolean doesRestrict(Type type) {
        return this.restrictions.getOrDefault(type, false);
    }

    public static Restriction defaultModRestrictions() {
        Restriction restrictions = new Restriction();
        restrictions.restrictions.put(Type.USABILITY, false);
        restrictions.restrictions.put(Type.CRAFTABILITY, false);
        restrictions.restrictions.put(Type.HITTABILITY, false);
        restrictions.restrictions.put(Type.BLOCK_INTERACTABILITY, false);
        restrictions.restrictions.put(Type.ENTITY_INTERACTABILITY, false);
        return restrictions;
    }

    public static Restriction defaultModRestrictions(boolean restricted) {
        Restriction restrictions = new Restriction();
        restrictions.restrictions.put(Type.USABILITY, restricted);
        restrictions.restrictions.put(Type.CRAFTABILITY, restricted);
        restrictions.restrictions.put(Type.HITTABILITY, restricted);
        restrictions.restrictions.put(Type.BLOCK_INTERACTABILITY, restricted);
        restrictions.restrictions.put(Type.ENTITY_INTERACTABILITY, restricted);
        return restrictions;
    }

    public static Restriction defaultItemRestrictions(boolean restricted) {
        Restriction restrictions = new Restriction();
        restrictions.restrictions.put(Type.USABILITY, restricted);
        restrictions.restrictions.put(Type.CRAFTABILITY, restricted);
        restrictions.restrictions.put(Type.HITTABILITY, restricted);
        return restrictions;
    }

    public static Restriction defaultBlockRestrictions(boolean restricted) {
        Restriction restrictions = new Restriction();
        restrictions.restrictions.put(Type.HITTABILITY, restricted);
        restrictions.restrictions.put(Type.BLOCK_INTERACTABILITY, restricted);
        return restrictions;
    }

    public static Restriction defaultEntityRestrictions(boolean restricted) {
        Restriction restrictions = new Restriction();
        restrictions.restrictions.put(Type.HITTABILITY, restricted);
        restrictions.restrictions.put(Type.ENTITY_INTERACTABILITY, restricted);
        return restrictions;
    }

    public enum Type {
        USABILITY,
        CRAFTABILITY,
        HITTABILITY,
        BLOCK_INTERACTABILITY,
        ENTITY_INTERACTABILITY;
    }
}
