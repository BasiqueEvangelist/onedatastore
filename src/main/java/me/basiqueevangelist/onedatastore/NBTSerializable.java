package me.basiqueevangelist.onedatastore;

import net.minecraft.nbt.NbtCompound;

public interface NBTSerializable {
    NbtCompound toTag(NbtCompound tag);
}
