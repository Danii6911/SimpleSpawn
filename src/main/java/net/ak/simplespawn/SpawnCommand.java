package net.ak.simplespawn;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class SpawnCommand {

    public static void register(ServerCommandSource source) {
        // Command registration logic is called from SimpleSpawn via the event callback
    }

    public static void register() {
        // Command registration logic goes here
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("spawn")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ServerPlayerEntity player = source.getPlayer();

                    if(source.getEntity() instanceof ServerPlayerEntity) {
                        SimpleSpawnManager.tpToSpawn(player);
                        source.sendFeedback(() -> Text.literal("Teleported to lobby!"), false);
                        return 1;
                    } else {
                        source.sendError(Text.literal("This command can only be used by a player."));
                    }
                    return 0;
                })
                .then(CommandManager.literal("set")
                    .require(source -> source.hasPermissionLevel(2)) // op
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        if (source.getEntity() instanceof ServerPlayerEntity player) {
                            BlockPos pos = player.getBlockPos();
                            String dim = player.getWorld().getRegistryKey().getValue().toString();
                            ConfigManager.SimpleSpawnConfig config = ConfigManager.getConfig();
                            config.setSpawn(dim, pos.getX(), pos.getY(), pos.getZ(), player.getYaw());
                            
                            ConfigManager.save(config);
                            source.sendFeedback(() -> Text.literal("Spawn point updated successfully!"), true);
                            return 1;
                        }
                    return 0;
                    })
                )
            );
        });
    }
}
