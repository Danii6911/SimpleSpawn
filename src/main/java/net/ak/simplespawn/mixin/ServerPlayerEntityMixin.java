package net.ak.simplespawn.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "getRespawnTarget", at = @At("HEAD"), cancellable = true)
    private void overrideDefaultRespawnPoint(boolean alive, ServerPlayerEntity.RespawnAnchorReason reason, CallbackInfoReturnable<ServerPlayerEntity.RespawnTarget> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        

        //check spawn point 
        if (player.getSpawnPointPosition() != null) {
            return; 
        }

        ServerWorld lobbyWorld = player.getServer().getWorld(ModDimensions.LOBBY_KEY);
        if (lobbyWorld == null) {
            return; 
        }

        SimpleSpawnConfig config = SimpleSpawn.SIMPLE_SPAWN_CONFIG;
        Vec3d targetPos = new Vec3d(config.x + 0.5, config.y, config.z + 0.5);

        ServerPlayerEntity.RespawnTarget customTarget = new ServerPlayerEntity.RespawnTarget(
            lobbyWorld, 
            targetPos, 
            Vec3d.ZERO, // 0 velocity 
            config.yaw, 
            0.0F
        );

        cir.setReturnValue(customTarget); 
    }
}