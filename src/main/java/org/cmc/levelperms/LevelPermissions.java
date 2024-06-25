package org.cmc.levelperms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.cmc.ServerTimeUpdate;
import org.jetbrains.annotations.NotNull;

import me.clip.placeholderapi.PlaceholderAPI;

public class LevelPermissions implements @NotNull Listener {
  public static void LevelCheck(Player player, int CurrentLevel, int RequiredLevel) {
    if ((CurrentLevel >= RequiredLevel) && !player.hasPermission("cmc.level." + RequiredLevel)) {
      Bukkit.getServer().getPluginManager().addPermission(new Permission("cmc.level." + RequiredLevel));
      if (player.hasPermission("cmc.level.1")) {
        Bukkit.getServer().getPluginManager().removePermission("cmc.level." + RequiredLevel);
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  // Check every tick for player levels
  public void tick(ServerTimeUpdate event) {
    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
      String result = PlaceholderAPI.setPlaceholders(player, "%auraskills_average_int%");
      // Level 1 & Remove level permissions
      int level = Integer.valueOf(result);
      if ((level == 1) && !player.hasPermission("cmc.level.1")) {
        Bukkit.getServer().getPluginManager().addPermission(new Permission("cmc.level.1"));
        Bukkit.getServer().getPluginManager().removePermission("cmc.level.12");
        Bukkit.getServer().getPluginManager().removePermission("cmc.level.32");
        Bukkit.getServer().getPluginManager().removePermission("cmc.level.48");
        Bukkit.getServer().getPluginManager().removePermission("cmc.level.64");
        Bukkit.getServer().getPluginManager().removePermission("cmc.level.86");
      }
      // Levels
      LevelCheck(player, level, 12);
      LevelCheck(player, level, 24);
      LevelCheck(player, level, 36);
      LevelCheck(player, level, 42);
      LevelCheck(player, level, 64);
      LevelCheck(player, level, 86);

      


    }
  }
}
