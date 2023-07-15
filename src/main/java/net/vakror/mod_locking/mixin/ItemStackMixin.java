package net.vakror.mod_locking.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.vakror.mod_locking.locking.Restriction;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.tree.ModTrees;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Redirect(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;onItemTooltip(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;)Lnet/minecraftforge/event/entity/player/ItemTooltipEvent;"))
    public ItemTooltipEvent addCustomTooltips(ItemStack itemStack, @Nullable Player player, List<Component> tooltip, TooltipFlag flags) {
        if (player != null) {
            List<ModTree> unlockTrees = ModTrees.getUnlockTrees(player);
            unlockTrees.forEach((unlockTree -> {
                Item item = itemStack.getItem();
                String restrictionCausedBy = Arrays.stream(Restriction.Type.values()).map(type -> unlockTree.restrictedBy(item, type)).filter(Objects::nonNull).findFirst().orElse(null);
                if (restrictionCausedBy == null) {
                    if (itemStack.getItem() instanceof BlockItem blockItem) {
                        restrictionCausedBy = Arrays.stream(Restriction.Type.values()).map(type -> unlockTree.restrictedBy(blockItem.getBlock(), type)).filter(Objects::nonNull).findFirst().orElse(null);
                    }
                    if (restrictionCausedBy == null) {
                        return;
                    }
                }
                Style textStyle = Style.EMPTY.withColor(TextColor.fromRgb(-5723992));
                Style style = Style.EMPTY.withColor(TextColor.fromRgb(-203978));
                MutableComponent text = Component.translatable("tooltip.requires_unlock");
                MutableComponent name = Component.literal(" " + restrictionCausedBy);
                text.setStyle(textStyle);
                name.setStyle(style);
                tooltip.add(Component.empty());
                tooltip.add(text);
                tooltip.add(name);
            }));
        }
        return ForgeEventFactory.onItemTooltip(itemStack, player, tooltip, flags);
    }
}
