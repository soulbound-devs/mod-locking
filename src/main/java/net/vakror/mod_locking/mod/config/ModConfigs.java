package net.vakror.mod_locking.mod.config;


import net.vakror.mod_locking.mod.config.configs.*;

public class ModConfigs {

    //TODO: Add reset/regret point config used to counter other points and regret/un-unlock something (all children need to be reset to reset parent)

    public static ModUnlocksConfig UNLOCKS;
    public static ModTreesConfig TREES;
    public static ModPointsConfig POINTS;
    public static ModPointObtainConfig POINT_OBTAIN_METHODS;

    public static void register(boolean overrideCurrent) {
        TREES = new ModTreesConfig().readConfig(overrideCurrent);
        POINTS = new ModPointsConfig().readConfig(overrideCurrent);
        POINT_OBTAIN_METHODS = new ModPointObtainConfig().readConfig(overrideCurrent);
        UNLOCKS = new ModUnlocksConfig().readConfig(overrideCurrent);
    }
}
