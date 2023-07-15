package net.vakror.mod_locking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.mod_locking.mod.capability.ModTreeCapability;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.point.IntColorPoint;
import net.vakror.mod_locking.mod.point.ModPoint;
import net.vakror.mod_locking.mod.util.NbtUtil;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

public class SyncPointsS2CPacket {

    private final Map<String, Integer> pointAmounts;
    private final Map<String, String > pointPluralNames;
    private final Map<String, Integer> pointColors;

    public SyncPointsS2CPacket(Map<String, Integer> pointAmounts, Map<String, String> pointPluralNames, Map<String, Integer> pointColors) {
        this.pointAmounts = pointAmounts;
        this.pointPluralNames = pointPluralNames;
        this.pointColors = pointColors;
    }

    public SyncPointsS2CPacket(FriendlyByteBuf buf) {
        CompoundTag nbt = buf.readNbt();
        pointAmounts = NbtUtil.deserializePoints(nbt);
        pointColors = NbtUtil.deserializePointColors(nbt);
        pointPluralNames = NbtUtil.deserializePointPluralNames(nbt);
    }

    public void encode(FriendlyByteBuf buf) {
        CompoundTag nbt = new CompoundTag();
        NbtUtil.serializePoints(nbt, pointAmounts);
        buf.writeNbt(nbt);
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.getCapability(ModTreeProvider.MOD_TREE).ifPresent((modTreeCapability -> {
                modTreeCapability.setPoints(pointAmounts, null);
            }));

            ModConfigs.POINTS.points = new ArrayList<>();
            pointAmounts.forEach((name, amount) -> {
                ModConfigs.POINTS.points.add(new IntColorPoint(name, pointColors.get(name), pointPluralNames.get(name)));
            });
        });
        return true;
    }
}

