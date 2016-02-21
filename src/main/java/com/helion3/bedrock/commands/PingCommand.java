package com.helion3.bedrock.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class PingCommand {
    private PingCommand() {}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
        .description(Text.of("Verify the server is responsive."))
        .executor((source, args) -> {
            source.sendMessage(Text.of("Pong!"));

            return CommandResult.success();
        }).build();
    }
}
