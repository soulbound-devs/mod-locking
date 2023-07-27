package net.vakror.mod_locking.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.vakror.mod_locking.locking.Restriction;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.point.obtain.RightClickItemObtainMethod;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.tree.ModTrees;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Shadow
    public abstract Item asItem();

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void addPointsOnUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        for (RightClickItemObtainMethod useItemObtainMethod : ModConfigs.USE_ITEM_POINT_OBTAIN_METHODS.useItemObtainMethods) {
            if (useItemObtainMethod.itemId.equals(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(player.getItemInHand(hand).getItem())).toString())) {
                if (player.getItemInHand(hand).getCount() > 0) {
                    player.getCapability(ModTreeProvider.MOD_TREE).ifPresent((modTreeCapability -> {
                        if (!level.isClientSide) {
                            modTreeCapability.addPoint(useItemObtainMethod.getPointType(), useItemObtainMethod.getAmount(), (ServerPlayer) player, null);
                            ItemStack newStack = player.getItemInHand(hand);
                            newStack.shrink(1);
                            cir.setReturnValue(InteractionResultHolder.consume(newStack));
                        } else {
                            if (useItemObtainMethod.getObtainSound().isPresent()) {
                                player.playSound(useItemObtainMethod.getObtainSound().get(), 15, 1);
                            }
                        }
                    }));
                }
            }
        }
    }
}