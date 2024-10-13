package org.cmc.items;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

public class EnchantItemListener implements Listener {

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        ItemStack enchantedItem = event.getItem();
        if (enchantedItem != null) {
            LoreUpdater.updateItemLoreWithEnchantments(enchantedItem);
        }
    }
}
