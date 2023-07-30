package net.vakror.mod_locking.screen;

import net.minecraft.client.Minecraft;
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
import net.vakror.mod_locking.packet.ModPackets;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

public class UnlockingUtils {
    public static void openUnlockingGui() {
        Minecraft.getInstance().setScreen(new ModUnlockingScreen(Component.literal("Mod Unlocking")));
    }
}
