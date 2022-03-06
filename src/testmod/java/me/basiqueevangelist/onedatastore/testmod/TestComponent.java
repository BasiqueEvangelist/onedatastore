package me.basiqueevangelist.onedatastore.testmod;

import me.basiqueevangelist.onedatastore.api.ComponentInstance;
import me.basiqueevangelist.onedatastore.api.DataStore;
import me.basiqueevangelist.onedatastore.api.PlayerDataEntry;
import net.minecraft.nbt.NbtCompound;

public class TestComponent implements ComponentInstance {
    public int value = 10;

    public TestComponent(DataStore handle) {
        System.out.println("I got a " + handle);
    }

    public TestComponent(PlayerDataEntry entry) {
        System.out.println("I got a " + entry);
    }

    @Override
    public void fromTag(NbtCompound tag) {
        value = tag.getInt("Value");
    }

    @Override
    public NbtCompound toTag(NbtCompound tag) {
        tag.putInt("Value", value);

        return tag;
    }
}
