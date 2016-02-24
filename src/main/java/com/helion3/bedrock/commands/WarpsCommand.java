package com.helion3.bedrock.commands;

import com.helion3.bedrock.Bedrock;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationBuilder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class WarpsCommand {
    private WarpsCommand() {}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
        .description(Text.of("List warps."))
        .permission("bedrock.warp")
        .executor((source, args) -> {
            // Build paginated content
            Optional<PaginationService> service = Bedrock.getGame().getServiceManager().provide(PaginationService.class);
            if (service.isPresent()) {
                PaginationBuilder builder = service.get().builder();
                builder.contents(Bedrock.getWarpManager().getWarpList());
                builder.sendTo(source);
            }

            return CommandResult.success();
        }).build();
    }
}
