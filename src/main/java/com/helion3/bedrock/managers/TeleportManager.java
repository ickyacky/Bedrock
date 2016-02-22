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
import com.helion3.bedrock.util.Format;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashMap;
import java.util.Map;

public class TeleportManager {
    private final Map<Player, Player> pendingRequests = new HashMap<>();

    /**
     * Request a teleport to another player.
     *
     * @param source Player to teleport
     * @param target Player target
     */
    public void request(Player source, Player target) {
        pendingRequests.put(target, source);

        source.sendMessage(Text.of(TextColors.YELLOW, String.format("%s is requesting to teleport to you\n", source.getName()),
            TextColors.WHITE, "Use /tpaccept or /tpdeny within 20 seconds"));

        target.sendMessage(Format.subdued("Sending request..."));

        Bedrock.getGame().getScheduler().createTaskBuilder().delayTicks(400L).execute(() -> {
            Player entry = pendingRequests.remove(source);

            if (entry != null) {
                target.sendMessage(Format.subdued("Your request did not receive a response."));
            }
        }).submit(Bedrock.getPlugin());
    }

    /**
     * Accept a pending teleport request.
     *
     * @param player Player request was made to.
     */
    public void accept(Player player) {
        if (!pendingRequests.containsKey(player)) {
            player.sendMessage(Format.error("You do not have any pending requests."));
        } else {
            teleport(pendingRequests.get(player), player);
            pendingRequests.remove(player);
        }
    }

    /**
     * Deny a pending teleport request.
     *
     * @param player Player request was made to.
     */
    public void deny(Player player) {
        if (!pendingRequests.containsKey(player)) {
            player.sendMessage(Format.error("You do not have any pending requests."));
        } else {
            pendingRequests.get(player).sendMessage(Format.message("Sorry, your request was denied."));
            pendingRequests.remove(player);
        }
    }

    /**
     * Teleport a player to another.
     *
     * @param source Player to teleport
     * @param target Player teleporting to
     */
    public void teleport(Player source, Player target) {
        // Teleport
        source.setLocation(target.getLocation());

        // Message
        source.sendMessage(Format.success(String.format("Teleporting you to %s", target.getName())));
    }
}
