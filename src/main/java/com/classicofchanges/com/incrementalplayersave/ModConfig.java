package com.classicofchanges.com.incrementalplayersave;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("incrementalplayersave.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LoggerFactory.getLogger(IncrementalPlayerSave.MOD_ID);

    private int ticksBetweenSaveAttempts = 20;
    private int ticksBetweenSavesPerPlayer = 6000;
    private int maxPlayersSavedPerAttempt = 3;
    private boolean enableDebugLog = false;

    private static ModConfig instance = new ModConfig();
    public static ModConfig getInstance() {
        return instance;
    }

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (FileReader reader = new FileReader(CONFIG_PATH.toFile())) {
                instance = GSON.fromJson(reader, ModConfig.class);
            } catch (Exception e) {
                LOGGER.error("Failed to load Incremental Player Save config! Using defaults.", e);
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
            GSON.toJson(instance, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to save Incremental Player Save config!", e);
        }
    }

    public int getTicksBetweenSaveAttempts() {
        return ticksBetweenSaveAttempts;
    }

    public int getTicksBetweenSavesPerPlayer() {
        return ticksBetweenSavesPerPlayer;
    }

    public int getMaxPlayersSavedPerAttempt() {
        return maxPlayersSavedPerAttempt;
    }

    public boolean isEnableDebugLog() {
        return enableDebugLog;
    }
}