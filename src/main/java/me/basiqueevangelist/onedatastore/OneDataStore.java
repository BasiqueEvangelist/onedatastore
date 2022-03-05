package me.basiqueevangelist.onedatastore;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class OneDataStore implements ModInitializer {
    static final Logger LOGGER = LoggerFactory.getLogger("OneDataStore");

    static final List<PlayerComponent<?>> PLAYER_COMPONENTS = new ArrayList<>();
    static final List<GlobalComponent<?>> GLOBAL_COMPONENTS = new ArrayList<>();

    @Override
    public void onInitialize() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            OneDataStoreState.getFrom(server).getFor(handler.player.getUuid());
        });
    }

    public static <T extends NBTSerializable> PlayerComponent<T> registerPlayerComponent(Identifier id, Supplier<T> defaultSupplier, Function<NbtCompound, T> deserializer) {
        PlayerComponent<T> component = new PlayerComponent<>(id, defaultSupplier, deserializer);
        PLAYER_COMPONENTS.add(component);
        return component;
    }

    public static <T extends NBTSerializable> GlobalComponent<T> registerGlobalComponent(Identifier id, Supplier<T> defaultSupplier, Function<NbtCompound, T> deserializer) {
        GlobalComponent<T> component = new GlobalComponent<>(id, defaultSupplier, deserializer);
        GLOBAL_COMPONENTS.add(component);
        return component;
    }

    public static PlayerDataEntry getPlayerEntry(MinecraftServer server, UUID id) {
        return OneDataStoreState.getFrom(server).getFor(id);
    }

    public static Map<UUID, PlayerDataEntry> getPlayers(MinecraftServer server) {
        return OneDataStoreState.getFrom(server).getPlayers();
    }
}
