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
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class ImportCommand {
    private ImportCommand() {}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
        .description(Text.of("Import homes from EssentialCmds"))
        .permission("bedrock.import")
        .executor((source, args) -> {
            System.out.println("Importing...");

            try {
                File conf = new File(Bedrock.getParentDirectory().getAbsolutePath() + "/../essentialcmds/data/homes.conf");

                if (!conf.exists()) {
                    System.out.println("Invalid homes.conf path!");
                }

                ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setFile(conf).build();
                ConfigurationNode rootNode = configLoader.load();
                ConfigurationNode usersNode = rootNode.getNode("home", "users");

                if (usersNode.isVirtual()) {
                    System.out.println("Invalid path!");
                }

                System.out.println("Users: " + usersNode.getChildrenMap().keySet().size());

                for (Object userKey : usersNode.getChildrenMap().keySet()) {
                    UUID playerUuid = UUID.fromString((String) userKey);
                    System.out.println("Loading homes for " + playerUuid.toString());

                    PlayerConfiguration config = new PlayerConfiguration(playerUuid);
                    ConfigurationNode homesNode = usersNode.getNode((String) userKey);

                    int homeCount = 0;
                    for (Object homeKey : homesNode.getChildrenMap().keySet()) {
                        String homeName = (String) homeKey;

                        if (homeName.equals("homes")) {
                            continue;
                        }

                        ConfigurationNode homeNode = homesNode.getNode(homeName);
                        ConfigurationNode node = config.getNode("homes", homeName);

                        String worldName = homeNode.getNode("world").getString();
                        Optional<World> world = Bedrock.getGame().getServer().getWorld(worldName);
                        if (!world.isPresent()) {
                            System.out.println("World " + worldName + " could not be found!");
                            continue;
                        }

                        node.getNode("x").setValue(homeNode.getNode("X").getDouble());
                        node.getNode("y").setValue(homeNode.getNode("Y").getDouble());
                        node.getNode("z").setValue(homeNode.getNode("Z").getDouble());
                        node.getNode("worldUuid").setValue(world.get().getUniqueId().toString());

                        homeCount++;
                    }

                    System.out.println("Save " + homeCount + " homes for " + playerUuid.toString());

                    // Save config
                    config.save();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return CommandResult.success();
        }).build();
    }
}
