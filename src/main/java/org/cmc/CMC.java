package org.cmc;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.cmc.dropitems.*;;

public final class CMC extends JavaPlugin {

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
