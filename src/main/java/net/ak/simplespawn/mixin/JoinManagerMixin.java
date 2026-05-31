package net.ak.simplespawn.mixin;

import net.ak.simplespawn.ConfigManager;
import net.ak.simplespawn.CustomDimension;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class) 
public class JoinManagerMixin {

    @Inject(method = "loadPlayerData", at = @At("RETURN"))
    private void spawnNewPlayerInLobby(ServerPlayerEntity player, CallbackInfoReturnable<NbtCompound> cir) {
        NbtCompound loadedNbt = cir.getReturnValue();

        if (loadedNbt == null) {
            ConfigManager.SimpleSpawnConfig config = ConfigManager.getConfig();
            
            ServerWorld lobbyWorld = player.getServer().getWorld(CustomDimension.LOBBY_KEY);
            
            if (lobbyWorld != null) {
                player.teleport(
                    lobbyWorld,
                    config.x + 0.5, 
                    config.y,
                    config.z + 0.5,
                    config.yaw,
                    0.0F
                );
            }
        }
    }
}