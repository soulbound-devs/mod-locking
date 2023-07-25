package net.vakror.mod_locking.mod.tree;

import com.google.gson.annotations.Expose;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;
import net.vakror.mod_locking.locking.Restriction;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.unlock.Unlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class ModTree implements INBTSerializable<CompoundTag> {

    public static final Codec<ModTree> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(ModTree::getName),
            Codec.STRING.fieldOf("backgroundTexture").forGetter(ModTree::getBackgroundTexture),
            Codec.STRING.fieldOf("icon").forGetter(ModTree::getIcon),
            CompoundTag.CODEC.optionalFieldOf("iconNbt").forGetter(ModTree::getIconNbtOptional),
            Codec.INT.optionalFieldOf("centeredX").forGetter(ModTree::getStartScrollXOptional),
            Codec.INT.optionalFieldOf("centeredY").forGetter(ModTree::getStartScrollYOptional),
            Codec.INT.optionalFieldOf("marginX").forGetter(ModTree::getMarginXOptional),
            Codec.INT.optionalFieldOf("marginY").forGetter(ModTree::getMarginYOptional)
    ).apply(instance, ModTree::new));

    public static final Codec<ModTree> CODEC_WITH_MODS_UNLOCKED = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(ModTree::getName),
            Codec.STRING.fieldOf("backgroundTexture").forGetter(ModTree::getBackgroundTexture),
            Codec.STRING.listOf().fieldOf("modsUnlocked").forGetter(ModTree::getModsUnlocked),
            Codec.STRING.fieldOf("icon").forGetter(ModTree::getIcon),
            CompoundTag.CODEC.optionalFieldOf("iconNbt").forGetter(ModTree::getIconNbtOptional),
            Codec.INT.optionalFieldOf("centeredX").forGetter(ModTree::getStartScrollXOptional),
            Codec.INT.optionalFieldOf("centeredY").forGetter(ModTree::getStartScrollYOptional),
            Codec.INT.optionalFieldOf("marginX").forGetter(ModTree::getMarginXOptional),
            Codec.INT.optionalFieldOf("marginY").forGetter(ModTree::getMarginYOptional)
    ).apply(instance, ModTree::new));

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
    public int marginX = 0;

    @Expose
    public int marginY = 30;

    public ModTree(String name, ResourceLocation icon) {
        this.name = name;
        this.icon = icon.toString();
    }

    public ModTree(String name, String backgroundTexture, String icon, Optional<CompoundTag> iconNbt, Optional<Integer> centeredX, Optional<Integer> centeredY, Optional<Integer> marginX, Optional<Integer> marginY) {
        this.name = name;
        this.backgroundTexture = backgroundTexture;
        this.icon = icon;
        iconNbt.ifPresent(this::withIconNbt);
        centeredX.ifPresent(this::withStartScrollX);
        centeredY.ifPresent(this::withStartScrollY);
        marginX.ifPresent(this::withMarginX);
        marginY.ifPresent(this::withMarginY);
    }

    public ModTree(String name, String backgroundTexture, List<String> modsUnlocked, String icon, Optional<CompoundTag> iconNbt, Optional<Integer> centeredX, Optional<Integer> centeredY, Optional<Integer> marginX, Optional<Integer> marginY) {
        this.name = name;
        this.backgroundTexture = backgroundTexture;
        this.icon = icon;
        this.modsUnlocked = new ArrayList<>(modsUnlocked);
        iconNbt.ifPresent(this::withIconNbt);
        centeredX.ifPresent(this::withStartScrollX);
        centeredY.ifPresent(this::withStartScrollY);
        marginX.ifPresent(this::withMarginX);
        marginY.ifPresent(this::withMarginY);
    }

    public ModTree withIconNbt(CompoundTag iconNbt) {
        this.iconNbt = iconNbt;
        return this;
    }

    public void withIconNbt(Optional<CompoundTag> iconNbt) {
        iconNbt.ifPresent(compoundTag -> this.iconNbt = compoundTag);
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
        for (Unlock<?> unlock : ModConfigs.UNLOCKS.getAll()) {
            if (modsUnlocked == null) modsUnlocked = new ArrayList<>();
            if (unlock.getTreeName().equals(name)) {
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

    public void withStartScrollX(Optional<Integer> startScrollX) {
        startScrollX.ifPresent(integer -> this.startScrollX = integer);
    }

    public void withStartScrollY(Optional<Integer> startScrollY) {
        startScrollY.ifPresent(integer -> this.startScrollY = integer);
    }

    public void withMarginX(Optional<Integer> marginX) {
        marginX.ifPresent(integer -> this.marginX = integer);
    }

    public void withMarginY(Optional<Integer> marginY) {
        marginY.ifPresent(integer -> this.marginY = integer);
    }

    public String getName() {
        return name;
    }

    public String getBackgroundTexture() {
        return backgroundTexture;
    }

    public String getIcon() {
        return icon;
    }

    public CompoundTag getIconNbt() {
        return iconNbt;
    }

    public Optional<CompoundTag> getIconNbtOptional() {
        if (iconNbt == null) {
            return Optional.empty();
        }
        return Optional.of(iconNbt);
    }

    public int getStartScrollX() {
        return startScrollX;
    }

    public int getStartScrollY() {
        return startScrollY;
    }

    public int getMarginX() {
        return marginX;
    }

    public int getMarginY() {
        return marginY;
    }

    public Optional<Integer> getStartScrollXOptional() {
        if (startScrollX == 0) {
            return Optional.empty();
        }
        return Optional.of(startScrollX);
    }

    public Optional<Integer> getStartScrollYOptional() {
        if (startScrollY == 0) {
            return Optional.empty();
        }
        return Optional.of(startScrollY);
    }

    public Optional<Integer> getMarginXOptional() {
        if (marginX == 0) {
            return Optional.empty();
        }
        return Optional.of(marginX);
    }

    public Optional<Integer> getMarginYOptional() {
        if (marginY == 30) {
            return Optional.empty();
        }
        return Optional.of(marginY);
    }

    public List<String> getModsUnlocked() {
        if (modsUnlocked == null) {
            modsUnlocked = new ArrayList<>();
        }
        return modsUnlocked;
    }
}
