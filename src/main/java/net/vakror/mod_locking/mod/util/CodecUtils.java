package net.vakror.mod_locking.mod.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.vakror.mod_locking.locking.Restriction;

import java.util.Map;

import static net.vakror.mod_locking.locking.Restriction.RESTRICTION_CODEC;

public class CodecUtils {
    public static final Codec<Map<String, Integer>> COST_CODEC = Codec.unboundedMap(Codec.STRING, Codec.INT);
    public static final Codec<Map<Restriction.Type, Boolean>> RESTRICTION_MAP_CODEC = Codec.unboundedMap(Restriction.Type.CODEC, Codec.BOOL);

    public static final Codec<Map<String, Restriction>> TYPE_MAP_CODEC = Codec.unboundedMap(Codec.STRING, RESTRICTION_CODEC);

//    public static final Codec<Item> ITEM_CODEC = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY).registry(Registries.ITEM).get().byNameCodec();
}
