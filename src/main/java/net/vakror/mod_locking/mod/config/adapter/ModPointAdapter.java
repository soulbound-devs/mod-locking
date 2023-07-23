package net.vakror.mod_locking.mod.config.adapter;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.vakror.mod_locking.mod.point.ModPoint;

import java.lang.reflect.Type;

public class ModPointAdapter
implements JsonSerializer<ModPoint>,
JsonDeserializer<ModPoint> {
    public static ModPointAdapter INSTANCE = new ModPointAdapter();

    private ModPointAdapter() {
    }

    public ModPoint deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return ModPoint.CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial((error) -> {throw new JsonParseException(error);}).get();
    }

    public JsonElement serialize(ModPoint point, Type typeOfSrc, JsonSerializationContext context) {
        return ModPoint.CODEC.encodeStart(JsonOps.INSTANCE, point).resultOrPartial((error) -> {throw new JsonIOException(error);}).get();
    }
}

