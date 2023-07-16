package net.vakror.mod_locking.mod.unlock;

import com.google.gson.annotations.Expose;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.vakror.mod_locking.locking.Restriction;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModUnlock extends Unlock<ModUnlock>{
    @Expose
    protected Set<String> modIds = new HashSet<String>();
    @Expose
    protected Restriction restriction = Restriction.defaultModRestrictions(true);

    public ModUnlock(String name, Map<String, Integer> costMap, String requiredUnlocks, float x, float y,String ... modIds) {
        super(name, costMap, requiredUnlocks, x, y);
        Collections.addAll(this.modIds, modIds);
    }

    public ModUnlock(String name, Map<String, Integer> costMap, float x, float y, String ... modIds) {
        super(name, costMap, x, y);
        Collections.addAll(this.modIds, modIds);
    }

    public Set<String> getModIds() {
        return this.modIds;
    }

    public Restriction getRestriction() {
        return this.restriction;
    }

    public ModUnlock withRestrictions(boolean canHitBlock, boolean canInteractWithEntity, boolean canInteractWithBlock, boolean canUseItem, boolean canCraftItem) {
        this.restriction.set(Restriction.Type.HITTABILITY, canHitBlock);
        this.restriction.set(Restriction.Type.ENTITY_INTERACTABILITY, canInteractWithEntity);
        this.restriction.set(Restriction.Type.BLOCK_INTERACTABILITY, canInteractWithBlock);
        this.restriction.set(Restriction.Type.USABILITY, canUseItem);
        this.restriction.set(Restriction.Type.CRAFTABILITY, canCraftItem);
        return this;
    }

    @Override
    public boolean restricts(Item item, Restriction.Type restrictionType) {
        if (!this.restriction.doesRestrict(restrictionType)) {
            return false;
        }
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(item);
        if (registryName == null) {
            return false;
        }
        return this.modIds.contains(registryName.getNamespace());
    }

    @Override
    public boolean restricts(Block block, Restriction.Type restrictionType) {
        if (!this.restriction.doesRestrict(restrictionType)) {
            return false;
        }
        ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(block);
        if (registryName == null) {
            return false;
        }
        return this.modIds.contains(registryName.getNamespace());
    }

    @Override
    public boolean restricts(EntityType<?> entityType, Restriction.Type restrictionType) {
        if (!this.restriction.doesRestrict(restrictionType)) {
            return false;
        }
        ResourceLocation registryName = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        if (registryName == null) {
            return false;
        }
        return this.modIds.contains(registryName.getNamespace());
    }
}

