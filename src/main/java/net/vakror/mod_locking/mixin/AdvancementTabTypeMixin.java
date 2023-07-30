package net.vakror.mod_locking.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.advancements.AdvancementTabType;
import net.vakror.mod_locking.screen.ModUnlockingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AdvancementTabType.class)
public class AdvancementTabTypeMixin {
    @ModifyConstant(method = "getX", constant = @Constant(intValue = 248), require = 1)
    public int getAdjustedTabX(int orig) {
        assert Minecraft.getInstance().screen != null;
        if (Minecraft.getInstance().screen instanceof ModUnlockingScreen sc) {
            return Minecraft.getInstance().screen.width - sc.getSelectedTab().getMarginX() * 2 - 4;
        } else {
            return 248;
        }
    }

    @ModifyConstant(method = "getY", constant = @Constant(intValue = 136), require = 1)
    public int getAdjustedTabY(int orig) {
        assert Minecraft.getInstance().screen != null;
        if (Minecraft.getInstance().screen instanceof ModUnlockingScreen sc) {
            return Minecraft.getInstance().screen.height - sc.getSelectedTab().getMarginY() * 2 - 4;
        } else {
            return 136;
        }
    }
}