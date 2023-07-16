package net.vakror.mod_locking.mod.config;


public class ModConfigs {
    public static ModUnlocksConfig UNLOCKS;
    public static ModTreesConfig TREES;
    public static ModPointsConfig POINTS;

    public static void register() {
        TREES = new ModTreesConfig().readConfig();
        UNLOCKS = new ModUnlocksConfig().readConfig();
        POINTS = new ModPointsConfig().readConfig();
    }
}
