package net.vakror.mod_locking.mod.point;

import com.google.gson.annotations.Expose;
import net.minecraft.nbt.CompoundTag;

public class IntColorPoint extends ModPoint {

    @Expose
    public int color;
    public IntColorPoint(String name, String pluralName) {
        super(name, pluralName);
        this.color = -1;
    }
    public IntColorPoint(String name, int color, String pluralName) {
        super(name, pluralName);
        this.color = color;
    }
    @Override
    public int getColor() {
        return color;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("name", name);
        nbt.putString("pluralName", pluralName);
        nbt.putInt("color", color);
        nbt.putString("type", "int");
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        pluralName = nbt.getString("pluralName");
        name = nbt.getString("name");
        color = nbt.getInt("color");
    }
}
