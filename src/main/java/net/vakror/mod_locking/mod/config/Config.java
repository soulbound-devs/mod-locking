package net.vakror.mod_locking.mod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fml.loading.FMLPaths;
import net.vakror.mod_locking.ModLockingMod;
import net.vakror.mod_locking.mod.config.adapter.*;
import net.vakror.mod_locking.mod.point.ModPoint;
import net.vakror.mod_locking.mod.point.obtain.KillEntityObtainMethod;
import net.vakror.mod_locking.mod.point.obtain.RightClickItemObtainMethod;
import net.vakror.mod_locking.mod.tree.ModTree;
import net.vakror.mod_locking.mod.unlock.FineGrainedModUnlock;
import net.vakror.mod_locking.mod.unlock.ModUnlock;

import java.io.*;
import java.lang.reflect.Field;

public abstract class Config {
    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization()
            .registerTypeAdapter(CompoundTag.class, CompoundTagAdapter.INSTANCE)
            .registerTypeAdapter(ModUnlock.class, ModUnlockAdapter.INSTANCE)
            .registerTypeAdapter(FineGrainedModUnlock.class, FineGrainedUnlockAdapter.INSTANCE)
            .registerTypeAdapter(ModPoint.class, ModPointAdapter.INSTANCE)
            .registerTypeAdapter(ModTree.class, ModTreeAdapter.INSTANCE)
            .registerTypeAdapter(RightClickItemObtainMethod.class, RightClickItemPointObtainMethodAdapter.INSTANCE)
            .registerTypeAdapter(KillEntityObtainMethod.class, KillEntityPointObtainMethodAdapter.INSTANCE)
            .setPrettyPrinting().create();

    public void generateConfig() {
        this.reset();
        try {
            this.writeConfig();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getConfigFile() {
        File configDir = FMLPaths.CONFIGDIR.get().resolve("mod-locking/config/").toFile();
        if (!configDir.exists() && configDir.mkdirs()) {
            return new File(configDir, getName() + ".json");
        }
        return new File(configDir, getName() + ".json");
    }

    public abstract String getName();

    public String toString() {
        return this.getName();
    }

    public <T extends Config> T readConfig(boolean overrideCurrent) {
        if (!overrideCurrent) {
            ModLockingMod.LOGGER.info("Reading config: " + this.getName());
            try (FileReader reader = new FileReader(this.getConfigFile())) {
                Config config = GSON.fromJson(reader, this.getClass());
                config.onLoad(this);
                if (!config.isValid()) {
                    ModLockingMod.LOGGER.error("Invalid config {}, using defaults", this);
                    config.reset();
                }
                Config config2 = config;
                return (T) config2;
            } catch (IOException e) {
                System.out.println(e.getClass());
                e.printStackTrace();
                ModLockingMod.LOGGER.warn("Config file {} not found, generating new", this);
                this.generateConfig();
                return (T) this;
            }
        } else {
            this.generateConfig();
            ModLockingMod.LOGGER.info("Successfully Overwrote Config: " + this.getName());
            return (T) this;
        }
    }

    protected boolean isValid() {
        return true;
    }

    protected void onLoad(Config oldConfigInstance) {
    }

    public static boolean checkAllFieldsAreNotNull(Object o) throws IllegalAccessException {
        for (Field v : o.getClass().getDeclaredFields()) {
            boolean b;
            if (!v.canAccess(o)) continue;
            Object field = v.get(o);
            if (field == null) {
                return false;
            }
            if (field.getClass().isPrimitive() || (b = Config.checkAllFieldsAreNotNull(field))) continue;
            return false;
        }
        return true;
    }

    protected abstract void reset();

    public void writeConfig() throws IOException {
        File cfgFile = this.getConfigFile();
        File dir = cfgFile.getParentFile();
        if (!dir.exists() && !dir.mkdirs()) {
            return;
        }
        if (!cfgFile.exists() && !cfgFile.createNewFile()) {
            return;
        }
        FileWriter writer = new FileWriter(cfgFile);
        GSON.toJson(this, writer);
        writer.flush();
        writer.close();
    }
}

