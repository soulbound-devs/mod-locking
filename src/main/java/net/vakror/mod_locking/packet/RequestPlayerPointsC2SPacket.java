package net.vakror.mod_locking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.config.ModConfigs;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class RequestPlayerPointsC2SPacket {


    public RequestPlayerPointsC2SPacket() {
    }

    public RequestPlayerPointsC2SPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            AtomicReference<Map<String, Integer>> points = new AtomicReference<>(new HashMap<>());
            ModConfigs.POINTS.points.forEach((point -> {
                context.getSender().getCapability(ModTreeProvider.MOD_TREE).ifPresent((modTreeCapability -> {
                    points.set(modTreeCapability.getPoints());
                }));
            }));
            ModPackets.sendToClient(new SyncPlayerPointsS2CPacket(points.get()), context.getSender());
        });
        return true;
    }
}

