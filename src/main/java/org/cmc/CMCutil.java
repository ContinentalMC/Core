package org.cmc;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
// Util class
public class CMCutil {
    // Server Time Event
    public static  int serverTimeSeconds = 0;
    public static void StartTimer() {
         new BukkitRunnable(){
                @Override
                public void run(){
                    serverTimeSeconds++;
                    ServerTimeUpdate stu = new ServerTimeUpdate(serverTimeSeconds);
                    Bukkit.getServer().getPluginManager().callEvent(stu);
                }
            }.runTaskTimer(CMCutil.getPlugin(), 20l, 20l);


    }
    // Debug messages in console
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
    public static void log(String message, ChatColor color){
        if(color == null){
            debug(message);
            return;
        }
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(color+message);
    }
    public static Plugin getPlugin() {
        return
        Bukkit.getServer().getPluginManager().getPlugin("CMC-Core");
    }





}
