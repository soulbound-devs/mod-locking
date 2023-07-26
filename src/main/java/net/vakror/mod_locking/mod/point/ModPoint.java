package net.vakror.mod_locking.mod.point;

import com.google.gson.annotations.Expose;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.vakror.mod_locking.locking.Restriction;
import net.vakror.mod_locking.mod.config.ConfigObject;
import net.vakror.mod_locking.mod.point.obtain.PointObtainMethod;

import java.util.Map;

public class ModPoint implements INBTSerializable<CompoundTag>, ConfigObject {

    public static final Codec<ModPoint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(ModPoint::getName),
            Codec.STRING.fieldOf("pluralName").forGetter(ModPoint::getPluralName),
            Codec.INT.fieldOf("red").forGetter(ModPoint::getRed),
            Codec.INT.fieldOf("green").forGetter(ModPoint::getGreen),
            Codec.INT.fieldOf("blue").forGetter(ModPoint::getBlue)
    ).apply(instance, ModPoint::new));

    public static final Codec<Map<String, Integer>> MAP_CODEC = Codec.unboundedMap(Codec.STRING, Codec.INT);
    @Expose
    public String name;

    @Expose
    public String pluralName;

    @Expose
    public int red;
    @Expose
    public int green;
    @Expose
    public int blue;

    public ModPoint(String name, String pluralName, int red, int green, int blue) {
        this.name = name;
        this.pluralName = pluralName;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getColor() {
        return encode(red, green, blue);
    }

    public String getName() {
        return name;
    }

    public String getPluralName() {
        return pluralName;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
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

    @Override
    public String getFileName() {
        return name;
    }
}
