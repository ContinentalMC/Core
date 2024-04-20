package org.cmc.dropitems;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DropItems extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        ConfigManager.setupConfig(this);
        Bukkit.getPluginManager().registerEvents(new TeleportDetectListener(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
