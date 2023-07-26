package net.vakror.mod_locking.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.vakror.mod_locking.mod.capability.ModTreeCapability;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.unlock.Unlock;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModUnlockingMenu extends AbstractContainerMenu {
    protected List<ModTree> trees;
    protected Map<String, Integer> playerPoints;
    protected List<ModTree> playerTrees;
    protected Map<String, Integer> pointColors;
    protected List<Unlock<?>> unlocks;
    protected Inventory playerInv;
    protected ModUnlockingMenu(int syncId, Inventory inv, FriendlyByteBuf data) {
        super(ModMenuTypes.UNLOCK_TREE.get(), syncId);
        this.playerInv = inv;
        this.trees = ModConfigs.TREES.trees;
        this.playerPoints = inv.player.getCapability(ModTreeProvider.MOD_TREE).orElse(new ModTreeCapability()).getPoints();
        this.playerTrees = inv.player.getCapability(ModTreeProvider.MOD_TREE).orElse(new ModTreeCapability()).getTrees();
        this.pointColors = inv.player.getCapability(ModTreeProvider.MOD_TREE).orElse(new ModTreeCapability()).getPointColors();
        this.unlocks = ModConfigs.MOD_UNLOCKS.getAll();
    }
    protected ModUnlockingMenu(int syncId, Inventory inv, List<Unlock<?>> unlocks, List<ModTree> trees, Map<String, Integer> playerPoints, List<ModTree> playerTrees) {
        super(ModMenuTypes.UNLOCK_TREE.get(), syncId);
        this.unlocks = unlocks;
        this.trees = trees;
        this.playerPoints = playerPoints;
        this.playerTrees = playerTrees;
        this.playerInv = inv;
    }

    public Map<String, Integer> getPointColors() {
        Map<String, Integer> pointColors = new HashMap<>();
        ModConfigs.POINTS.points.forEach((point -> {
            pointColors.put(point.name, point.getColor());
        }));
        return pointColors;
    }

    public Map<String, Integer> getPlayerPoints() {
        return playerPoints;
    }

    public List<ModTree> getPlayerTrees() {
        return playerTrees;
    }

    public ModUnlockingMenu setPlayerTrees(List<ModTree> playerTrees) {
        this.playerTrees = playerTrees;
        return this;
    }

    public void setPlayerPoints(Map<String, Integer> playerPoints) {
        this.playerPoints = playerPoints;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player p_38941_, int p_38942_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }
}