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

import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DropItemsListener implements Listener {

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
            CMCutil.debug(itemValue + " was not detected / is not a real item! Will not be added to the blacklist.", ChatColor.RED);
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

    private boolean matchesItemStack(ItemStack itemStack, ItemStack configItem) {
        if (itemStack.getType() != configItem.getType()) {
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        ItemMeta configMeta = configItem.getItemMeta();

        if (itemMeta == null || configMeta == null) {
            return true; // If no meta, match based only on material
        }

        boolean itemHasCMD = itemMeta.hasCustomModelData();
        boolean configHasCMD = configMeta.hasCustomModelData();

        if (itemHasCMD != configHasCMD) {
            return false;
        }

        if (itemHasCMD && itemMeta.getCustomModelData() != configMeta.getCustomModelData()) {
            return false;
        }

        return true; // Material and custom model data match
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        Inventory inventory = player.getInventory();
        Location deathLocation = player.getLocation();

        List<String> items = ConfigManager.getDropBlacklist();
        List<ItemStack> configItems = convertItems(items);

        List<ItemStack> itemsToDrop = new ArrayList<>();
        player.sendMessage(NamedTextColor.YELLOW + "You died and lost some items!");

        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            boolean shouldDrop = true;
            for (ItemStack configItem : configItems) {
                if (matchesItemStack(item, configItem)) {
                    shouldDrop = false;
                    break;
                }
            }

            if (shouldDrop) {
                itemsToDrop.add(item);
                inventory.remove(item);
            }
        }

        Random random = new Random();
        for (ItemStack item : itemsToDrop) {
            double offsetX = (random.nextDouble() - 0.5) * 0.5;
            double offsetZ = (random.nextDouble() - 0.5) * 0.5;
            double offsetY = random.nextDouble() * 0.5;
            Location dropLocation = deathLocation.clone().add(offsetX, 0, offsetZ);
            player.getWorld().dropItem(dropLocation, item).setVelocity(new Vector(offsetX, offsetY, offsetZ));
        }

        e.getDrops().clear();
    }
}
