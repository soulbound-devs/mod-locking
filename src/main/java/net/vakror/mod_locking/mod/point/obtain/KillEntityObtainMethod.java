package net.vakror.mod_locking.mod.point.obtain;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vakror.mod_locking.mod.util.CodecUtils;

public class KillEntityObtainMethod extends PointObtainMethod {
    public static Codec<KillEntityObtainMethod> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("pointType").forGetter(KillEntityObtainMethod::getPointType),
            Codec.STRING.fieldOf("entity").forGetter(KillEntityObtainMethod::getEntityId),
            Codec.INT.fieldOf("amount").forGetter(KillEntityObtainMethod::getAmount)
    ).apply(instance, KillEntityObtainMethod::new));
    public String entityId;

    public KillEntityObtainMethod(String entity, int amount, String pointType) {
        super(amount, pointType);
        this.entityId = entity;
    }

    public KillEntityObtainMethod(String pointType, String entity, int amount) {
        super(amount, pointType);
        this.entityId = entity;
    }

    public String getEntityId() {
        return entityId;
    }
}
