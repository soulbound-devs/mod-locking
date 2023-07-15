package net.vakror.mod_locking.compat.jei;

import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rect2i;
import net.vakror.mod_locking.screen.ModUnlockingScreen;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ModUnlockingScreenContainerHandler implements IGuiContainerHandler<ModUnlockingScreen> {

    @Override
    public @NotNull List<Rect2i> getGuiExtraAreas(@NotNull ModUnlockingScreen containerScreen) {

        List<Rect2i> list = new ArrayList<>();

        assert Minecraft.getInstance().screen != null;
        list.add(new Rect2i(0, 0, Minecraft.getInstance().screen.width, Minecraft.getInstance().screen.height));

        return list;
    }
}
