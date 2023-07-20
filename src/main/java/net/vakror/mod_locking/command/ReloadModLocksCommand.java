package net.vakror.mod_locking.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.config.ModPointsConfig;
import net.vakror.mod_locking.packet.ModPackets;
import net.vakror.mod_locking.packet.SyncAllDataS2CPacket;
import net.vakror.mod_locking.packet.SyncModTreesS2CPacket;

import java.util.HashMap;
import java.util.Map;

public class ReloadModLocksCommand {
    public ReloadModLocksCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("mod-locks").then(Commands.literal("reload")
                .executes(this::execute)));
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        try {
            ModConfigs.register(false);
            Map<String, Integer> points = new HashMap<>();
            ModConfigs.POINTS.points.forEach((point -> {
                points.put(point.name, 0);
            }));
            Map<String, Integer> pointColors = new HashMap<>();
            ModConfigs.POINTS.points.forEach((point -> {
                pointColors.put(point.name, point.getColor());
            }));
            Map<String, String> pointPluralNames = new HashMap<>();
            ModConfigs.POINTS.points.forEach((point -> {
                pointPluralNames.put(point.name, point.pluralName);
            }));
            ModPackets.sendToClients(new SyncAllDataS2CPacket(points, pointPluralNames, pointColors, ModConfigs.UNLOCKS.getAll(), ModConfigs.TREES.trees));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}