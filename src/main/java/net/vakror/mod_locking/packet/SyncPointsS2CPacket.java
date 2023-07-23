package net.vakror.mod_locking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.point.ModPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SyncPointsS2CPacket {
    private final List<ModPoint> points;

    public SyncPointsS2CPacket(List<ModPoint> points) {
        this.points = points;
    }

    public SyncPointsS2CPacket(FriendlyByteBuf buf) {
        CompoundTag nbt = buf.readNbt();
        CompoundTag pointTag = nbt.getCompound("points");
        points = new ArrayList<>();
        for (String key : pointTag.getAllKeys()) {
            points.add(ModPoint.CODEC.parse(NbtOps.INSTANCE, pointTag.getCompound(key)).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
        }
    }

    public void encode(FriendlyByteBuf buf) {
        CompoundTag nbt = new CompoundTag();
        CompoundTag pointTag = new CompoundTag();
        for (ModPoint point : points) {
            pointTag.put(point.name, ModPoint.CODEC.encodeStart(NbtOps.INSTANCE, point).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
        }
        nbt.put("points", pointTag);
        buf.writeNbt(nbt);
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            assert Minecraft.getInstance().player != null;

            ModConfigs.POINTS.points.clear();
            ModConfigs.POINTS.points.addAll(points);
        });
        return true;
    }
}

