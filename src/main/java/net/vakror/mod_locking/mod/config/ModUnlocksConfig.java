package net.vakror.mod_locking.mod.config;

import com.google.gson.annotations.Expose;
import net.minecraft.network.chat.Component;
import net.vakror.mod_locking.locking.Restriction;
import net.vakror.mod_locking.mod.unlock.FineGrainedModUnlock;
import net.vakror.mod_locking.mod.unlock.ModUnlock;
import net.vakror.mod_locking.mod.unlock.Unlock;

import java.util.*;

public class ModUnlocksConfig extends Config {
    @Expose
    public List<Unlock> unlocks = new ArrayList<>();

    @Override
    public String getName() {
        return "unlocks";
    }

    public List<Unlock> getAll() {
        return new ArrayList<>(this.unlocks);
    }
    @Override
    protected void reset() {
    }

    public Map<String, Integer> createCostMap(String point, int count) {
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put(point, count);
        return hashMap;
    }

    public Map<String, Integer> createCostMap(String point, String point1, int count, int count1) {
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put(point, count);
        hashMap.put(point1, count1);
        return hashMap;
    }

    public Map<String, Integer> getAllPoints() {
        Map<String, Integer> costMap = new HashMap<>();

        unlocks.forEach((unlock -> costMap.putAll(unlock.getCost())));

        return costMap;
    }
}
