package net.vakror.mod_locking.mod.unlock;

import com.google.gson.annotations.Expose;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.vakror.mod_locking.locking.Restriction;

public class FineGrainedModUnlock extends Unlock<FineGrainedModUnlock> {
    @Expose
    protected Map<String, Restriction> itemRestrictions = new HashMap<String, Restriction>();
    @Expose
    protected Map<String, Restriction> blockRestrictions = new HashMap<String, Restriction>();
    @Expose
    protected Map<String, Restriction> entityRestrictions = new HashMap<String, Restriction>();

    public FineGrainedModUnlock(String name, Map<String, Integer> cost, float x, float y, String... requiredUnlocks) {
        super(name, cost, requiredUnlocks, x, y);
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