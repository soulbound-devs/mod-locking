package net.vakror.mod_locking.mod.config.configs;

import com.google.gson.annotations.Expose;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.individual.SimpleIndividualFileConfig;
import net.vakror.mod_locking.ModLockingMod;
import net.vakror.mod_locking.mod.point.obtain.PointObtainMethod;
import net.vakror.mod_locking.mod.point.obtain.RightClickItemObtainMethod;

import java.util.ArrayList;
import java.util.List;

public class UseItemPointObtainConfig extends SimpleIndividualFileConfig<RightClickItemObtainMethod> {
    @Expose
    public List<RightClickItemObtainMethod> useItemObtainMethods = new ArrayList<>();

    public static final UseItemPointObtainConfig INSTANCE = new UseItemPointObtainConfig();

    public UseItemPointObtainConfig() {
        super("mod-locking/point-obtain-methods/use", new ResourceLocation(ModLockingMod.MOD_ID, "use-point-obtain"));
    }

    public List<PointObtainMethod> getAll() {
        List<PointObtainMethod> allMethods = new ArrayList<>();
        allMethods.addAll(useItemObtainMethods);
        allMethods.addAll(KillEntityPointObtainConfig.INSTANCE.killEntityObtainMethods);
        return allMethods;
    }

    @Override
    public List<RightClickItemObtainMethod> getObjects() {
        return useItemObtainMethods;
    }

    @Override
    public String getFileName(RightClickItemObtainMethod object) {
        return object.getName();
    }

    @Override
    protected void resetToDefault() {
    }

    @Override
    public Class<RightClickItemObtainMethod> getConfigObjectClass() {
        return RightClickItemObtainMethod.class;
    }
}
