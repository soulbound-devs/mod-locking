package net.vakror.mod_locking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.util.CodecUtils;
import net.vakror.mod_locking.screen.ModUnlockingScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncModWidgetDescriptionsS2CPacket {

    public SyncModWidgetDescriptionsS2CPacket() {
    }

    public SyncModWidgetDescriptionsS2CPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            assert Minecraft.getInstance().player != null;
            if (Minecraft.getInstance().screen instanceof ModUnlockingScreen unlockingScreen) {
                unlockingScreen.updateDescriptions();
            }
        });
        return true;
    }
}

