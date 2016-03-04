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
import com.helion3.bedrock.PlayerConfiguration;
import com.helion3.bedrock.util.ConfigurationUtil;
import com.helion3.bedrock.util.Format;
import ninja.leaping.configurate.ConfigurationNode;
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
import java.util.Optional;

public class HomesCommand {
    private HomesCommand() {}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
        .description(Text.of("List your homes."))
        .permission("bedrock.home")
        .executor((source, args) -> {
            if (!(source instanceof Player)) {
                source.sendMessage(Format.error("Only players may use this command."));
                return CommandResult.empty();
            }

            Player player = (Player) source;
            player.sendMessage(Format.heading("Homes:"));

            // Load player's config
            PlayerConfiguration config = Bedrock.getPlayerConfigManager().getPlayerConfig(player);

            // Get homes list
            ConfigurationNode homesNode = config.getNode("homes");
            if (homesNode.getChildrenMap().isEmpty()) {
                source.sendMessage(Format.subdued("You have no homes."));
                return CommandResult.success();
            }

            // Build paginated content
            Optional<PaginationService> service = Bedrock.getGame().getServiceManager().provide(PaginationService.class);
            if (service.isPresent()) {
                PaginationBuilder pagination = service.get().builder();
                ArrayList<Text> homes = new ArrayList<>();

                for (Object obj : config.getNode("homes").getChildrenMap().keySet()) {
                    Text.Builder builder = Text.builder().append(Format.message(obj));

                    Optional<Location<World>> location = ConfigurationUtil.getNamedLocation(homesNode, (String) obj);
                    builder.onClick(TextActions.executeCallback(t -> {
                        if (t instanceof Player) {
                            if (!location.isPresent()) {
                                source.sendMessage(Format.error("Home location is no longer valid."));
                                return;
                            }

                            ((Player) t).setLocation(location.get());
                        }
                    }));

                    homes.add(builder.build());
                }

                pagination.contents(homes);
                pagination.sendTo(source);
            }

            return CommandResult.success();
        }).build();
    }
}
