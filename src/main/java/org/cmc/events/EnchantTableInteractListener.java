package org.cmc.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnchantTableInteractListener implements Listener {
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Check if the player interacted with an enchantment table
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENCHANTING_TABLE)  {
            // Get the player's name
            String playerName = event.getPlayer().getName();

            // Run the command in the console
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mi stations open imbuement-table " + playerName);
            event.setCancelled(true);
        }
    }
}
