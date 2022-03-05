package me.basiqueevangelist.onedatastore;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.function.Function;
import java.util.function.Supplier;

public class GlobalComponent<T extends NBTSerializable> extends Component<T> {
    GlobalComponent(Identifier id, Supplier<T> defaultSupplier, Function<NbtCompound, T> deserializer) {
        super(id, defaultSupplier, deserializer);
    }

    public T getFrom(MinecraftServer server) {
        return OneDataStoreState.getFrom(server).get(this);
    }
}
