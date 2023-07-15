package net.vakror.mod_locking.screen;

import net.minecraft.nbt.CompoundTag;
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
import net.vakror.mod_locking.mod.config.ModUnlocksConfig;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.util.NbtUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class UnlockingUtils {
    public static void openUnlockingGui(ServerPlayer player) {
        NetworkHooks.openScreen(player, new MenuProvider() {
            @Override
            public @NotNull Component getDisplayName() {
                return Component.translatable("gui.unlocks");
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int syncId, @NotNull Inventory inv, @NotNull Player player) {
                return new ModUnlockingMenu(syncId, inv, ModConfigs.UNLOCKS.unlocks, ModConfigs.TREES.trees, player.getCapability(ModTreeProvider.MOD_TREE).orElse(new ModTreeCapability()).getPoints());
            }
        }, (buf -> {
            CompoundTag tag = new CompoundTag();
            CompoundTag allPointsTag = new CompoundTag();
            NbtUtil.serializeUnlocks(tag, ModConfigs.UNLOCKS.unlocks);
            NbtUtil.serializeTrees(tag, ModConfigs.TREES.trees);
            NbtUtil.serializePoints(tag, player.getCapability(ModTreeProvider.MOD_TREE).orElse(new ModTreeCapability()).getPoints());
            Map<String, Integer> allPoints = new HashMap<>(ModConfigs.POINTS.points.size());
            ModConfigs.POINTS.points.forEach((point -> {
                allPoints.put(point.name, 1);
            }));
            NbtUtil.serializePoints(allPointsTag, allPoints);
            tag.put("allPoints", allPointsTag);
            buf.writeNbt(tag);
        }));
    }
}
