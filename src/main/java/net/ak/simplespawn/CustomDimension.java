package net.ak.simplespawn;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.item.Item;

public class CustomDimension {
    public static final String LOBBY_ID = "lobby";

    /**
     * Maps to: data/simplespawn/dimension/lobby.json
     */
    public static final RegistryKey<World> LOBBY_KEY = RegistryKey.of(
        RegistryKeys.WORLD, 
        Identifier.of(SimpleSpawn.MOD_ID, LOBBY_ID)
    );

    /**
     * Maps to: data/simplespawn/dimension_type/lobby.json
     */
    public static final RegistryKey<DimensionType> LOBBY_TYPE_KEY = RegistryKey.of(
        RegistryKeys.DIMENSION_TYPE, 
        Identifier.of(SimpleSpawn.MOD_ID, LOBBY_ID)
    );

    public static void register() {
        SimpleSpawn.LOGGER.info("Successfully registered native custom world keys for: " + SimpleSpawn.MOD_ID + ":" + LOBBY_ID);
    }
}


