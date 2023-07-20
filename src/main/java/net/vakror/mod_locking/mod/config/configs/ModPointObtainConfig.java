package net.vakror.mod_locking.mod.config.configs;

import com.google.gson.annotations.Expose;
import net.vakror.mod_locking.mod.config.Config;
import net.vakror.mod_locking.mod.point.obtain.KillEntityObtainMethod;
import net.vakror.mod_locking.mod.point.obtain.PointObtainMethod;
import net.vakror.mod_locking.mod.point.obtain.RightClickItemObtainMethod;

import java.util.ArrayList;
import java.util.List;

public class ModPointObtainConfig extends Config {
    @Expose
    public List<RightClickItemObtainMethod> useItemObtainMethods = new ArrayList<>();
    @Expose
    public List<KillEntityObtainMethod> killEntityObtainMethods = new ArrayList<>();

    public List<PointObtainMethod> getAll() {
        List<PointObtainMethod> allMethods = new ArrayList<>();
        allMethods.addAll(useItemObtainMethods);
        allMethods.addAll(killEntityObtainMethods);
        return allMethods;
    }

    @Override
    public String getName() {
        return "point_obtain_methods";
    }

    @Override
    protected void reset() {
    }
}
