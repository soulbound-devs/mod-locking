package net.vakror.mod_locking.mod.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.vakror.mod_locking.locking.Restriction;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.point.ModPoint;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.unlock.FineGrainedModUnlock;
import net.vakror.mod_locking.mod.unlock.ModUnlock;
import net.vakror.mod_locking.mod.unlock.Unlock;

import java.util.*;

public class NbtUtil {
    public static void serializeTrees(CompoundTag nbt, List<ModTree> trees) {
        String[] treeNames = new String[trees.size()];

        final int[] i = {0};
        trees.forEach((modTree -> {
            nbt.put("tree_" + modTree.name, modTree.serializeNBT());
            treeNames[i[0]] = modTree.name;
            i[0]++;
        }));

        nbt.putString("treeNames", Arrays.toString(treeNames));
    }

    public static List<ModTree> deserializeTrees(CompoundTag nbt) {
        String[] treeNames = toArray(nbt.getString("treeNames"));

        List<ModTree> trees = new ArrayList<>(treeNames.length);
        Arrays.stream(treeNames).forEach((treeName -> {
            ModTree tree = new ModTree("", new ResourceLocation("minecraft:dirt"));
            tree.deserializeNBT(nbt.getCompound("tree_" + treeName));
            trees.add(tree);
        }));

        return trees;
    }

    public static void serializeUnlocks(CompoundTag nbt, List<Unlock<?>> unlocks) {
        String[] unlockNames = new String[unlocks.size()];

        final int[] i = {0};
        unlocks.forEach((unlock -> {
            nbt.put("unlock_" + unlock.getName(), serializeUnlock(unlock));
            unlockNames[i[0]] = unlock.getName();
            i[0]++;
        }));

        nbt.putString("unlockNames", Arrays.toString(unlockNames));
    }

    public static List<Unlock<?>> deserializeUnlocks(CompoundTag nbt) {
        String[] unlockNames = toArray(nbt.getString("unlockNames"));

        List<Unlock<?>> unlocks = new ArrayList<>(unlockNames.length);
        Arrays.stream(unlockNames).forEach((unlockName -> {
            unlocks.add(deserializeUnlock(nbt.getCompound("unlock_" + unlockName)));
        }));

        return unlocks;
    }

    public static Unlock<?> deserializeUnlock(CompoundTag compound) {
        return (compound.getString("type").equals("mod") ? deserializeModUnlock(compound.getString("name"), compound, deserializePoints(compound), compound.getFloat("x"), compound.getFloat("y"), toArray(compound.getString("requiredUnlock"))):
                deserializeFineGrainedUnlock(compound, compound.getString("name"), deserializePoints(compound), compound.getFloat("x"), compound.getFloat("y"), toArray(compound.getString("requiredUnlock")))).withDescription(Component.literal(compound.getString("description"))).withIcon(compound.getString("icon")).withIconNbt(compound.getCompound("iconNbt")).withTree(compound.getString("tree"));
    }

    public static CompoundTag serializeUnlock(Unlock<?> unlock) {
        CompoundTag nbt = new CompoundTag();

        nbt.putString("name", unlock.getName());
        nbt.putString("type", (unlock instanceof ModUnlock ? "mod": "fine-grained"));
        nbt.putFloat("x", unlock.getX());
        nbt.putFloat("y", unlock.getY());
        nbt.putString("description", unlock.getDescription());
        nbt.putString("icon", unlock.getIcon());
        nbt.putString("tree", unlock.getTree());
        nbt.put("iconNbt", (unlock.getIconNbt() == null ? new CompoundTag(): unlock.getIconNbt()));
        nbt.putString("requiredUnlock", (unlock.getRequiredUnlocks() == null ? "": Arrays.toString(unlock.getRequiredUnlocks())));
        serializePoints(nbt, unlock.getCost());
        if (unlock instanceof ModUnlock modUnlock) {
            serializeModUnlock(nbt, modUnlock);
        } else {
            serializeFineGrainedUnlock(nbt, (FineGrainedModUnlock) unlock);
        }
        return nbt;
    }

    private static void serializeFineGrainedUnlock(CompoundTag nbt, FineGrainedModUnlock unlock) {
        CompoundTag compoundTag = new CompoundTag();
        CompoundTag tag = new CompoundTag();
        serializeRestriction(tag, unlock.getItemRestrictions(), "item");
        compoundTag.put("item", tag);
        CompoundTag tag1 = new CompoundTag();
        serializeRestriction(tag1, unlock.getBlockRestrictions(), "block");
        compoundTag.put("block", tag1);
        CompoundTag tag2 = new CompoundTag();
        serializeRestriction(tag2, unlock.getEntityRestrictions(), "entity");
        compoundTag.put("entity", tag2);
        nbt.put("restrictions", compoundTag);
    }

