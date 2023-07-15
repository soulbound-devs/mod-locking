package net.vakror.mod_locking.mod.point;

import com.google.gson.annotations.Expose;
import net.minecraft.nbt.CompoundTag;

public class RGBPoint extends ModPoint {
    @Expose
    public int red;
    @Expose
    public int green;
    @Expose
    public int blue;

    public RGBPoint(String name, String pluralName, int red, int green, int blue) {
        super(name, pluralName);
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    @Override
    public int getColor() {
        return encode(red, green, blue);
    }

    public static int encode(int r, int g, int b) {
        return (0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
    }

    @Override
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

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        pluralName = nbt.getString("pluralName");
        name = nbt.getString("name");
        red = nbt.getInt("red");
        green = nbt.getInt("green");
        green = nbt.getInt("green");
    }
}
