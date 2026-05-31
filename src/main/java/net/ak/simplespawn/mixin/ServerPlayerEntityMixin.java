package net.ak.simplespawn.mixin;

import net.minecraft.server.MinecraftServer;
import com.llamalad7.mixinextras.sugar.*;
import com.mojang.authlib.*;

import net.ak.simplespawn.ConfigManager;
import net.ak.simplespawn.CustomDimension;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.server.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    /**
     * Intercepts the player respawn target determination loop.
     * If the player does not have a valid bed or respawn anchor set, 
     * they are cleanly routed to the custom config coordinates inside the lobby dimension.
     */
    @Inject(
        method = "getRespawnTarget(ZLnet/minecraft/world/TeleportTarget$PostDimensionTransition;)Lnet/minecraft/world/TeleportTarget;", 
        at = @At("HEAD"), 
        cancellable = true
    )
    private void overrideDefaultRespawnPoint(
        boolean alive, 
        TeleportTarget.PostDimensionTransition postDimensionTransition, 
        CallbackInfoReturnable<TeleportTarget> cir
    ) {
        if(alive) {
            return; 
        }

        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        if (player.getSpawnPointPosition() != null) {
            return;
        }

        ServerWorld lobbyWorld = player.getServer().getWorld(CustomDimension.LOBBY_KEY);
        if (lobbyWorld == null) {
            return;
        }

        ConfigManager.SimpleSpawnConfig config = ConfigManager.getConfig(); // This will auto-load the config if it hasn't been loaded yet

        BlockPos lobbyBlockPos = new BlockPos(config.x, config.y, config.z);

        TeleportTarget customTarget = new TeleportTarget(
            lobbyWorld, 
            Vec3d.ofCenter(lobbyBlockPos), 
            Vec3d.ZERO,                    
            config.yaw, 
            0.0F,                         
            postDimensionTransition        
        );

        cir.setReturnValue(customTarget); 
    }
}