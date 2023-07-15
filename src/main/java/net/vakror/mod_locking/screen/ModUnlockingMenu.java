package net.vakror.mod_locking.screen;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.unlock.Unlock;
import net.vakror.mod_locking.mod.util.NbtUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ModUnlockingMenu extends AbstractContainerMenu {
    protected List<ModTree> trees;
    protected Map<String, Integer> playerPoints;
    protected Map<String, Integer> pointColors;
    protected List<Unlock> unlocks;
    protected Inventory playerInv;
    protected ModUnlockingMenu(int syncId, Inventory inv, FriendlyByteBuf data) {
        super(ModMenuTypes.UNLOCK_TREE.get(), syncId);
        CompoundTag nbt = data.readNbt();
        assert nbt != null;
        this.unlocks = NbtUtil.deserializeUnlocks(nbt);
        this.trees = NbtUtil.deserializeTrees(nbt);
        this.playerPoints = NbtUtil.deserializePoints(nbt);
        this.pointColors = NbtUtil.deserializePointColors(nbt.getCompound("allPoints"));
        this.playerInv = inv;
    }
    protected ModUnlockingMenu(int syncId, Inventory inv, List<Unlock> unlocks, List<ModTree> trees, Map<String, Integer> playerPoints) {
        super(ModMenuTypes.UNLOCK_TREE.get(), syncId);
        this.unlocks = unlocks;
        this.trees = trees;
        this.playerPoints = playerPoints;
        this.playerInv = inv;
    }

    public Map<String, Integer> getPointColors() {
        return pointColors;
    }

    public Map<String, Integer> getPlayerPoints() {
        return playerPoints;
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