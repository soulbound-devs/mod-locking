package net.vakror.mod_locking.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.vakror.mod_locking.locking.Restriction;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.tree.ModTrees;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Slot.class)
public abstract class SlotMixin {
    @Shadow
    public abstract boolean hasItem();

    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "mayPickup", at = @At(value = "HEAD"), cancellable = true)
    public void restrictCraftingLockedItems(Player player, CallbackInfoReturnable<Boolean> cir) {
        Slot thisSlot = (Slot) (Object) this;
        if (!(thisSlot instanceof ResultSlot)) {
            return;
        }
        if (!this.hasItem()) {
            return;
        }
        ItemStack resultStack = this.getItem();
        List<ModTree> unlockTrees = ModTrees.getUnlockTrees(player);
        unlockTrees.forEach((unlockTree -> {
            String restrictedBy = unlockTree.restrictedBy(resultStack.getItem(), Restriction.Type.CRAFTABILITY);
            if (restrictedBy != null) {
                cir.setReturnValue(false);
            }
        }));
    }
}
