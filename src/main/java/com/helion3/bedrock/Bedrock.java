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
package com.helion3.bedrock;

import com.google.inject.Inject;
import com.helion3.bedrock.commands.BedrockCommands;
import com.helion3.bedrock.commands.FlyCommand;
import com.helion3.bedrock.commands.TeleportCommand;
import com.helion3.bedrock.commands.WeatherCommand;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;

@Plugin(id = "Bedrock", name = "Bedrock", version = "1.0")
public class Bedrock {
    private static Configuration config;
    private static Game game;
    private static Logger logger;
    private static File parentDirectory;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private File defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        parentDirectory = defaultConfig.getParentFile();

        // Commands
        game.getCommandManager().register(this, BedrockCommands.getCommand(), "br", "bedrock");
        game.getCommandManager().register(this, FlyCommand.getCommand(), "fly");
        game.getCommandManager().register(this, TeleportCommand.getCommand(), "tp", "teleport");
        game.getCommandManager().register(this, WeatherCommand.getCommand(), "weather");

        logger.info("Bedrock started.");
    }

    /**
     * Get the config
     *
     * @return Configuration
     */
    public static Configuration getConfig() {
        return config;
    }

    /**
     * Get the Game instance.
     *
     * @return Game
     */
    public static Game getGame() {
        return game;
    }

    /**
     * Injected Game instance.
     *
     * @param injectGame Game
     */
    @Inject
    public void setGame(Game injectGame) {
        game = injectGame;
    }

    /**
     * Get the logger.
     *
     * @return Logger
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Injects the Logger instance for this plugin.
     *
     * @param log Logger
     */
    @Inject
    private void setLogger(Logger log) {
        logger = log;
    }

    /**
     * Get parent directory.
     *
     * @return File
     */
    public static File getParentDirectory() {
        return parentDirectory;
    }
}
