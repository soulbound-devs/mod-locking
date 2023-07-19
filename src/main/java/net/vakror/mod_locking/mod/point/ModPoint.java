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

    public ModPoint(String name, String pluralName, int red, int green, int blue) {
        this.name = name;
        this.pluralName = pluralName;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    @Expose
    public int red;
    @Expose
    public int green;
    @Expose
    public int blue;

    public int getColor() {
        return encode(red, green, blue);
    }

    public static int encode(int r, int g, int b) {
        return (0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("pluralName", pluralName);
        nbt.putString("name", name);
        nbt.putInt("red", red);
        nbt.putInt("green", green);
        nbt.putInt("blue", blue);
        nbt.putString("type", "rgb");
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        pluralName = nbt.getString("pluralName");
        name = nbt.getString("name");
        red = nbt.getInt("red");
        green = nbt.getInt("green");
        green = nbt.getInt("green");
    }
}
