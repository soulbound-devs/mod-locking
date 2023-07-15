package net.vakror.mod_locking.mod.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModTreeProvider implements ICapabilitySerializable<CompoundTag> {
    public static Capability<ModTreeCapability> MOD_TREE = CapabilityManager.get(new CapabilityToken<ModTreeCapability>() { });

    private ModTreeCapability modTreeCapability = null;
    private final LazyOptional<ModTreeCapability> optional = LazyOptional.of(this::createCap);

    private @NotNull ModTreeCapability createCap() {
        if (this.modTreeCapability == null) {
            this.modTreeCapability = new ModTreeCapability();
        }

        return this.modTreeCapability;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == MOD_TREE) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createCap().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createCap().loadNBTData(nbt);
    }
}