package net.ak.simplespawn.mixin;

import com.llamalad7.mixinextras.injector.*;
import net.ak.simplespawn.*;
import net.minecraft.nbt.*;
import net.minecraft.server.*;
import net.minecraft.server.network.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import java.util.*;

@Mixin(JoinManager.class)
public abstract class JoinManagerMixin {
	
	@Shadow @Final private MinecraftServer server;
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @ModifyReturnValue(method = "loadPlayerData", at = @At("RETURN"))
	public Optional<NbtCompound> loadPlayerData(Optional<NbtCompound> original, ServerPlayerEntity player) {
		NbtCompound nbt = original.orElse(new NbtCompound());
		if (SimpleSpawnManager.isInitialSpawnPointActive(this.server) && SimpleSpawnMixinHandler.isNewPlayer(player)) {
			nbt = SimpleSpawnMixinHandler.modifySpawnRegistryPositionAndDimensionForNewPlayer(this.server, nbt);
			player.readNbt(nbt);
		} else if (SimpleSpawnManager.isSimpleSpawnPointActive(this.server) && SimpleSpawn.Simple_SPAWN_CONFIG.spawnAtSimpleSpawnOnEveryJoin) {
			nbt = SimpleSpawnMixinHandler.modifySpawnRegistryPositionAndDimensionForExistingPlayer(this.server, nbt);
			player.readNbt(nbt);
		}
		return Optional.of(nbt);
	}
}