package net.vakror.mod_locking.mixin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.PlayMessages;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayMessages.OpenContainer.class)
public class PlayMessagesMixin {
    @Redirect(method = "decode", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;readByteArray(I)[B"))
    private static byte[] fixMaxPacketSize(FriendlyByteBuf instance, int max) {
        return instance.readByteArray(Integer.MAX_VALUE);
    }
}
