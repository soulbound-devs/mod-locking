package net.vakror.mod_locking.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRuntimeRegistration;
import mezz.jei.api.runtime.*;
import net.minecraft.resources.ResourceLocation;
import net.vakror.mod_locking.ModLockingMod;
import net.vakror.mod_locking.screen.ModUnlockingScreen;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class ModLockingJeiPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(ModLockingMod.MOD_ID, "mod-locking");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(ModUnlockingScreen.class, new ModUnlockingScreenContainerHandler());
        IModPlugin.super.registerGuiHandlers(registration);
    }

    @Override
    public void registerRuntime(IRuntimeRegistration registration) {
        System.out.println("ihhfregeoifhergyehygeg");
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        IModPlugin.super.onRuntimeAvailable(jeiRuntime);
    }
}
