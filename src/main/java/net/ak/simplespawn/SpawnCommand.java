package net.ak.simplespawn;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.command.v2.*;
import net.minecraft.command.argument.*;
import net.minecraft.core.BlockPos;
import net.minecraft.registry.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;
public class SpawnCommand {
    

    public static void register() {
        // Command registration logic goes here
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("spawn")
                .executes(context -> {
                    ServerComandSource source = context.getSource();
                    ServerPlayerEntity player = source.getPlayer();

                    if(source.getEntity() instanceof ServerPlayerEntity) {


                        SimpleSpawnManager.tpToSpawn(player);
                        source.sendFeedback(new LiteralText("Teleported to lobby!"), false);
                        return 1;
                    } else {
                        source.sendError(new LiteralText("This command can only be used by a player."));
                    }
                    return 0;
                })
                .then(CommandManager.literal("set")
                    .require(source -> source.hasPermissionLevel(2)) // op
                    .executes(context -> {
                        ServerComandSource source = context.getSource();
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
