package com.helion3.bedrock.managers;

import com.helion3.bedrock.Bedrock;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MutableMessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MessageManager {
    private final Map<Player,Player> lastSenders = new HashMap<>();

    /**
     * Clear any entries with a given player as the sender or recipient.
     *
     * @param player Player
     */
    public void clear(Player player) {
        lastSenders.remove(player);

        for (Map.Entry<Player, Player> entry : lastSenders.entrySet()) {
            if (entry.getValue().equals(player)) {
                lastSenders.remove(entry.getKey());
            }
        }
    }

    /**
     * Send a message.
     *
     * @param sender Player sender
     * @param recipient Player recipient
     * @param message String message
     */
    public void message(Player sender, Player recipient, String message) {
        // Send to recipient
        recipient.sendMessage(Text.of(TextStyles.ITALIC, TextColors.GRAY, sender.getName(), ": ", message));

        // Send to watchers
        MutableMessageChannel channel = MessageChannel.permission("bedrock.socialspy").asMutable();
        channel.getMembers().stream().filter(receiver -> receiver instanceof Player && !Bedrock.getMessageManager().playerIsSpying((Player) receiver)).forEach(channel::removeMember);
        channel.send(Text.of(TextStyles.ITALIC, TextColors.GRAY, sender.getName(), " -> ", recipient.getName(), ": ", message));

        // Store sender for easy reply
        setLastSender(sender, recipient);
    }

    /**
     * Get the last message sender, if any.
     *
     * @param recipient Player recipient
     * @return Optional Player
     */
    public Optional<Player> getLastSender(Player recipient) {
        return Optional.ofNullable(lastSenders.get(recipient));
    }

    /**
     * Sets the last known sender for messages.
     *
     * @param sender Player sender
     * @param recipient Player recipient
     */
    public void setLastSender(Player sender, Player recipient) {
        lastSenders.put(recipient, sender);
    }

    /**
     * Get whether this player has spy enabled.
     *
     * @param player
     * @return If spy enabled.
     */
    public boolean playerIsSpying(Player player) {
        return true;
    }
}
