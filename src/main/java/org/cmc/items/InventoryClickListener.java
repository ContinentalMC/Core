package org.cmc.items;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack cursorItem = event.getCursor();
        ItemStack clickedItem = event.getCurrentItem();

        if (cursorItem != null && !cursorItem.getType().isAir()) {
            LoreUpdater.updateItemLoreWithEnchantments(cursorItem);
        }

        if (clickedItem != null && !clickedItem.getType().isAir()) {
            LoreUpdater.updateItemLoreWithEnchantments(clickedItem);
        }
    }
}
