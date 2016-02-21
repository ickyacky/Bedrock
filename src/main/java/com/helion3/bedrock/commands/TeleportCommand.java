package com.helion3.bedrock.commands;

import com.helion3.bedrock.util.Format;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class TeleportCommand {
    private TeleportCommand() {}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
        .arguments(
            GenericArguments.player(Text.of("player"))
        )
        .description(Text.of("Teleport to another player."))
        .permission("bedrock.tp")
        .executor((source, args) -> {
            if (!(source instanceof Player)) {
                source.sendMessage(Format.error("Only players may use this command."));
                return CommandResult.empty();
            }

            Player player = (Player) source;
            Player target = args.<Player>getOne("player").get();

            // Teleport
            player.setLocation(target.getLocation());

            // Message
            player.sendMessage(Format.success(String.format("Teleporting you to %s", target.getName())));

            return CommandResult.success();
        }).build();
    }
}
