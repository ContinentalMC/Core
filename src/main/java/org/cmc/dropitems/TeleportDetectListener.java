package org.cmc.dropitems;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Location;
import org.cmc.CMCutil;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class TeleportDetectListener implements Listener {

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
            CMCutil.debug(itemValue + " was not detected / is not a real item! Will not be added to the Teleport whitelist.", ChatColor.RED);
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
    public void onTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        if (!(e.getCause().equals(PlayerTeleportEvent.TeleportCause.COMMAND) || e.getCause().equals(PlayerTeleportEvent.TeleportCause.PLUGIN)) || (player.hasMetadata("MT-Teleported")) || (player.hasMetadata("DontDropTP") || (player.hasMetadata("StaffMode"))))  {
            return;
        }

        Inventory inventory = player.getInventory();
        Location fromLocation = e.getFrom();

        List<String> items = ConfigManager.getDropBlacklist();
        List<ItemStack> configItems = convertItems(items);

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

            // Don't drop if item is in config
            if (!shouldDrop) {
                continue;
            }

            player.getWorld().dropItem(fromLocation, item);
            inventory.remove(item);

            int amountDropped = item.getAmount();

            // Send a message to the player with the dropped item's name
            String itemName = item.getType().toString();
            player.sendMessage("§cTeleporting Dropped " + "§8(" + amountDropped + "x§8)§e " + itemName);
        }
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
