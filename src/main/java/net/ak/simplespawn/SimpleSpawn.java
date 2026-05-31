package net.ak.simplespawn;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.util.ActionResult;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;
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

		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
			if (entity.getWorld().getRegistryKey().equals(CustomDimension.LOBBY_KEY)) {
				return false;
			}
			return true;
		});

		ServerTickEvents.START_SERVER_TICK.register(server -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				if (player.getWorld().getRegistryKey().equals(CustomDimension.LOBBY_KEY)) {
					player.getHungerManager().setFoodLevel(20);
					player.getHungerManager().setSaturationLevel(5.0F);
				}
			}
		});

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			ServerWorld lobbyWorld = server.getWorld(CustomDimension.LOBBY_KEY);
			if (lobbyWorld != null) {
				lobbyWorld.getGameRules().get(GameRules.DO_MOB_SPAWNING).set(false, server);
				lobbyWorld.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(false, server);
				lobbyWorld.setTimeOfDay(6000);
			}
		});

		ServerLifecycleEvents.SERVER_STARTED.register(SimpleSpawnManager::initialize);
		LOGGER.info("SimpleSpawn mod initialized.");

	}
}