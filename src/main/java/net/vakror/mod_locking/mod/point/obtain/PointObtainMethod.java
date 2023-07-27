package net.vakror.mod_locking.mod.point.obtain;

import net.minecraft.sounds.SoundEvent;
import net.vakror.mod_locking.mod.config.ConfigObject;

import java.util.Optional;

public abstract class PointObtainMethod implements ConfigObject {
    private int amount = 1;
    private final String pointType;
    private final String name;
    private final SoundEvent obtainSound;

    public PointObtainMethod(String name, int amount, String pointType) {
        this.amount = amount;
        this.pointType = pointType;
        this.name = name;
        this.obtainSound = null;
    }

    public PointObtainMethod(String name, int amount, String pointType, Optional<SoundEvent> obtainSound) {
        this.amount = amount;
        this.pointType = pointType;
        this.name = name;
        this.obtainSound = obtainSound.orElse(null);
    }

    public PointObtainMethod(String name, int amount, String pointType, SoundEvent obtainSound) {
        this.amount = amount;
        this.pointType = pointType;
        this.name = name;
        this.obtainSound = obtainSound;
    }

    public int getAmount() {
        return amount;
    }

    public String getPointType() {
        return pointType;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getFileName() {
        return name;
    }

    public Optional<SoundEvent> getObtainSound() {
        if (obtainSound == null) {
            return Optional.empty();
        }
        return Optional.of(obtainSound);
    }
}
