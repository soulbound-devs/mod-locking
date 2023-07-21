package net.vakror.mod_locking.mod.config.adapter;

import com.google.gson.*;
import net.minecraftforge.registries.ForgeRegistries;
import net.vakror.mod_locking.mod.point.obtain.KillEntityObtainMethod;
import net.vakror.mod_locking.mod.util.JsonUtil;

import java.lang.reflect.Type;

import static net.vakror.mod_locking.mod.util.JsonUtil.getAndThrowIfNotValidRegistryObject;
import static net.vakror.mod_locking.mod.util.JsonUtil.getAndThrowIfNull;

public class KillEntityPointObtainMethodAdapter
implements JsonSerializer<KillEntityObtainMethod>,
JsonDeserializer<KillEntityObtainMethod> {
    public static KillEntityPointObtainMethodAdapter INSTANCE = new KillEntityPointObtainMethodAdapter();

    private KillEntityPointObtainMethodAdapter() {
    }

    public KillEntityObtainMethod deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obtainMethodObject = json.getAsJsonObject();

        getAndThrowIfNull(obtainMethodObject.get("pointType"), "Point Type Cannot Be Null In Point Obtain Method Config!");
        getAndThrowIfNull(obtainMethodObject.get("pointType").getAsString(), "Point Type Cannot Be Blank In Point Obtain Method Config!");

        getAndThrowIfNull(obtainMethodObject.get("entityId"), "Entity Id Cannot Be Null In Point Obtain Method Config!");
        getAndThrowIfNull(obtainMethodObject.get("entityId").getAsString(), "Entity Id Cannot Be Blank In Point Obtain Method Config!");
        getAndThrowIfNotValidRegistryObject(obtainMethodObject.get("entityId").getAsString(), "Entity Id" + obtainMethodObject.get("entityId").getAsString() + "of Point Obtain Method is not valid!", ForgeRegistries.ENTITY_TYPES);

        getAndThrowIfNull(obtainMethodObject.get("amount"), "Point Amount of Point Obtain Method Cannot Be Null In Config!");

        return new KillEntityObtainMethod(obtainMethodObject.get("entityId").getAsString(), obtainMethodObject.get("amount").getAsInt(), obtainMethodObject.get("pointType").getAsString());
    }

    public JsonElement serialize(KillEntityObtainMethod point, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject pointObject = new JsonObject();
        JsonUtil.addPointObtainAndAlertIfNull("pointType", point.getPointType(), pointObject, true);
        pointObject.addProperty("amount", point.getAmount());
        JsonUtil.addPointObtainAndAlertIfNull("entityId", point.getEntityId(), pointObject, true);
        return pointObject;
    }
}

