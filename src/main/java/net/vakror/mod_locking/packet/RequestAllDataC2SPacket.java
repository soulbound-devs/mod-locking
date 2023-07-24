package net.vakror.mod_locking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.mod_locking.mod.config.ModConfigs;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RequestAllDataC2SPacket {


    public RequestAllDataC2SPacket() {
    }

    public RequestAllDataC2SPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            Map<String, Integer> points = new HashMap<>();
            ModConfigs.POINTS.points.forEach((point -> {
                points.put(point.name, 0);
            }));
            Map<String, Integer> pointColors = new HashMap<>();
            ModConfigs.POINTS.points.forEach((point -> {
                pointColors.put(point.name, point.getColor());
            }));
            Map<String, String> pointPluralNames = new HashMap<>();
            ModConfigs.POINTS.points.forEach((point -> {
                pointPluralNames.put(point.name, point.pluralName);
            }));
            ModPackets.sendToClient(new SyncAllDataS2CPacket(ModConfigs.POINTS.points, ModConfigs.UNLOCKS.getAll(), ModConfigs.TREES.trees, ModConfigs.POINT_OBTAIN_METHODS.getAll(), false), context.getSender());
        });
        return true;
    }
}

