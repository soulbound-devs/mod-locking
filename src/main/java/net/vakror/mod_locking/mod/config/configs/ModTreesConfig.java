package net.vakror.mod_locking.mod.config.configs;

import com.google.gson.annotations.Expose;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.vakror.mod_locking.mod.config.Config;
import net.vakror.mod_locking.mod.tree.ModTree;

import java.util.ArrayList;
import java.util.List;

public class ModTreesConfig extends Config<ModTree> {
    @Expose
    public List<ModTree> trees = new ArrayList<>();

    @Override
    public String getSubPath() {
        return "mod_trees";
    }

    @Override
    public String getName() {
        return "mod_trees";
    }

    @Override
    public void add(ModTree object) {
        trees.add(object);
    }

    @Override
    public List<ModTree> getObjects() {
        return trees;
    }

    @Override
    protected void reset() {

    }

    public ModTree getTreeFromName(String name) {
        for (ModTree tree: trees) {
            if (tree.name.equals(name)) {
                return tree;
            }
        }
        return null;
    }
}
