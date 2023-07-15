package net.vakror.mod_locking;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String LOCK_SCREEN = "key.mod-locking.locking_screen";
    public static final String MOD_LOCKING_KEY_CATEGORY = "key.category.mod-locking";

    public static final KeyMapping LOCK_SREEN_KEY = new KeyMapping(LOCK_SCREEN, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, MOD_LOCKING_KEY_CATEGORY);
}