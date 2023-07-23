package net.vakror.mod_locking.screen.widget;

import com.google.common.collect.Maps;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.client.gui.screens.advancements.AdvancementTabType;
import net.minecraft.client.gui.screens.advancements.AdvancementWidget;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.unlock.Unlock;
import net.vakror.mod_locking.screen.ModUnlockingScreen;

import javax.annotation.Nullable;
import java.util.*;

public class ModTreeTab {
    private final Minecraft minecraft;
    private final ModUnlockingScreen screen;
    private final AdvancementTabType type;
    private final int index;
    private final ItemStack icon;
    private final Component title;
    private final Map<Unlock, ModWidget> widgets = Maps.newLinkedHashMap();
    private double scrollX;
    private double scrollY;
    private int minX = Integer.MAX_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int maxY = Integer.MIN_VALUE;
    private float fade;
    private boolean centered;
    private int page;
    private ModTree tree;
    private List<ModWidget> rootUnlocks;
    private String name;

    public ModTreeTab(Minecraft minecraft, ModUnlockingScreen screen, AdvancementTabType tabType, List<ModWidget> rootUnlocks, int index, ModTree tree) {
        this.minecraft = minecraft;
        this.screen = screen;
        this.type = tabType;
        this.index = index;
        this.tree = tree;
        this.rootUnlocks = rootUnlocks;
        this.icon = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(tree.icon))));
        if (tree.iconNbt != null) {
            this.icon.setTag(tree.iconNbt);
        }
        this.title = Component.literal(tree.name);
        this.name = tree.name;
    }

    public ModTreeTab(Minecraft mc, ModUnlockingScreen screen, AdvancementTabType type, List<ModWidget> rootUnlocks, int index, int page, ModTree tree) {
        this(mc, screen, type, rootUnlocks, index, tree);
        this.page = page;
    }

    public void setRootUnlocks(Map<Unlock<?>, ModWidget> unlocks) {
        this.rootUnlocks = unlocks.values().stream().toList();
        this.widgets.putAll(unlocks);

        unlocks.forEach(((unlock, modWidget) -> {
            int i = modWidget.getX();
            int j = i + 28;
            int k = modWidget.getY();
            int l = k + 27;
            this.minX = Math.min(this.minX, i);
            this.maxX = Math.max(this.maxX, j);
            this.minY = Math.min(this.minY, k);
            this.maxY = Math.max(this.maxY, l);
        }));
    }

    public int getPage() {
        return page;
    }

    public String getName() {
        return name;
    }

    public AdvancementTabType getType() {
        return this.type;
    }

    public int getIndex() {
        return this.index;
    }

    public Component getTitle() {
        return this.title;
    }

    public List<ModWidget> getRootUnlocks() {
        return rootUnlocks;
    }

    public void drawTab(GuiGraphics graphics, int offsetX, int offsetY, boolean isSelected) {
        this.type.draw(graphics, offsetX, offsetY, isSelected, this.index);
    }

    public void drawIcon(GuiGraphics graphics, int offsetX, int offsetY) {
        this.type.drawIcon(graphics, offsetX, offsetY, this.index, this.icon);
    }

    public void drawContents(GuiGraphics graphics, int x, int y) {
        if (!this.centered) {
            this.scrollX = tree.startScrollX;
            this.scrollY = tree.startScrollY;
            this.scrollX = Mth.clamp(this.scrollX, -(this.maxX), 0.0D);
            this.scrollY = Mth.clamp(this.scrollY, -(this.maxY), 0.0D);

            this.centered = true;
        }

        graphics.enableScissor(x, y, x + (screen.width - getMarginX() * 2 - 2 * 9), y + (screen.height - getMarginY() * 2 - 3 * 9));
        graphics.pose().pushPose();
        graphics.pose().translate((float) x, (float) y, 0.0F);
        ResourceLocation resourcelocation = new ResourceLocation(this.tree.backgroundTexture);
        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        int k = i % 16;
        int l = j % 16;

        for (int i1 = -1; i1 <= ((screen.width - getMarginX() * 2) / 16 + 1); ++i1) {
            for (int j1 = -1; j1 <= ((screen.height - getMarginY() * 2) / 16 + 1); ++j1) {
                graphics.blit(resourcelocation, k + 16 * i1, l + 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }

        widgets.values().forEach((widget -> {
            widget.drawConnectivity(graphics, i, j, true);
            widget.drawConnectivity(graphics, i, j, false);
        }));
        for (ModWidget widget : widgets.values()) {
            widget.draw(graphics, i, j);
        }
        graphics.pose().popPose();
        graphics.disableScissor();
    }

    public void drawTooltips(GuiGraphics graphics, int mouseX, int mouseY, int width, int height) {
        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, -200.0F);
        graphics.fill(0, 0, (screen.width - getMarginX() * 2 - 2 * 9), (screen.height - getMarginY() * 2 - 3 * 9), Mth.floor(this.fade * 255.0F) << 24);
        boolean flag = false;
        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        if (mouseX > 0 && mouseX < (screen.width - getMarginX()* 2 - 2 * 9) && mouseY > 0 && mouseY < (screen.height - getMarginY() * 2 - 3 * 9)) {
            for (ModWidget modWidget : this.widgets.values()) {
                if (modWidget.isMouseOver(i, j, mouseX, mouseY)) {
                    flag = true;
                    modWidget.drawHover(graphics, i, j, this.fade, width, height);
                    break;
                }
            }
        }

        graphics.pose().popPose();
        if (flag) {
            this.fade = Mth.clamp(this.fade + 0.02F, 0.0F, 0.3F);
        } else {
            this.fade = Mth.clamp(this.fade - 0.04F, 0.0F, 1.0F);
        }

    }

    public boolean isMouseOver(int p_97155_, int p_97156_, double p_97157_, double p_97158_) {
        return this.type.isMouseOver(p_97155_, p_97156_, this.index, p_97157_, p_97158_);
    }

    @Nullable
    public static ModTreeTab create(Minecraft minecraft, ModUnlockingScreen screen, int index, ModTree tree) {
        for (AdvancementTabType type : AdvancementTabType.values()) {
            if ((index % AdvancementTabType.MAX_TABS) < type.getMax()) {
                return new ModTreeTab(minecraft, screen, type, null, index % AdvancementTabType.MAX_TABS, index / AdvancementTabType.MAX_TABS, tree);
            }

            index -= type.getMax();
        }

        return null;
    }

    public void scroll(double p_97152_, double p_97153_) {
        if (this.maxX - this.minX > (screen.width - getMarginX() * 2 - 2 * 9)) {
            this.scrollX = Mth.clamp(this.scrollX + p_97152_, (double)(-(this.maxX - 234)), 0.0D);
        }

        if (this.maxY - this.minY > (screen.height - getMarginY() * 2)) {
            this.scrollY = Mth.clamp(this.scrollY + p_97153_, (double)(-(this.maxY - 113)), 0.0D);
        }
    }

    public void addUnlock(Unlock<?> unlock) {
        if (!widgets.containsKey(unlock)) {
            ModWidget modWidget = new ModWidget(this, this.minecraft, unlock);
            this.addWidget(modWidget, unlock);
        }
    }

    private void addWidget(ModWidget modWidget, Unlock<?> unlock) {
        this.widgets.put(unlock, modWidget);
        int i = modWidget.getX();
        int j = i + 28;
        int k = modWidget.getY();
        int l = k + 27;
        this.minX = Math.min(this.minX, i);
        this.maxX = Math.max(this.maxX, j);
        this.minY = Math.min(this.minY, k);
        this.maxY = Math.max(this.maxY, l);

        for (ModWidget modWidget1 : this.widgets.values()) {
            modWidget1.attachToParent();
        }
    }

    @Nullable
    public ModWidget getWidget(Unlock<?> unlock) {
        for (ModWidget widget : this.widgets.values()) {
            if (unlock.getName().equals(widget.getUnlock().getName())) {
                return widget;
            }
        }
        this.screen.exceptionMessage = ("Cannot find widget of name " + unlock.getName() + " In tree " + tree.name + "! Declare child unlock AFTER all parents!");
        return new ModWidget(this, Minecraft.getInstance(), unlock);
    }

    public ModUnlockingScreen getScreen() {
        return this.screen;
    }

    public int getMarginX() {
        return this.tree.marginX;
    }

    public int getMarginY() {
        return Math.max(this.tree.marginY, 30);
    }
}
