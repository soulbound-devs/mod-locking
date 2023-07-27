package net.vakror.mod_locking.mod.point.obtain;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.sounds.SoundEvent;
import net.vakror.mod_locking.mod.util.CodecUtils;

import java.util.Optional;

public class KillEntityObtainMethod extends PointObtainMethod {


    //TODO: Add optional particle and sound effects to be played when point is obtained
    public static Codec<KillEntityObtainMethod> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("pointType").forGetter(KillEntityObtainMethod::getPointType),
            Codec.STRING.fieldOf("type").forGetter((obtainMethod) -> getType()),
            Codec.STRING.fieldOf("entity").forGetter(KillEntityObtainMethod::getEntityId),
            SoundEvent.CODEC.optionalFieldOf("obtainSound").forGetter(KillEntityObtainMethod::getObtainSound),
            Codec.INT.fieldOf("amount").forGetter(KillEntityObtainMethod::getAmount)
    ).apply(instance, KillEntityObtainMethod::new));
    public String entityId;

    public KillEntityObtainMethod(String entity, int amount, String pointType) {
        super(pointType + "_" + entity, amount, pointType);
        this.entityId = entity;
    }

    public KillEntityObtainMethod(String pointType, String type, String entity, Optional<SoundEvent> obtainSound, int amount) {
        super(pointType + "_" + entity, amount, pointType, obtainSound);
        this.entityId = entity;
    }

    public KillEntityObtainMethod(String pointType, String type, String entity, SoundEvent obtainSound, int amount) {
        super(pointType + "_" + entity, amount, pointType, obtainSound);
        this.entityId = entity;
    }

    public String getEntityId() {
        return entityId;
    }

    public static String getType() {
        return "killEntity";
    }
}
