package org.cmc;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.cmc.dropitems.*;
import org.cmc.levelperms.LevelPermissions;

import net.md_5.bungee.api.ChatColor;;

public final class CMC extends JavaPlugin {
    public static void debug(String message){
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(ChatColor.YELLOW+message);
    }
    
    public static void debug(String message, ChatColor color){
        if(color == null){
            debug(message);
            return;
        }
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(color+message);
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        ConfigManager.setupConfig(this);
        Bukkit.getPluginManager().registerEvents(new TeleportDetectListener(), this);
        Bukkit.getPluginManager().registerEvents(new LevelPermissions(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
