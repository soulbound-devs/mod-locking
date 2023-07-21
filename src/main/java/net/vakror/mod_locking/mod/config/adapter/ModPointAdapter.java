package net.vakror.mod_locking.mod.config.adapter;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.vakror.mod_locking.ModLockingMod;
import net.vakror.mod_locking.mod.point.ModPoint;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.util.JsonUtil;

import java.lang.reflect.Type;

import static net.vakror.mod_locking.mod.util.JsonUtil.*;

public class ModPointAdapter
implements JsonSerializer<ModPoint>,
JsonDeserializer<ModPoint> {
    public static ModPointAdapter INSTANCE = new ModPointAdapter();

    private ModPointAdapter() {
    }

    public ModPoint deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject pointObject = json.getAsJsonObject();

        getAndThrowIfNull(pointObject.get("name"), "Point Name Cannot Be Null In Config!");
        getAndThrowIfNull(pointObject.get("name").getAsString(), "Point Name Cannot Be Blank In Config!");

        getAndThrowIfNull(pointObject.get("pluralName"), "Point Plural Name Cannot Be Null In Config!");
        getAndThrowIfNull(pointObject.get("pluralName").getAsString(), "Point Plural Name Cannot Be Blank In Config!");

        getAndThrowIfNull(pointObject.get("red"), "Red Color of Point Cannot Be Null In Config! Point Name: " + pointObject.get("name").getAsString());
        getAndThrowIfNull(pointObject.get("green"), "Green Color of Point Cannot Be Null In Config! Point Name: " + pointObject.get("name").getAsString());
        getAndThrowIfNull(pointObject.get("blue"), "Blue Color of Point Cannot Be Null In Config! Point Name: " + pointObject.get("name").getAsString());

        ModPoint point = new ModPoint(pointObject.get("name").getAsString(), pointObject.get("pluralName").getAsString(), pointObject.get("red").getAsInt(), pointObject.get("green").getAsInt(), pointObject.get("blue").getAsInt());

        return point;
    }

    public JsonElement serialize(ModPoint point, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject pointObject = new JsonObject();
        JsonUtil.addTreeAndAlertIfNull("name", point.name, point.name, pointObject, true);
        JsonUtil.addTreeAndAlertIfNull("pluralName", point.name, point.pluralName, pointObject, true);
        pointObject.addProperty("red", point.red);
        pointObject.addProperty("green", point.green);
        pointObject.addProperty("blue", point.blue);

        return pointObject;
    }
}

