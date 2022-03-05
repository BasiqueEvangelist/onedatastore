package me.basiqueevangelist.onedatastore.testmod;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.basiqueevangelist.onedatastore.GlobalComponent;
import me.basiqueevangelist.onedatastore.OneDataStore;
import me.basiqueevangelist.onedatastore.PlayerComponent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class OneDataStoreTest implements ModInitializer {
    public static final GlobalComponent<TestComponent> TEST_GLOBAL = OneDataStore.registerGlobalComponent(
        new Identifier("onedatastore-testmod", "test_global"),
        TestComponent::new,
        TestComponent::new
    );

    public static final PlayerComponent<TestComponent> TEST_PLAYER = OneDataStore.registerPlayerComponent(
        new Identifier("onedatastore-testmod", "test_player"),
        TestComponent::new,
        TestComponent::new
    );

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(literal("read_global_component")
                .executes(ctx -> {
                    ctx.getSource().sendFeedback(new LiteralText("global component value is " + TEST_GLOBAL.getFrom(ctx.getSource().getServer()).value), false);

                    return 1;
                }));

            dispatcher.register(literal("write_global_component")
                .then(argument("value", IntegerArgumentType.integer())
                    .executes(ctx -> {
                        int value = IntegerArgumentType.getInteger(ctx, "value");

                        TEST_GLOBAL.getFrom(ctx.getSource().getServer()).value = value;

                        ctx.getSource().sendFeedback(new LiteralText("set global component value to " + value), false);

                        return 1;
                    })));

            dispatcher.register(literal("read_player_component")
                .executes(ctx -> {
                    ctx.getSource().sendFeedback(new LiteralText("player component value is " + TEST_PLAYER.getFor(ctx.getSource().getServer(), ctx.getSource().getPlayer().getUuid()).value), false);

                    return 1;
                }));

            dispatcher.register(literal("write_player_component")
                .then(argument("value", IntegerArgumentType.integer())
                    .executes(ctx -> {
                        int value = IntegerArgumentType.getInteger(ctx, "value");

                        TEST_PLAYER.getFor(ctx.getSource().getServer(), ctx.getSource().getPlayer().getUuid()).value = value;

                        ctx.getSource().sendFeedback(new LiteralText("set player component value to " + value), false);

                        return 1;
                    })));

        });
    }
}
