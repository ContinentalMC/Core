package org.cmc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.cmc.dropitems.*;
import org.cmc.levelperms.LevelPermissions;
import org.cmc.movecraft.RotateEvent;

public final class CMC extends JavaPlugin {

    private static CMC instance;


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        ConfigManager.setupConfig(this);
        CMCutil.StartTimer();
        Bukkit.getPluginManager().registerEvents(new TeleportDetectListener(), this);
        Bukkit.getPluginManager().registerEvents(new LevelPermissions(), this);
        Bukkit.getPluginManager().registerEvents(new RotateEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
    }
    public static CMC getInstance() {
        return instance;
    }
}
