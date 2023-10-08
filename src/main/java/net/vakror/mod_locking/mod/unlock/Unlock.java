package net.vakror.mod_locking.mod.unlock;

import net.minecraft.advancements.FrameType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.vakror.mod_locking.Tooltip;
import net.vakror.mod_locking.locking.Restriction;
import net.vakror.mod_locking.mod.config.configs.ModPointsConfig;
import net.vakror.mod_locking.mod.config.configs.ModUnlocksConfig;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.screen.ModUnlockingScreen;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Unlock<T extends Unlock<T>> {
    protected String name;
    protected Map<String, Integer> cost;

    protected String[] requiredUnlocks;

    protected float x;
    protected float y;
    protected String description = "sample description";
    protected String icon;
    protected String treeName;
    protected SoundEvent unlockSound;
    protected String frameType;

    protected CompoundTag iconNbt;

    public Unlock(String name, Map<String, Integer> costMap, String[] requiredUnlocks, float x, float y) {
        this.name = name;
        this.cost = costMap;
        this.requiredUnlocks = requiredUnlocks;
        this.x = x;
        this.y = y;
    }

    public Unlock(String name, Map<String, Integer> costMap, String requiredUnlock, float x, float y) {
        this.name = name;
        this.cost = costMap;
        this.requiredUnlocks = new String[]{requiredUnlock};
        this.x = x;
        this.y = y;
    }

    public Unlock(String name, Map<String, Integer> costMap, float x, float y) {
        this.name = name;
        this.cost = costMap;
        this.requiredUnlocks = new String[]{""};
        this.x = x;
        this.y =y;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return name;
    }

    public Map<String, Integer> getCost() {
        return cost;
    }

    public T withDescription(Component component) {
        description = component.getString();
        return (T) this;
    }

    public T withIcon(String item) {
        icon = item;
        return (T) this;
    }

    public T withTree(ModTree tree) {
        this.treeName = tree.name;
        return (T) this;
    }


    public T withTree(String tree) {
        this.treeName = tree;
        return (T) this;
    }

    public T withIconNbt(CompoundTag nbt) {
        iconNbt = nbt;
        return (T) this;
    }

    public String getTreeName() {
        return treeName;
    }

    public List<Unlock<?>> getParents() {
        if (this.requiredUnlocks == null || this.requiredUnlocks.length == 0 || this.requiredUnlocks[0].equals("")) {
            return null;
        }

        List<Unlock<?>> unlocks = new ArrayList<>();

        for (Unlock<?> unlock: ModUnlocksConfig.INSTANCE.getAll()) {
            for (String requiredUnlock: this.requiredUnlocks) {
                if (unlock.getName().equals(requiredUnlock)) {
                    unlocks.add(unlock);
                }
            }
        }
        if (unlocks.size() > 0) {
            return unlocks;
        }
        return null;
    }

    public String getIcon() {
        return icon;
    }

    public CompoundTag getIconNbt() {
        return iconNbt;
    }

    public Optional<CompoundTag> getOptionalIconNbt() {
        if (iconNbt == null) {
            return Optional.empty();
        }
        return Optional.of(iconNbt);
    }

    public T withUnlockSound(SoundEvent unlockSound) {
        this.unlockSound = unlockSound;
        return (T) this;
    }

    public String[] getRequiredUnlocks() {
        return requiredUnlocks;
    }

    public List<String> getRequiredUnlocksAsList() {
        return new ArrayList<>(List.of(requiredUnlocks));
    }

    public Optional<List<String>> getOptionalRequiredUnlocksAsList() {
        if (requiredUnlocks == null) {
            return Optional.empty();
        }
        return Optional.of(new ArrayList<>(List.of(requiredUnlocks)));
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getDescription() {
        return description;
    }

    public List<Component> createDescription(ModUnlockingScreen screen) {
        List<Component> list = new ArrayList<>(3 + getCost().size());

        if (!hasUnlocked(screen)) {
            list.add(new Tooltip.TooltipComponentBuilder().addPart("Cost: ").setStyle(Style.EMPTY.withBold(true)).build().getTooltip());

            getCost().forEach((name, amount) -> {
                MutableComponent component = Component.literal(amount + " " + (amount > 1 ? ModPointsConfig.getPoint(name).pluralName : name));
                if (screen.getPointColors().get(name) != null) {
                    component.setStyle(Style.EMPTY.withColor(screen.getPointColors().get(name)));
                }
                list.add(component);
            });

            list.add(Component.empty());
            if (requiredUnlocks != null && !requiredUnlocks[0].equals("")) {
                for (String required : requiredUnlocks) {
                    list.add(Component.literal("Requires: §e" + required));
                }
            }

            if (!canUnlock(screen)) {
                list.addAll(getReasonsWhyPlayerCannotAfford(screen));
            }

            list.add(Component.empty());
        }

        list.add(Component.literal(description));
        return list;
    }

    public boolean hasUnlocked(ModUnlockingScreen screen) {
        boolean unlocked = false;
        for (ModTree tree : screen.getPlayerTrees()) {
            if (tree.modsUnlocked.contains(name)) {
                unlocked = true;
                break;
            }
        }
        return unlocked;
    }

    public boolean canUnlock(ModUnlockingScreen screen) {
        AtomicBoolean canAfford = new AtomicBoolean(true);
        getCost().forEach((name, amount) -> {
            if (screen.getPlayerPoints().containsKey(name) && screen.getPlayerPoints().get(name) >= amount) {
                if (canAfford.get()) {
                    canAfford.set(true);
                }
            } else {
                canAfford.set(false);
            }
        });

        List<String> allModsUnlocked = new ArrayList<>();
        for (ModTree tree : screen.getPlayerTrees()) {
            allModsUnlocked.addAll(tree.modsUnlocked);
        }
        for (String requiredUnlock : requiredUnlocks) {
            if (!requiredUnlock.isBlank() && !allModsUnlocked.contains(requiredUnlock)) {
                canAfford.set(false);
            }
        }

        return canAfford.get();
    }

    public boolean canUnlock(Map<String, Integer> playerPoints, List<ModTree> playerTrees) {
        AtomicBoolean canAfford = new AtomicBoolean(true);
        getCost().forEach((name, amount) -> {
            if (playerPoints.containsKey(name) && playerPoints.get(name) >= amount) {
                if (canAfford.get()) {
                    canAfford.set(true);
                }
            } else {
                canAfford.set(false);
            }
        });

        List<String> allModsUnlocked = new ArrayList<>();
        for (ModTree tree : playerTrees) {
            if (tree.modsUnlocked != null) {
                allModsUnlocked.addAll(tree.modsUnlocked);
            }
        }
        for (String requiredUnlock : requiredUnlocks) {
            if (!requiredUnlock.isBlank() && !allModsUnlocked.contains(requiredUnlock)) {
                canAfford.set(false);
            }
        }

        return canAfford.get();
    }

    public boolean canUnlock(Map<String, Integer> playerPoints, List<ModTree> playerTrees, boolean a) {
        AtomicBoolean canAfford = new AtomicBoolean(true);
        getCost().forEach((name, amount) -> {
            if (playerPoints.containsKey(name) && playerPoints.get(name) >= amount) {
                if (canAfford.get()) {
                    canAfford.set(true);
                }
            } else {
                canAfford.set(false);
            }
        });

        List<String> allModsUnlocked = new ArrayList<>();
        for (ModTree tree : playerTrees) {
            if (tree.modsUnlocked != null) {
                allModsUnlocked.addAll(tree.modsUnlocked);
            }
        }
        for (String requiredUnlock : requiredUnlocks) {
            if (!requiredUnlock.isBlank() && !allModsUnlocked.contains(requiredUnlock)) {
                canAfford.set(false);
            }
        }

        if (canAfford.get() && allModsUnlocked.contains(this.name)) {
            canAfford.set(false);
        }

        return canAfford.get();
    }

    public T withFrameType(String frameType) {
        this.frameType = frameType;
        return (T) this;
    }

    public T withFrameType(FrameType frameType) {
        this.frameType = frameType.getName();
        return (T) this;
    }

    public List<Component> getReasonsWhyPlayerCannotAfford(ModUnlockingScreen screen) {
        List<Component> reasonsWhyPlayerCannotAfford = new ArrayList<>(1 + requiredUnlocks.length);
        AtomicBoolean canAfford = new AtomicBoolean(true);
        Arrays.stream(this.requiredUnlocks).toList();
        getCost().forEach((name, amount) -> {
            if (screen.getPlayerPoints().containsKey(name) && screen.getPlayerPoints().get(name) >= amount) {
                if (canAfford.get()) {
                    canAfford.set(true);
                }
            } else {
                canAfford.set(false);
            }
        });

        if (!canAfford.get()) {
            reasonsWhyPlayerCannotAfford.add(Component.literal(""));
            reasonsWhyPlayerCannotAfford.add(new Tooltip.TooltipComponentBuilder().addPart("You can't Afford This", Tooltip.TooltipComponentBuilder.ColorCode.RED).build().getTooltip());
        }

        List<String> allModsUnlocked = new ArrayList<>();
        for (ModTree tree : screen.getPlayerTrees()) {
            allModsUnlocked.addAll(tree.modsUnlocked);
        }
        for (String requiredUnlock : requiredUnlocks) {
            if (!requiredUnlock.isBlank() && !allModsUnlocked.contains(requiredUnlock)) {
                canAfford.set(false);
            }
        }
        return reasonsWhyPlayerCannotAfford;
    }

    public boolean restricts(Item item, Restriction.Type restrictionType) {
        return false;
    }

    public <P> boolean restricts(P item, Restriction.Type restrictionType, boolean a) {
        return false;
    }

    public boolean restricts(Block item, Restriction.Type restrictionType) {
        return false;
    }

    public boolean restricts(EntityType<?> item, Restriction.Type restrictionType) {
        return false;
    }

    public Unlock setCost(Map<String, Integer> cost) {
        this.cost = cost;
        return this;
    }

    public Optional<SoundEvent> getSound() {
        if (unlockSound == null) {
            return Optional.empty();
        }
        return Optional.of(unlockSound);
    }

    public Optional<String> getFrameTypeName() {
        if (frameType == null || frameType.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(frameType);
    }

    public FrameType getFrameType() {
        if (frameType == null) {
            return FrameType.CHALLENGE;
        }
        return FrameType.byName(frameType);
    }
}

