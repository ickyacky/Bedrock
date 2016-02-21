package com.helion3.bedrock.listeners;

import com.helion3.bedrock.Bedrock;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class DisconnectListener {
    @Listener
    public void onPlayerQuit(final ClientConnectionEvent.Disconnect event) {
        Bedrock.getMessageManager().clear(event.getTargetEntity());
    }
}
