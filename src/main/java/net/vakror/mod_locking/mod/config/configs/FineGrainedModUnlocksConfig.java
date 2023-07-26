package net.vakror.mod_locking.mod.config.configs;

import com.google.gson.annotations.Expose;
import net.vakror.mod_locking.mod.config.Config;
import net.vakror.mod_locking.mod.config.ModConfigs;
import net.vakror.mod_locking.mod.unlock.FineGrainedModUnlock;
import net.vakror.mod_locking.mod.unlock.Unlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FineGrainedModUnlocksConfig extends Config<FineGrainedModUnlock> {
    @Expose
    public List<FineGrainedModUnlock> fineGrainedUnlocks = new ArrayList<>();

    @Override
    public String getName() {
        return "unlocks";
    }

    @Override
    public String getSubPath() {
        return "unlocks/fine-grained";
    }
    @Override
    public void add(FineGrainedModUnlock object) {
        this.fineGrainedUnlocks.add(object);
    }

    @Override
    public List<FineGrainedModUnlock> getObjects() {
        return fineGrainedUnlocks;
    }

    public List<Unlock<?>> getAll() {
        List<Unlock<?>> all = new ArrayList<>();
        all.addAll(ModConfigs.MOD_UNLOCKS.modUnlocks);
        all.addAll(fineGrainedUnlocks);
        return all;
    }
    @Override
    protected void reset() {
    }

    public static Map<String, Integer> createCostMap(String point, int count) {
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put(point, count);
        return hashMap;
    }

    public static Map<String, Integer> createCostMap(String point, String point1, int count, int count1) {
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put(point, count);
        hashMap.put(point1, count1);
        return hashMap;
    }
}
