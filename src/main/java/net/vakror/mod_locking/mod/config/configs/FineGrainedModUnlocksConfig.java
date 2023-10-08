package net.vakror.mod_locking.mod.config.configs;

import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.individual.SimpleIndividualFileConfig;
import net.vakror.mod_locking.ModLockingMod;
import net.vakror.mod_locking.mod.unlock.FineGrainedModUnlock;
import net.vakror.mod_locking.mod.unlock.Unlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FineGrainedModUnlocksConfig extends SimpleIndividualFileConfig<FineGrainedModUnlock> {
    public List<FineGrainedModUnlock> fineGrainedUnlocks = new ArrayList<>();

    public static final FineGrainedModUnlocksConfig INSTANCE = new FineGrainedModUnlocksConfig();

    public FineGrainedModUnlocksConfig() {
        super("mod-locking/unlocks/fine-grained", new ResourceLocation(ModLockingMod.MOD_ID, "fine-grained"));
    }

    public List<Unlock<?>> getAll() {
        List<Unlock<?>> all = new ArrayList<>();
        all.addAll(ModUnlocksConfig.INSTANCE.modUnlocks);
        all.addAll(fineGrainedUnlocks);
        return all;
    }

    @Override
    public List<FineGrainedModUnlock> getObjects() {
        return fineGrainedUnlocks;
    }

    @Override
    public String getFileName(FineGrainedModUnlock object) {
        return object.getName();
    }

    @Override
    protected void resetToDefault() {
    }

    @Override
    public Class<FineGrainedModUnlock> getConfigObjectClass() {
        return FineGrainedModUnlock.class;
    }
}
