package net.vakror.mod_locking.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.client.gui.screens.advancements.AdvancementTabType;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.vakror.mod_locking.mod.unlock.Unlock;
import net.vakror.mod_locking.screen.widget.ModTreeTab;
import net.vakror.mod_locking.screen.widget.ModWidget;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ModUnlockingScreen extends AbstractContainerScreen<ModUnlockingMenu> {

    @Override
    protected void renderBg(PoseStack matrices, float partialTick, int pMouseX, int pMouseY) {
        int i = (this.width - (width - selectedTab.getMarginX() * 2)) / 2;
        int j = (this.height - (height - selectedTab.getMarginY() * 2)) / 2;
        this.currentMouseXPos = pMouseX;
        this.currentMouseYPos = pMouseY;
        this.renderBackground(matrices);
        if (maxPages != 0) {
            net.minecraft.network.chat.Component page = Component.literal(String.format("%d / %d", tabPage + 1, maxPages + 1));
            int width = this.font.width(page);
            this.font.drawShadow(matrices, page.getVisualOrderText(), i + ((float) (width - selectedTab.getMarginX() * 2) / 2) - ((float) width / 2), j - 44, -1);
        }
        this.renderInside(matrices, pMouseX, pMouseY, i, j);
        this.renderWindow(matrices, i, j);
    }













    private static final ResourceLocation WINDOW_LOCATION = new ResourceLocation("textures/gui/advancements/window.png");
    public static final ResourceLocation TABS_LOCATION = new ResourceLocation("textures/gui/advancements/tabs.png");
    public static final int WINDOW_WIDTH = 252;
    public static final int WINDOW_HEIGHT = 140;
    private static final int WINDOW_INSIDE_X = 9;
    private static final int WINDOW_INSIDE_Y = 18;
    public static final int WINDOW_INSIDE_WIDTH = 234;
    public static final int WINDOW_INSIDE_HEIGHT = 113;
    private static final int WINDOW_TITLE_X = 8;
    private static final int WINDOW_TITLE_Y = 6;
    public static final int BACKGROUND_TILE_WIDTH = 16;
    public static final int BACKGROUND_TILE_HEIGHT = 16;
    public static final int BACKGROUND_TILE_COUNT_X = 14;
    public static final int BACKGROUND_TILE_COUNT_Y = 7;
    private static final Component VERY_SAD_LABEL = Component.translatable("advancements.sad_label");
    private static final Component NO_ADVANCEMENTS_LABEL = Component.translatable("advancements.empty");
    private static final Component TITLE = Component.literal("Mod Unlocking");
    private final Map<List<ModWidget>, ModTreeTab> tabs = new LinkedHashMap<>(this.menu.trees.size());
    @Nullable
    private ModTreeTab selectedTab;
    private boolean isScrolling;
    private static int tabPage, maxPages;
    public String exceptionMessage;

    public int currentMouseXPos = 0;
    public int currentMouseYPos = 0;

    public ModUnlockingScreen(ModUnlockingMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        super.init();
        this.tabs.clear();

        List<Unlock<?>> roots = new ArrayList<>(this.menu.unlocks.size());
        List<Unlock<?>> nonRoots = new ArrayList<>(this.menu.unlocks.size());
        for (Unlock<?> unlock: this.menu.unlocks) {
            if (unlock.getParents() == null || unlock.getRequiredUnlocks()[0].equals("")) {
                roots.add(unlock);
            } else {
                nonRoots.add(unlock);
            }
        }
        onAddUnlockRoots(roots, true);
        for (Unlock<?> nonRoot: nonRoots) {
            onAddNonRootUnlock(nonRoot);
        }

        if (this.selectedTab == null && !this.tabs.isEmpty()) {
            this.selectedTab = this.tabs.values().stream().toList().get(0);
        }
        if (this.tabs.size() > AdvancementTabType.MAX_TABS) {
            int guiLeft = (this.width - 252) / 2;
            int guiTop = (this.height - 140) / 2;
            addRenderableWidget(new net.minecraft.client.gui.components.Button(guiLeft, guiTop - 50, 20, 20, Component.literal("<"), b -> tabPage = Math.max(tabPage - 1, 0       )));
            addRenderableWidget(new net.minecraft.client.gui.components.Button(guiLeft + 252 - 20, guiTop - 50, 20, 20, Component.literal(">"), b -> tabPage = Math.min(tabPage + 1, maxPages)));
            maxPages = this.tabs.size() / AdvancementTabType.MAX_TABS;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton == 0) {
            assert selectedTab != null;
            int i = (this.width - (width - selectedTab.getMarginX() * 2)) / 2;
            int j = (this.height - (height - selectedTab.getMarginY() * 2)) / 2;

            for(ModTreeTab tab : this.tabs.values()) {
                if (tab.getPage() == tabPage && tab.isMouseOver(i, j, mouseX, mouseY)) {
                    selectedTab = tab;
                    System.out.println(selectedTab);
                }
            }
            int offsetX = (this.width - (width - selectedTab.getMarginX() * 2)) / 2;
            int offsetY = (this.height - (height - selectedTab.getMarginY() * 2)) / 2;
            this.selectedTab.mouseClicked(mouseX - offsetX - 9, mouseY - offsetY - 18);
        }

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void render(PoseStack matrices, int pMouseX, int pMouseY, float pPartialTick) {
        try {
            assert selectedTab != null;
            int i = (this.width - (width - selectedTab.getMarginX() * 2)) / 2;
            int j = (this.height - (height - selectedTab.getMarginY() * 2)) / 2;
            this.renderBg(matrices, pPartialTick, pMouseX, pMouseY);
            this.renderTooltips(matrices, pMouseX, pMouseY, i, j);
        } catch (NullPointerException e) {
            throw new NullPointerException(exceptionMessage);
        }
    }

    @Override
    public boolean mouseDragged(double p_97347_, double p_97348_, int p_97349_, double p_97350_, double p_97351_) {
        if (p_97349_ != 0) {
            this.isScrolling = false;
            return false;
        } else {
            if (!this.isScrolling) {
                this.isScrolling = true;
            } else if (this.selectedTab != null) {
                this.selectedTab.scroll(p_97350_, p_97351_);
            }

            return true;
        }
    }

    private void renderInside(PoseStack pPoseStack, int pMouseX, int pMouseY, int pOffsetX, int pOffsetY) {
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate((double)(pOffsetX + 9), (double)(pOffsetY + 18), 0.0D);
        RenderSystem.applyModelViewMatrix();
        selectedTab.drawContents(pPoseStack);
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.depthFunc(515);
        RenderSystem.disableDepthTest();
    }

    public void renderWindow(PoseStack matrices, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WINDOW_LOCATION);
        renderCorrect(matrices, x, y);
        if (this.tabs.size() > 1) {
            RenderSystem.setShaderTexture(0, TABS_LOCATION);

            for(ModTreeTab treeTab : this.tabs.values()) {
                if (treeTab.getPage() == tabPage)
                    treeTab.drawTab(matrices, x, y, treeTab == this.selectedTab);
            }

            RenderSystem.defaultBlendFunc();

            for(ModTreeTab treeTab1 : this.tabs.values()) {
                if (treeTab1.getPage() == tabPage)
                    treeTab1.drawIcon(x, y, this.itemRenderer);
            }

            RenderSystem.disableBlend();
        }

        this.font.draw(matrices, TITLE, (float)(x + 8), (float)(y + 6), 4210752);
    }

    private void renderTooltips(PoseStack matrices, int mouseX, int mouseY, int offsetX, int offsetY) {
        if (this.selectedTab != null) {
            matrices.pushPose();
            matrices.translate((float)(offsetX + 9), (float)(offsetY + 18), 400.0F);
//            RenderSystem.enableDepthTest();
            this.selectedTab.drawTooltips(matrices, mouseX - offsetX - 9, mouseY - offsetY - 18, offsetX, offsetY);
//            RenderSystem.disableDepthTest();
            matrices.popPose();
        }

        if (this.tabs.size() > 1) {
            for(ModTreeTab tab : this.tabs.values()) {
                if (tab.getPage() == tabPage && tab.isMouseOver(offsetX, offsetY, (double)mouseX, (double)mouseY)) {
                    this.renderTooltip(matrices, tab.getTitle(), mouseX, mouseY);
                }
            }
        }

    }

    public void renderCorrect(PoseStack matrices, int x, int y) {
        int iw = 0;

        int screenW = 252;
        int screenH = 140;
        // actual size that will be available for the box
        assert selectedTab != null;
        int actualW = width - selectedTab.getMarginX() - iw - x;
        int actualH = width - selectedTab.getMarginY() - y;
        int halfW = screenW / 2;
        int halfH = screenH / 2;
        // When the screen is less than the default size the corners overlap
        int clipXh = (int) (Math.max(0, screenW - actualW) / 2. + 0.5);
        int clipXl = (int) (Math.max(0, screenW - actualW) / 2.);
        int clipYh = (int) (Math.max(0, screenH - actualH) / 2. + 0.5);
        int clipYl = (int) (Math.max(0, screenH - actualH) / 2.);

        // The base screen has a resolution of 252x140, divided into 4 quadrants this gives:
        // 1 │ 2       x    y        x    y
        // ──┼──    1: 0    0     2: 126  0
        // 3 │ 4    3: 0    70    4: 126  70

        int rightQuadX = width - selectedTab.getMarginX() - halfW - iw + clipXh;
        int bottomQuadY = height - selectedTab.getMarginY() - halfH + clipYh;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WINDOW_LOCATION);
        blit(matrices, x, y, 0, 0, halfW - clipXl, halfH - clipYl);
        blit(matrices, rightQuadX, y, halfW + clipXh, 0, halfW - clipXh, halfH - clipYl); // top right
        blit(matrices, x, bottomQuadY, 0, halfH + clipYh, halfW - clipXl, halfH - clipYh); // bottom left
        blit(matrices, rightQuadX, bottomQuadY, halfW + clipXh, halfH + clipYh, halfW - clipXh, halfH - clipYh); // bottom right

        // draw borders
        iterate(x + halfW - clipXl, rightQuadX, 200, (pos, len) -> {
            blit(matrices, pos, y, 15, 0, len, halfH); // top
            blit(matrices, pos, bottomQuadY, 15, halfH + clipYh, len, halfH - clipYh); // bottom
        });
        iterate(y + halfH - clipYl, bottomQuadY, 100, (pos, len) -> {
            blit(matrices, x, pos, 0, 25, halfW, len); // left
            blit(matrices, rightQuadX, pos, halfW + clipXh, 25, halfW - clipXh, len); // right
        });
    }

    private void iterate(int start, int end, int maxstep, IteratorReceiver func) {
        if(start >= end) return;
        int size;
        for (int i=start; i<end; i+=maxstep) {
            size=maxstep;
            if (i+size > end) {
                size = end - i;
                if(size <= 0) return;
            }
            func.accept(i, size);
        }
    }

    public void onAddUnlockRoots(List<Unlock<?>> unlock, boolean a) {
        for (int i = 0; i < this.menu.trees.size(); i++) {
            List<Unlock<?>> unlocks = new ArrayList<>(unlock.size());
            for (Unlock<?> unlock1: unlock) {
                if (unlock1.getTreeName().equals(this.menu.trees.get(i).name)) {
                    unlocks.add(unlock1);
                }
            }
            onAddUnlockRoots(unlocks);
        }
    }

    public void onAddUnlockRoots(List<Unlock<?>> unlock) {
        ModTreeTab treeTab = ModTreeTab.create(this.minecraft, this, this.tabs.size(), this.menu.trees.get(this.tabs.size()));
        List<ModWidget> root = new ArrayList<>(unlock.size());
        assert treeTab != null;
        for (Unlock<?> unlock1: unlock) {
            assert this.minecraft != null;
            root.add(new ModWidget(treeTab, this.minecraft, unlock1));
        }
        Map<Unlock<?>, ModWidget> unlockModWidgetMap = new HashMap<>(unlock.size());
        for (int i = 0; i < unlock.size(); i++) {
            unlockModWidgetMap.put(unlock.get(i), root.get(i));
        }
        treeTab.setRootUnlocks(unlockModWidgetMap);
        this.tabs.put(root, treeTab);
    }

    public void onAddNonRootUnlock(Unlock<?> unlock) {
        ModTreeTab tab = this.getTab(unlock);
        if (tab != null) {
            tab.addUnlock(unlock);
        }
    }

    @Nullable
    public ModWidget getUnlockWidget(Unlock<?> unlock) {
        ModTreeTab tab = this.getTab(unlock);
        return tab == null ? null : tab.getWidget(unlock);
    }

    @Nullable
    private ModTreeTab getTab(Unlock<?> unlock) {
        while(unlock.getParents() != null && unlock.getParents().get(0) != null) {
            unlock = unlock.getParents().get(0);
        }

        AtomicReference<ModTreeTab> tab = new AtomicReference<>();
        Unlock<?> finalUnlock = unlock;
        this.tabs.forEach(((modWidgets, modTreeTab) -> {
            for (ModWidget widget: modWidgets) {
                if (widget.getUnlock().getName().equals(finalUnlock.getName())) {
                    tab.set(modTreeTab);
                    break;
                }
            }
        }));

        return tab.get();
    }

    public void updateDescriptions() {
        for (ModTreeTab tab : this.tabs.values()) {
            for (ModWidget widget : tab.getWidgets().values()) {
                widget.description = widget.getUnlock().createDescription(this);
            }
        }
    }
}

