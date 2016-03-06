package com.helion3.bedrock.commands;

import com.helion3.bedrock.Bedrock;
import com.helion3.bedrock.util.Format;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class ReloadCommand {
    private ReloadCommand() {}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
        .description(Text.of("Reload bedrock configuration files."))
        .permission("bedrock.reload")
        .executor((source, args) -> {
            Bedrock.getPlugin().load();

            source.sendMessage(Format.success("Reloaded plugin configuration files."));

            return CommandResult.success();
        }).build();
    }
}
