package net.ak.simplespawn;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.ak.simplespawn.*;

public class SimpleSpawnManager {

    public static void initialize(MinecraftServer server) {
        ServerWorld lobbyWorld = server.getWorld(CustomDimension.LOBBY_KEY);
        if (lobbyWorld != null) {
            SimpleSpawn.LOGGER.info("Native lobby dimension successfully mounted.");
        }
    }

    public static void tpToSpawn(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        ServerWorld targetWorld = server.getWorld(CustomDimension.LOBBY_KEY);

        if (targetWorld == null) {
            targetWorld = server.getOverworld();
        }

        ConfigManager.SimpleSpawnConfig config = ConfigManager.getConfig();
        double x = config.x + 0.5;
        double y = config.y;
        double z = config.z + 0.5;
        float yaw = config.yaw;

        player.teleport(targetWorld, x, y, z, yaw, 0.0F);
    }
}
