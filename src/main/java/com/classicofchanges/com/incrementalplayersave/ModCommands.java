package com.classicofchanges.com.incrementalplayersave;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;

public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("incrementalplayersave")
                    .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                    .then(Commands.literal("reload")
                            .executes(ModCommands::executeReload)
                    )
            );
        });
    }

    private static int executeReload(CommandContext<CommandSourceStack> context) {
        ModConfig.load();
        context.getSource().sendSuccess(() -> Component.literal("Incremental Player Save Config reloaded."), false);
        return 1;
    }
}