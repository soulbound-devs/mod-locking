package net.vakror.mod_locking.locking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import net.vakror.mod_locking.mod.util.CodecUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Restriction {
    public static final Codec<Restriction> RESTRICTION_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecUtils.RESTRICTION_MAP_CODEC.fieldOf("restrictions").forGetter((Restriction::get))
    ).apply(instance, Restriction::new));


    protected Map<Type, Boolean> restrictions = new HashMap<Type, Boolean>();
    
    public Restriction set(Type type, boolean restrictions) {
        this.restrictions.put(type, restrictions);
        return this;
    }

    public Restriction(Map<Type, Boolean> restrictions) {
        this.restrictions = restrictions;
    }
    public Restriction() {}

    public Map<Type, Boolean> get() {
        return restrictions;
    }

    public Map<Type, Boolean> getRestrictions() {
        return restrictions;
    }

    public boolean doesRestrict(Type type) {
        return this.restrictions.getOrDefault(type, false);
    }

    public static Restriction defaultRestrictions() {
        Restriction restrictions = new Restriction();
        restrictions.restrictions.put(Type.USABILITY, false);
        restrictions.restrictions.put(Type.CRAFTABILITY, false);
        restrictions.restrictions.put(Type.HITTABILITY, false);
        restrictions.restrictions.put(Type.BLOCK_INTERACTABILITY, false);
        restrictions.restrictions.put(Type.ENTITY_INTERACTABILITY, false);
        return restrictions;
    }

    public static Restriction defaultRestrictions(boolean restricted) {
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

    public enum Type implements StringRepresentable{
        USABILITY,
        CRAFTABILITY,
        HITTABILITY,
        BLOCK_INTERACTABILITY,
        ENTITY_INTERACTABILITY;

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        @Override
        public @NotNull String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}
