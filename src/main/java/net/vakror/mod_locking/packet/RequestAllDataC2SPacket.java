package net.vakror.mod_locking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.mod_locking.mod.config.configs.ModPointsConfig;
import net.vakror.mod_locking.mod.config.configs.ModTreesConfig;
import net.vakror.mod_locking.mod.config.configs.ModUnlocksConfig;
import net.vakror.mod_locking.mod.config.configs.UseItemPointObtainConfig;

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
            ModPointsConfig.INSTANCE.points.forEach((point -> {
                points.put(point.name, 0);
            }));
            Map<String, Integer> pointColors = new HashMap<>();
            ModPointsConfig.INSTANCE.points.forEach((point -> {
                pointColors.put(point.name, point.getColor());
            }));
            Map<String, String> pointPluralNames = new HashMap<>();
            ModPointsConfig.INSTANCE.points.forEach((point -> {
                pointPluralNames.put(point.name, point.pluralName);
            }));
            ModPackets.sendToClient(new SyncAllDataS2CPacket(ModPointsConfig.INSTANCE.points, ModUnlocksConfig.INSTANCE.getAll(), ModTreesConfig.INSTANCE.trees, UseItemPointObtainConfig.INSTANCE.getAll(), false), context.getSender());
        });
        return true;
    }
}

