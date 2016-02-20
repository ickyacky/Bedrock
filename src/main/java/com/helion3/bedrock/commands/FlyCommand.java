package com.helion3.bedrock.commands;


import com.helion3.bedrock.util.Format;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class FlyCommand {
    private FlyCommand() {}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
        .arguments(
            GenericArguments.playerOrSource(Text.of("player"))
        )
        .description(Text.of("Toggle flying."))
        .executor((source, args) -> {
            Optional<Player> player = args.<Player>getOne("player");
            if (!player.isPresent()) {
                source.sendMessage(Format.error("Invalid player defined."));
                return CommandResult.empty();
            }

            boolean forSelf = source.equals(player.get());

            // Permissions
            if (!forSelf && !source.hasPermission("bedrock.fly.others")) {
                source.sendMessage(Format.error("You do not have permission to toggle fly for other players."));
                return CommandResult.empty();
            }
            else if (forSelf && !source.hasPermission("bedrock.fly")) {
                source.sendMessage(Format.error("You do not have permission to toggle fly."));
                return CommandResult.empty();
            }

            // Set fly
            boolean flying = player.get().get(Keys.CAN_FLY).orElse(false);
            player.get().offer(Keys.CAN_FLY, !flying);

            // Message source
            source.sendMessage(Format.success((flying ? "Dis" : "En") + "abled fly" + (forSelf ? "" : " for " + player.get().getName())));

            // Message target, if any
            if (!forSelf) {
                player.get().sendMessage(Format.success("Fly has been" + (flying ? "Dis" : "En") + "abled"));
            }

            return CommandResult.success();
        }).build();
    }
}
