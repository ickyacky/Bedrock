package com.helion3.bedrock.commands;

import com.helion3.bedrock.Bedrock;
import com.helion3.bedrock.util.Format;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class ReplyCommand {
    private ReplyCommand() {}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
        .arguments(
            GenericArguments.remainingJoinedStrings(Text.of("message"))
        )
        .description(Text.of("Reply to a direct message"))
        .permission("bedrock.message")
        .executor((source, args) -> {
            if (!(source instanceof Player)) {
                source.sendMessage(Format.error("Only players may use this command."));
                return CommandResult.empty();
            }

            Player sender = (Player) source;
            Optional<Player> recipient = Bedrock.getMessageManager().getLastSender(sender);

            if (!recipient.isPresent()) {
                source.sendMessage(Format.error("No valid senders to reply to."));
                return CommandResult.empty();
            }

            Bedrock.getMessageManager().message(sender, recipient.get(), args.<String>getOne("message").get());

            return CommandResult.success();
        }).build();
    }
}
