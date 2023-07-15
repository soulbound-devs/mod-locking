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
    @Shadow public abstract Item asItem();

    @Inject(method = "use", at = @At("HEAD"))
    public void addPointsOnUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (!level.isClientSide) {
            ModConfigs.POINTS.points.forEach((point -> {
                if (point.pointObtainMethod.getName().equals("rightClickItem")) {
                    RightClickItemObtainMethod method = (RightClickItemObtainMethod) point.pointObtainMethod;
                    Objects.equals(ForgeRegistries.ITEMS.getValue(new ResourceLocation(method.itemId)), this.asItem());
                    if ((Objects.equals(ForgeRegistries.ITEMS.getValue(new ResourceLocation(method.itemId)), this.asItem()))) {
                        player.getCapability(ModTreeProvider.MOD_TREE).ifPresent((modTreeCapability -> {
                            modTreeCapability.addPoint(point.name, method.getAmount() == 0 ? 1 : method.getAmount(), player);
                        }));
                    }
                }
            }));
        }
    }
}
