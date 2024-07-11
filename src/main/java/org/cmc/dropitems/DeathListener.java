package org.cmc.dropitems;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.cmc.CMCutil;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeathListener implements Listener {

    private List<ItemStack> convertItems(List<String> itemList) {
        List<ItemStack> items = new ArrayList<>();
        String itemValue = "item";
        try {
            for (String stringValue : itemList) {
                itemValue = stringValue;
                ItemStack itemStack = parseItemStack(stringValue);
                if (itemStack != null) {
                    items.add(itemStack);
                }
            }
        } catch (IllegalArgumentException e) {
            CMCutil.debug(itemValue + " was not detected / is not a real item! Will not be added to the KeepInventory whitelist.", ChatColor.RED);
        }
        return items;
    }

    private ItemStack parseItemStack(String itemString) {
        String[] parts = itemString.split(":");
        Material material = Material.matchMaterial(parts[0]);
        if (material == null) {
            return null;
        }

        ItemStack itemStack = new ItemStack(material);
        if (parts.length > 1) {
            try {
                int customModelData = Integer.parseInt(parts[1]);
                ItemMeta meta = itemStack.getItemMeta();
                if (meta != null) {
                    meta.setCustomModelData(customModelData);
                    itemStack.setItemMeta(meta);
                }
            } catch (NumberFormatException e) {
                CMCutil.debug("Invalid custom model data value for item: " + itemString, ChatColor.RED);
                return null;
            }
        }

        return itemStack;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Inventory inventory = player.getInventory();
        Location deathLocation = player.getLocation();

        List<String> items = ConfigManager.getDropBlacklist();
        List<ItemStack> configItems = convertItems(items);

        List<ItemStack> itemsToDrop = new ArrayList<>();

        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            boolean shouldDrop = true;
            for (ItemStack configItem : configItems) {
                if (matchesItemStack(item, configItem)) {
                    shouldDrop = false;
                    break; // No need to continue checking once a match is found
                }
            }

            // Only drop items not in the config
            if (shouldDrop) {
                itemsToDrop.add(item);
                inventory.remove(item);
            }
        }

        // Drop the items at random locations near the player's death location
        Random random = new Random();
        for (ItemStack item : itemsToDrop) {
            double offsetX = (random.nextDouble() - 0.5) * 0.5; // Random offset 
            double offsetZ = (random.nextDouble() - 0.5) * 0.5; // Random offset 
            double offsetY = random.nextDouble() * 0.5; // Random offset between 0 and 0.5 for upward launch
            Location dropLocation = deathLocation.clone().add(offsetX, 0, offsetZ);
            player.getWorld().dropItem(dropLocation, item).setVelocity(new Vector(offsetX, offsetY, offsetZ));
        }

        // Clear the event drops to prevent default behavior
        e.getDrops().clear();
    }

    @SuppressWarnings("deprecation") // We need to change getDurability to ItemMeta later
    private boolean matchesItemStack(ItemStack itemStack, ItemStack configItem) {
        if (itemStack.getType() != configItem.getType()) {
            return false;
        }

        if (itemStack.getDurability() != configItem.getDurability()) {
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        ItemMeta configMeta = configItem.getItemMeta();
        if (itemMeta == null || configMeta == null) {
            return false;
        }

        if (itemMeta.hasCustomModelData() != configMeta.hasCustomModelData()) {
            return false;
        }

        if (itemMeta.hasCustomModelData()) {
            return itemMeta.getCustomModelData() == configMeta.getCustomModelData();
        }

        return true; // No custom model data, compare based on material and durability
    }
}
