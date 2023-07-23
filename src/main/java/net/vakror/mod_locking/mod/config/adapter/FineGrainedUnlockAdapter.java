package net.vakror.mod_locking.mod.config.adapter;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.vakror.mod_locking.mod.unlock.FineGrainedModUnlock;

import java.lang.reflect.Type;

public class FineGrainedUnlockAdapter implements JsonSerializer<FineGrainedModUnlock>, JsonDeserializer<FineGrainedModUnlock> {
    public static FineGrainedUnlockAdapter INSTANCE = new FineGrainedUnlockAdapter();

    private FineGrainedUnlockAdapter() {
    }

    public FineGrainedModUnlock deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return FineGrainedModUnlock.CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial((error) -> {throw new JsonParseException(error);}).get();
    }

    public JsonElement serialize(FineGrainedModUnlock src, Type typeOfSrc, JsonSerializationContext context) {
        return FineGrainedModUnlock.CODEC.encodeStart(JsonOps.INSTANCE, src).resultOrPartial((error) -> {throw new JsonIOException(error);}).get();
    }
}

