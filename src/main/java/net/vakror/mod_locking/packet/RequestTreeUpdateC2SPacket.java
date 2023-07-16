package net.vakror.mod_locking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.mod_locking.mod.capability.ModTreeCapability;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.config.ModConfigs;

import java.util.function.Supplier;

public class RequestTreeUpdateC2SPacket {


    public RequestTreeUpdateC2SPacket() {
    }

    public RequestTreeUpdateC2SPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            ModPackets.sendToClient(new SyncModTreesS2CPacket(ModConfigs.TREES.trees), context.getSender());
        });
        return true;
    }
}

