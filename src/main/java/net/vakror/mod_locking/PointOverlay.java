package net.vakror.mod_locking;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.vakror.mod_locking.mod.capability.ModTreeCapability;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.point.ModPoint;

public class PointOverlay {
    public static final IGuiOverlay POINT_HUD = ((gui, graphics, partialTick, width, height) -> {
        if (gui.shouldDrawSurvivalElements()) {
            int x = width / 2;
            int y = height;

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            int i = 1;
            for (ModPoint point : ModConfigs.POINTS.points) {
                assert Minecraft.getInstance().player != null;
                if (Minecraft.getInstance().player.getCapability(ModTreeProvider.MOD_TREE).orElse(new ModTreeCapability()).getPoints().get(point.name) != null) {
                    int amount = Minecraft.getInstance().player.getCapability(ModTreeProvider.MOD_TREE).orElse(new ModTreeCapability()).getPoints().get(point.name);
                    String nameString = amount + (amount > 1 ? point.pluralName : point.name);
                    int nameWidth = Minecraft.getInstance().font.width(nameString);
                    graphics.drawString(Minecraft.getInstance().font, nameString, width - nameWidth, y - (10 * i), point.getColor(), false);
                    i++;
                }
            }
        }
    });
}