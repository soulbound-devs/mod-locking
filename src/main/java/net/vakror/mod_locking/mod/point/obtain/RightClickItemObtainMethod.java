package net.vakror.mod_locking.mod.point.obtain;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.Item;
import net.vakror.mod_locking.mod.util.CodecUtils;

public class RightClickItemObtainMethod extends PointObtainMethod {

    public static Codec<RightClickItemObtainMethod> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("pointType").forGetter(RightClickItemObtainMethod::getPointType),
            Codec.STRING.fieldOf("item").forGetter(RightClickItemObtainMethod::getItem),
            Codec.STRING.fieldOf("type").forGetter((obtainMethod) -> getType()),
            Codec.INT.fieldOf("amount").forGetter(RightClickItemObtainMethod::getAmount)
    ).apply(instance, RightClickItemObtainMethod::new));
    public String itemId;

    public RightClickItemObtainMethod(String item, int amount, String pointType) {
        super(amount, pointType);
        this.itemId = item;
    }

    public RightClickItemObtainMethod(String pointType, String item, String type, int amount) {
        super(amount, pointType);
        this.itemId = item;
    }

    public String getItem() {
        return itemId;
    }

    public static String getType() {
        return "useItem";
    }
}
