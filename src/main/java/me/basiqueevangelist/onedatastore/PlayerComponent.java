package me.basiqueevangelist.onedatastore;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class PlayerComponent<T extends NBTSerializable> extends Component<T> {
    PlayerComponent(Identifier id, Supplier<T> defaultSupplier, Function<NbtCompound, T> deserializer) {
        super(id, defaultSupplier, deserializer);
    }

    public T getFor(MinecraftServer server, UUID playerId) {
        return OneDataStore.getPlayerEntry(server, playerId).get(this);
    }
}
