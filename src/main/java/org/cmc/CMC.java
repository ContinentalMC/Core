package org.cmc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.cmc.dropitems.ConfigManager;
import org.cmc.dropitems.DropItemsListener;
import org.cmc.events.EnchantTableInteractListener;
import org.cmc.items.getEnchantsCommand;
import org.cmc.movecraft.RotateEvent;


public final class CMC extends JavaPlugin {

    private static CMC instance;


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        ConfigManager.setupConfig(this);
        CMCutil.StartTimer();
        // Events
        Bukkit.getPluginManager().registerEvents(new DropItemsListener(), this);
        Bukkit.getPluginManager().registerEvents(new RotateEvent(), this);
        Bukkit.getPluginManager().registerEvents(new EnchantTableInteractListener(), this);
        this.getCommand("getenchantments").setExecutor(new getEnchantsCommand());


        // Integrations

        // Startup
        CMCutil.log("   .----------------. .----------------. .----------------. ", null);
        CMCutil.log("  | .--------------. | .--------------. | .--------------. |", null);
        CMCutil.log("  | |     ______   | | | ____    ____ | | |     ______   | |", null);
        CMCutil.log("  | |   .' ___  |  | | ||_   \\  /   _|| | |   .' ___  |  | |", null);
        CMCutil.log("  | |  / .'   \\_|  | | |  |   \\/   |  | | |  / .'   \\_|  | |", null);
        CMCutil.log("  | |  | |         | | |  | |\\  /| |  | | |  | |         | |", null);
        CMCutil.log("  | |  \\ `.___.'\\  | | | _| |_\\/| |_  | | |  \\ `.___.'\\  | |", null);
        CMCutil.log("  | |   `._____.'  | | ||_____||_____|| | |   `._____.'  | |", null);
        CMCutil.log("  | |              | | |              | | |              | |", null);
        CMCutil.log("  | '--------------' | '--------------' | '--------------' |", null);
        CMCutil.log("   '----------------' '----------------' '----------------' ", null);
        CMCutil.log("Has  loaded!", null);
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
