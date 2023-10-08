package net.vakror.mod_locking.mod.config.configs;

import com.google.gson.annotations.Expose;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.individual.SimpleIndividualFileConfig;
import net.vakror.mod_locking.ModLockingMod;
import net.vakror.mod_locking.mod.unlock.ModUnlock;
import net.vakror.mod_locking.mod.unlock.Unlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModUnlocksConfig extends SimpleIndividualFileConfig<ModUnlock> {
    @Expose
    public List<ModUnlock> modUnlocks = new ArrayList<>();

    public static final ModUnlocksConfig INSTANCE = new ModUnlocksConfig();

    public ModUnlocksConfig() {
        super("mod-locking/unlocks/mod", new ResourceLocation(ModLockingMod.MOD_ID, "mod-unlocks"));
    }

    @Override
    public List<ModUnlock> getObjects() {
        return modUnlocks;
    }

    @Override
    public String getFileName(ModUnlock object) {
        return object.getFileName();
    }

    @Override
    protected void resetToDefault() {
    }

    @Override
    public Class<ModUnlock> getConfigObjectClass() {
        return ModUnlock.class;
    }

    public List<Unlock<?>> getAll() {
        List<Unlock<?>> all = new ArrayList<>();
        all.addAll(modUnlocks);
        all.addAll(FineGrainedModUnlocksConfig.INSTANCE.fineGrainedUnlocks);
        return all;
    }
}
