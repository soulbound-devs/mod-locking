package net.vakror.mod_locking.mod.tree;

import com.google.gson.annotations.Expose;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.INBTSerializable;
import net.vakror.mod_locking.locking.Restriction;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.unlock.ModUnlock;
import net.vakror.mod_locking.mod.unlock.Unlock;

import java.util.ArrayList;
import java.util.List;

public class ModTree implements INBTSerializable<CompoundTag> {

    public List<String> modsUnlocked;

    @Expose
    public String name;

    @Expose
    public String backgroundTexture;

    @Expose
    public String icon;

    @Expose
    public CompoundTag iconNbt;

    public ModTree(String name, ResourceLocation icon) {
        this.name = name;
        this.icon = icon.toString();
    }

    public ModTree withIconNbt(CompoundTag iconNbt) {
        this.iconNbt = iconNbt;
        return this;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag unlocks = new ListTag();
        if (modsUnlocked == null) {
            modsUnlocked = new ArrayList<>();
        }
        this.modsUnlocked.forEach(mod -> {
            CompoundTag modUnlock = new CompoundTag();
            modUnlock.putString("name", mod);
            unlocks.add(modUnlock);
        });
        nbt.put("unlocks", unlocks);
        nbt.putString("treeName", name);
        nbt.putString("background", (backgroundTexture == null ? "": backgroundTexture));
        nbt.putString("icon", icon);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag researches = nbt.getList("unlocks", 10);
        if (modsUnlocked == null) {
            modsUnlocked = new ArrayList<>();
        }
        this.modsUnlocked.clear();
        for (int i = 0; i < researches.size(); ++i) {
            CompoundTag researchNBT = researches.getCompound(i);
            String name = researchNBT.getString("name");
            this.modsUnlocked.add(name);
        }
        name = nbt.getString("treeName");
        backgroundTexture = nbt.getString("background");
        icon = nbt.getString("icon");
    }

    public String restrictedBy(Item item, Restriction.Type restrictionType) {
        for (Unlock unlock : ModConfigs.UNLOCKS.getAll()) {
            if (modsUnlocked == null) modsUnlocked = new ArrayList<>();
            if (unlock.getTree().equals(name)) {
                if (this.modsUnlocked.contains(unlock.getName()) || !unlock.restricts(item, restrictionType)) continue;
                return unlock.getName();
            }
        }
        return null;
    }

    public String restrictedBy(Block block, Restriction.Type restrictionType) {
        for (Unlock unlock : ModConfigs.UNLOCKS.getAll()) {
            if (modsUnlocked == null) modsUnlocked = new ArrayList<>();
            if (unlock.getTree().equals(name)) {
                if (this.modsUnlocked.contains(unlock.getName()) || !unlock.restricts(block, restrictionType)) continue;
                return unlock.getName();
            }
        }
        return null;
    }

    public String restrictedBy(EntityType<?> entityType, Restriction.Type restrictionType) {
        for (Unlock unlock : ModConfigs.UNLOCKS.getAll()) {
            if (modsUnlocked == null) modsUnlocked = new ArrayList<>();
            if (unlock.getTree().equals(name)) {
                if (this.modsUnlocked.contains(unlock.getName()) || !unlock.restricts(entityType, restrictionType))
                    continue;
                return unlock.getName();
            }
        }
        return null;
    }

    public ModTree withBackgroundTexture(String backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
        return this;
    }
}
