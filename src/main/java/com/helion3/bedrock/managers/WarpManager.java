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

import com.google.common.collect.ImmutableMap;
import com.helion3.bedrock.NamedConfiguration;
import com.helion3.bedrock.util.ConfigurationUtil;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

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
        return ConfigurationUtil.getNamedLocation(config.getRootNode(), name);
    }

    /**
     * Get a list of all warps.
     *
     * @return ImmutableMap of warp names and locations
     */
    public ImmutableMap<String, Optional<Location<World>>> getWarps() {
        ImmutableMap.Builder<String, Optional<Location<World>>> builder = ImmutableMap.builder();
        for (Object obj : config.getRootNode().getChildrenMap().keySet()) {
            String name = (String) obj;
            builder.put(name, getWarp(name));
        }

        return builder.build();
    }
}
