package net.vakror.mod_locking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.mod_locking.mod.capability.ModTreeCapability;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.screen.UnlockingUtils;

import java.util.function.Supplier;

public class RequestPointUpdateC2SPacket {


    public RequestPointUpdateC2SPacket() {
    }

    public RequestPointUpdateC2SPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            assert context.getSender() != null;
            ModTreeCapability capability = context.getSender().getCapability(ModTreeProvider.MOD_TREE).orElse(new ModTreeCapability());
            ModPackets.sendToClient(new SyncPointsS2CPacket(capability.getPoints(), capability.getPointPluralNames(), capability.getPointColors()), context.getSender());
        });
        return true;
    }
}

