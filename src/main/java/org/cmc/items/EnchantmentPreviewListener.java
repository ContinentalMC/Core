package org.cmc.items;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantmentPreviewListener implements Listener {

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        // Get the result item from the anvil
        ItemStack resultItem = event.getResult();
        if (resultItem != null && !resultItem.getType().isAir()) {
            // Update lore to show enchantment preview
            LoreUpdater.updateItemLoreWithEnchantments(resultItem);
        }
    }

    @EventHandler
    public void onPrepareGrindstone(PrepareGrindstoneEvent event) {
        // Get the result item from the grindstone
        ItemStack resultItem = event.getResult();
        if (resultItem != null && !resultItem.getType().isAir()) {
            // Update lore to show enchantment preview
            LoreUpdater.updateItemLoreWithEnchantments(resultItem);
        }
    }
}
