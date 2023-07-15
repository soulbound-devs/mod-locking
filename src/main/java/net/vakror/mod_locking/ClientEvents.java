package net.vakror.mod_locking;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.vakror.mod_locking.packet.ModPackets;
import net.vakror.mod_locking.packet.OpenLockingScreenC2SPacket;
import net.vakror.mod_locking.screen.ModMenuTypes;
import net.vakror.mod_locking.screen.ModUnlockingScreen;

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
    }
}