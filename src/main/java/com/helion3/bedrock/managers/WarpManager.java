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
package com.helion3.bedrock.managers;

import com.helion3.bedrock.Bedrock;
import com.helion3.bedrock.NamedConfiguration;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.UUID;

public class WarpManager {
    private final NamedConfiguration config;

    public WarpManager() {
        config = new NamedConfiguration("warps");
    }

    /**
     * Create a new warp and save to disk.
     *
     * @param name String name
     * @param location Location
     */
    public void create(String name, Location<World> location) {
        ConfigurationNode node = config.getRootNode().getNode(name);
        node.getNode("x").setValue(location.getBlockX());
        node.getNode("y").setValue(location.getBlockY());
        node.getNode("z").setValue(location.getBlockZ());
        node.getNode("worldUuid").setValue(location.getExtent().getUniqueId().toString());
        config.save();
    }

    /**
     * Delete a warp.
     *
     * @param name String warp name.
     * @return boolean If it existed
     */
    public boolean delete(String name) {
        ConfigurationNode node = config.getRootNode().getNode(name);
        if (node.isVirtual()) {
            return false;
        }

        config.getRootNode().removeChild(name);
        config.save();

        return true;
    }

    /**
     * Get location for a named warp.
     *
     * @param name String warp name
     * @return Optional Location
     */
    public Optional<Location<World>> getWarp(String name) {
        ConfigurationNode node = config.getRootNode().getNode(name);
        if (!node.isVirtual()) {
            // Build location
            int x = node.getNode("x").getInt();
            int y = node.getNode("y").getInt();
            int z = node.getNode("z").getInt();
            UUID worldUuid = UUID.fromString(node.getNode("worldUuid").getString());

            Optional<World> world = Bedrock.getGame().getServer().getWorld(worldUuid);
            if (world.isPresent()) {
                return Optional.of(world.get().getLocation(x, y, z));
            }
        }

        return Optional.empty();
    }
}
