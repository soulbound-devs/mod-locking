package net.vakror.mod_locking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.point.ModPoint;
import net.vakror.mod_locking.mod.util.CodecUtils;
import net.vakror.mod_locking.screen.ModUnlockingScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncPlayerPointsS2CPacket {
    private final Map<String, Integer> playerPoints;
    private final SoundEvent soundEvent;

    public SyncPlayerPointsS2CPacket(Map<String, Integer> playerPoints, SoundEvent event) {
        this.playerPoints = playerPoints;
        this.soundEvent = event;
    }

    public SyncPlayerPointsS2CPacket(FriendlyByteBuf buf) {
        playerPoints = new HashMap<>();
        CompoundTag nbt = buf.readNbt();
        playerPoints.putAll(CodecUtils.POINT_MAP_CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("points")).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
        if (nbt.contains("soundEvent")) {
            soundEvent = SoundEvent.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("soundEvent")).resultOrPartial((error) -> {}).get();
        } else {
            soundEvent = null;
        }
    }

    public void encode(FriendlyByteBuf buf) {
        CompoundTag nbt = new CompoundTag();
        nbt.put("points", CodecUtils.POINT_MAP_CODEC.encodeStart(NbtOps.INSTANCE, playerPoints).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
        if (soundEvent != null) {
            nbt.put("soundEvent", SoundEvent.CODEC.encodeStart(NbtOps.INSTANCE, soundEvent).resultOrPartial((error) -> {}).get());
        }
        buf.writeNbt(nbt);
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.getCapability(ModTreeProvider.MOD_TREE).ifPresent((modTreeCapability -> {
                modTreeCapability.setPoints(playerPoints);
            }));
            if (Minecraft.getInstance().screen instanceof ModUnlockingScreen unlockingScreen) {
                unlockingScreen.getMenu().setPlayerPoints(playerPoints);
            }
            Minecraft.getInstance().player.playSound(soundEvent, 15, 1);
        });
        return true;
    }
}

