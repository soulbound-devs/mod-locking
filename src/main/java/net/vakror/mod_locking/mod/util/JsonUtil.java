package net.vakror.mod_locking.mod.util;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.vakror.mod_locking.ModLockingMod;
import net.vakror.mod_locking.locking.Restriction;
import net.vakror.mod_locking.mod.unlock.Unlock;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    public static void addUnlockAndAlertIfNull(String name, String unlockName, String value, JsonObject object, boolean warn) {
        if (value == null || value.equals("")) {
            if (warn) {
                ModLockingMod.LOGGER.error(name + " of Unlock " + ((unlockName == null || unlockName.isBlank()) ? "" : unlockName + " ") + "is null or empty!");
            }
        } else {
            object.addProperty(name, value);
        }
    }

    public static void addTreeAndAlertIfNull(String name, String treeName, String value, JsonObject object, boolean warn) {
        if (value == null || value.equals("")) {
            if (warn) {
                ModLockingMod.LOGGER.error(name + " of Tree " + ((treeName == null || treeName.isBlank()) ? "" : treeName + " ") + "is null or empty!");
            }
        } else {
            object.addProperty(name, value);
        }
    }

    public static void addPointObtainAndAlertIfNull(String name, String value, JsonObject object, boolean warn) {
        if (value == null || value.equals("")) {
            if (warn) {
                ModLockingMod.LOGGER.error(name + " of Point Obtain Method is null or empty!");
            }
        } else {
            object.addProperty(name, value);
        }
    }

    public static void addPointAndAlertIfNull(String name, String pointName, String value, JsonObject object, boolean warn) {
        if (value == null || value.equals("")) {
            if (warn) {
                ModLockingMod.LOGGER.error(name + " of Point Type " + ((pointName == null || pointName.isBlank()) ? "" : pointName + " ") + "is null or empty!");
            }
        } else {
            object.addProperty(name, value);
        }
    }

    public static void getAndThrowIfNotValidRegistryObject(String string, String message, IForgeRegistry<?> registry) {
        if (!registry.containsKey(new ResourceLocation(string))) {
            throw new JsonParseException(message);
        }
    }

    public static String[] getRequiredUnlocks(JsonObject unlockObject) {
        String[] requiredUnlocks = new String[unlockObject.getAsJsonArray("requiredUnlocks").size()];
        final int[] a = {0};
        unlockObject.getAsJsonArray("requiredUnlocks").forEach((unlockPrimitive) -> {
            if (unlockPrimitive instanceof JsonPrimitive primitive) {
                requiredUnlocks[a[0]] = primitive.getAsString();
                a[0]++;
            }
        });
        return requiredUnlocks;
    }

    public static String[] getModIds(JsonObject unlockObject) {
        String[] modIds = new String[unlockObject.getAsJsonArray("modIds").size()];
        final int[] a = {0};
        unlockObject.getAsJsonArray("modIds").forEach((modIdPrimitive) -> {
            if (modIdPrimitive instanceof JsonPrimitive primitive) {
                modIds[a[0]] = primitive.getAsString();
                a[0]++;
            }
        });
        return modIds;
    }

    public static void serializeCost(Map<String, Integer> costMap, JsonObject unlockObject) {
        if (costMap != null) {
            JsonObject costObject = new JsonObject();
            costMap.forEach(costObject::addProperty);
            unlockObject.add("cost", costObject);
        } else {
            ModLockingMod.LOGGER.error("Cost of Unlock is Null — Assuming unlock is free");
            unlockObject.add("cost", new JsonObject());
        }
    }

    public static Map<String, Integer> getCost(JsonObject unlockObject) {
        JsonObject costObject = unlockObject.getAsJsonObject("cost");
        if (costObject != null) {
            Map<String, Integer> costMap = new HashMap<>();
            for (String name : costObject.keySet()) {
                NbtUtil.getPoint(name, "Could not find point type " + name + " Referenced in unlock " + unlockObject.get("name") + "!");
                costMap.put(name, costObject.get(name).getAsInt());
            }
            return costMap;
        }
        return new HashMap<>();
    }

    public static <T> T getAndThrowIfNull(T object, String errorMessage) {
        if (object == null || (object instanceof String s && s.isBlank())) {
            throw new JsonParseException(errorMessage);
        }
        return object;
    }

    public static void serializeRestrictions(Map<String, Restriction> restrictions, JsonObject object) {
        restrictions.forEach((item, restriction) -> {
            JsonObject restrictionObject = new JsonObject();
            for (Restriction.Type value : Restriction.Type.values()) {
                if (restriction.doesRestrict(value)) {
                    restrictionObject.add(value.name().toLowerCase(), new JsonPrimitive(true));
                }
            }
            object.add(item, restrictionObject);
        });
    }

    public static ResourceLocation getAndThrowIfNotResourceLocation(String string, String message) {
        if (!string.contains(":") || (string.replaceAll("[^._:/a-zA-Z0-9]", "").length() != string.length())) {
            throw new JsonParseException(message);
        } else {
            return new ResourceLocation(string);
        }
    }

    public static Map<String, Restriction> getRestrictions(JsonObject unlockObject, String name) {
        JsonObject restrictionObject = unlockObject.getAsJsonObject(name);
        Map<String, Restriction> map = new HashMap<>();
        for (String item : restrictionObject.keySet()) {
            getAndThrowIfNotValidRegistryObject(item, "Unlock Restricted " + (name.equals("itemRestrictions") ? "Item": name.equals("blockRestrictions") ? "Block": "Entity") + " " + item + " Not Valid! Unlock Name: " + unlockObject.get("name").getAsString(), (name.equals("itemRestrictions") ? ForgeRegistries.ITEMS: name.equals("blockRestrictions") ? ForgeRegistries.BLOCKS: ForgeRegistries.ENTITY_TYPES) );
            for (Restriction.Type type : Restriction.Type.values()) {
                if (restrictionObject.getAsJsonPrimitive(type.toString().toLowerCase()) != null) {
                    JsonPrimitive shouldRestrict = restrictionObject.getAsJsonObject(item).getAsJsonPrimitive(type.toString().toLowerCase());
                    if (shouldRestrict != null) {
                        map.put(item, new Restriction().set(type, shouldRestrict.getAsBoolean()));
                    }
                }
            }
        }
        return map;
    }

    public static JsonObject serialize(Unlock<?> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject unlockObject = new JsonObject();

        addUnlockAndAlertIfNull("name", src.getName(), src.getName(), unlockObject, true);
        serializeCost(src.getCost(), unlockObject);
        if (src.getRequiredUnlocks() != null && src.getRequiredUnlocks().length != 0 && !src.getRequiredUnlocks()[0].equals("")) {
            JsonArray requiredUnlocks = new JsonArray();
            for (String requiredUnlock : src.getRequiredUnlocks()) {
                requiredUnlocks.add(requiredUnlock);
            }
            unlockObject.add("requiredUnlocks", requiredUnlocks);
        }

        unlockObject.addProperty("x", src.getX());
        unlockObject.addProperty("y", src.getY());
        addUnlockAndAlertIfNull("description", src.getName(), src.getDescription(), unlockObject, true);
        addUnlockAndAlertIfNull("icon", src.getName(), src.getIcon(), unlockObject, true);
        addUnlockAndAlertIfNull("tree", src.getName(), src.getTree(), unlockObject, true);
        if (src.getIconNbt() != null) {
            addUnlockAndAlertIfNull("iconNbt", src.getName(), src.getIconNbt().toString(), unlockObject, false);
        }
        return unlockObject;
    }

    public static void addDataToUnlock(Unlock<?> unlock, JsonObject unlockObject) {
        getAndThrowIfNotValidRegistryObject(unlockObject.get("icon").getAsString(), "Unlock Icon " + unlock.getIcon() + " Is not a valid item! Unlock Name: " + unlockObject.get("name").getAsString(), ForgeRegistries.ITEMS);
        unlock.withIcon(getAndThrowIfNull(unlockObject.get("icon"), "Icon of unlock " + unlockObject.get("name") + " cannot be null!").getAsString());
        unlock.withTree(getAndThrowIfNull(unlockObject.get("tree"), "Tree of unlock " + unlockObject.get("name") + " cannot be null!").getAsString());
        unlock.withDescription(Component.literal(getAndThrowIfNull(unlockObject.get("description"), "Description of unlock " + unlockObject.get("name") + " cannot be null!").getAsString()));
        if (unlockObject.get("iconNbt") != null) {
            try {
                unlock.withIconNbt(TagParser.parseTag(unlockObject.get("iconNbt").getAsString()));
            } catch (CommandSyntaxException e) {
                ModLockingMod.LOGGER.error("Icon Nbt " + unlockObject.getAsJsonPrimitive("iconNbt").getAsString() + " is invalid for tree " + unlockObject.get("name").getAsString());}
        }
    }
}
