package net.vakror.mod_locking.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementTabType;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.unlock.FineGrainedModUnlock;
import net.vakror.mod_locking.mod.unlock.ModUnlock;
import net.vakror.mod_locking.mod.unlock.Unlock;
import net.vakror.mod_locking.screen.widget.ModTreeTab;
import net.vakror.mod_locking.screen.widget.ModWidget;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ModUnlockingScreen extends AbstractContainerScreen<ModUnlockingMenu> {

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int pMouseX, int pMouseY) {
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
    private final Map<List<ModWidget>, ModTreeTab> tabs = new HashMap<>(this.menu.trees.size());
    @Nullable
    private ModTreeTab selectedTab;
    private boolean isScrolling;
    private static int tabPage, maxPages;

    public ModUnlockingScreen(ModUnlockingMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        super.init();
        this.tabs.clear();

        List<Unlock> roots = new ArrayList<>(this.menu.unlocks.size());
        List<Unlock> nonRoots = new ArrayList<>(this.menu.unlocks.size());
        for (Unlock unlock: this.menu.unlocks) {
            if (unlock.getParent() == null || unlock.getRequiredUnlock().equals("")) {
                roots.add(unlock);
            } else {
                nonRoots.add(unlock);
            }
        }
        onAddUnlockRoots(roots, true);
        for (Unlock nonRoot: nonRoots) {
            onAddNonRootUnlock(nonRoot);
        }

        if (this.selectedTab == null && !this.tabs.isEmpty()) {
            this.selectedTab = this.tabs.values().iterator().next();
        }
        if (this.tabs.size() > AdvancementTabType.MAX_TABS) {
            int guiLeft = (this.width - 252) / 2;
            int guiTop = (this.height - 140) / 2;
            addRenderableWidget(net.minecraft.client.gui.components.Button.builder(Component.literal("<"), b -> tabPage = Math.max(tabPage - 1, 0       ))
                    .pos(guiLeft, guiTop - 50).size(20, 20).build());
            addRenderableWidget(net.minecraft.client.gui.components.Button.builder(Component.literal(">"), b -> tabPage = Math.min(tabPage + 1, maxPages))
                    .pos(guiLeft + WINDOW_WIDTH - 20, guiTop - 50).size(20, 20).build());
            maxPages = this.tabs.size() / AdvancementTabType.MAX_TABS;
        }
    }

    @Override
    protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {
    }

    @Override
    public boolean mouseClicked(double p_97343_, double p_97344_, int p_97345_) {
        if (p_97345_ == 0) {
            int i = (this.width - (width - 30 * 2)) / 2;
            int j = (this.height - (height - 30 * 2)) / 2;

            for(ModTreeTab tab : this.tabs.values()) {
                if (tab.getPage() == tabPage && tab.isMouseOver(i, j, p_97343_, p_97344_)) {
                    selectedTab = tab;
                    System.out.println(selectedTab);
                }
            }
        }

        return super.mouseClicked(p_97343_, p_97344_, p_97345_);
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        int i = (this.width - (width - 30 * 2)) / 2;
        int j = (this.height - (height - 30 * 2)) / 2;
        this.renderBackground(graphics);
        if (maxPages != 0) {
            net.minecraft.network.chat.Component page = Component.literal(String.format("%d / %d", tabPage + 1, maxPages + 1));
            int width = this.font.width(page);
            graphics.drawString(this.font, page.getVisualOrderText(), i + ((width - 30 * 2) / 2) - (width / 2), j - 44, -1);
        }
        this.renderInside(graphics, pMouseX, pMouseY, i, j);
        this.renderWindow(graphics, i, j);
        this.renderTooltips(graphics, pMouseX, pMouseY, i, j);
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

    private void renderInside(GuiGraphics p_282012_, int p_97375_, int p_97376_, int p_97377_, int p_97378_) {
        selectedTab.drawContents(p_282012_, p_97377_ + 9, p_97378_ + 18);
    }

    public void renderWindow(GuiGraphics graphics, int x, int y) {
        RenderSystem.enableBlend();
//        p_283395_.blit(WINDOW_LOCATION, p_281890_, p_282532_, 0, 0, 252, 140);
        renderCorrect(graphics, x, y);
        if (this.tabs.size() > 1) {
            for(ModTreeTab tab : this.tabs.values()) {
                if (tab.getPage() == tabPage)
                    tab.drawTab(graphics, x, y, tab == this.selectedTab);
            }

            for(ModTreeTab tab : this.tabs.values()) {
                if (tab.getPage() == tabPage)
                    tab.drawIcon(graphics, x, y);
            }
        }

        graphics.drawString(this.font, TITLE, x + 8, y + 6, 4210752, false);
    }

    private void renderTooltips(GuiGraphics p_282784_, int p_283556_, int p_282458_, int p_281519_, int p_283371_) {
        if (this.selectedTab != null) {
            p_282784_.pose().pushPose();
            p_282784_.pose().translate((float)(p_281519_ + 9), (float)(p_283371_ + 18), 400.0F);
            RenderSystem.enableDepthTest();
            this.selectedTab.drawTooltips(p_282784_, p_283556_ - p_281519_ - 9, p_282458_ - p_283371_ - 18, p_281519_, p_283371_);
            RenderSystem.disableDepthTest();
            p_282784_.pose().popPose();
        }

        if (this.tabs.size() > 1) {
            for(ModTreeTab tab : this.tabs.values()) {
                if (tab.getPage() == tabPage && tab.isMouseOver(p_281519_, p_283371_, (double)p_283556_, (double)p_282458_)) {
                    p_282784_.renderTooltip(this.font, tab.getTitle(), p_283556_, p_282458_);
                }
            }
        }

    }

    public void renderCorrect(GuiGraphics graphics, int x, int y) {
        int iw = 0;

        int screenW = 252;
        int screenH = 140;
        // actual size that will be available for the box
        int actualW = width - 30 - iw - x;
        int actualH = width - 30 - y;
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

        int rightQuadX = width - 30 - halfW - iw + clipXh;
        int bottomQuadY = height - 30 - halfH + clipYh;

        graphics.blit(WINDOW_LOCATION, x, y, 0, 0, halfW - clipXl, halfH - clipYl);
        graphics.blit(WINDOW_LOCATION, rightQuadX, y, halfW + clipXh, 0, halfW - clipXh, halfH - clipYl); // top right
        graphics.blit(WINDOW_LOCATION, x, bottomQuadY, 0, halfH + clipYh, halfW - clipXl, halfH - clipYh); // bottom left
        graphics.blit(WINDOW_LOCATION, rightQuadX, bottomQuadY, halfW + clipXh, halfH + clipYh, halfW - clipXh, halfH - clipYh); // bottom right

        // draw borders
        iterate(x + halfW - clipXl, rightQuadX, 200, (pos, len) -> {
            graphics.blit(WINDOW_LOCATION, pos, y, 15, 0, len, halfH); // top
            graphics.blit(WINDOW_LOCATION, pos, bottomQuadY, 15, halfH + clipYh, len, halfH - clipYh); // bottom
        });
        iterate(y + halfH - clipYl, bottomQuadY, 100, (pos, len) -> {
            graphics.blit(WINDOW_LOCATION, x, pos, 0, 25, halfW, len); // left
            graphics.blit(WINDOW_LOCATION, rightQuadX, pos, halfW + clipXh, 25, halfW - clipXh, len); // right
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

    public void onAddUnlockRoots(List<Unlock> unlock, boolean a) {
        for (int i = 0; i < this.menu.trees.size(); i++) {
            List<Unlock> unlocks = new ArrayList<>(unlock.size());
            for (Unlock unlock1: unlock) {
                if (unlock1.getTree().equals(this.menu.trees.get(i).name)) {
                    unlocks.add(unlock1);
                }
            }
            onAddUnlockRoots(unlocks);
        }
    }

    public void onAddUnlockRoots(List<Unlock> unlock) {
        ModTreeTab treeTab = ModTreeTab.create(this.minecraft, this, this.tabs.size(), this.menu.trees.get(Math.max(this.tabs.size(), 0)));
        List<ModTree> trees = this.menu.trees;
        Map<List<ModWidget>, ModTreeTab> treeTabs = this.tabs;
        List<ModWidget> root = new ArrayList<>(unlock.size());
        assert treeTab != null;
        for (Unlock unlock1: unlock) {
            assert this.minecraft != null;
            root.add(new ModWidget(treeTab, this.minecraft, unlock1));
        }
        Map<Unlock, ModWidget> unlockModWidgetMap = new HashMap<>(unlock.size());
        for (int i = 0; i < unlock.size(); i++) {
            unlockModWidgetMap.put(unlock.get(i), root.get(i));
        }
        treeTab.setRootUnlocks(unlockModWidgetMap);
        this.tabs.put(root, treeTab);
    }

    public void onAddNonRootUnlock(Unlock unlock) {
        ModTreeTab tab = this.getTab(unlock);
        if (tab != null) {
            tab.addUnlock(unlock);
        }
    }

    @Nullable
    public ModWidget getUnlockWidget(Unlock unlock) {
        ModTreeTab tab = this.getTab(unlock);
        return tab == null ? null : tab.getWidget(unlock);
    }

    @Nullable
    private ModTreeTab getTab(Unlock unlock) {
        while(unlock.getParent() != null) {
            unlock = unlock.getParent();
        }

        AtomicReference<ModTreeTab> tab = new AtomicReference<>();
        Unlock finalUnlock = unlock;
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
}

