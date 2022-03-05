package me.basiqueevangelist.onedatastore;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataEntry {
    private final UUID playerId;
    private final Map<PlayerComponent<?>, NBTSerializable> components = new HashMap<>();

    PlayerDataEntry(UUID playerId) {
        this.playerId = playerId;

        for (PlayerComponent<?> comp : OneDataStore.PLAYER_COMPONENTS) {
            components.put(comp, comp.getDefaultSupplier().get());
        }
    }

    PlayerDataEntry(UUID playerId, NbtCompound tag) {
        this.playerId = playerId;

        for (PlayerComponent<?> comp : OneDataStore.PLAYER_COMPONENTS) {
            var tagName = comp.getId().toString();

            if (tag.contains(tagName, NbtElement.COMPOUND_TYPE)) {
                try {
                    components.put(comp, comp.getDeserializer().apply(tag.getCompound(tagName)));
                    continue;
                } catch (Exception e) {
                    OneDataStore.LOGGER.error("Encountered error while deserializing {} for {}", tagName, playerId, e);
                }
            }

            components.put(comp, comp.getDefaultSupplier().get());
        }
    }

    public UUID getPlayerId() {
        return playerId;
    }

    @SuppressWarnings("unchecked")
    public <T extends NBTSerializable> T get(PlayerComponent<T> component) {
        return (T) components.get(component);
    }

    NbtCompound toTag(NbtCompound tag) {
        tag.put("UUID", NbtHelper.fromUuid(playerId));

        for (Map.Entry<PlayerComponent<?>, NBTSerializable> entry : components.entrySet()) {
            var tagName = entry.getKey().getId().toString();

            try {
                tag.put(tagName, entry.getValue().toTag(new NbtCompound()));
            } catch (Exception e) {
                OneDataStore.LOGGER.error("Encountered error while serializing {} for {}", tagName, playerId, e);
            }
        }

        return tag;
    }
}
