package net.vakror.mod_locking.mod.config.adapter;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.vakror.mod_locking.mod.point.obtain.RightClickItemObtainMethod;

import java.lang.reflect.Type;


public class RightClickItemPointObtainMethodAdapter
implements JsonSerializer<RightClickItemObtainMethod>,
JsonDeserializer<RightClickItemObtainMethod> {
    public static RightClickItemPointObtainMethodAdapter INSTANCE = new RightClickItemPointObtainMethodAdapter();

    private RightClickItemPointObtainMethodAdapter() {
    }

    public RightClickItemObtainMethod deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return RightClickItemObtainMethod.CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial((error) -> {throw new JsonParseException(error);}).get();
    }

    public JsonElement serialize(RightClickItemObtainMethod method, Type typeOfSrc, JsonSerializationContext context) {
        return RightClickItemObtainMethod.CODEC.encodeStart(JsonOps.INSTANCE, method).resultOrPartial((error) -> {throw new JsonIOException(error);}).get();
    }
}

