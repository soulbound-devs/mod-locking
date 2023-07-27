package net.vakror.mod_locking;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.command.ConfigCommand;
import net.vakror.mod_locking.command.ModLockingCommands;
import net.vakror.mod_locking.locking.Restriction;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.tree.ModTrees;
import net.vakror.mod_locking.mod.capability.ModTreeCapability;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.packet.ModPackets;
import net.vakror.mod_locking.packet.RequestAllDataC2SPacket;
import net.vakror.mod_locking.packet.RequestPlayerPointsC2SPacket;
import net.vakror.mod_locking.packet.RequestPlayerTreesC2SPacket;

import java.util.List;

public class Events {
    public static void registerCapabilitiesEvent(RegisterCapabilitiesEvent event) {
        event.register(ModTreeCapability.class);
    }

    public static void attachTreeCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(ModLockingMod.MOD_ID, "mod-tree"), new ModTreeProvider());
        }
    }

    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        if (!event.isCancelable()) {
            return;
        }
        Player player = event.getEntity();
        if (player.isCreative()) {
            return;
        }
        List<ModTree> unlockTrees = ModTrees.getUnlockTrees(player);
        unlockTrees.forEach((unlockTree -> {
            String restrictedBy = unlockTree.restrictedBy(event.getItemStack().getItem(), Restriction.Type.USABILITY);
            if (restrictedBy == null) {
                return;
            }
            if (event.getSide() == LogicalSide.CLIENT) {
                warnPlayerNeedsUnlock(restrictedBy, "usage");
            }
            event.setCanceled(true);
        }));
    }

    public static void onPlayerLogIn(ClientPlayerNetworkEvent.LoggingIn event) {
        ModPackets.sendToServer(new RequestAllDataC2SPacket());
        ModPackets.sendToServer(new RequestPlayerPointsC2SPacket());
        ModPackets.sendToServer(new RequestPlayerTreesC2SPacket());
    }

    public static void onBlockInteraction(PlayerInteractEvent.RightClickBlock event) {
        if (!event.isCancelable()) {
            return;
        }
        Player player = event.getEntity();
        if (player.isCreative()) {
            return;
        }
        List<ModTree> unlockTrees = ModTrees.getUnlockTrees(player);
        unlockTrees.forEach((tree -> {
            String restrictedBy = tree.restrictedBy((player.level.getBlockState(event.getPos())).getBlock(), Restriction.Type.BLOCK_INTERACTABILITY);
            if (restrictedBy != null) {
                if (event.getSide() == LogicalSide.CLIENT) {
                    warnPlayerNeedsUnlock(restrictedBy, "interact_block");
                }
                event.setCanceled(true);
                return;
            }
            ItemStack itemStack = event.getItemStack();
            if (itemStack == ItemStack.EMPTY) {
                return;
            }
            Item item = itemStack.getItem();
            restrictedBy = tree.restrictedBy(item, Restriction.Type.USABILITY);
            if (restrictedBy != null) {
                if (event.getSide() == LogicalSide.CLIENT) {
                    warnPlayerNeedsUnlock(restrictedBy, "usage");
                }
                event.setCanceled(true);
            }
        }));
    }

    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(ModTreeProvider.MOD_TREE).ifPresent(oldStore -> {
                event.getOriginal().getCapability(ModTreeProvider.MOD_TREE).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new ModLockingCommands.ReloadModLocksCommand(event.getDispatcher());
        new ModLockingCommands.ReloadPlayerTreeCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }


    public static void onBlockHit(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        if (player.isCreative()) {
            return;
        }
        List<ModTree> modTrees = ModTrees.getUnlockTrees(player);
        modTrees.forEach((modTree -> {
            String restrictedBy = modTree.restrictedBy((player.level.getBlockState(event.getPos())).getBlock(), Restriction.Type.HITTABILITY);
            if (restrictedBy != null) {
                if (event.getLevel().isClientSide()) {
                    warnPlayerNeedsUnlock(restrictedBy, "hit");
                }
                event.setCanceled(true);
                return;
            }
            ItemStack itemStack = event.getItemStack();
            if (itemStack == ItemStack.EMPTY) {
                return;
            }
            Item item = itemStack.getItem();
            restrictedBy = modTree.restrictedBy(item, Restriction.Type.USABILITY);
            if (restrictedBy != null) {
                if (event.getLevel().isClientSide()) {
                    warnPlayerNeedsUnlock(restrictedBy, "usage");
                }
                event.setCanceled(true);
            }
        }));
    }

    public static void onEntityInteraction(PlayerInteractEvent.EntityInteract event) {
        if (!event.isCancelable()) {
            return;
        }
        Player player = event.getEntity();
        if (player.isCreative()) {
            return;
        }
        List<ModTree> unlockTrees = ModTrees.getUnlockTrees(player);
        unlockTrees.forEach((modTree -> {
            String restrictedBy = modTree.restrictedBy((event.getEntity()).getType(), Restriction.Type.ENTITY_INTERACTABILITY);
            if (restrictedBy != null) {
                if (event.getSide() == LogicalSide.CLIENT) {
                    warnPlayerNeedsUnlock(restrictedBy, "interact_entity");
                }
                event.setCanceled(true);
                return;
            }
            ItemStack itemStack = event.getItemStack();
            if (itemStack == ItemStack.EMPTY) {
                return;
            }
            Item item = itemStack.getItem();
            restrictedBy = modTree.restrictedBy(item, Restriction.Type.USABILITY);
            if (restrictedBy != null) {
                if (event.getSide() == LogicalSide.CLIENT) {
                    warnPlayerNeedsUnlock(restrictedBy, "usage");
                }
                event.setCanceled(true);
            }
        }));
    }

    public static void onEntityDeath(LivingDeathEvent event) {
        ModConfigs.KILL_ENTITY_POINT_OBTAIN_METHODS.killEntityObtainMethods.forEach((killEntityObtainMethod -> {
            if (event.getEntity().getType().equals(ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(killEntityObtainMethod.entityId)))) {
                if (event.getSource().getEntity() instanceof Player player) {
                    player.getCapability(ModTreeProvider.MOD_TREE).ifPresent((modTreeCapability -> modTreeCapability.addPoint(killEntityObtainMethod.getPointType(), killEntityObtainMethod.getAmount(), (ServerPlayer) player, killEntityObtainMethod.getObtainSound().orElse(null))));
                }
            }
        }));
    }

    public static void onPlayerAttack(AttackEntityEvent event) {
        if (!event.isCancelable()) {
            return;
        }
        Player player = event.getEntity();
        if (player.isCreative()) {
            return;
        }
        List<ModTree> unlockTrees = ModTrees.getUnlockTrees(player);
        unlockTrees.forEach((unlockTree -> {
            String restrictedBy = unlockTree.restrictedBy((event.getEntity()).getType(), Restriction.Type.HITTABILITY);
            if (restrictedBy != null) {
                if (player.level.isClientSide) {
                    warnPlayerNeedsUnlock(restrictedBy, "hit_entity");
                }
                event.setCanceled(true);
                return;
            }
            ItemStack itemStack = player.getMainHandItem();
            if (itemStack == ItemStack.EMPTY) {
                return;
            }
            Item item = itemStack.getItem();
            restrictedBy = unlockTree.restrictedBy(item, Restriction.Type.USABILITY);
            if (restrictedBy != null) {
                if (player.level.isClientSide) {
                    warnPlayerNeedsUnlock(restrictedBy, "usage");
                }
                event.setCanceled(true);
            }
        }));
    }

    private static void warnPlayerNeedsUnlock(String researchName, String i18nKey) {
        MutableComponent name = Component.literal(researchName);
        Style style = Style.EMPTY.withColor(TextColor.fromRgb(-203978));
        name.setStyle(style);
        MutableComponent text = Component.translatable("overlay.requires_unlock." + i18nKey, name);
        Minecraft.getInstance().gui.setOverlayMessage(text, false);
    }
}
