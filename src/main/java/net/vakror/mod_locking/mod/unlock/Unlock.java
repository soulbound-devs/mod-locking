package net.vakror.mod_locking.mod.unlock;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.vakror.mod_locking.Tooltip;
import net.vakror.mod_locking.locking.Restriction;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.util.NbtUtil;
import net.vakror.mod_locking.screen.ModUnlockingScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Unlock<T extends Unlock> {
    protected String name;
    protected Map<String, Integer> cost;

    protected String[] requiredUnlocks;

    protected float x;
    protected float y;
    protected String description = "sample description";
    protected String icon;
    protected String tree;

    protected CompoundTag iconNbt;

    public Unlock(String name, Map<String, Integer> costMap, String[] requiredUnlock, float x, float y) {
        this.name = name;
        this.cost = costMap;
        this.requiredUnlocks = requiredUnlock;
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
        this.tree = tree.name;
        return (T) this;
    }


    public T withTree(String tree) {
        this.tree = tree;
        return (T) this;
    }

    public T withIconNbt(CompoundTag nbt) {
        iconNbt = nbt;
        return (T) this;
    }

    public String getTree() {
        return tree;
    }

    public List<Unlock<?>> getParents() {
        if (this.requiredUnlocks == null || this.requiredUnlocks.length == 0 || this.requiredUnlocks[0].equals("")) {
            return null;
        }

        List<Unlock<?>> unlocks = new ArrayList<>();

        for (Unlock<?> unlock: ModConfigs.UNLOCKS.getAll()) {
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

    public String[] getRequiredUnlocks() {
        return requiredUnlocks;
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



        list.add(new Tooltip.TooltipComponentBuilder().addPart("Cost: ").setStyle(Style.EMPTY.withBold(true)).build().getTooltip());

        getCost().forEach((name, amount) -> {
            list.add(Component.literal(amount + " " + (amount > 1 ? NbtUtil.getPoint(name).pluralName: name)).setStyle(Style.EMPTY.withColor(screen.getMenu().getPointColors().get(name))));
        });

        AtomicBoolean canAfford = new AtomicBoolean(true);

        getCost().forEach((name, amount) -> {
            if (screen.getMenu().getPlayerPoints().containsKey(name) && screen.getMenu().getPlayerPoints().get(name) >= amount) {
                if (canAfford.get()) {
                    canAfford.set(true);
                }
            } else {
                canAfford.set(false);
            }
        });

        if (!canAfford.get()) {
            list.add(Component.literal(""));
            list.add(new Tooltip.TooltipComponentBuilder().addPart("You do not have enough points to afford this!", Tooltip.TooltipComponentBuilder.ColorCode.RED).build().getTooltip());
        }

        list.add(Component.empty());
        if (requiredUnlocks != null && !requiredUnlocks[0].equals("")) {
            for (String required : requiredUnlocks) {
                list.add(Component.literal("Requires: §e" + required));
            }
        }
        list.add(Component.empty());
        list.add(Component.literal(description));
        return list;
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
}

