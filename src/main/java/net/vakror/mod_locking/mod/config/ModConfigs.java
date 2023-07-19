package net.vakror.mod_locking.mod.config;


public class ModConfigs {
    public static ModUnlocksConfig UNLOCKS;
    public static ModTreesConfig TREES;
    public static ModPointsConfig POINTS;
    public static ModPointObtainConfig POINT_OBTAIN_METHODS;

    public static void register() {
        TREES = new ModTreesConfig().readConfig();
        UNLOCKS = new ModUnlocksConfig().readConfig();
        POINTS = new ModPointsConfig().readConfig();
        POINT_OBTAIN_METHODS = new ModPointObtainConfig().readConfig();
    }
}
