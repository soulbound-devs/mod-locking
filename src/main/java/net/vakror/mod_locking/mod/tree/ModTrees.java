package net.vakror.mod_locking.mod.tree;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.vakror.mod_locking.mod.capability.ModTreeCapability;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.packet.ModPackets;
import net.vakror.mod_locking.packet.RequestTreeUpdateC2SPacket;
import net.vakror.mod_locking.packet.SyncModTreesS2CPacket;

import java.util.List;

public class ModTrees {
    public static List<ModTree> getUnlockTrees(Player player) {
        if (!player.level().isClientSide) {
            ModPackets.sendToClient(new SyncModTreesS2CPacket(player.getCapability(ModTreeProvider.MOD_TREE).orElse(new ModTreeCapability()).getTrees()), (ServerPlayer) player);
        } else {
            ModPackets.sendToServer(new RequestTreeUpdateC2SPacket());
        }
        return player.getCapability(ModTreeProvider.MOD_TREE).orElse(new ModTreeCapability()).getTrees();
    }
}
