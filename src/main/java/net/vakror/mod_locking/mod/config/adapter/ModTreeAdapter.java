package net.vakror.mod_locking.mod.config.adapter;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.vakror.mod_locking.ModLockingMod;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.util.JsonUtil;

import java.lang.reflect.Type;

import static net.vakror.mod_locking.mod.util.JsonUtil.*;

public class ModTreeAdapter
implements JsonSerializer<ModTree>,
JsonDeserializer<ModTree> {
    public static ModTreeAdapter INSTANCE = new ModTreeAdapter();

    private ModTreeAdapter() {
    }

    public ModTree deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject treeObject = json.getAsJsonObject();

        getAndThrowIfNull(treeObject.get("name"), "Tree Name Cannot Be Null In Config!");
        getAndThrowIfNull(treeObject.get("name").getAsString(), "Tree Name Cannot Be Blank In Config!");

        getAndThrowIfNull(treeObject.get("backgroundTexture"), "Tree Background Texture Cannot Be Null In Config! Tree Name: " + treeObject.get("name").getAsString());
        getAndThrowIfNull(treeObject.get("backgroundTexture").getAsString(), "Tree Background Texture Cannot Be Blank In Config! Tree Name: " + treeObject.get("name").getAsString());
        getAndThrowIfNotResourceLocation(treeObject.get("backgroundTexture").getAsString(), "Tree Background Texture Has to Be Resource Location! Tree Name: " + treeObject.get("name").getAsString());

        getAndThrowIfNull(treeObject.get("icon"), "Tree Icon Cannot Be Null In Config! Tree Name: " + treeObject.get("name").getAsString());
        getAndThrowIfNull(treeObject.get("icon").getAsString(), "Tree Icon Cannot Be Blank In Config! Tree Name: " + treeObject.get("name").getAsString());
        getAndThrowIfNotResourceLocation(treeObject.get("icon").getAsString(), "Tree Icon Has to Be Resource Location In Config! Tree Name: " + treeObject.get("name").getAsString());
        getAndThrowIfNotValidRegistryObject(treeObject.get("icon").getAsString(), "Tree Icon Item" + treeObject.get("icon") + "Not A Valid Item! Tree Name: " + treeObject.get("name").getAsString(), ForgeRegistries.ITEMS);


        getAndThrowIfNull(treeObject.get("centeredX"), "Tree Centered X Cannot Be Null In Config! Tree Name: " + treeObject.get("name").getAsString());
        getAndThrowIfNull(treeObject.get("centeredY"), "Tree Centered Y Cannot Be Null In Config! Tree Name: " + treeObject.get("name").getAsString());

        getAndThrowIfNull(treeObject.get("marginX"), "Tree Margin X Cannot Be Null In Config! Tree Name: " + treeObject.get("name").getAsString());
        getAndThrowIfNull(treeObject.get("marginY"), "Tree Margin Y Cannot Be Null In Config! Tree Name: " + treeObject.get("name").getAsString());

        ModTree tree = new ModTree(treeObject.get("name").getAsString(), new ResourceLocation(treeObject.get("icon").getAsString()))
                .withBackgroundTexture(treeObject.get("backgroundTexture").getAsString())
                .withMarginX(treeObject.get("marginX").getAsInt())
                .withMarginY(treeObject.get("marginY").getAsInt())
                .withStartScrollX(treeObject.get("centeredX").getAsInt())
                .withStartScrollY(treeObject.get("centeredY").getAsInt());

        if (treeObject.getAsJsonPrimitive("iconNbt") != null) {
            try {
                tree.withIconNbt(TagParser.parseTag(treeObject.getAsJsonPrimitive("iconNbt").getAsString()));
            } catch (CommandSyntaxException e) {
                ModLockingMod.LOGGER.error("Icon Nbt " + treeObject.getAsJsonPrimitive("iconNbt").getAsString() + " is invalid for tree " + treeObject.get("name").getAsString());
            }
        }
        return tree;
    }

    public JsonElement serialize(ModTree tree, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject treeObject = new JsonObject();
        JsonUtil.addTreeAndAlertIfNull("name", tree.name, tree.name, treeObject, true);
        JsonUtil.addTreeAndAlertIfNull("backgroundTexture", tree.name, tree.backgroundTexture, treeObject, true);
        JsonUtil.addTreeAndAlertIfNull("icon", tree.name, tree.icon, treeObject, true);
        if (tree.iconNbt != null) {
            addTreeAndAlertIfNull("iconNbt", tree.name, tree.iconNbt.toString(), treeObject, false);
        }
        treeObject.addProperty("centeredX", tree.startScrollX);
        treeObject.addProperty("centeredY", tree.startScrollY);
        treeObject.addProperty("marginX", tree.marginX);
        treeObject.addProperty("marginY", tree.marginY);
        return treeObject;
    }
}

