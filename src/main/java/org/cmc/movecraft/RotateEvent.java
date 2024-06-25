package org.cmc.movecraft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.EventPriority;
import net.countercraft.movecraft.listener;

public class RotateEvent implements Listener {
   @EventHandler
   public void onDrop(PlayerDropItemEvent e) {
      ItemStack itemStack = e.getItemDrop().getItemStack();
      Player player = e.getPlayer();
      if (itemStack.getItemMeta().getDisplayName().equals("§6Controller")) {
         player.performCommand("rotate right");
      }

      e.setCancelled(true);
   }

   @EventHandler
   public void onDrop(PlayerSwapHandItemsEvent e) {
      ItemStack itemStack = e.getOffHandItem();
      Player player = e.getPlayer();
      if (itemStack.getItemMeta().getDisplayName().equals("§6Controller")) {
         player.performCommand("rotate left");
      }

      e.setCancelled(true);
   }
   @EventHandler(priority = EventPriority.LOWEST)
      public onDrop(PlayerInteractEvent e) {
      ItemStack itemStack = e.getItem();
      Player player = e.getPlayer();
      if (itemStack.getItemMeta().getDisplayName().equals("§6Controller")) {
         e.setCancelled(true);
      }
   }
}