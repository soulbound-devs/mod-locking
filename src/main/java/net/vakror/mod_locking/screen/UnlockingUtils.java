package net.vakror.mod_locking.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkHooks;
import net.vakror.mod_locking.mod.capability.ModTreeCapability;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.config.ModConfigs;
import org.jetbrains.annotations.NotNull;

public class UnlockingUtils {
    public static void openUnlockingGui(ServerPlayer player) {
        NetworkHooks.openScreen(player, new MenuProvider() {
            @Override
            public @NotNull Component getDisplayName() {
                return Component.translatable("gui.unlocks");
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int syncId, @NotNull Inventory inv, @NotNull Player player) {
                return new ModUnlockingMenu(syncId, inv, ModConfigs.UNLOCKS.getAll(), ModConfigs.TREES.trees, player.getCapability(ModTreeProvider.MOD_TREE).orElse(new ModTreeCapability()).getPoints());
            }
        });
    }
}
