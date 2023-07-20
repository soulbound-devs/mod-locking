package net.vakror.mod_locking.mod.config;


import net.vakror.mod_locking.mod.config.configs.ModPointObtainConfig;
import net.vakror.mod_locking.mod.config.configs.ModPointsConfig;
import net.vakror.mod_locking.mod.config.configs.ModTreesConfig;
import net.vakror.mod_locking.mod.config.configs.ModUnlocksConfig;

public class ModConfigs {
    public static ModUnlocksConfig UNLOCKS;
    public static ModTreesConfig TREES;
    public static ModPointsConfig POINTS;
    public static ModPointObtainConfig POINT_OBTAIN_METHODS;

    public static void register(boolean overrideCurrent) {
        TREES = new ModTreesConfig().readConfig(overrideCurrent);
        UNLOCKS = new ModUnlocksConfig().readConfig(overrideCurrent);
        POINTS = new ModPointsConfig().readConfig(overrideCurrent);
        POINT_OBTAIN_METHODS = new ModPointObtainConfig().readConfig(overrideCurrent);
    }
}
