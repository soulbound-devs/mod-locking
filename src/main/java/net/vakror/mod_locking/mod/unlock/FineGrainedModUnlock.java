package net.vakror.mod_locking.mod.unlock;

import com.google.gson.annotations.Expose;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.vakror.mod_locking.locking.Restriction;

import static net.vakror.mod_locking.mod.util.CodecUtils.*;

public class FineGrainedModUnlock extends Unlock<FineGrainedModUnlock> {
    public static final Codec<FineGrainedModUnlock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(FineGrainedModUnlock::getName),
            POINT_MAP_CODEC.fieldOf("cost").forGetter(FineGrainedModUnlock::getCost),
            Codec.STRING.listOf().optionalFieldOf("requiredUnlocks").forGetter((FineGrainedModUnlock::getOptionalRequiredUnlocksAsList)),
            Codec.FLOAT.fieldOf("x").forGetter(FineGrainedModUnlock::getX),
            Codec.FLOAT.fieldOf("y").forGetter(FineGrainedModUnlock::getY),
            Codec.STRING.fieldOf("icon").forGetter(FineGrainedModUnlock::getIcon),
            CompoundTag.CODEC.optionalFieldOf("iconNbt").forGetter(FineGrainedModUnlock::getOptionalIconNbt),
            Codec.STRING.fieldOf("description").forGetter(FineGrainedModUnlock::getDescription),
            Codec.STRING.fieldOf("treeName").forGetter(FineGrainedModUnlock::getTreeName),
            TYPE_MAP_CODEC.optionalFieldOf("itemRestrictions").forGetter(FineGrainedModUnlock::getItemRestrictionsOptional),
            TYPE_MAP_CODEC.optionalFieldOf("blockRestrictions").forGetter(FineGrainedModUnlock::getBlockRestrictionsOptional),
            TYPE_MAP_CODEC.optionalFieldOf("entityRestrictions").forGetter(FineGrainedModUnlock::getEntityRestrictionsOptional)
            // up to 16 fields can be declared here
    ).apply(instance, FineGrainedModUnlock::new));

    @Expose
    protected Map<String, Restriction> itemRestrictions = new HashMap<String, Restriction>();
    @Expose
    protected Map<String, Restriction> blockRestrictions = new HashMap<String, Restriction>();
    @Expose
    protected Map<String, Restriction> entityRestrictions = new HashMap<String, Restriction>();

    public FineGrainedModUnlock(String name, Map<String, Integer> cost, float x, float y, String... requiredUnlocks) {
        super(name, cost, requiredUnlocks, x, y);
    }

    public FineGrainedModUnlock(String name, Map<String, Integer> cost, Optional<List<String>> requiredUnlocks, float x, float y, String icon, Optional<CompoundTag> iconNbt, String description, String treeName, Optional<Map<String, Restriction>> itemRestrictions, Optional<Map<String, Restriction>> blockRestrictions, Optional<Map<String, Restriction>> entityRestrictions) {
        super(name, cost, getRequiredUnlocks(requiredUnlocks), x, y);
        this.withIcon(icon);
        iconNbt.ifPresent(this::withIconNbt);
        this.withDescription(Component.literal(description));
        this.withTree(treeName);
        itemRestrictions.ifPresent(this::withItemRestrictionsMap);
        blockRestrictions.ifPresent(this::withBlockRestrictionsMap);
        entityRestrictions.ifPresent(this::withEntityRestrictionsMap);
    }

    public static String[] getRequiredUnlocks(Optional<List<String>> unlocks) {
        return unlocks.map(strings -> strings.toArray(new String[0])).orElseGet(() -> new String[]{""});
    }

    public Map<String, Restriction> getItemRestrictions() {
        return this.itemRestrictions;
    }

    public Map<String, Restriction> getBlockRestrictions() {
        return this.blockRestrictions;
    }

    public Map<String, Restriction> getEntityRestrictions() {
        return this.entityRestrictions;
    }

    public Optional<Map<String, Restriction>> getItemRestrictionsOptional() {
        if (this.itemRestrictions == null) {
            return Optional.empty();
        }
        return Optional.of(this.itemRestrictions);
    }

    public Optional<Map<String, Restriction>> getBlockRestrictionsOptional() {
        if (this.blockRestrictions == null) {
            return Optional.empty();
        }
        return Optional.of(this.blockRestrictions);
    }

    public Optional<Map<String, Restriction>> getEntityRestrictionsOptional() {
        if (this.entityRestrictions == null) {
            return Optional.empty();
        }
        return Optional.of(this.entityRestrictions);
    }

    public void setItemRestrictions(Map<String, Restriction> itemRestrictions) {
        this.itemRestrictions = itemRestrictions;
    }

    public void setBlockRestrictions(Map<String, Restriction> blockRestrictions) {
        this.blockRestrictions = blockRestrictions;
    }

    public void setEntityRestrictions(Map<String, Restriction> entityRestrictions) {
        this.entityRestrictions = entityRestrictions;
    }

    public FineGrainedModUnlock withItemRestriction(String item, boolean restricted) {
        this.itemRestrictions.put(item, Restriction.defaultItemRestrictions(restricted));
        return this;
    }

    public FineGrainedModUnlock withItemRestriction(String item, Restriction restriction) {
        this.itemRestrictions.put(item, restriction);
        return this;
    }

    public FineGrainedModUnlock withBlockItemRestriction(String item, boolean restricted) {
        this.itemRestrictions.put(item, Restriction.defaultItemRestrictions(restricted));
        this.blockRestrictions.put(item, Restriction.defaultBlockRestrictions(restricted));
        return this;
    }

    public FineGrainedModUnlock withBlockRestriction(String block, boolean restricted) {
        this.blockRestrictions.put(block, Restriction.defaultBlockRestrictions(restricted));
        return this;
    }

    public FineGrainedModUnlock withEntityRestriction(String entity, boolean restricted) {
        this.entityRestrictions.put(entity, Restriction.defaultEntityRestrictions(restricted));
        return this;
    }

    public FineGrainedModUnlock withItemRestrictionsMap(Map<String, Restriction> restrictionMap) {
        this.itemRestrictions.putAll(restrictionMap);
        return this;
    }

    public FineGrainedModUnlock withBlockRestrictionsMap(Map<String, Restriction> restrictionMap) {
        this.blockRestrictions.putAll(restrictionMap);
        return this;
    }

    public FineGrainedModUnlock withEntityRestrictionsMap(Map<String, Restriction> restrictionMap) {
        this.entityRestrictions.putAll(restrictionMap);
        return this;
    }

    public FineGrainedModUnlock withBlockRestriction(String block, Restriction restriction) {
        this.blockRestrictions.put(block, restriction);
        return this;
    }

    public FineGrainedModUnlock withEntityRestriction(String entity, Restriction restriction) {
        this.entityRestrictions.put(entity, restriction);
        return this;
    }

    @Override
    public <P> boolean restricts(P item, Restriction.Type restrictionType, boolean a) {
        ResourceLocation registryName = null;
        if (item instanceof Item) {
            registryName = ForgeRegistries.ITEMS.getKey((Item) item);
        }
        if (item instanceof Block) {
            registryName = ForgeRegistries.BLOCKS.getKey((Block) item);
        }
        if (item instanceof EntityType<?>) {
            registryName = ForgeRegistries.ENTITY_TYPES.getKey((EntityType<?>) item);
        }
        if (registryName == null) {
            return false;
        }
        String sid = registryName.toString();
        Restriction restriction = null;
        if (item instanceof Item) {
            restriction = this.itemRestrictions.get(sid);
        }
        if (item instanceof Block) {
            restriction = this.blockRestrictions.get(sid);
        }
        if (item instanceof EntityType<?>) {
            restriction = this.entityRestrictions.get(sid);
        }
        if (restriction == null) {
            return false;
        }
        return restriction.doesRestrict(restrictionType);
    }
}