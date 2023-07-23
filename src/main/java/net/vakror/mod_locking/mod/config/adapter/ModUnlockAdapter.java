package net.vakror.mod_locking.mod.config.adapter;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.vakror.mod_locking.mod.unlock.ModUnlock;

import java.lang.reflect.Type;

public class ModUnlockAdapter
        implements JsonSerializer<ModUnlock>,
        JsonDeserializer<ModUnlock> {
    public static ModUnlockAdapter INSTANCE = new ModUnlockAdapter();

    private ModUnlockAdapter() {
    }

    public ModUnlock deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return ModUnlock.CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial((error) -> {throw new JsonParseException(error);}).get();
    }

    public JsonElement serialize(ModUnlock src, Type typeOfSrc, JsonSerializationContext context) {
        return ModUnlock.CODEC.encodeStart(JsonOps.INSTANCE, src).resultOrPartial((error) -> {throw new JsonIOException(error);}).get();
    }
}

