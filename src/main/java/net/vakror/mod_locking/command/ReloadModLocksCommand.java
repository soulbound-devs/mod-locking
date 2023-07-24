package net.vakror.mod_locking.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.packet.ModPackets;
import net.vakror.mod_locking.packet.SyncAllDataS2CPacket;

import java.util.HashMap;
import java.util.Map;

public class ReloadModLocksCommand {
    public ReloadModLocksCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("reload").then(Commands.literal("unlocks")
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
            ModPackets.sendToClients(new SyncAllDataS2CPacket(ModConfigs.POINTS.points, ModConfigs.UNLOCKS.getAll(), ModConfigs.TREES.trees, ModConfigs.POINT_OBTAIN_METHODS.getAll(), true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}