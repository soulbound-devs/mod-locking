package net.vakror.mod_locking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.mod_locking.mod.capability.ModTreeCapability;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.screen.UnlockingUtils;

import java.util.List;
import java.util.function.Supplier;

public class OpenLockingScreenC2SPacket {


    public OpenLockingScreenC2SPacket() {
    }

    public OpenLockingScreenC2SPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> UnlockingUtils.openUnlockingGui(context.getSender()));
        return true;
    }
}