    private static Unlock<FineGrainedModUnlock> deserializeFineGrainedUnlock(CompoundTag nbt, String name, Map<String, Integer> cost, float x, float y,String[] requiredUnlock) {
        FineGrainedModUnlock unlock = new FineGrainedModUnlock(name, cost, x, y,requiredUnlock);
        unlock.setItemRestrictions(deserializeRestriction(nbt.getCompound("restrictions").getCompound("item"), "item"));
        unlock.setBlockRestrictions(deserializeRestriction(nbt.getCompound("restrictions").getCompound("block"), "block"));
        unlock.setEntityRestrictions(deserializeRestriction(nbt.getCompound("restrictions").getCompound("entity"), "entity"));
        return unlock;
    }

    private static void serializeRestriction(CompoundTag nbt, Map<String, Restriction> restrictions, String a) {
        restrictions.forEach((name, restriction) -> {
            serializeRestrictions(nbt, restriction, a, name);
            nbt.putInt("size", restrictions.size());
        });
    }

    private static Map<String, Restriction> deserializeRestriction(CompoundTag nbt, String a) {
        Map<String, Restriction> map = new HashMap<>(nbt.getInt("size"));
        nbt.getAllKeys().forEach((key -> {
            if (!key.equals("size")) {
                map.put(key, deserializeRestriction(nbt.getCompound(key)));
            }
        }));
        return map;
    }

    public static Restriction deserializeRestriction(CompoundTag nbt) {
        Restriction restriction = new Restriction();
        for (String key : nbt.getAllKeys()) {
            if (!key.equals("size")) {
                restriction.set(Restriction.Type.valueOf(key), nbt.getBoolean(key));
            }
        }
        return restriction;
    }

    private static void serializeModUnlock(CompoundTag nbt, ModUnlock unlock) {
        nbt.putString("modIds", Arrays.toString(unlock.getModIds().toArray()));
        serializeRestrictions(nbt, unlock.getRestriction());
    }

    private static ModUnlock deserializeModUnlock(String name, CompoundTag nbt, Map<String, Integer> cost, float x, float y, String[] requiredUnlock) {
        return new ModUnlock(name, cost, requiredUnlock, x, y, toArray(nbt.getString("modIds")));
    }

    private static void serializeRestrictions(CompoundTag nbt, Restriction restriction) {
        CompoundTag tag = new CompoundTag();
        restriction.getRestrictions().forEach((restrictionType, doesRestrict) -> tag.putBoolean(restrictionType.toString(), doesRestrict));
        nbt.put("restrictions", tag);
    }

    private static void serializeRestrictions(CompoundTag nbt, Restriction restriction, String a) {
        CompoundTag tag = new CompoundTag();
        restriction.getRestrictions().forEach((restrictionType, doesRestrict) -> tag.putBoolean(restrictionType.toString(), doesRestrict));
        nbt.put(a, tag);
    }

    private static void serializeRestrictions(CompoundTag nbt, Restriction restriction, String a, String name) {
        CompoundTag tag = new CompoundTag();
        restriction.getRestrictions().forEach((restrictionType, doesRestrict) -> tag.putBoolean(restrictionType.toString(), doesRestrict));
        nbt.put(name, tag);
    }

    private static Restriction deserializeRestrictions(CompoundTag nbt) {
        CompoundTag tag = nbt.getCompound("restrictions");
        return new Restriction().set(Restriction.Type.USABILITY, tag.getBoolean("USABILITY")).set(Restriction.Type.CRAFTABILITY, tag.getBoolean("CRAFTABILITY")).set(Restriction.Type.HITTABILITY, tag.getBoolean("HITTABILITY")).set(Restriction.Type.BLOCK_INTERACTABILITY, tag.getBoolean("BLOCK_INTERACTABILITY")).set(Restriction.Type.ENTITY_INTERACTABILITY, tag.getBoolean("ENTITY_INTERACTABILITY"));
    }

    private static Restriction deserializeRestrictions(CompoundTag nbt, String a) {
        CompoundTag tag = nbt.getCompound(a);
        return new Restriction().set(Restriction.Type.USABILITY, tag.getBoolean("USABILITY")).set(Restriction.Type.CRAFTABILITY, tag.getBoolean("CRAFTABILITY")).set(Restriction.Type.HITTABILITY, tag.getBoolean("HITTABILITY")).set(Restriction.Type.BLOCK_INTERACTABILITY, tag.getBoolean("BLOCK_INTERACTABILITY")).set(Restriction.Type.ENTITY_INTERACTABILITY, tag.getBoolean("ENTITY_INTERACTABILITY"));
    }

    public static Map<String, Integer> deserializePoints(CompoundTag nbt) {
        String[] pointTypes = toArray(nbt.getString("pointTypes"));

        if (pointTypes.length != 0) {
            Map<String, Integer> points = new HashMap<>(pointTypes.length);
            Arrays.stream(pointTypes).forEach((pointType -> points.put(pointType, nbt.getInt("point_amount_" + pointType))));
            return points;
        }
        return new HashMap<>();
    }

