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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        this.title = Component.literal(tree.name);
        this.name = tree.name;
    }

    public ModTreeTab(Minecraft mc, ModUnlockingScreen screen, AdvancementTabType type, List<ModWidget> rootUnlocks, int index, int page, ModTree tree) {
        this(mc, screen, type, rootUnlocks, index, tree);
        this.page = page;
    }

    public void setRootUnlocks(Map<Unlock, ModWidget> unlocks) {
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

    public void drawTab(GuiGraphics p_282671_, int p_282721_, int p_282964_, boolean p_283052_) {
        this.type.draw(p_282671_, p_282721_, p_282964_, p_283052_, this.index);
    }

    public void drawIcon(GuiGraphics p_282895_, int p_283419_, int p_283293_) {
        this.type.drawIcon(p_282895_, p_283419_, p_283293_, this.index, this.icon);
    }

    public void drawContents(GuiGraphics graphics, int x, int y) {
        if (!this.centered) {
            this.scrollX = ((double) screen.width / 2 - (115)) - (double) (this.maxX + this.minX) / 2;
            this.scrollY = ((double) screen.height / 2) - (double) (this.maxY + this.minY) / 2;
            this.scrollX = Mth.clamp(this.scrollX, -(this.maxX), 0.0D);
            this.scrollY = Mth.clamp(this.scrollY, -(this.maxY), 0.0D);

            this.centered = true;
        }

        graphics.enableScissor(x, y, x + (screen.width - 30 * 2 - 2 * 9), y + (screen.height - 30 * 2 - 3 * 9));
        graphics.pose().pushPose();
        graphics.pose().translate((float) x, (float) y, 0.0F);
        ResourceLocation resourcelocation = new ResourceLocation(this.tree.backgroundTexture);
        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        int k = i % 16;
        int l = j % 16;

        for (int i1 = -1; i1 <= ((screen.width - 30 * 2) / 16 + 1); ++i1) {
            for (int j1 = -1; j1 <= ((screen.height - 30 * 2) / 16 + 1); ++j1) {
                graphics.blit(resourcelocation, k + 16 * i1, l + 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }

        rootUnlocks.forEach((root -> {
            root.drawConnectivity(graphics, i, j, true);
            root.drawConnectivity(graphics, i, j, false);
            root.draw(graphics, i, j);
        }));
        graphics.pose().popPose();
        graphics.disableScissor();
    }

    public void drawTooltips(GuiGraphics p_282892_, int p_283658_, int p_282602_, int p_282652_, int p_283595_) {
        p_282892_.pose().pushPose();
        p_282892_.pose().translate(0.0F, 0.0F, -200.0F);
        p_282892_.fill(0, 0, (screen.width - 30 * 2 - 2 * 9), (screen.height - 30 * 2 - 3 * 9), Mth.floor(this.fade * 255.0F) << 24);
        boolean flag = false;
        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        if (p_283658_ > 0 && p_283658_ < (screen.width - 30 * 2 - 2 * 9) && p_282602_ > 0 && p_282602_ < (screen.height - 30 * 2 - 3 * 9)) {
            for (ModWidget modWidget : this.widgets.values()) {
                if (modWidget.isMouseOver(i, j, p_283658_, p_282602_)) {
                    flag = true;
                    modWidget.drawHover(p_282892_, i, j, this.fade, p_282652_, p_283595_);
                    break;
                }
            }
        }

        p_282892_.pose().popPose();
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
        this.scrollX = Mth.clamp(this.scrollX + p_97152_, -(this.maxX), 0.0D);

        this.scrollY = Mth.clamp(this.scrollY + p_97153_, -(this.maxY), 0.0D);
    }

    public void addUnlock(Unlock unlock) {
        ModWidget modWidget = new ModWidget(this, this.minecraft, unlock);
        this.addWidget(modWidget, unlock);
    }

    private void addWidget(ModWidget modWidget, Unlock unlock) {
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
    public ModWidget getWidget(Unlock unlock) {
        for (ModWidget widget: this.widgets.values()) {
            if (unlock.getName().equals(widget.getUnlock().getName())) {
                return widget;
            }
        }
        return null;
    }

    public ModUnlockingScreen getScreen() {
        return this.screen;
    }
}
