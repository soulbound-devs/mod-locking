package net.vakror.mod_locking.mod.tree;

import net.minecraft.world.entity.player.Player;
import net.vakror.mod_locking.mod.capability.ModTreeCapability;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;

import java.util.List;

public class ModTrees {
    public static List<ModTree> getUnlockTrees(Player player) {
        return player.getCapability(ModTreeProvider.MOD_TREE).orElse(new ModTreeCapability()).getTrees();
    }
}