    public static Map<String, Integer> deserializePointColors(CompoundTag nbt) {
        String[] pointTypes = toArray(nbt.getString("pointTypes"));

        if (pointTypes.length != 0) {
            Map<String, Integer> points = new HashMap<>(pointTypes.length);
            Arrays.stream(pointTypes).forEach((pointType -> points.put(pointType, nbt.getInt("point_color_" + pointType))));
            return points;
        }
        return new HashMap<>();
    }

    public static Map<String, String> deserializePointPluralNames(CompoundTag nbt) {
        String[] pointTypes = toArray(nbt.getString("pointTypes"));

        if (pointTypes.length != 0) {
            Map<String, String> points = new HashMap<>(pointTypes.length);
            Arrays.stream(pointTypes).forEach((pointType -> points.put(pointType, nbt.getString("point_plural_name_" + pointType))));
            return points;
        }
        return new HashMap<>();
    }
    public static void serializePoints(CompoundTag nbt, Map<String, Integer> points) {
        String[] pointTypes = new String[points.size()];

        final int[] i = {0};
        points.forEach(((point, amount) -> {
            nbt.putInt("point_amount_" + point, amount);
            nbt.putString("point_plural_name_" + point, getPoint(point).pluralName);
            nbt.putInt("point_color_" + point, getPoint(point).getColor());
            pointTypes[i[0]] = point;
            i[0]++;
        }));

        nbt.putString("pointTypes", Arrays.toString(pointTypes));
    }

    public static ModPoint getPoint(String point) {
        for (ModPoint point1: ModConfigs.POINTS.points) {
            if (point1.name.equals(point)) {
                return point1;
            }
        }
        throw new IllegalArgumentException("Could Not Find Point Type: " + point);
    }

    public static ModPoint getPoint(String point, String errorMessage) {
        for (ModPoint point1: ModConfigs.POINTS.points) {
            if (point1.name.equals(point)) {
                return point1;
            }
        }
        throw new IllegalArgumentException(errorMessage);
    }

//    public static List<ModUnlockGroup> deserializeUnlockGroups(CompoundTag nbt) {
//        String[] groupNames = toArray(nbt.getString("groupNames"));
//
//        List<ModUnlockGroup> groups = new ArrayList<>(groupNames.length);
//        Arrays.stream(groupNames).forEach((groupName -> {
//            CompoundTag tag = nbt.getCompound("group_" + groupName);
//            groups.add(new ModUnlockGroup.Builder(tag.getString("name"))
//                    .withUnlocks(toArray(tag.getString("unlocks")))
//                    .withGlobalCostIncrease(tag.getFloat("globalCostIncrease"))
//                    .withGroupCostIncreases(deserializeGroupCostIncrease(tag.getCompound("groupCostIncrease")))
//                    .build());
//        }));
//
//        return groups;
//    }
//
//    public static void serializeUnlockGroups(CompoundTag nbt, List<ModUnlockGroup> groups) {
//        String[] groupNames = new String[groups.size()];
//
//        final int[] i = {0};
//        groups.forEach(((group) -> {
//            CompoundTag tag = new CompoundTag();
//            tag.putString("name", group.getTitle());
//            tag.putString("unlocks", Arrays.toString(group.getUnlocks().toArray()));
//            tag.putFloat("globalCostIncrease", group.getGlobalCostIncrease());
//            serializeGroupCostIncrease(tag, group.getGroupCostIncrease());
//            nbt.put("group_" + group.getTitle(), tag);
//            groupNames[i[0]] = group.getTitle();
//            i[0]++;
//        }));
//
//        nbt.putString("groupNames", Arrays.toString(groupNames));
//    }

    public static void serializeGroupCostIncrease(CompoundTag nbt, Map<String, Float> costIncrease) {
        CompoundTag tag = new CompoundTag();

        String[] costIncreaseGroups = new String[costIncrease.size()];

        final int[] i = {0};
        costIncrease.forEach(((groupName, amount) -> {
            tag.putFloat(groupName, amount);
            costIncreaseGroups[i[0]] = groupName;
            i[0]++;
        }));
        tag.putString("costIncreaseGroupNames", Arrays.toString(costIncreaseGroups));

        nbt.put("groupCostIncrease", tag);
    }

    public static Map<String, Float> deserializeGroupCostIncrease(CompoundTag nbt) {
        String[] costIncreaseGroupNames = toArray(nbt.getString("costIncreaseGroupNames"));

        Map<String, Float> groupCostIncrease = new HashMap<>(costIncreaseGroupNames.length);
        Arrays.stream(costIncreaseGroupNames).forEach((groupName -> groupCostIncrease.put(groupName, nbt.getFloat(groupName))));

        return groupCostIncrease;
    }

    public static String[] toArray(String input) {
        return input.replace("[", "").replace("]", "").split(", ");
    }
}
