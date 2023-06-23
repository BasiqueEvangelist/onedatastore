package me.basiqueevangelist.onedatastore.testmod;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.basiqueevangelist.onedatastore.api.Component;
import me.basiqueevangelist.onedatastore.api.DataStore;
import me.basiqueevangelist.onedatastore.api.PlayerDataEntry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class OneDataStoreTest implements ModInitializer {
    public static final Component<TestComponent, DataStore> TEST_GLOBAL = Component.registerGlobal(
        new Identifier("onedatastore-testmod", "test_global"),
        TestComponent::new
    );

    public static final Component<TestComponent, PlayerDataEntry> TEST_PLAYER = Component.registerPlayer(
        new Identifier("onedatastore-testmod", "test_player"),
        TestComponent::new
    );

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("read_global_component")
                .executes(ctx -> {
                    ctx.getSource().sendFeedback(() -> Text.literal("global component value is " + DataStore.getFor(ctx.getSource().getServer()).get(TEST_GLOBAL).value), false);

                    return 1;
                }));

            dispatcher.register(literal("write_global_component")
                .then(argument("value", IntegerArgumentType.integer())
                    .executes(ctx -> {
                        int value = IntegerArgumentType.getInteger(ctx, "value");

                        DataStore.getFor(ctx.getSource().getServer()).get(TEST_GLOBAL).value = value;

                        ctx.getSource().sendFeedback(() -> Text.literal("set global component value to " + value), false);

                        return 1;
                    })));

            dispatcher.register(literal("read_player_component")
                .executes(ctx -> {
                    ctx.getSource().sendFeedback(() -> Text.literal("player component value is " + DataStore.getFor(ctx.getSource().getServer()).getPlayer(ctx.getSource().getPlayer().getUuid(), TEST_PLAYER).value), false);

                    return 1;
                }));

            dispatcher.register(literal("write_player_component")
                .then(argument("value", IntegerArgumentType.integer())
                    .executes(ctx -> {
                        int value = IntegerArgumentType.getInteger(ctx, "value");

                        DataStore.getFor(ctx.getSource().getServer()).getPlayer(ctx.getSource().getPlayer().getUuid(), TEST_PLAYER).value = value;

                        ctx.getSource().sendFeedback(() -> Text.literal("set player component value to " + value), false);

                        return 1;
                    })));

        });
    }
}
