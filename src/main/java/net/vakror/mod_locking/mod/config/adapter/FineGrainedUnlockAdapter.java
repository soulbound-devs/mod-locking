package net.vakror.mod_locking.mod.config.adapter;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.vakror.mod_locking.mod.unlock.FineGrainedModUnlock;
import net.vakror.mod_locking.mod.util.JsonUtil;

import java.lang.reflect.Type;

import static net.vakror.mod_locking.mod.util.JsonUtil.*;

public class FineGrainedUnlockAdapter
implements JsonSerializer<FineGrainedModUnlock>,
JsonDeserializer<FineGrainedModUnlock> {
    public static FineGrainedUnlockAdapter INSTANCE = new FineGrainedUnlockAdapter();

    private FineGrainedUnlockAdapter() {
    }

    public FineGrainedModUnlock deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject unlockObject = (JsonObject) json;
        JsonUtil.getAndThrowIfNull(unlockObject.get("name"), "Name of unlock cannot be null in config!");
        JsonUtil.getAndThrowIfNull(unlockObject.get("name").getAsString(), "Name of unlock cannot be blank in config!");
        if (unlockObject.get("x") == null) {
            throw new JsonParseException("x value of Unlock " + unlockObject.get("name") + "cannot be null");
        }
        if (unlockObject.get("y") == null) {
            throw new JsonParseException("y value of Unlock " + unlockObject.get("name") + "cannot be null");
        }
        String[] requiredUnlocks = null;
        if (unlockObject.getAsJsonArray("requiredUnlocks") != null) {
            requiredUnlocks = getRequiredUnlocks(unlockObject);
        }
        FineGrainedModUnlock unlock = new FineGrainedModUnlock(
                getAndThrowIfNull(unlockObject.get("name").getAsString(), "Name cannot be blank or null in config!"),
                getCost(unlockObject),
                unlockObject.get("x").getAsFloat(),
                unlockObject.get("y").getAsFloat(),
                requiredUnlocks
                );

        getRestrictions(unlockObject, "itemRestrictions").forEach(unlock::withItemRestriction);
        getRestrictions(unlockObject, "blockRestrictions").forEach(unlock::withBlockRestriction);
        getRestrictions(unlockObject, "entityRestrictions").forEach(unlock::withEntityRestriction);

        addDataToUnlock(unlock, unlockObject);
        return unlock;
    }

    public JsonElement serialize(FineGrainedModUnlock src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject unlockObject = JsonUtil.serialize(src, typeOfSrc, context);
        JsonObject itemRestrictions = new JsonObject();
        serializeRestrictions(src.getItemRestrictions(), itemRestrictions);
        unlockObject.add("itemRestrictions", itemRestrictions);

        JsonObject blockRestrictions = new JsonObject();
        serializeRestrictions(src.getBlockRestrictions(), blockRestrictions);
        unlockObject.add("blockRestrictions", blockRestrictions);

        JsonObject entityRestrictions = new JsonObject();
        serializeRestrictions(src.getEntityRestrictions(), entityRestrictions);
        unlockObject.add("entityRestrictions", entityRestrictions);

        return unlockObject;
    }
}

