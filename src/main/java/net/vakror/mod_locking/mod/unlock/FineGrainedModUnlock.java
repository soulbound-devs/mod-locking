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

public class FineGrainedModUnlock extends Unlock {
    @Expose
    protected Map<String, Restriction> itemRestriction = new HashMap<String, Restriction>();
    @Expose
    protected Map<String, Restriction> blockRestriction = new HashMap<String, Restriction>();
    @Expose
    protected Map<String, Restriction> entityRestriction = new HashMap<String, Restriction>();

    public FineGrainedModUnlock(String name, Map<String, Integer> cost, float x, float y, String requiredUnlock) {
        super(name, cost, requiredUnlock, x, y);
    }

    public Map<String, Restriction> getItemRestriction() {
        return this.itemRestriction;
    }

    public Map<String, Restriction> getBlockRestriction() {
        return this.blockRestriction;
    }

    public Map<String, Restriction> getEntityRestriction() {
        return this.entityRestriction;
    }

    public void setItemRestriction(Map<String, Restriction> itemRestriction) {
        this.itemRestriction = itemRestriction;
    }

    public void setBlockRestriction(Map<String, Restriction> blockRestriction) {
        this.blockRestriction = blockRestriction;
    }

    public void setEntityRestriction(Map<String, Restriction> entityRestriction) {
        this.entityRestriction = entityRestriction;
    }

    public FineGrainedModUnlock withItemRestriction(String item, boolean restricted) {
        this.itemRestriction.put(item, Restriction.defaultItemRestrictions(restricted));
        return this;
    }

    public FineGrainedModUnlock withBlockRestriction(String block, boolean restricted) {
        this.blockRestriction.put(block, Restriction.defaultItemRestrictions(restricted));
        return this;
    }

    public FineGrainedModUnlock withEntityRestriction(String entity, boolean restricted) {
        this.entityRestriction.put(entity, Restriction.defaultItemRestrictions(restricted));
        return this;
    }

    @Override
    public boolean restricts(Item item, Restriction.Type restrictionType) {
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(item);
        if (registryName == null) {
            return false;
        }
        String sid = registryName.toString();
        Restriction restriction = this.itemRestriction.get(sid);
        if (restriction == null) {
            return false;
        }
        return restriction.doesRestrict(restrictionType);
    }

    @Override
    public boolean restricts(Block block, Restriction.Type restrictionType) {
        ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(block);
        if (registryName == null) {
            return false;
        }
        String sid = registryName.toString();
        Restriction restriction = this.blockRestriction.get(sid);
        if (restriction == null) {
            return false;
        }
        return restriction.doesRestrict(restrictionType);
    }

    @Override
    public boolean restricts(EntityType<?> entityType, Restriction.Type restrictionType) {
        ResourceLocation registryName = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        if (registryName == null) {
            return false;
        }
        String sid = registryName.toString();
        Restriction restriction = this.entityRestriction.get(sid);
        if (restriction == null) {
            return false;
        }
        return restriction.doesRestrict(restrictionType);
    }
}