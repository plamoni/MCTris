package com.bukkit.playmoney.webhooks;

import org.bukkit.Material;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;

/**
 * <pluginname> block listener
 * @author <yourname>
 */
public class WebHooksBlockListener extends BlockListener {
    private final WebHooks plugin;

    public WebHooksBlockListener(final WebHooks plugin) {
        this.plugin = plugin;
    }

    //put all Block related code here
}