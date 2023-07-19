package net.vakror.mod_locking;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.vakror.mod_locking.locking.Restriction;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.tree.ModTrees;
import net.vakror.mod_locking.packet.ModPackets;
import net.vakror.mod_locking.packet.OpenLockingScreenC2SPacket;
import net.vakror.mod_locking.screen.ModMenuTypes;
import net.vakror.mod_locking.screen.ModUnlockingScreen;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = ModLockingMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModForgeEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.LOCK_SREEN_KEY);
        }

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.UNLOCK_TREE.get(), ModUnlockingScreen::new);
        }

        @SubscribeEvent
        public static void registerGuiOverlay(RegisterGuiOverlaysEvent event) {
            event.registerBelowAll("point", PointOverlay.POINT_HUD);
        }
    }

    @Mod.EventBusSubscriber(modid = ModLockingMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if(KeyBinding.LOCK_SREEN_KEY.consumeClick()) {
                ModPackets.sendToServer(new OpenLockingScreenC2SPacket());
            }
        }

        @SubscribeEvent
        public static void addUnlockTooltip(ItemTooltipEvent event) {
            Player player = event.getEntity();
            if (player != null) {
                List<ModTree> unlockTrees = ModTrees.getUnlockTrees(player);
                unlockTrees.forEach((unlockTree -> {
                    Item item = event.getItemStack().getItem();
                    String restrictionCausedBy = Arrays.stream(Restriction.Type.values()).map(type -> unlockTree.restrictedBy(item, type)).filter(Objects::nonNull).findFirst().orElse(null);
                    if (restrictionCausedBy == null) {
                        return;
                    }
                    List<String> locks = Arrays.stream(restrictionCausedBy.split(", ")).toList();
                    Style textStyle = Style.EMPTY.withColor(TextColor.fromRgb(-5723992));
                    Style style = Style.EMPTY.withColor(TextColor.fromRgb(-203978));
                    MutableComponent text = Component.translatable("tooltip.requires_unlock");
                    text.setStyle(textStyle);
                    event.getToolTip().add(Component.empty());
                    event.getToolTip().add(text);
                    locks.forEach((lock -> {
                        MutableComponent name = Component.literal(" " + lock);
                        name.setStyle(style);
                        event.getToolTip().add(name);
                    }));
                }));
            }
        }
    }
}