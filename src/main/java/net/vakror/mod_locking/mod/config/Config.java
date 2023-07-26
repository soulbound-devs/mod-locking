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
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class Config<P extends ConfigObject> {
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

    @NotNull
    private File getConfigDir() {
        File configDir;
        configDir = FMLPaths.CONFIGDIR.get().resolve("mod-locking/" + getSubPath()).toFile();
        return configDir;
    }

    @NotNull
    private File getConfigFile(String fileName) {
        File configDir;
        configDir = FMLPaths.CONFIGDIR.get().resolve("mod-locking/" + getSubPath() + "/" + fileName + ".json").toFile();
        return configDir;
    }

    public abstract String getSubPath();

    public abstract String getName();

    public String toString() {
        return this.getName();
    }

    public void readConfig(boolean overrideCurrent, Class<P> type) {
        if (!overrideCurrent) {
            ModLockingMod.LOGGER.info("Reading configs: " + this.getName());
            File[] configFiles = this.getConfigDir().listFiles(File::isFile);
            if (configFiles != null && configFiles.length != 0) {
                for (File file : configFiles) {
                    try (FileReader reader = new FileReader(file)) {
                        P object = (P) GSON.fromJson(reader, type);
                        this.add(object);
                    } catch (IOException e) {
                        System.out.println(e.getClass());
                        e.printStackTrace();
                        ModLockingMod.LOGGER.warn("Error with config {}, generating new", this);
                        this.generateConfig();
                    }
                }
            } else {
                this.generateConfig();
                ModLockingMod.LOGGER.warn("Config " + this.getName() + "not found, generating new");
            }
        } else {
            this.generateConfig();
            ModLockingMod.LOGGER.info("Successfully Overwrote Config: " + this.getName());
        }
    }

    public abstract void add(P object);

    protected boolean isValid() {
        return true;
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

    public abstract List<P> getObjects();

    protected abstract void reset();

    public void writeConfig() throws IOException {
        File cfgDIr = this.getConfigDir();
        if (!cfgDIr.exists() && !cfgDIr.mkdirs()) {
            return;
        }
        for (P object : getObjects()) {
            FileWriter writer = new FileWriter(getConfigFile(object.getFileName().replaceAll(" ", "_").replaceAll("[^A-Za-z0-9_]", "").toLowerCase()));
            GSON.toJson(object, writer);
            writer.flush();
            writer.close();
        }
    }
}

