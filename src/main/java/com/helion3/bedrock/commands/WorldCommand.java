package com.helion3.bedrock.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;

import java.util.List;

public class WorldCommand {
    private WorldCommand() {}

    public static CommandSpec getCommand() {
        ImmutableMap.Builder<List<String>, CommandCallable> builder = ImmutableMap.<List<String>, CommandCallable>builder();
        builder.put(ImmutableList.of("difficulty"), DifficultyCommand.getCommand());

        return CommandSpec.builder().executor((src, args) -> {
            return CommandResult.empty();
        }).children(builder.build()).build();
    }
}
