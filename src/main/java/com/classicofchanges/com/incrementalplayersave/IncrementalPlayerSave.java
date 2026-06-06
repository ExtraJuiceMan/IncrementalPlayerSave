package com.classicofchanges.com.incrementalplayersave;

import com.classicofchanges.com.incrementalplayersave.duck.IncrementalSaveAccess;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncrementalPlayerSave implements DedicatedServerModInitializer, ModInitializer {
	public static final String MOD_ID = "incrementalplayersave";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModConfig.load();
		ModCommands.register();
		LOGGER.info("Incremental Player Save loaded.");
	}

	@Override
	public void onInitializeServer() {
		ServerTickEvents.END_SERVER_TICK.register((server) -> {
			ModConfig config = ModConfig.getInstance();
			IncrementalSaveAccess saveAccess = (IncrementalSaveAccess) server.getPlayerList();
			if (!saveAccess.incrementalplayersave$getFullySavedLastRound()
				|| server.getTickCount() % config.getTicksBetweenSaveAttempts() == 0) {
				saveAccess.incrementalplayersave$saveAllIncremental(
						config.getTicksBetweenSavesPerPlayer(),
						config.getMaxPlayersSavedPerAttempt()
				);
			}
		});
	}
}