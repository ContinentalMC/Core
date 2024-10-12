package org.cmc.items;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.cmc.CMC;

public class PickupItemListener implements Listener {

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        // Get the Item entity that was picked up
        Item pickedUpItem = event.getItem();

        // Get the ItemStack from the entity
        ItemStack item = pickedUpItem.getItemStack();

        // Update the item lore with the enchantments
        LoreUpdater.updateItemLoreWithEnchantments(item);

        // Update the Item entity with the new ItemStack
        pickedUpItem.setItemStack(item);
    }
}
