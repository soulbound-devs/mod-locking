package net.vakror.mod_locking.mod.config.configs;

import com.google.gson.annotations.Expose;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.individual.SimpleIndividualFileConfig;
import net.vakror.mod_locking.ModLockingMod;
import net.vakror.mod_locking.mod.point.ModPoint;

import java.util.ArrayList;
import java.util.List;

public class ModPointsConfig extends SimpleIndividualFileConfig<ModPoint> {
    @Expose
    public List<ModPoint> points = new ArrayList<>();

    public static final ModPointsConfig INSTANCE = new ModPointsConfig();

    public ModPointsConfig() {
        super("mod-locking/points", new ResourceLocation(ModLockingMod.MOD_ID, "points"));
    }

    @Override
    public List<ModPoint> getObjects() {
        return points;
    }

    @Override
    public String getFileName(ModPoint object) {
        return object.getName();
    }

    @Override
    protected void resetToDefault() {
    }

    @Override
    public Class<ModPoint> getConfigObjectClass() {
        return ModPoint.class;
    }

        public static ModPoint getPoint(String point) {
        for (ModPoint point1: ModPointsConfig.INSTANCE.points) {
            if (point1.name.equals(point)) {
                return point1;
            }
        }
        throw new IllegalArgumentException("Could Not Find Point Type: " + point);
    }
}
