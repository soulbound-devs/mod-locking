package net.vakror.mod_locking.mod.config.configs;

import com.google.gson.annotations.Expose;
import net.vakror.mod_locking.mod.config.Config;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.point.obtain.KillEntityObtainMethod;
import net.vakror.mod_locking.mod.point.obtain.PointObtainMethod;
import net.vakror.mod_locking.mod.point.obtain.RightClickItemObtainMethod;

import java.util.ArrayList;
import java.util.List;

public class KillEntityPointObtainConfig extends Config<KillEntityObtainMethod> {
    @Expose
    public List<KillEntityObtainMethod> killEntityObtainMethods = new ArrayList<>();

    public List<PointObtainMethod> getAll() {
        List<PointObtainMethod> allMethods = new ArrayList<>();
        allMethods.addAll(killEntityObtainMethods);
        allMethods.addAll(ModConfigs.USE_ITEM_POINT_OBTAIN_METHODS.useItemObtainMethods);
        return allMethods;
    }

    @Override
    public String getSubPath() {
        return "point_obtain_methods/kill_entity";
    }

    @Override
    public String getName() {
        return "point_obtain_methods";
    }

    @Override
    public void add(KillEntityObtainMethod object) {
        killEntityObtainMethods.add(object);
    }

    @Override
    public List<KillEntityObtainMethod> getObjects() {
        return killEntityObtainMethods;
    }

    @Override
    protected void reset() {
    }
}
