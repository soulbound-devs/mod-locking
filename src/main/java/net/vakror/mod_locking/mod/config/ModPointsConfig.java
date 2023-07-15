package net.vakror.mod_locking.mod.config;

import com.google.gson.annotations.Expose;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.vakror.mod_locking.mod.point.IntColorPoint;
import net.vakror.mod_locking.mod.point.ModPoint;
import net.vakror.mod_locking.mod.point.RGBPoint;
import net.vakror.mod_locking.mod.point.obtain.RightClickItemObtainMethod;

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
