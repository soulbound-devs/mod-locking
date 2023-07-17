package net.vakror.mod_locking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.util.NbtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SyncModTreesS2CPacket {

    private final List<ModTree> trees;

    public SyncModTreesS2CPacket(List<ModTree> trees) {
        this.trees = trees;
    }

    public SyncModTreesS2CPacket(FriendlyByteBuf buf) {
        CompoundTag nbt = buf.readNbt();
        assert nbt != null;
        trees = NbtUtil.deserializeTrees(nbt);
    }

    public void encode(FriendlyByteBuf buf) {
        CompoundTag nbt = new CompoundTag();
        NbtUtil.serializeTrees(nbt, trees);
        buf.writeNbt(nbt);
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.getCapability(ModTreeProvider.MOD_TREE).ifPresent((modTreeCapability -> {
                modTreeCapability.setTrees(trees);
            }));

            ModConfigs.TREES.trees = new ArrayList<>();
            ModConfigs.TREES.trees.addAll(trees);
        });
        return true;
    }
}

