package com.helion3.bedrock.commands;

import com.helion3.bedrock.Bedrock;
import com.helion3.bedrock.util.Format;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.difficulty.Difficulty;

import java.util.Optional;

public class DifficultyCommand {
    private DifficultyCommand() {}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
        .arguments(
            GenericArguments.string(Text.of("difficulty"))
        )
        .description(Text.of("Set difficulty for the current world."))
        .permission("bedrock.world.difficulty")
        .executor((source, args) -> {
            if (!(source instanceof Player)) {
                source.sendMessage(Format.error("Invalid player defined."));
                return CommandResult.empty();
            }

            Player player = (Player) source;
            String difficultyName = args.<String>getOne("difficulty").get();
            Optional<Difficulty> difficulty = Bedrock.getGame().getRegistry().getType(Difficulty.class, difficultyName);

            if (!difficulty.isPresent()) {
                source.sendMessage(Format.error("Invalid difficulty."));
                return CommandResult.empty();
            }

            player.getWorld().getProperties().setDifficulty(difficulty.get());

            return CommandResult.success();
        }).build();
    }
}
