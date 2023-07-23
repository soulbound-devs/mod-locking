package net.vakror.mod_locking.mod.config.adapter;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.vakror.mod_locking.mod.tree.ModTree;

import java.lang.reflect.Type;

public class ModTreeAdapter
implements JsonSerializer<ModTree>,
JsonDeserializer<ModTree> {
    public static ModTreeAdapter INSTANCE = new ModTreeAdapter();

    private ModTreeAdapter() {
    }

    public ModTree deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return ModTree.CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial((error) -> {throw new JsonParseException(error);}).get();
    }

    public JsonElement serialize(ModTree tree, Type typeOfSrc, JsonSerializationContext context) {
        return ModTree.CODEC.encodeStart(JsonOps.INSTANCE, tree).resultOrPartial((error) -> {throw new JsonIOException(error);}).get();
    }
}

