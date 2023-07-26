package net.vakror.mod_locking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.tree.ModTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class RequestPlayerTreesC2SPacket {


    public RequestPlayerTreesC2SPacket() {
    }

    public RequestPlayerTreesC2SPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            AtomicReference<List<ModTree>> trees = new AtomicReference<>(new ArrayList<>());
            context.getSender().getCapability(ModTreeProvider.MOD_TREE).ifPresent((modTreeCapability -> {
                trees.set(modTreeCapability.getTrees());
            }));
            ModPackets.sendToClient(new SyncPlayerTreesS2CPacket(trees.get()), context.getSender());
        });
        return true;
    }
}

