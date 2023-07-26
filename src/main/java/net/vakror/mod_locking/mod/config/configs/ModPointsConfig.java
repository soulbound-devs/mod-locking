package net.vakror.mod_locking.mod.config.configs;

import com.google.gson.annotations.Expose;
import net.vakror.mod_locking.mod.config.Config;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.point.ModPoint;

import java.util.ArrayList;
import java.util.List;

public class ModPointsConfig extends Config<ModPoint> {
    @Expose
    public List<ModPoint> points = new ArrayList<>();

    @Override
    public String getSubPath() {
        return "points";
    }

    @Override
    public String getName() {
        return "points";
    }

    @Override
    public void add(ModPoint object) {
        points.add(object);
    }

    @Override
    public List<ModPoint> getObjects() {
        return points;
    }

    @Override
    protected void reset() {
    }

    public static ModPoint getPoint(String point) {
        for (ModPoint point1: ModConfigs.POINTS.points) {
            if (point1.name.equals(point)) {
                return point1;
            }
        }
        throw new IllegalArgumentException("Could Not Find Point Type: " + point);
    }
}
