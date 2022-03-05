package me.basiqueevangelist.onedatastore;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class OneDataStoreState extends PersistentState {
    private final Map<UUID, PlayerDataEntry> players = new HashMap<>();
    private final Map<GlobalComponent<?>, NBTSerializable> components = new HashMap<>();

    public static OneDataStoreState getFrom(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager().getOrCreate(
            OneDataStoreState::new,
            OneDataStoreState::new,
            "onedatastore"
        );
    }

    private OneDataStoreState() {
        for (GlobalComponent<?> comp : OneDataStore.GLOBAL_COMPONENTS) {
            components.put(comp, comp.getDefaultSupplier().get());
        }
    }

    private OneDataStoreState(NbtCompound tag) {
        var playersTag = tag.getList("Players", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < playersTag.size(); i++) {
            var playerTag = playersTag.getCompound(i);

            UUID playerId = playerTag.getUuid("UUID");

            players.put(playerId, new PlayerDataEntry(playerId, playerTag));
        }

        for (GlobalComponent<?> comp : OneDataStore.GLOBAL_COMPONENTS) {
            var tagName = comp.getId().toString();

            if (tag.contains(tagName, NbtElement.COMPOUND_TYPE)) {
                try {
                    components.put(comp, comp.getDeserializer().apply(tag.getCompound(tagName)));
                    continue;
                } catch (Exception e) {
                    OneDataStore.LOGGER.error("Encountered error while deserializing {}", tagName, e);
                }
            }

            components.put(comp, comp.getDefaultSupplier().get());
        }
    }

    public PlayerDataEntry getFor(UUID playerId) {
        return players.computeIfAbsent(playerId, PlayerDataEntry::new);
    }

    @SuppressWarnings("unchecked")
    public <T extends NBTSerializable> T get(GlobalComponent<T> component) {
        return (T) components.get(component);
    }

    public Map<UUID, PlayerDataEntry> getPlayers() {
        return players;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        var playersTag = new NbtList();
        tag.put("Players", playersTag);

        for (var entry : players.entrySet()) {
            playersTag.add(entry.getValue().toTag(new NbtCompound()));
        }

        for (Map.Entry<GlobalComponent<?>, NBTSerializable> entry : components.entrySet()) {
            var tagName = entry.getKey().getId().toString();

            try {
                tag.put(tagName, entry.getValue().toTag(new NbtCompound()));
            } catch (Exception e) {
                OneDataStore.LOGGER.error("Encountered error while serializing {}", tagName, e);
            }
        }

        return tag;
    }

    @Override
    public boolean isDirty() {
        return true;
    }
}
