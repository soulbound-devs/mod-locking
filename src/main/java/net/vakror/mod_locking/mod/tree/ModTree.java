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

    @Expose
    public int startScrollX = 0;

    @Expose
    public int startScrollY = 0;

    @Expose
    public int marginX = 30;

    @Expose
    public int marginY = 30;

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
        nbt.putInt("startScrollX", startScrollX);
        nbt.putInt("startScrollY", startScrollY);
        nbt.putInt("marginX", marginX);
        nbt.putInt("marginY", marginY);
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
        startScrollX = nbt.getInt("startScrollX");
        startScrollY = nbt.getInt("startScrollY");
        marginX = nbt.getInt("marginX");
        marginY = nbt.getInt("marginY");
        backgroundTexture = nbt.getString("background");
        icon = nbt.getString("icon");
    }
    public <P> String restrictedBy(P item, Restriction.Type restrictionType) {
        StringBuilder sb = new StringBuilder();
        for (Unlock unlock : ModConfigs.UNLOCKS.getAll()) {
            if (modsUnlocked == null) modsUnlocked = new ArrayList<>();
            if (unlock.getTree().equals(name)) {
                if (this.modsUnlocked.contains(unlock.getName()) || !unlock.restricts(item, restrictionType, true)) continue;
                if (sb.isEmpty()) {
                    sb.append(unlock.getName());
                } else {
                    sb.append(", ").append(unlock.getName());
                }
            }
        }
        return sb.toString().isBlank() ? null: sb.toString();
    }

    public ModTree withBackgroundTexture(String backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
        return this;
    }

    public ModTree withStartScrollX(int startScrollX) {
        this.startScrollX = startScrollX;
        return this;
    }

    public ModTree withStartScrollY(int startScrollY) {
        this.startScrollY = startScrollY;
        return this;
    }

    public ModTree withMarginX(int marginX) {
        this.marginX = marginX;
        return this;
    }

    public ModTree withMarginY(int marginY) {
        this.marginY = marginY;
        return this;
    }
}
