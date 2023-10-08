package net.vakror.mod_locking.mod.config.configs;

import net.vakror.jamesconfig.config.manager.NoAdapterConfigManager;

public class ModConfigs {
    public static void addConfigs() {
        NoAdapterConfigManager.INSTANCE.addConfig(FineGrainedModUnlocksConfig.INSTANCE);
        NoAdapterConfigManager.INSTANCE.addConfig(KillEntityPointObtainConfig.INSTANCE);
        NoAdapterConfigManager.INSTANCE.addConfig(ModPointsConfig.INSTANCE);
        NoAdapterConfigManager.INSTANCE.addConfig(ModTreesConfig.INSTANCE);
        NoAdapterConfigManager.INSTANCE.addConfig(ModUnlocksConfig.INSTANCE);
        NoAdapterConfigManager.INSTANCE.addConfig(UseItemPointObtainConfig.INSTANCE);
    }
}
