package net.vakror.mod_locking.mod.config;

import com.google.gson.annotations.Expose;
import net.minecraft.resources.ResourceLocation;
import net.vakror.mod_locking.mod.tree.ModTree;

import java.util.ArrayList;
import java.util.List;

public class ModTreesConfig extends Config {
    @Expose
    public List<ModTree> trees = new ArrayList<>();

    @Override
    public String getName() {
        return "mod_trees";
    }

    @Override
    protected void reset() {
    }
}
