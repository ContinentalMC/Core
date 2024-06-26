package org.cmc.movecraft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.inventory.ItemStack;
import org.cmc.CMC;

public class RotateEvent implements Listener {

   @EventHandler
   public void onDrop(PlayerDropItemEvent e) {
      ItemStack itemStack = e.getItemDrop().getItemStack();
      Player player = e.getPlayer();
      if (itemStack.getItemMeta().getDisplayName().equals("§6Controller")) {
         player.setMetadata("movecraft_rotating", new FixedMetadataValue(CMC.getInstance(), "movecraft_rotating"));
         player.performCommand("rotate right");
         player.removeMetadata("movecraft_rotating", CMC.getInstance());
      }

      e.setCancelled(true);
   }

   @EventHandler
   public void onSwap(PlayerSwapHandItemsEvent e) {
      ItemStack itemStack = e.getOffHandItem();
      Player player = e.getPlayer();
      if (itemStack.getItemMeta().getDisplayName().equals("§6Controller")) {
         player.setMetadata("movecraft_rotating", new FixedMetadataValue(CMC.getInstance(), "movecraft_rotating"));
         player.performCommand("rotate left");
         player.removeMetadata("movecraft_rotating", CMC.getInstance());
      }

      e.setCancelled(true);
   }
}
