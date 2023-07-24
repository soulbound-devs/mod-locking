package net.vakror.mod_locking.mod.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.config.configs.ModPointsConfig;
import net.vakror.mod_locking.mod.point.ModPoint;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.packet.ModPackets;
import net.vakror.mod_locking.packet.RequestPlayerPointsC2SPacket;
import net.vakror.mod_locking.packet.SyncPlayerPointsS2CPacket;

import java.util.*;

public class ModTreeCapability {
    List<ModTree> trees = new ArrayList<>();

    Map<String, Integer> points = new HashMap<>();
    public void saveNBTData(CompoundTag nbt) {
        CompoundTag treeTag = new CompoundTag();
        for (ModTree tree: trees) {
            treeTag.put("tree_" + tree.name, ModTree.CODEC_WITH_MODS_UNLOCKED.encodeStart(NbtOps.INSTANCE, tree).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
        }
        nbt.put("points", ModPoint.MAP_CODEC.encodeStart(NbtOps.INSTANCE, points).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
        nbt.put("trees", treeTag);
    }

    public ModTreeCapability loadNBTData(CompoundTag nbt) {
        CompoundTag treeTag = nbt.getCompound("trees");
        CompoundTag pointTag = nbt.getCompound("points");
        treeTag.getAllKeys().forEach((key) -> trees.add(ModTree.CODEC_WITH_MODS_UNLOCKED.parse(NbtOps.INSTANCE, treeTag.getCompound(key)).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get()));
        points = new HashMap<>(ModPoint.MAP_CODEC.parse(NbtOps.INSTANCE, pointTag).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());

        return this;
    }


    public List<ModTree> getTrees() {
        if (trees == null) {
            trees = new ArrayList<>(ModConfigs.TREES.trees);
        }

        trees.removeIf(tree -> !ModConfigs.TREES.trees.contains(tree));
        return trees;
    }

    public Map<String, Integer> getPoints() {
        return points;
    }

    public Map<String, Integer> getPointColors() {
        Map<String, Integer> colors = new HashMap<>(points.size());
        points.forEach((name, amount) -> {
            colors.put(name, ModPointsConfig.getPoint(name).getColor());
        });
        return colors;
    }

    public Map<String, String> getPointPluralNames() {
        Map<String, String> colors = new HashMap<>(points.size());
        points.forEach((name, amount) -> {
            colors.put(name, ModPointsConfig.getPoint(name).pluralName);
        });
        return colors;
    }

    public void setTrees(List<ModTree> trees) {
        this.trees = trees;
    }

    public void setPoints(Map<String, Integer> points) {
        this.points = points;
    }

    public void addPoint(String point, int amount) {
        if (this.points.containsKey(point)) {
            this.points.put(point, this.points.get(point) + amount);
        } else {
            this.points.put(point, amount);
        }
        if (Thread.currentThread().getThreadGroup().equals(SidedThreadGroups.CLIENT)) {
            ModPackets.sendToServer(new RequestPlayerPointsC2SPacket());
        }
    }

    public void addPoint(String point, int amount, ServerPlayer player) {
        if (this.points.containsKey(point)) {
            this.points.put(point, this.points.get(point) + amount);
        } else {
            this.points.put(point, amount);
        }
        ModPackets.sendToClient(new SyncPlayerPointsS2CPacket(getPoints()), player);
    }
}
