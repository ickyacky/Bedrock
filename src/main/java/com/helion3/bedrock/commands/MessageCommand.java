package com.helion3.bedrock.commands;

import com.helion3.bedrock.Bedrock;
import com.helion3.bedrock.util.Format;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class MessageCommand {
    private MessageCommand() {}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
        .arguments(
            GenericArguments.player(Text.of("player")),
            GenericArguments.remainingJoinedStrings(Text.of("message"))
        )
        .description(Text.of("Direct message another player."))
        .permission("bedrock.message")
        .executor((source, args) -> {
            if (!(source instanceof Player)) {
                source.sendMessage(Format.error("Only players may use this command."));
                return CommandResult.empty();
            }

            Player sender = (Player) source;
            Player recipient = args.<Player>getOne("player").get();

            Bedrock.getMessageManager().message(sender, recipient, args.<String>getOne("message").get());

            return CommandResult.success();
        }).build();
    }
}
