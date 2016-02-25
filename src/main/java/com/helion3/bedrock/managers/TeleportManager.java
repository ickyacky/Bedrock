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

import com.flowpowered.math.vector.Vector3d;
import com.helion3.bedrock.Bedrock;
import com.helion3.bedrock.util.Format;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TeleportManager {
    private final Map<Player, Teleport> pendingRequests = new HashMap<>();

    public static class Teleport {
        private final Player source;
        private final Player target;
        private Player requestedBy;

        public Teleport(Player source, Player target) {
            this.source = source;
            this.target = target;
        }

        public Optional<Player> getRequester() {
            return Optional.ofNullable(requestedBy);
        }

        public Player getSource() {
            return source;
        }

        public Player getTarget() {
            return target;
        }

        public void setRequestedBy(Player player) {
            this.requestedBy = player;
        }
    }

    /**
     * Request a teleport to another player.
     *
     * @param teleport Teleport
     */
    public void request(Teleport teleport) {
        Player target = teleport.getTarget();
        Player requester = teleport.getRequester().get();

        // Store
        pendingRequests.put(target, teleport);

        target.sendMessage(Text.of(TextColors.YELLOW,
            String.format("%s is requesting to teleport to you\n", requester.getName()),
            TextColors.WHITE, "Use /tpaccept or /tpdeny within 20 seconds"));

        // Handle request
        requester.sendMessage(Format.subdued("Sending request..."));
        Bedrock.getGame().getScheduler().createTaskBuilder().delayTicks(400L).execute(() -> {
            if (pendingRequests.remove(target) != null) {
                teleport.getTarget().sendMessage(Format.subdued("Your request did not receive a response."));
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
            Teleport request = pendingRequests.get(player);
            Player requester = request.getRequester().get();

            requester.sendMessage(Format.success(String.format("Teleporting %s....", player.getName())));
            player.sendMessage(Format.message(String.format("Teleporting you to %s", requester.getName())));

            teleport(requester, player);
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
            Teleport request = pendingRequests.get(player);
            Player requester = request.getRequester().get();

            requester.sendMessage(Format.message("Sorry, your request was denied."));
            player.sendMessage(Format.success(String.format("Denied %s's tp request.", requester.getName())));

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

    /**
     * Teleport a player to a specific location.
     *
     * @param source Player to teleport
     * @param position Vector3d teleporting to
     */
    public void teleport(Player source, Vector3d position) {
        // Teleport
        source.setLocation(source.getWorld().getLocation(position));

        // Message
        source.sendMessage(Format.success(
            String.format("Teleporting you to %f %f %f", position.getX(), position.getY(), position.getZ())));
    }

    /**
     * Teleport a player to another world.
     *
     * @param source Player to teleport
     * @param world World teleporting to
     */
    public void teleport(Player source, World world) {
        // Teleport
        source.transferToWorld(world.getUniqueId(), world.getSpawnLocation().getPosition());

        // Message
        source.sendMessage(Format.success(String.format("Teleporting you to world %s", world.getName())));
    }
}
