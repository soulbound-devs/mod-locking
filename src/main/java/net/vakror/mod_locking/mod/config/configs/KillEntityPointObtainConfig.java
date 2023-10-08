package net.vakror.mod_locking.mod.config.configs;

import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.individual.SimpleIndividualFileConfig;
import net.vakror.mod_locking.ModLockingMod;
import net.vakror.mod_locking.mod.point.obtain.KillEntityObtainMethod;
import net.vakror.mod_locking.mod.point.obtain.PointObtainMethod;

import java.util.ArrayList;
import java.util.List;

public class KillEntityPointObtainConfig extends SimpleIndividualFileConfig<KillEntityObtainMethod> {
    public List<KillEntityObtainMethod> killEntityObtainMethods = new ArrayList<>();

    public static final KillEntityPointObtainConfig INSTANCE = new KillEntityPointObtainConfig();

    public KillEntityPointObtainConfig() {
        super("mod-locking/point-obtain-methods/kill", new ResourceLocation(ModLockingMod.MOD_ID, "kill-point-obtain"));
    }

    public List<PointObtainMethod> getAll() {
        List<PointObtainMethod> allMethods = new ArrayList<>();
        allMethods.addAll(killEntityObtainMethods);
        allMethods.addAll(UseItemPointObtainConfig.INSTANCE.useItemObtainMethods);
        return allMethods;
    }

    @Override
    public List<KillEntityObtainMethod> getObjects() {
        return killEntityObtainMethods;
    }

    @Override
    public String getFileName(KillEntityObtainMethod object) {
        return object.getName();
    }

    @Override
    protected void resetToDefault() {
    }

    @Override
    public Class<KillEntityObtainMethod> getConfigObjectClass() {
        return KillEntityObtainMethod.class;
    }
}
