package net.vakror.mod_locking.mod.config.configs;

import com.google.gson.annotations.Expose;
import net.vakror.mod_locking.mod.config.Config;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.point.obtain.KillEntityObtainMethod;
import net.vakror.mod_locking.mod.point.obtain.PointObtainMethod;
import net.vakror.mod_locking.mod.point.obtain.RightClickItemObtainMethod;

import java.util.ArrayList;
import java.util.List;

public class UseItemPointObtainConfig extends Config<RightClickItemObtainMethod> {
    @Expose
    public List<RightClickItemObtainMethod> useItemObtainMethods = new ArrayList<>();

    public List<PointObtainMethod> getAll() {
        List<PointObtainMethod> allMethods = new ArrayList<>();
        allMethods.addAll(useItemObtainMethods);
        allMethods.addAll(ModConfigs.KILL_ENTITY_POINT_OBTAIN_METHODS.killEntityObtainMethods);
        return allMethods;
    }

    @Override
    public String getSubPath() {
        return "point_obtain_methods/use_item";
    }

    @Override
    public String getName() {
        return "point_obtain_methods";
    }

    @Override
    public void add(RightClickItemObtainMethod object) {
        useItemObtainMethods.add(object);
    }

    @Override
    public List<RightClickItemObtainMethod> getObjects() {
        return useItemObtainMethods;
    }

    @Override
    protected void reset() {
    }
}
