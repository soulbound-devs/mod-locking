package net.vakror.mod_locking.mod.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.point.ModPoint;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.util.NbtUtil;
import net.vakror.mod_locking.packet.ModPackets;
import net.vakror.mod_locking.packet.RequestPointUpdateC2SPacket;
import net.vakror.mod_locking.packet.SyncPointsS2CPacket;

import java.util.*;

public class ModTreeCapability {
    List<ModTree> trees;

    Map<String, Integer> points = new HashMap<>();
    public void saveNBTData(CompoundTag nbt) {
        NbtUtil.serializePoints(nbt, points);
        NbtUtil.serializeTrees(nbt, trees);
    }

    public ModTreeCapability loadNBTData(CompoundTag nbt) {
        trees = NbtUtil.deserializeTrees(nbt);
        points = NbtUtil.deserializePoints(nbt);

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
            colors.put(name, NbtUtil.getPoint(name).getColor());
        });
        return colors;
    }

    public Map<String, String> getPointPluralNames() {
        Map<String, String> colors = new HashMap<>(points.size());
        points.forEach((name, amount) -> {
            colors.put(name, NbtUtil.getPoint(name).pluralName);
        });
        return colors;
    }

    public void setTrees(List<ModTree> trees) {
        this.trees = trees;
    }

    public void setPoints(Map<String, Integer> points, Player player) {
        this.points = points;
        if (Thread.currentThread().getThreadGroup().equals(SidedThreadGroups.SERVER)) {
            ModPackets.sendToClient(new SyncPointsS2CPacket(getPoints(), getPointPluralNames(), getPointColors()), (ServerPlayer) player);
        }
    }

    public void addPoint(String point, int amount, Player player) {
        this.points.put(point, this.points.get(point) + amount);
        if (Thread.currentThread().getThreadGroup().equals(SidedThreadGroups.SERVER)) {
            ModPackets.sendToClient(new SyncPointsS2CPacket(getPoints(), getPointPluralNames(), getPointColors()), (ServerPlayer) player);
        }
    }
}
