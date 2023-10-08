package net.vakror.mod_locking;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vakror.jamesconfig.config.adapter.SimpleCodecAdapter;
import net.vakror.jamesconfig.config.event.GetConfigTypeAdaptersEvent;
import net.vakror.mod_locking.mod.point.ModPoint;
import net.vakror.mod_locking.mod.point.obtain.KillEntityObtainMethod;
import net.vakror.mod_locking.mod.point.obtain.RightClickItemObtainMethod;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.unlock.FineGrainedModUnlock;
import net.vakror.mod_locking.mod.unlock.ModUnlock;
import net.vakror.mod_locking.packet.ModPackets;
import net.vakror.mod_locking.screen.ModMenuTypes;
import org.slf4j.Logger;

import static net.vakror.mod_locking.ModLockingMod.MOD_ID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
public class ModLockingMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "mod_locking";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public ModLockingMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addConfigAdapters);

        ModMenuTypes.register(modEventBus);

        MinecraftForge.EVENT_BUS.addListener(Events::onBlockHit);
        MinecraftForge.EVENT_BUS.addListener(Events::onEntityInteraction);
        MinecraftForge.EVENT_BUS.addListener(Events::onBlockInteraction);
        MinecraftForge.EVENT_BUS.addListener(Events::onItemUse);
        MinecraftForge.EVENT_BUS.addListener(Events::registerCapabilitiesEvent);
        MinecraftForge.EVENT_BUS.addListener(Events::onPlayerCloned);
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, Events::attachTreeCapabilityEvent);
        MinecraftForge.EVENT_BUS.addListener(Events::onPlayerAttack);
        MinecraftForge.EVENT_BUS.addListener(Events::onEntityDeath);
        MinecraftForge.EVENT_BUS.addListener(Events::onCommandsRegister);
        MinecraftForge.EVENT_BUS.addListener(Events::onPlayerLogIn);

        net.vakror.mod_locking.mod.config.configs.ModConfigs.addConfigs();
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void addConfigAdapters(GetConfigTypeAdaptersEvent event) {
        if (event.getConfigName().getNamespace().equals(MOD_ID)) {
            event.addAdapter(ModUnlock.class, new SimpleCodecAdapter<>(ModUnlock.CODEC));
            event.addAdapter(FineGrainedModUnlock.class, new SimpleCodecAdapter<>(FineGrainedModUnlock.CODEC));
            event.addAdapter(ModPoint.class, new SimpleCodecAdapter<>(ModPoint.CODEC));
            event.addAdapter(ModTree.class, new SimpleCodecAdapter<>(ModTree.CODEC));
            event.addAdapter(RightClickItemObtainMethod.class, new SimpleCodecAdapter<>(RightClickItemObtainMethod.CODEC));
            event.addAdapter(KillEntityObtainMethod.class, new SimpleCodecAdapter<>(KillEntityObtainMethod.CODEC));
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork((ModPackets::register));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
        }
    }
}
