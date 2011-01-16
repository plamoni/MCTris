package com.bukkit.playmoney.webhooks;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Handle events for all Player related events
 * @author <yourname>
 */
public class WebHooksPlayerListener extends PlayerListener {
    private final WebHooks plugin;

    public WebHooksPlayerListener(WebHooks instance) {
        plugin = instance;
    }

    //Insert Player related code here
}