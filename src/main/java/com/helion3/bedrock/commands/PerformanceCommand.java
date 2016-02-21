package com.helion3.bedrock.commands;

import com.google.common.collect.Iterables;
import com.helion3.bedrock.Bedrock;
import com.helion3.bedrock.util.Format;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

public class PerformanceCommand {
    private PerformanceCommand() {}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
        .description(Text.of("Display server performance statistics."))
        .permission("bedrock.performance")
        .executor((source, args) -> {
            source.sendMessage(Format.heading("Performance Stats"));
            source.sendMessage(Format.message("TPS: " + Bedrock.getGame().getServer().getTicksPerSecond()));

            source.sendMessage(Format.message("Worlds:"));
            for (World world : Bedrock.getGame().getServer().getWorlds()) {
                source.sendMessage(Text.of(TextColors.WHITE, world.getName() + "\n", TextColors.GRAY,
                    " Entities: ", TextColors.YELLOW, world.getEntities().size(), TextColors.GRAY,
                    " Chunks: ", TextColors.YELLOW, Iterables.size(world.getLoadedChunks())));
            }

            return CommandResult.success();
        }).build();
    }
}
