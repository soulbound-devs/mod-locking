package net.vakror.mod_locking.mod.point;

import com.google.gson.annotations.Expose;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.vakror.mod_locking.mod.point.obtain.PointObtainMethod;

public class ModPoint implements INBTSerializable<CompoundTag> {

    @Expose
    public String name;

    @Expose
    public String pluralName;

    @Expose
    public PointObtainMethod pointObtainMethod;

    public ModPoint(String name, String pluralName) {
        this.name = name;
        this.pluralName = pluralName;
    }

    public ModPoint withPointObtainMethod(PointObtainMethod method) {
        this.pointObtainMethod = method;
        return this;
    }

    public void deserializeNBT(CompoundTag nbt) {
    };

    public CompoundTag serializeNBT() {
        return null;
    };

    public int getColor() {
        return -1;
    };
}
