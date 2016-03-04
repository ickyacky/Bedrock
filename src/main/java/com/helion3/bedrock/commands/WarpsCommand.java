/**
 * This file is part of Bedrock, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016 Helion3 http://helion3.com/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.helion3.bedrock.commands;

import com.helion3.bedrock.Bedrock;
import com.helion3.bedrock.util.Format;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationBuilder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class WarpsCommand {
    private WarpsCommand() {}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
        .description(Text.of("List warps."))
        .permission("bedrock.warp")
        .executor((source, args) -> {
            // Build pagination
            PaginationService service = Bedrock.getGame().getServiceManager().provide(PaginationService.class).get();
            PaginationBuilder pagination = service.builder();

            // Build warp list
            ArrayList<Text> contents = new ArrayList<>();
            Map<String, Optional<Location<World>>> warps = Bedrock.getWarpManager().getWarps();
            if (warps.isEmpty()) {
                source.sendMessage(Format.subdued("There are no warps."));
                return CommandResult.success();
            }

            for (Map.Entry<String, Optional<Location<World>>> entry : warps.entrySet()) {
                Text.Builder builder = Text.builder().append(Format.message(entry.getKey()));

                builder.onClick(TextActions.executeCallback(t -> {
                    if (t instanceof Player) {
                        if (!entry.getValue().isPresent()) {
                            source.sendMessage(Format.error("Warp is not a valid location."));
                            return;
                        }

                        ((Player) t).setLocation(entry.getValue().get());
                    }
                }));

                contents.add(builder.build());
            }

            pagination.contents(contents);
            pagination.sendTo(source);

            return CommandResult.success();
        }).build();
    }
}
