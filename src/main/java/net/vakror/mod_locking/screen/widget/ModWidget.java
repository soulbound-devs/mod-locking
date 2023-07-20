package net.vakror.mod_locking.screen.widget;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementWidgetType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.vakror.mod_locking.mod.unlock.Unlock;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModWidget {
    private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/advancements/widgets.png");
    private static final int HEIGHT = 26;
    private static final int BOX_X = 0;
    private static final int BOX_WIDTH = 200;
    private static final int FRAME_WIDTH = 26;
    private static final int ICON_X = 8;
    private static final int ICON_Y = 5;
    private static final int ICON_WIDTH = 26;
    private static final int TITLE_PADDING_LEFT = 3;
    private static final int TITLE_PADDING_RIGHT = 5;
    private static final int TITLE_X = 32;
    private static final int TITLE_Y = 9;
    private static final int TITLE_MAX_WIDTH = 163;
    private static final int[] TEST_SPLIT_OFFSETS = new int[]{0, 10, -10, 25, -25};
    private final ModTreeTab tab;
    private final Unlock unlock;
    private final Component title;
    private final int width;
    private final List<Component> description;
    private final Minecraft minecraft;
    @Nullable
    private List<ModWidget> parents;
    private final List<ModWidget> children = Lists.newArrayList();
    @Nullable
    private boolean isDone;
    private final int x;
    private final int y;

    public ModWidget(ModTreeTab treeTab, Minecraft minecraft, Unlock<?> unlock) {
        this.tab = treeTab;
        this.unlock = unlock;
        this.minecraft = minecraft;
        this.title = Component.literal(unlock.getName());
        this.x = Mth.floor(unlock.getX() * 28.0F);
        this.y = Mth.floor(unlock.getY() * 27.0F);
        int l = 29 + minecraft.font.width(this.title);
        this.description = this.findOptimalLines(unlock.createDescription(treeTab.getScreen()), l);

        for(Component component : this.description) {
            l = Math.max(l, minecraft.font.width(component));
        }

        this.width = l + 3 + 5;
    }

    private static float getMaxWidth(StringSplitter p_97304_, List<FormattedText> p_97305_) {
        return (float)p_97305_.stream().mapToDouble(p_97304_::stringWidth).max().orElse(0.0D);
    }

    // find TOOLTIP lines!
    private List<Component> findOptimalLines(List<Component> components, int p_97310_) {
        return components;
    }

    @Nullable
    private List<ModWidget> getParents(Unlock<?> unlock) {
        List<ModWidget> parents = new ArrayList<>();
        if (unlock.getParents() != null) {
            for (Unlock<?> parent: unlock.getParents()) {
                if (parent != null) {
                    parents.add(this.tab.getWidget(parent));
                }
            }
        }

        return parents.isEmpty() ? null : parents;
    }

    public void drawConnectivity(GuiGraphics graphics, int x, int y, boolean shadow) {
        if (this.parents != null) {
            for (ModWidget parent : this.parents) {
                int i = x + parent.x + 13;
                int j = x + parent.x + 26 + 4;
                int k = y + parent.y + 13;
                int l = x + this.x + 13;
                int i1 = y + this.y + 13;
                int j1 = shadow ? -16777216 : -1;
                if (shadow) {
                    graphics.hLine(j, i, k - 1, j1);
                    graphics.hLine(j + 1, i, k, j1);
                    graphics.hLine(j, i, k + 1, j1);
                    graphics.hLine(l, j - 1, i1 - 1, j1);
                    graphics.hLine(l, j - 1, i1, j1);
                    graphics.hLine(l, j - 1, i1 + 1, j1);
                    graphics.vLine(j - 1, i1, k, j1);
                    graphics.vLine(j + 1, i1, k, j1);
                } else {
                    graphics.hLine(j, i, k, j1);
                    graphics.hLine(l, j, i1, j1);
                    graphics.vLine(j, i1, k, j1);
                }
            }
        }
        for (ModWidget widget : this.children) {
            widget.drawConnectivity(graphics, x, y, shadow);
        }
    }

    public void draw(GuiGraphics graphics, int x, int y) {
        AdvancementWidgetType widgetType = this.isDone ? AdvancementWidgetType.OBTAINED: AdvancementWidgetType.UNOBTAINED;

        graphics.blit(WIDGETS_LOCATION, x + this.x + 3, y + this.y, 26, 128 + widgetType.getIndex() * 26, 26, 26);
        ItemStack fakeUnlockStack = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.unlock.getIcon()))));
        CompoundTag tag = unlock.getIconNbt();
        if (tag != null) {
            fakeUnlockStack.setTag(tag);
        }
        graphics.renderFakeItem(fakeUnlockStack, x + this.x + 8, y + this.y + 5);

        for (ModWidget widget : this.children) {
            widget.draw(graphics, x, y);
        }
    }

    public int getWidth() {
        return this.width;
    }

    public void setDone(boolean progress) {
        this.isDone = progress;
    }

    public void addChild(ModWidget p_97307_) {
        this.children.add(p_97307_);
    }
    public Unlock getUnlock() {
        return unlock;
    }

    public void drawHover(GuiGraphics graphics, int x, int y, float fade, int width, int height) {
        boolean flag = width + x + this.x + this.width + 26 >= this.tab.getScreen().width;
        boolean flag1 = 113 - y - this.y - 26 <= 6 + this.description.size() * 9;
        int j = this.width / 2;
        AdvancementWidgetType advancementwidgettype;
        AdvancementWidgetType advancementwidgettype1;
        AdvancementWidgetType advancementwidgettype2;
        if (isDone) {
            advancementwidgettype = AdvancementWidgetType.OBTAINED;
            advancementwidgettype1 = AdvancementWidgetType.OBTAINED;
            advancementwidgettype2 = AdvancementWidgetType.OBTAINED;
        } else {
            advancementwidgettype = AdvancementWidgetType.UNOBTAINED;
            advancementwidgettype1 = AdvancementWidgetType.UNOBTAINED;
            advancementwidgettype2 = AdvancementWidgetType.UNOBTAINED;
        }

        int k = this.width - j;
        RenderSystem.enableBlend();
        int l = y + this.y;
        int i1;
        if (flag) {
            i1 = x + this.x - this.width + 26 + 6;
        } else {
            i1 = x + this.x;
        }

        int j1 = 32 + this.description.size() * 9;
        if (!this.description.isEmpty()) {
            if (flag1) {
                graphics.blitNineSliced(WIDGETS_LOCATION, i1, l + 26 - j1, this.width, j1, 10, 200, 26, 0, 52);
            } else {
                graphics.blitNineSliced(WIDGETS_LOCATION, i1, l, this.width, j1, 10, 200, 26, 0, 52);
            }
        }

        graphics.blit(WIDGETS_LOCATION, i1, l, 0, advancementwidgettype.getIndex() * 26, j, 26);
        graphics.blit(WIDGETS_LOCATION, i1 + j, l, 200 - k, advancementwidgettype1.getIndex() * 26, k, 26);
        graphics.blit(WIDGETS_LOCATION, x + this.x + 3, y + this.y, 26, 128 + advancementwidgettype2.getIndex() * 26, 26, 26);
        if (flag) {
            graphics.drawString(this.minecraft.font, this.title, i1 + 5, y + this.y + 9, -1);
        } else {
            graphics.drawString(this.minecraft.font, this.title, x + this.x + 32, y + this.y + 9, -1);
        }

        if (flag1) {
            for(int k1 = 0; k1 < this.description.size(); ++k1) {
                graphics.drawString(this.minecraft.font, this.description.get(k1), i1 + 5, l + 26 - j1 + 7 + k1 * 9, -5592406, false);
            }
        } else {
            for(int l1 = 0; l1 < this.description.size(); ++l1) {
                graphics.drawString(this.minecraft.font, this.description.get(l1), i1 + 5, y + this.y + 9 + 17 + l1 * 9, -5592406, false);
            }
        }

        ItemStack fakeUnlockStack = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.unlock.getIcon()))));
        CompoundTag tag = unlock.getIconNbt();
        if (tag != null) {
            fakeUnlockStack.setTag(tag);
        }

        graphics.renderFakeItem(fakeUnlockStack, x + this.x + 8, y + this.y + 5);
    }

    public boolean isMouseOver(int p_97260_, int p_97261_, int p_97262_, int p_97263_) {
        int i = p_97260_ + this.x;
        int j = i + 26;
        int k = p_97261_ + this.y;
        int l = k + 26;
        return p_97262_ >= i && p_97262_ <= j && p_97263_ >= k && p_97263_ <= l;
    }

    public void attachToParent() {
        if (this.parents == null && this.unlock.getParents() != null) {
            this.parents = this.getParents(this.unlock);
            if (this.parents != null) {
                this.parents.forEach((parent) -> parent.addChild(this));
            }
        }
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }
}