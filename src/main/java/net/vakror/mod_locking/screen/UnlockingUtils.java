package net.vakror.mod_locking.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class UnlockingUtils {
    public static void openUnlockingGui() {
        Minecraft.getInstance().setScreen(new ModUnlockingScreen(Component.literal("Mod Unlocking")));
    }
}
