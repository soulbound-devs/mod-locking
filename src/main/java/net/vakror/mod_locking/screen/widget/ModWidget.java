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
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.vakror.mod_locking.mod.config.configs.ModPointsConfig;
import net.vakror.mod_locking.mod.unlock.Unlock;
import net.vakror.mod_locking.packet.ModPackets;
import net.vakror.mod_locking.packet.UnlockModWidgetC2SPacket;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public List<Component> description = new ArrayList<>();
    private final Minecraft minecraft;
    @Nullable
    private List<ModWidget> parents;
    @Nullable
    private boolean unlocked;
    private final int x;
    private final int y;

    public ModWidget(ModTreeTab treeTab, Minecraft minecraft, Unlock<?> unlock) {
        this.tab = treeTab;
        this.unlock = unlock;
        this.minecraft = minecraft;
        this.title = Component.literal(unlock.getName());
        this.x = Mth.floor(unlock.getX() * 28.0F);
        this.y = Mth.floor(unlock.getY() * 27.0F);
        final int[] l = {29 + minecraft.font.width(this.title)};
        if (!unlock.getReasonsWhyPlayerCannotAfford(this.tab.getScreen()).isEmpty()) {
            int newL = 29 + minecraft.font.width(unlock.getReasonsWhyPlayerCannotAfford(this.tab.getScreen()).get(1));
            if (newL > l[0]) {
                l[0] = newL;
            }
        }
        if (unlock.getRequiredUnlocks() != null) {
            for (String requiredUnlock : unlock.getRequiredUnlocks()) {
                int newL = 29 + minecraft.font.width("Requires: " + requiredUnlock);
                if (newL > l[0]) l[0] = newL;
            }
        }
        if (unlock.getCost() != null) {
            unlock.getCost().forEach((pointName, amount) -> {
                String pointAmountString = amount + " " + (amount > 1 ? ModPointsConfig.getPoint(pointName).pluralName : pointName);
                int newL = 29 + minecraft.font.width(pointAmountString);
                if (newL > l[0]) l[0] = newL;
            });
        }
        List<FormattedText> tooltipLines = this.findOptimalLines(unlock.createDescription(treeTab.getScreen()), l[0]);
        tooltipLines.forEach((tooltipLine) -> {
            MutableComponent component = Component.literal(tooltipLine.getString());
            tooltipLine.visit(((pStyle, pContent) -> {
                component.setStyle(pStyle);
                return Optional.empty();
            }), Style.EMPTY);
            this.description.add(component);
            if (!tooltipLine.getString().equals("Cost: ")) {
                this.description.add(Component.empty());
            }
        });
        this.unlocked = unlock.hasUnlocked(treeTab.getScreen());

        for(Component component : this.description) {
            l[0] = Math.max(l[0], minecraft.font.width(component));
        }

        this.width = l[0] + 3 + 5;
    }

    private static float getMaxWidth(StringSplitter p_97304_, List<FormattedText> p_97305_) {
        return (float)p_97305_.stream().mapToDouble(p_97304_::stringWidth).max().orElse(0.0D);
    }

    // find TOOLTIP lines!
    private List<FormattedText> findOptimalLines(List<Component> components, int pMaxWidth) {
        StringSplitter stringsplitter = this.minecraft.font.getSplitter();
        List<FormattedText> list = new ArrayList<>();

        for (Component component : components) {
            List<FormattedText> list1 = stringsplitter.splitLines(component, pMaxWidth - 0, component.getStyle());
            list.addAll(list1);
        }

        return list;
    }

    @Nullable
    private List<ModWidget> getParents(Unlock<?> unlock) {
        List<ModWidget> parents = new ArrayList<>();
        if (unlock.getParents() != null) {
            for (Unlock<?> parent: unlock.getParents()) {
                if (parent != null) {
                    if (this.tab.getWidget(parent) != null) {
                        parents.add(this.tab.getWidget(parent));
                    } else {
                        this.tab.addUnlock(parent);
                        parents.add(this.tab.getWidget(parent));
                    }
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
    }

    public void draw(GuiGraphics graphics, int x, int y) {
        AdvancementWidgetType widgetType = this.unlocked ? AdvancementWidgetType.OBTAINED: AdvancementWidgetType.UNOBTAINED;

        graphics.blit(WIDGETS_LOCATION, x + this.x + 3, y + this.y, 26, 128 + widgetType.getIndex() * 26, 26, 26);
        ItemStack fakeUnlockStack = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.unlock.getIcon()))));
        CompoundTag tag = unlock.getIconNbt();
        if (tag != null) {
            fakeUnlockStack.setTag(tag);
        }
        graphics.renderFakeItem(fakeUnlockStack, x + this.x + 8, y + this.y + 5);
    }

    public int getWidth() {
        return this.width;
    }

    public void setUnlocked(boolean progress) {
        this.unlocked = progress;
    }

    public Unlock<?> getUnlock() {
        return unlock;
    }

    public void drawHover(GuiGraphics graphics, int x, int y, float fade, int width, int height) {
        boolean flag = width + x + this.x + this.width + 26 >= this.tab.getScreen().width;
        boolean flag1 = 113 - y - this.y - 26 <= 6 + this.description.size() * 9;
        int j = this.width / 2;
        AdvancementWidgetType advancementwidgettype;
        AdvancementWidgetType advancementwidgettype1;
        AdvancementWidgetType advancementwidgettype2;
        if (unlocked) {
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

    public void mouseClicked() {
        if (this.unlock.canUnlock(this.tab.getScreen())) {
            this.unlocked = true;
            ModPackets.sendToServer(new UnlockModWidgetC2SPacket(this));
        }
    }

    public boolean isMouseOver(int x, int y, double mouseX, double mouseY) {
        int i = x + this.x;
        int j = i + 26;
        int k = y + this.y;
        int l = k + 26;
        return mouseX >= i && mouseX <= j && mouseY >= k && mouseY <= l;
    }

    public void attachToParent() {
        if (this.parents == null && this.unlock.getParents() != null) {
            this.parents = this.getParents(this.unlock);
        }
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }
}