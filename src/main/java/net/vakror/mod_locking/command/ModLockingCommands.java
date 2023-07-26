package net.vakror.mod_locking.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.vakror.mod_locking.mod.capability.ModTreeCapability;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.packet.ModPackets;
import net.vakror.mod_locking.packet.SyncAllDataS2CPacket;
import net.vakror.mod_locking.packet.SyncPlayerTreesS2CPacket;

public class ModLockingCommands {
    public static class ReloadModLocksCommand {
        public ReloadModLocksCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal("mod-locking")
                    .then(Commands.literal("reload-config")
                            .requires(commandSourceStack -> commandSourceStack.hasPermission(0))
                            .executes(this::execute)));
        }

        private int execute(CommandContext<CommandSourceStack> context) {
            try {
                ModConfigs.register(false);
                ModPackets.sendToClients(new SyncAllDataS2CPacket(ModConfigs.POINTS.points, ModConfigs.MOD_UNLOCKS.getAll(), ModConfigs.TREES.trees, ModConfigs.USE_ITEM_POINT_OBTAIN_METHODS.getAll(), true));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        }
    }

    public static class ReloadPlayerTreeCommand {
        public ReloadPlayerTreeCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal("mod-locking")
                    .then(Commands.literal("reload-trees")
                            .requires(commandSourceStack -> commandSourceStack.hasPermission(0))
                            .executes(this::execute)));
        }

        private int execute(CommandContext<CommandSourceStack> context) {
            try {
                ServerPlayer player = context.getSource().getPlayerOrException();
                if (player != null) {
                    player.getCapability(ModTreeProvider.MOD_TREE).orElse(new ModTreeCapability()).updateTrees();
                    ModPackets.sendToClient(new SyncPlayerTreesS2CPacket(player.getCapability(ModTreeProvider.MOD_TREE).orElse(new ModTreeCapability()).getTrees(), false, ""), player);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        }
    }
}