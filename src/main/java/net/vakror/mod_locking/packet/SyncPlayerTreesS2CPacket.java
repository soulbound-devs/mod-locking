package net.vakror.mod_locking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.unlock.Unlock;
import net.vakror.mod_locking.mod.util.CodecUtils;
import net.vakror.mod_locking.screen.ModUnlockingScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SyncPlayerTreesS2CPacket {
    private final List<ModTree> playerTrees;
    private final boolean reasonUnlocked;
    private final String unlockName;

    public SyncPlayerTreesS2CPacket(List<ModTree> playerTrees, boolean reasonUnlocked, String unlockName) {
        this.playerTrees = playerTrees;
        this.reasonUnlocked = reasonUnlocked;
        this.unlockName = unlockName;
    }

    public SyncPlayerTreesS2CPacket(FriendlyByteBuf buf) {
        playerTrees = new ArrayList<>();
        CompoundTag treeTag = buf.readNbt().getCompound("trees");
        for (String key : treeTag.getAllKeys()) {
            playerTrees.add(ModTree.CODEC_WITH_MODS_UNLOCKED.parse(NbtOps.INSTANCE, treeTag.getCompound(key)).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
        }
        this.reasonUnlocked = buf.readBoolean();
        this.unlockName = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        CompoundTag nbt = new CompoundTag();
        CompoundTag treeTag = new CompoundTag();
        for (ModTree tree : playerTrees) {
            treeTag.put(tree.name, ModTree.CODEC_WITH_MODS_UNLOCKED.encodeStart(NbtOps.INSTANCE, tree).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
        }
        nbt.put("trees", treeTag);
        buf.writeNbt(nbt);
        buf.writeBoolean(reasonUnlocked);
        buf.writeUtf(unlockName == null ? "": unlockName);
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.getCapability(ModTreeProvider.MOD_TREE).ifPresent((modTreeCapability -> {
                modTreeCapability.setTrees(playerTrees);
            }));
            if (Minecraft.getInstance().screen instanceof ModUnlockingScreen unlockingScreen) {
                unlockingScreen.setPlayerTrees(playerTrees);
            }
            if (reasonUnlocked) {
                for (Unlock<?> unlock : ModConfigs.MOD_UNLOCKS.getAll()) {
                    if (unlock.getName().equals(unlockName) && unlock.getSound().isPresent()) {
                        Minecraft.getInstance().player.playSound(unlock.getSound().get(), 15,1);
                    }
                }
            }
        });
        return true;
    }
}

