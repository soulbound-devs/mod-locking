package net.vakror.mod_locking.mod.config;


import net.vakror.mod_locking.mod.config.configs.*;
import net.vakror.mod_locking.mod.point.ModPoint;
import net.vakror.mod_locking.mod.point.obtain.KillEntityObtainMethod;
import net.vakror.mod_locking.mod.point.obtain.PointObtainMethod;
import net.vakror.mod_locking.mod.point.obtain.RightClickItemObtainMethod;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.unlock.FineGrainedModUnlock;
import net.vakror.mod_locking.mod.unlock.ModUnlock;

public class ModConfigs {

    //TODO: Add reset/regret point config used to counter other points and regret/un-unlock something (all children need to be reset to reset parent)

    public static ModUnlocksConfig MOD_UNLOCKS;
    public static FineGrainedModUnlocksConfig FINE_GRAINED_MOD_UNLOCKS;
    public static ModTreesConfig TREES;
    public static ModPointsConfig POINTS;
    public static UseItemPointObtainConfig USE_ITEM_POINT_OBTAIN_METHODS;
    public static KillEntityPointObtainConfig KILL_ENTITY_POINT_OBTAIN_METHODS;

    public static void register(boolean overrideCurrent) {
        TREES = new ModTreesConfig();
        TREES.readConfig(overrideCurrent, ModTree.class);

        POINTS = new ModPointsConfig();
        POINTS.readConfig(overrideCurrent, ModPoint.class);

        USE_ITEM_POINT_OBTAIN_METHODS = new UseItemPointObtainConfig();
        USE_ITEM_POINT_OBTAIN_METHODS.readConfig(overrideCurrent, RightClickItemObtainMethod.class);

        KILL_ENTITY_POINT_OBTAIN_METHODS = new KillEntityPointObtainConfig();
        KILL_ENTITY_POINT_OBTAIN_METHODS.readConfig(overrideCurrent, KillEntityObtainMethod.class);

        MOD_UNLOCKS = new ModUnlocksConfig();
        MOD_UNLOCKS.readConfig(overrideCurrent, ModUnlock.class);

        FINE_GRAINED_MOD_UNLOCKS = new FineGrainedModUnlocksConfig();
        FINE_GRAINED_MOD_UNLOCKS.readConfig(overrideCurrent, FineGrainedModUnlock.class);
    }
}
