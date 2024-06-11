package org.cmc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.cmc.dropitems.*;
import org.cmc.levelperms.LevelPermissions;

public final class CMC extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        ConfigManager.setupConfig(this);
        Bukkit.getPluginManager().registerEvents(new TeleportDetectListener(), this);
        Bukkit.getPluginManager().registerEvents(new LevelPermissions(), this);
        CMCutil.StartTimer();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
