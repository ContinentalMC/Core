package org.cmc.dropitems;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
                items.add(new ItemStack(Material.valueOf(stringValue)));
            }
        } catch (IllegalArgumentException e) {
            CMCutil.debug(itemValue + " was not detected / is not a real item! Will not be added to the KeepInventory whitelist.", ChatColor.RED);
        }
        return items;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Inventory inventory = player.getInventory();
        Location deathLocation = player.getLocation();

        List<String> items = ConfigManager.getItemList();
        List<ItemStack> configItems = convertItems(items);

        List<ItemStack> itemsToDrop = new ArrayList<>();
        
        for (ItemStack item : inventory.getContents()) {
            if (item == null) {
                continue;
            }

            boolean shouldDrop = true;
            for (ItemStack itemStack : configItems) {
                if (item.getType().equals(itemStack.getType())) {
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
            double offsetX = (random.nextDouble() - 0.5) * 2; // Random offset between -1 and 1
            double offsetZ = (random.nextDouble() - 0.5) * 2; // Random offset between -1 and 1
            double offsetY = random.nextDouble() * 0.5; // Random offset between 0 and 0.5 for upward launch
            Location dropLocation = deathLocation.clone().add(offsetX, 0, offsetZ);
            Item droppedItem = player.getWorld().dropItem(dropLocation, item);

            // Apply random velocity for explosion effect
            Vector velocity = new Vector(offsetX, offsetY, offsetZ);
            droppedItem.setVelocity(velocity);
        }

        // Clear the event drops to prevent default behavior
        e.getDrops().clear();
    }
}
