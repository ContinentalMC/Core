package org.cmc;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.cmc.items.LoreUpdater;

import net.md_5.bungee.api.ChatColor;

// Util class
public class CMCutil {
    // Server Time Event
    public static int serverTimeSeconds = 0;

    public static void StartTimer() {
        // Timer for server time updates
        new BukkitRunnable() {
            @Override
            public void run() {
                serverTimeSeconds++;
                ServerTimeUpdate stu = new ServerTimeUpdate(serverTimeSeconds);
                Bukkit.getServer().getPluginManager().callEvent(stu);
            }
        }.runTaskTimer(CMCutil.getPlugin(), 20L, 20L); // Every second

        // Timer for lore updates
        new BukkitRunnable() {
            @Override
            public void run() {
                updateLoreForAllPlayers();
            }
        }.runTaskTimer(CMCutil.getPlugin(), 0L, 40L); // Every 2 seconds (40 ticks)
    }

    // Method to update lore for all players
    private static void updateLoreForAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack item = player.getInventory().getItemInMainHand();
            LoreUpdater.updateItemLoreWithEnchantments(item); // Update the lore with enchantments
        }
    }

    // Debug messages in console
    public static void debug(String message) {
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(ChatColor.YELLOW + message);
    }

    public static void debug(String message, ChatColor color) {
        if (color == null) {
            debug(message);
            return;
        }
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(color + message);
    }

    public static void log(String message, ChatColor color) {
        if (color == null) {
            debug(message);
            return;
        }
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(color + message);
    }

    public static Plugin getPlugin() {
        return Bukkit.getServer().getPluginManager().getPlugin("CMC-Core");
    }
}
