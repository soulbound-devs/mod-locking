package net.vakror.mod_locking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.config.configs.ModPointsConfig;
import net.vakror.mod_locking.mod.point.ModPoint;
import net.vakror.mod_locking.mod.tree.ModTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class RequestPlayerTreesC2SPacket {
    private final boolean openScreen;

    public RequestPlayerTreesC2SPacket() {
        openScreen = false;
    }

    public RequestPlayerTreesC2SPacket(boolean openScreen) {
        this.openScreen = openScreen;
    }

    public RequestPlayerTreesC2SPacket(FriendlyByteBuf buf) {
        openScreen = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(openScreen);
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            AtomicReference<List<ModTree>> trees = new AtomicReference<>(new ArrayList<>());
            context.getSender().getCapability(ModTreeProvider.MOD_TREE).ifPresent((modTreeCapability -> {
                trees.set(modTreeCapability.getTrees());
            }));
            AtomicReference<Map<String, Integer>> points = new AtomicReference<>(new HashMap<>());
            context.getSender().getCapability(ModTreeProvider.MOD_TREE).ifPresent((modTreeCapability -> {
                points.set(modTreeCapability.getPoints());
            }));
            AtomicReference<Map<String, Integer>> pointColors = new AtomicReference<>(new HashMap<>());
            for (ModPoint point : ModPointsConfig.INSTANCE.points) {
                Map<String, Integer> newMap = pointColors.get();
                newMap.put(point.name, point.getColor());
                pointColors.set(newMap);
            }
            ModPackets.sendToClient(new SyncPlayerTreesS2CPacket(trees.get(), false, ""), context.getSender());
            ModPackets.sendToClient(new SyncPlayerPointsS2CPacket(points.get(), null), context.getSender());
            ModPackets.sendToClient(new SyncPlayerPointsS2CPacket(points.get(), null, true), context.getSender());
        });
        return true;
    }
}

