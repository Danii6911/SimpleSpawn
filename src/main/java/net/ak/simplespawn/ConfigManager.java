package net.ak.simplespawn;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("lobby_config.json").toFile();
    
    private static SimpleSpawnConfig activeConfig;

    public static class SimpleSpawnConfig {
        public String dimension = "simplespawn:lobby";
        public int x = 0;
        public int y = 65;
        public int z = 0;
        public float yaw = 0.0f;

        public void setSpawn(String dimension, int x, int y, int z, float yaw) {
            this.dimension = dimension;
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
        }
    }

    public static SimpleSpawnConfig getConfig() {
        if (activeConfig == null) {
            load();
        }
        return activeConfig;
    }

    public static void load() {
        try {
            if (!CONFIG_FILE.exists()) {
                activeConfig = new SimpleSpawnConfig();
                save();
                return;
            }
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                activeConfig = GSON.fromJson(reader, SimpleSpawnConfig.class);
            }
        } catch (Exception e) {
            activeConfig = new SimpleSpawnConfig(); 
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(activeConfig, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
