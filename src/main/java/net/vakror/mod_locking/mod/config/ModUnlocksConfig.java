package net.vakror.mod_locking.mod.config;

import com.google.gson.annotations.Expose;
import net.vakror.mod_locking.mod.unlock.FineGrainedModUnlock;
import net.vakror.mod_locking.mod.unlock.ModUnlock;
import net.vakror.mod_locking.mod.unlock.Unlock;

import java.util.*;

public class ModUnlocksConfig extends Config {
    @Expose
    public List<ModUnlock> modUnlocks = new ArrayList<>();
    @Expose
    public List<FineGrainedModUnlock> fineGrainedUnlocks = new ArrayList<>();

    @Override
    public String getName() {
        return "unlocks";
    }

    public List<Unlock<?>> getAll() {
        List<Unlock<?>> all = new ArrayList<>();
        all.addAll(modUnlocks);
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
