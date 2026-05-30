package net.ak.simplespawn;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.ak.simplespawn.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleSpawn implements ModInitializer {
	public static final String MOD_ID = "simplespawn";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		CustomDimension.register();
		ConfigManager.load();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			SpawnCommand.register(dispatcher);
		});		

		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			if (world.getRegistryKey().equals(CustomDimension.LOBBY_KEY) && !player.hasPermissionLevel(2)) {
				player.sendMessage(Text.literal("§cYou cannot break blocks in the lobby!"), true);
				return ActionResult.FAIL; 
			}
			return ActionResult.PASS;
		});

		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (world.getRegistryKey().equals(CustomDimension.LOBBY_KEY) && !player.hasPermissionLevel(2)) {
				player.sendMessage(Text.literal("§cYou cannot build or interact here!"), true);
				return ActionResult.FAIL; 
			}
			return ActionResult.PASS;
		});

		ServerLifecycleEvents.SERVER_STARTED.register(SimpleSpawnManager::initialize);
		LOGGER.info("SimpleSpawn mod initialized.");

	}
}