package net.vakror.mod_locking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.point.ModPoint;
import net.vakror.mod_locking.mod.point.obtain.PointObtainMethod;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.unlock.FineGrainedModUnlock;
import net.vakror.mod_locking.mod.unlock.ModUnlock;
import net.vakror.mod_locking.mod.unlock.Unlock;
import net.vakror.mod_locking.mod.util.NbtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SyncAllDataS2CPacket {
    private final Map<String, Integer> pointAmounts;
    private final Map<String, String > pointPluralNames;
    private final Map<String, Integer> pointColors;
    private final List<Unlock<?>> unlocks;
    private final List<ModTree> trees;

    public SyncAllDataS2CPacket(Map<String, Integer> pointAmounts, Map<String, String> pointPluralNames, Map<String, Integer> pointColors, List<Unlock<?>> unlocks, List<ModTree> trees) {
        this.pointAmounts = pointAmounts;
        this.pointPluralNames = pointPluralNames;
        this.pointColors = pointColors;
        this.unlocks = unlocks;
        this.trees = trees;
    }

    public SyncAllDataS2CPacket(FriendlyByteBuf buf) {
        CompoundTag nbt = buf.readNbt();
        pointAmounts = NbtUtil.deserializePoints(nbt);
        pointColors = NbtUtil.deserializePointColors(nbt);
        pointPluralNames = NbtUtil.deserializePointPluralNames(nbt);
        unlocks = NbtUtil.deserializeUnlocks(nbt);
        trees = NbtUtil.deserializeTrees(nbt);
    }

    public void encode(FriendlyByteBuf buf) {
        CompoundTag nbt = new CompoundTag();
        NbtUtil.serializePoints(nbt, pointAmounts);
        NbtUtil.serializeUnlocks(nbt, unlocks);
        NbtUtil.serializeTrees(nbt, trees);
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
            ModConfigs.UNLOCKS.modUnlocks = new ArrayList<>();
            ModConfigs.UNLOCKS.fineGrainedUnlocks = new ArrayList<>();
            ModConfigs.TREES.trees = trees;
            pointAmounts.forEach((name, amount) -> ModConfigs.POINTS.points.add(new ModPoint(name, pointPluralNames.get(name), decode(pointColors.get(name))[0], decode(pointColors.get(name))[1], decode(pointColors.get(name))[2])));
            this.unlocks.forEach((unlock -> {
                if (unlock instanceof FineGrainedModUnlock fineGrainedModUnlock) {
                    ModConfigs.UNLOCKS.fineGrainedUnlocks.add(fineGrainedModUnlock);
                } else if (unlock instanceof ModUnlock modUnlock) {
                    ModConfigs.UNLOCKS.modUnlocks.add(modUnlock);
                }
            }));
        });
        return true;
    }

    public static int[] decode(int color) {
        int[] rgb = new int[4];
        rgb[3] = (color >> 24) & 0xff; // or color >>> 24
        rgb[0] = (color >> 16) & 0xff;
        rgb[1] = (color >>  8) & 0xff;
        rgb[2] = (color      ) & 0xff;
        return rgb;
    }
}
