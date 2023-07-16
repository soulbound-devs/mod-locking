package net.vakror.mod_locking.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.vakror.mod_locking.mod.config.ModConfigs;

public class ReloadModLocksCommand {
    public ReloadModLocksCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("mod-locks").then(Commands.literal("reload")
                .executes(this::execute)));
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        try {
            ModConfigs.register();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}