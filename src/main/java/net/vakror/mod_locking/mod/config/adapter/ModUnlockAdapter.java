package net.vakror.mod_locking.mod.config.adapter;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.vakror.mod_locking.ModLockingMod;
import net.vakror.mod_locking.mod.unlock.ModUnlock;
import net.vakror.mod_locking.mod.util.JsonUtil;

import java.lang.reflect.Type;

import static net.vakror.mod_locking.mod.util.JsonUtil.*;

public class ModUnlockAdapter
        implements JsonSerializer<ModUnlock>,
        JsonDeserializer<ModUnlock> {
    public static ModUnlockAdapter INSTANCE = new ModUnlockAdapter();

    private ModUnlockAdapter() {
    }

    public ModUnlock deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject unlockObject = (JsonObject) json;
        getAndThrowIfNull(unlockObject.get("name").getAsString(), "Name cannot be blank or null in config!");
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
        String[] modIds = null;
        if (unlockObject.getAsJsonArray("modIds") != null) {
            modIds = getModIds(unlockObject);
        }
        ModUnlock unlock = new ModUnlock(
                getAndThrowIfNull(unlockObject.get("name").getAsString(), "Name cannot be blank or null in config!"),
                getCost(unlockObject),
                requiredUnlocks,
                unlockObject.get("x").getAsFloat(),
                unlockObject.get("y").getAsFloat(),
                modIds
        );
        addDataToUnlock(unlock, unlockObject);
        return unlock;
    }

    public JsonElement serialize(ModUnlock src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject unlockObject = JsonUtil.serialize(src, typeOfSrc, context);
        if (src.getModIds() == null || src.getModIds().isEmpty()) {
            ModLockingMod.LOGGER.error("Mod Ids of unlock " + (src.getName() == null || src.getName().isBlank() ? "" : src.getName() + " ") + "are null!");
        } else {
            JsonArray modIds = new JsonArray();
            for (String modId : src.getModIds()) {
                modIds.add(modId);
            }
            unlockObject.add("modIds", modIds);
        }
        return unlockObject;
    }
}

