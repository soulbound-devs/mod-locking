package net.vakror.mod_locking.mod.unlock;

import com.google.gson.annotations.Expose;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.vakror.mod_locking.locking.Restriction;

import java.util.*;

import static net.vakror.mod_locking.locking.Restriction.RESTRICTION_CODEC;
import static net.vakror.mod_locking.mod.util.CodecUtils.*;

public class ModUnlock extends Unlock<ModUnlock> {
    public static final Codec<ModUnlock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(ModUnlock::getName),
            COST_CODEC.fieldOf("cost").forGetter(ModUnlock::getCost),
            Codec.STRING.listOf().optionalFieldOf("requiredUnlocks").forGetter((ModUnlock::getOptionalRequiredUnlocksAsList)),
            RESTRICTION_CODEC.fieldOf("restrictions").forGetter(ModUnlock::getRestriction),
            Codec.FLOAT.fieldOf("x").forGetter(ModUnlock::getX),
            Codec.FLOAT.fieldOf("y").forGetter(ModUnlock::getY),
            Codec.STRING.listOf().fieldOf("modIds").forGetter((ModUnlock::getModIds)),
            Codec.STRING.fieldOf("icon").forGetter(ModUnlock::getIcon),
            CompoundTag.CODEC.optionalFieldOf("iconNbt").forGetter(ModUnlock::getOptionalIconNbt),
            Codec.STRING.fieldOf("description").forGetter(ModUnlock::getDescription),
            Codec.STRING.fieldOf("treeName").forGetter(ModUnlock::getTreeName),
            TYPE_MAP_CODEC.optionalFieldOf("itemOverrides").forGetter(ModUnlock::getItemOverrides),
            TYPE_MAP_CODEC.optionalFieldOf("blockOverrides").forGetter(ModUnlock::getBlockOverrides),
            TYPE_MAP_CODEC.optionalFieldOf("entityOverrides").forGetter(ModUnlock::getEntityOverrides)
            // up to 16 fields can be declared here
    ).apply(instance, ModUnlock::new));
    @Expose
    protected List<String> modIds = new ArrayList<>();
    @Expose
    protected Restriction restriction = Restriction.defaultRestrictions(true);
    @Expose
    public Map<String, Restriction> itemOverrides = new HashMap<String, Restriction>();
    @Expose
    public Map<String, Restriction> blockOverrides = new HashMap<String, Restriction>();
    @Expose
    public Map<String, Restriction> entityOverrides = new HashMap<String, Restriction>();

    public ModUnlock(String name, Map<String, Integer> costMap, String[] requiredUnlocks, float x, float y,String ... modIds) {
        super(name, costMap, requiredUnlocks, x, y);
        Collections.addAll(this.modIds, modIds);
    }

    public ModUnlock(String name, Map<String, Integer> costMap, Optional<List<String>> requiredUnlocks, Restriction restrictions ,float x, float y, List<String> modIds, String icon, Optional<CompoundTag> iconNbt, String description, String treeName, Optional<Map<String, Restriction>> itemOverrides, Optional<Map<String, Restriction>> blockOverrides, Optional<Map<String, Restriction>> entityOverrides) {
        super(name, costMap, getRequiredUnlocks(requiredUnlocks), x, y);
        this.withDescription(Component.literal(description));
        this.withIcon(icon);
        iconNbt.ifPresent(this::withIconNbt);
        itemOverrides.ifPresent(this::withItemOverrideMap);
        blockOverrides.ifPresent(this::withBlockOverrideMap);
        entityOverrides.ifPresent(this::withEntityOverrideMap);
        this.withTree(treeName);
        Collections.addAll(this.modIds, modIds.toArray(new String[0]));
    }

    public static String[] getRequiredUnlocks(Optional<List<String>> unlocks) {
        return unlocks.map(strings -> strings.toArray(new String[0])).orElseGet(() -> new String[]{""});
    }

    public Optional<Map<String, Restriction>> getItemOverrides() {
        if (itemOverrides.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(itemOverrides);
    }

    public Optional<Map<String, Restriction>> getBlockOverrides() {
        if (blockOverrides.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(blockOverrides);
    }

    public Optional<Map<String, Restriction>> getEntityOverrides() {
        if (entityOverrides.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(entityOverrides);
    }

    public ModUnlock(String name, Map<String, Integer> costMap, String requiredUnlock, float x, float y, String ... modIds) {
        super(name, costMap, requiredUnlock, x, y);
        Collections.addAll(this.modIds, modIds);
    }

    public ModUnlock(String name, Map<String, Integer> costMap, float x, float y, String ... modIds) {
        super(name, costMap, x, y);
        Collections.addAll(this.modIds, modIds);
    }

    public ModUnlock withItemOverrideSimple(String item, boolean restricted) {
        this.itemOverrides.put(item, Restriction.defaultItemRestrictions(restricted));
        return this;
    }

    public ModUnlock withBlockItemOverrideSimple(String item, boolean restricted) {
        this.itemOverrides.put(item, Restriction.defaultItemRestrictions(restricted));
        this.blockOverrides.put(item, Restriction.defaultBlockRestrictions(restricted));
        return this;
    }

    public ModUnlock withBlockOverrideSimple(String block, boolean restricted) {
        this.blockOverrides.put(block, Restriction.defaultBlockRestrictions(restricted));
        return this;
    }

    public ModUnlock withEntityOverrideSimple(String entity, boolean restricted) {
        this.entityOverrides.put(entity, Restriction.defaultEntityRestrictions(restricted));
        return this;
    }

    public ModUnlock withItemOverride(String item, Restriction restriction) {
        this.itemOverrides.put(item, restriction);
        return this;
    }

    public ModUnlock withBlockOverride(String block, Restriction restriction) {
        this.blockOverrides.put(block, restriction);
        return this;
    }

    public ModUnlock withEntityOverride(String entity, Restriction restriction) {
        this.entityOverrides.put(entity, restriction);
        return this;
    }

    public ModUnlock withItemOverrideMap(Map<String, Restriction> restrictionMap) {
        this.itemOverrides.putAll(restrictionMap);
        return this;
    }

    public ModUnlock withBlockOverrideMap(Map<String, Restriction> restrictionMap) {
        this.blockOverrides.putAll(restrictionMap);
        return this;
    }

    public ModUnlock withEntityOverrideMap(Map<String, Restriction> restrictionMap) {
        this.entityOverrides.putAll(restrictionMap);
        return this;
    }

    public ModUnlock setItemOverrides(Map<String, Restriction> itemOverrides) {
        this.itemOverrides = itemOverrides;
        return this;
    }

    public ModUnlock setBlockOverrides(Map<String, Restriction> blockOverrides) {
        this.blockOverrides = blockOverrides;
        return this;
    }

    public ModUnlock setEntityOverrides(Map<String, Restriction> entityOverrides) {
        this.entityOverrides = entityOverrides;
        return this;
    }

    public List<String> getModIds() {
        return this.modIds;
    }

    public Restriction getRestriction() {
        return this.restriction;
    }

    public ModUnlock withRestrictions(boolean canHitBlock, boolean canInteractWithEntity, boolean canInteractWithBlock, boolean canUseItem, boolean canCraftItem) {
        this.restriction.set(Restriction.Type.HITTABILITY, canHitBlock);
        this.restriction.set(Restriction.Type.ENTITY_INTERACTABILITY, canInteractWithEntity);
        this.restriction.set(Restriction.Type.BLOCK_INTERACTABILITY, canInteractWithBlock);
        this.restriction.set(Restriction.Type.USABILITY, canUseItem);
        this.restriction.set(Restriction.Type.CRAFTABILITY, canCraftItem);
        return this;
    }

    @Override
    public <P> boolean restricts(P item, Restriction.Type restrictionType, boolean a) {
        if (!this.restriction.doesRestrict(restrictionType)) {
            return false;
        }
        ResourceLocation registryName = null;
        if (item instanceof Item) {
            registryName = ForgeRegistries.ITEMS.getKey((Item) item);
        }
        if (item instanceof Block) {
            registryName = ForgeRegistries.BLOCKS.getKey((Block) item);
        }
        if (item instanceof EntityType<?>) {
            registryName = ForgeRegistries.ENTITY_TYPES.getKey((EntityType<?>) item);
        }
        if (registryName == null) {
            return false;
        }
        boolean restricted = excludes(item, restrictionType, a);
        if (!restricted) {
            return false;
        }

        return this.modIds.contains(registryName.getNamespace());
    }

    @Override
    public boolean restricts(Item item, Restriction.Type restrictionType) {
        if (!this.restriction.doesRestrict(restrictionType)) {
            return false;
        }
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(item);
        if (registryName == null) {
            return false;
        }
        return this.modIds.contains(registryName.getNamespace()) && excludes(item, restrictionType, true);
    }

    @Override
    public boolean restricts(Block block, Restriction.Type restrictionType) {
        if (!this.restriction.doesRestrict(restrictionType)) {
            return false;
        }
        ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(block);
        if (registryName == null) {
            return false;
        }
        return this.modIds.contains(registryName.getNamespace()) && excludes(block, restrictionType, true);
    }

    @Override
    public boolean restricts(EntityType<?> entityType, Restriction.Type restrictionType) {
        if (!this.restriction.doesRestrict(restrictionType)) {
            return false;
        }
        ResourceLocation registryName = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        if (registryName == null) {
            return false;
        }
        return this.modIds.contains(registryName.getNamespace()) && excludes(entityType, restrictionType, true);
    }

    public <P> boolean excludes(P item, Restriction.Type restrictionType, boolean a) {
        ResourceLocation registryName = null;
        if (item instanceof Item) {
            registryName = ForgeRegistries.ITEMS.getKey((Item) item);
        }
        if (item instanceof Block) {
            registryName = ForgeRegistries.BLOCKS.getKey((Block) item);
        }
        if (item instanceof EntityType<?>) {
            registryName = ForgeRegistries.ENTITY_TYPES.getKey((EntityType<?>) item);
        }
        if (registryName == null) {
            return true;
        }
        String sid = registryName.toString();
        Restriction restriction = null;
        if (item instanceof Item) {
            restriction = this.itemOverrides.get(sid);
        }
        if (item instanceof Block) {
            restriction = this.blockOverrides.get(sid);
        }
        if (item instanceof EntityType<?>) {
            restriction = this.entityOverrides.get(sid);
        }
        if (restriction == null) {
            return true;
        }
        return restriction.doesRestrict(restrictionType);
    }
}

