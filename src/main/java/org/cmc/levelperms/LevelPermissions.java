 package org.cmc.levelperms;

 import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
 import org.bukkit.event.EventHandler;
 import org.bukkit.event.EventPriority;
 import org.bukkit.event.Listener;
 import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;


 import me.clip.placeholderapi.PlaceholderAPI;

 public class LevelPermissions implements @NotNull Listener {
    
         @EventHandler(priority = EventPriority.HIGHEST)
     // Check every tick for player levels
    public void tick(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String result = PlaceholderAPI.setPlaceholders(player, "%auraskills_average_int%");
        try {
          // Level 4
            int level = Integer.parseInt(result);
            if ((level >= 4)) {
            // Execute actions based on the result
            player.chat("I'm level!"+ level);
            // Give the player a permission
            Bukkit.getServer().getPluginManager().addPermission(new Permission("cmc.level.4"));
        }
    }   finally {}
    }
 }

