package net.vakror.mod_locking.mod.config;

import com.google.gson.annotations.Expose;
import net.vakror.mod_locking.mod.point.ModPoint;

import java.util.ArrayList;
import java.util.List;

public class ModPointsConfig extends Config {
    @Expose
    public List<ModPoint> points = new ArrayList<>();

    @Override
    public String getName() {
        return "points";
    }

    @Override
    protected void reset() {
    }
}
