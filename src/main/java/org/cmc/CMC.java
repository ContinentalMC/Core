package org.cmc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.cmc.dropitems.*;
import org.cmc.movecraft.RotateEvent;
import org.cmc.shops.shopcommand;

public final class CMC extends JavaPlugin {

    private static CMC instance;


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        ConfigManager.setupConfig(this);
        CMCutil.StartTimer();
        // Events
        Bukkit.getPluginManager().registerEvents(new TeleportDetectListener(), this);
        Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new RotateEvent(), this);
        // Commands
        this.getCommand("shops").setExecutor(new shopcommand());
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
