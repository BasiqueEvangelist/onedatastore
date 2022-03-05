package me.basiqueevangelist.onedatastore;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Component<T extends NBTSerializable> {
    private final Identifier id;
    private final Supplier<T> defaultSupplier;
    private final Function<NbtCompound, T> deserializer;

    Component(Identifier id, Supplier<T> defaultSupplier, Function<NbtCompound, T> deserializer) {
        this.id = id;
        this.defaultSupplier = defaultSupplier;
        this.deserializer = deserializer;
    }

    public Identifier getId() {
        return id;
    }

    public Supplier<T> getDefaultSupplier() {
        return defaultSupplier;
    }

    public Function<NbtCompound, T> getDeserializer() {
        return deserializer;
    }
}
