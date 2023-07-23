package net.vakror.mod_locking.mod.config.adapter;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.vakror.mod_locking.mod.point.obtain.KillEntityObtainMethod;

import java.lang.reflect.Type;


public class KillEntityPointObtainMethodAdapter
implements JsonSerializer<KillEntityObtainMethod>,
JsonDeserializer<KillEntityObtainMethod> {
    public static KillEntityPointObtainMethodAdapter INSTANCE = new KillEntityPointObtainMethodAdapter();

    private KillEntityPointObtainMethodAdapter() {
    }

    public KillEntityObtainMethod deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return KillEntityObtainMethod.CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial((error) -> {throw new JsonParseException(error);}).get();
    }

    public JsonElement serialize(KillEntityObtainMethod method, Type typeOfSrc, JsonSerializationContext context) {
        return KillEntityObtainMethod.CODEC.encodeStart(JsonOps.INSTANCE, method).resultOrPartial((error) -> {throw new JsonIOException(error);}).get();
    }
}

