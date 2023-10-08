package net.vakror.mod_locking.mod.config.configs;

import com.google.gson.annotations.Expose;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.individual.SimpleIndividualFileConfig;
import net.vakror.mod_locking.ModLockingMod;
import net.vakror.mod_locking.mod.tree.ModTree;

import java.util.ArrayList;
import java.util.List;

public class ModTreesConfig extends SimpleIndividualFileConfig<ModTree> {
    @Expose
    public List<ModTree> trees = new ArrayList<>();

    public static final ModTreesConfig INSTANCE = new ModTreesConfig();

    public ModTreesConfig() {
        super("mod-locking/trees", new ResourceLocation(ModLockingMod.MOD_ID, "trees"));
    }

    @Override
    public List<ModTree> getObjects() {
        return trees;
    }

    @Override
    public String getFileName(ModTree object) {
        return object.getName();
    }

    @Override
    protected void resetToDefault() {
    }

    @Override
    public Class<ModTree> getConfigObjectClass() {
        return ModTree.class;
    }
}
