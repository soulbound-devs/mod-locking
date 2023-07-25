package net.vakror.mod_locking.mod.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.config.configs.ModPointsConfig;
import net.vakror.mod_locking.mod.point.ModPoint;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.unlock.Unlock;
import net.vakror.mod_locking.packet.ModPackets;
import net.vakror.mod_locking.packet.RequestPlayerPointsC2SPacket;
import net.vakror.mod_locking.packet.SyncPlayerPointsS2CPacket;
import net.vakror.mod_locking.packet.SyncPlayerTreesS2CPacket;

import java.util.*;

public class ModTreeCapability {
    List<ModTree> trees = null;

    Map<String, Integer> points = new HashMap<>();
    public void saveNBTData(CompoundTag nbt) {
        CompoundTag treeTag = new CompoundTag();
        if (trees != null) {
            for (ModTree tree : trees) {
                treeTag.put("tree_" + tree.name, ModTree.CODEC_WITH_MODS_UNLOCKED.encodeStart(NbtOps.INSTANCE, tree).resultOrPartial((error) -> {
                    throw new IllegalStateException(error);
                }).get());
            }
            nbt.put("trees", treeTag);
        }
        nbt.put("points", ModPoint.MAP_CODEC.encodeStart(NbtOps.INSTANCE, points).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
    }

    public ModTreeCapability loadNBTData(CompoundTag nbt) {
        CompoundTag treeTag = nbt.getCompound("trees");
        CompoundTag pointTag = nbt.getCompound("points");
        if (treeTag != null) {
            treeTag.getAllKeys().forEach((key) -> {
                trees = new ArrayList<>();
                trees.add(ModTree.CODEC_WITH_MODS_UNLOCKED.parse(NbtOps.INSTANCE, treeTag.getCompound(key)).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
            });
            points = new HashMap<>(ModPoint.MAP_CODEC.parse(NbtOps.INSTANCE, pointTag).resultOrPartial((error) -> {
                throw new IllegalStateException(error);
            }).get());
        }

        return this;
    }


    public List<ModTree> getTrees() {
        if (trees == null) {
            trees = ModConfigs.TREES.trees;
        }
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

    private boolean removePoints(Map<String, Integer> points, ServerPlayer player) {
        for (Map.Entry<String, Integer> entry : points.entrySet()) {
            String pointName;
            int pointAmount;
            try {
                pointName = entry.getKey();
                pointAmount = entry.getValue();
            } catch (IllegalStateException e) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(e);
            }
            if (this.points.containsKey(pointName)) {
                this.addPoint(pointName, -pointAmount);
                ModPackets.sendToClient(new SyncPlayerPointsS2CPacket(getPoints()), player);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void addPoint(String point, int amount, ServerPlayer player) {
        if (this.points.containsKey(point)) {
            this.points.put(point, this.points.get(point) + amount);
        } else {
            this.points.put(point, amount);
        }
        ModPackets.sendToClient(new SyncPlayerPointsS2CPacket(getPoints()), player);
    }

    public void addUnlockedUnlock(Unlock<?> unlock, ServerPlayer player) {
        for (ModTree tree : this.getTrees()) {
            if (tree.name.equals(unlock.getTreeName())) {
                if (removePoints(unlock.getCost(), player)) {
                    if (tree.modsUnlocked == null) tree.modsUnlocked = new ArrayList<>();
                    tree.modsUnlocked.add(unlock.getName());
                    ModPackets.sendToClient(new SyncPlayerTreesS2CPacket(getTrees()), player);
                }
                break;
            }
        }
    }
}
