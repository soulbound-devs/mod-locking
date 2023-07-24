package net.vakror.mod_locking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.point.ModPoint;
import net.vakror.mod_locking.mod.point.obtain.KillEntityObtainMethod;
import net.vakror.mod_locking.mod.point.obtain.PointObtainMethod;
import net.vakror.mod_locking.mod.point.obtain.RightClickItemObtainMethod;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.unlock.FineGrainedModUnlock;
import net.vakror.mod_locking.mod.unlock.ModUnlock;
import net.vakror.mod_locking.mod.unlock.Unlock;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SyncAllDataS2CPacket {
    private final List<ModPoint> points;
    private final List<Unlock<?>> unlocks;
    private final List<PointObtainMethod> pointObtainMethods;
    private final List<ModTree> trees;
    private final boolean reloaded;

    public SyncAllDataS2CPacket(List<ModPoint> points, List<Unlock<?>> unlocks, List<ModTree> trees, List<PointObtainMethod> pointObtainMethods, boolean reloaded) {
        this.points = points;
        this.unlocks = unlocks;
        this.trees = trees;
        this.pointObtainMethods = pointObtainMethods;
        this.reloaded = reloaded;
    }

    public SyncAllDataS2CPacket(FriendlyByteBuf buf) {
        CompoundTag nbt = buf.readNbt().getCompound("data");
        CompoundTag pointTag = nbt.getCompound("points");
        points = new ArrayList<>();
        for (String key : pointTag.getAllKeys()) {
            points.add(ModPoint.CODEC.parse(NbtOps.INSTANCE, pointTag.getCompound(key)).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
        }
        CompoundTag treeTag = nbt.getCompound("trees");
        trees = new ArrayList<>();
        for (String key : treeTag.getAllKeys()) {
            trees.add(ModTree.CODEC.parse(NbtOps.INSTANCE, treeTag.getCompound(key)).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
        }
        CompoundTag unlockTag = nbt.getCompound("unlocks");
        unlocks = new ArrayList<>();
        for (String key : unlockTag.getAllKeys()) {
            if (unlockTag.getCompound(key).getBoolean("isMod")) {
                unlocks.add(ModUnlock.CODEC.parse(NbtOps.INSTANCE, unlockTag.getCompound(key)).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
            } else {
                unlocks.add(FineGrainedModUnlock.CODEC.parse(NbtOps.INSTANCE, unlockTag.getCompound(key)).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
            }
        }
        CompoundTag obtainMethodTag = nbt.getCompound("obtainMethods");
        pointObtainMethods = new ArrayList<>();
        for (String key: obtainMethodTag.getAllKeys()) {
            if (obtainMethodTag.getCompound(key).getString("type").equals("useItem")) {
                pointObtainMethods.add(RightClickItemObtainMethod.CODEC.parse(NbtOps.INSTANCE, obtainMethodTag.getCompound(key)).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
            } else if (obtainMethodTag.getCompound(key).getString("type").equals("killEntity")) {
                pointObtainMethods.add(KillEntityObtainMethod.CODEC.parse(NbtOps.INSTANCE, obtainMethodTag.getCompound(key)).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
            }
        }
        reloaded = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        CompoundTag nbt = new CompoundTag();
        CompoundTag tag = new CompoundTag();
        CompoundTag pointTag = new CompoundTag();
        CompoundTag treeTag = new CompoundTag();
        CompoundTag unlockTag = new CompoundTag();
        CompoundTag obtainTag = new CompoundTag();

        for (ModPoint point : points) {
            pointTag.put(point.name, ModPoint.CODEC.encodeStart(NbtOps.INSTANCE, point).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
        }

        for (ModTree tree : trees) {
            treeTag.put(tree.name, ModTree.CODEC.encodeStart(NbtOps.INSTANCE, tree).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
        }

        for (Unlock<?> unlock : unlocks) {
            if (unlock instanceof ModUnlock) {
                unlockTag.put(unlock.getName(), ModUnlock.CODEC.encodeStart(NbtOps.INSTANCE, (ModUnlock) unlock).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
                unlockTag.getCompound(unlock.getName()).putBoolean("isMod", true);
            }
            if (unlock instanceof FineGrainedModUnlock) {
                unlockTag.put(unlock.getName(), FineGrainedModUnlock.CODEC.encodeStart(NbtOps.INSTANCE, (FineGrainedModUnlock) unlock).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
                unlockTag.getCompound(unlock.getName()).putBoolean("isMod", false);
            }
        }

        int i = 0;
        for (PointObtainMethod method: pointObtainMethods) {
            if (method instanceof KillEntityObtainMethod killEntityObtainMethod) {
                obtainTag.put("killEntityMethod" + i, KillEntityObtainMethod.CODEC.encodeStart(NbtOps.INSTANCE, killEntityObtainMethod).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
            } else if (method instanceof RightClickItemObtainMethod rightClickItemObtainMethod){
                obtainTag.put("useItemMethod" + i, RightClickItemObtainMethod.CODEC.encodeStart(NbtOps.INSTANCE, rightClickItemObtainMethod).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
            }
            i++;
        }

        nbt.put("points", pointTag);
        nbt.put("trees", treeTag);
        nbt.put("unlocks", unlockTag);
        nbt.put("obtainMethods", obtainTag);
        tag.put("data", nbt);
        buf.writeNbt(tag);
        buf.writeBoolean(reloaded);
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            assert Minecraft.getInstance().player != null;

            ModConfigs.POINTS.points.clear();
            ModConfigs.POINTS.points.addAll(points);
            ModConfigs.UNLOCKS.modUnlocks = new ArrayList<>();
            ModConfigs.UNLOCKS.fineGrainedUnlocks = new ArrayList<>();
            ModConfigs.TREES.trees = trees;
            ModConfigs.POINT_OBTAIN_METHODS.killEntityObtainMethods.clear();
            ModConfigs.POINT_OBTAIN_METHODS.useItemObtainMethods.clear();
            for (PointObtainMethod pointObtainMethod : pointObtainMethods) {
                if (pointObtainMethod instanceof KillEntityObtainMethod) {
                    ModConfigs.POINT_OBTAIN_METHODS.killEntityObtainMethods.add((KillEntityObtainMethod) pointObtainMethod);
                }
                if (pointObtainMethod instanceof RightClickItemObtainMethod) {
                    ModConfigs.POINT_OBTAIN_METHODS.useItemObtainMethods.add((RightClickItemObtainMethod) pointObtainMethod);
                }
            }
            this.unlocks.forEach((unlock -> {
                if (unlock instanceof FineGrainedModUnlock fineGrainedModUnlock) {
                    ModConfigs.UNLOCKS.fineGrainedUnlocks.add(fineGrainedModUnlock);
                } else if (unlock instanceof ModUnlock modUnlock) {
                    ModConfigs.UNLOCKS.modUnlocks.add(modUnlock);
                }
            }));
            if (reloaded) {
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Successfully Reloaded All Mod Locks!"));
            }
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
