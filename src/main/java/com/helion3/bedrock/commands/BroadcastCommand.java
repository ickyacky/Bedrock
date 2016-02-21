package com.helion3.bedrock.commands;

import com.helion3.bedrock.util.Format;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;

public class BroadcastCommand {
    private BroadcastCommand() {}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
            .arguments(GenericArguments.remainingJoinedStrings(Text.of("message")))
            .description(Text.of("Send a messag to all players."))
            .permission("bedrock.broadcast")
            .executor((source, args) -> {
                MessageChannel.TO_ALL.send(Format.broadcast(args.<String>getOne("message").get()));

                return CommandResult.success();
            }).build();
    }
}
