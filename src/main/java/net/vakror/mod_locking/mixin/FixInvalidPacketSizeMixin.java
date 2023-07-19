package net.vakror.mod_locking.mixin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import io.netty.handler.codec.DecoderException;

@Mixin(NetworkHooks.class)
public class FixInvalidPacketSizeMixin {
    @Redirect(method = "openScreen(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/MenuProvider;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;readableBytes()I", ordinal = 1))
    private static int fixPacketSize(FriendlyByteBuf instance) {
        return 1;
    }
}
