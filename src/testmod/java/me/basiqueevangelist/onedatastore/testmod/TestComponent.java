package me.basiqueevangelist.onedatastore.testmod;

import me.basiqueevangelist.onedatastore.NBTSerializable;
import net.minecraft.nbt.NbtCompound;

public class TestComponent implements NBTSerializable {
    public int value = 10;

    public TestComponent() {
    }

    public TestComponent(NbtCompound tag) {
        value = tag.getInt("Value");
    }

    @Override
    public NbtCompound toTag(NbtCompound tag) {
        tag.putInt("Value", value);

        return tag;
    }
}
