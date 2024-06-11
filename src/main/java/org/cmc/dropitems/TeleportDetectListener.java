package org.cmc.dropitems;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.cmc.CMC;

import java.util.ArrayList;
import java.util.List;

public class TeleportDetectListener implements Listener {


    private List<ItemStack> convertItems(List<String> itemList) {
        List<ItemStack> items = new ArrayList<>();
        String itemValue = "item";
        try {
            for (String stringValue : itemList) {
                itemValue = stringValue;
                items.add(new ItemStack(Material.valueOf(stringValue)));
            }
        } catch (IllegalArgumentException e) {
            CMC.debug(itemValue + " was not detected / is not a real item!");
            // di.getLogger().warning(itemValue + " was not detected / is not a real item!");
        }
        return items;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        if (!(e.getCause().equals(PlayerTeleportEvent.TeleportCause.COMMAND) || e.getCause().equals(PlayerTeleportEvent.TeleportCause.PLUGIN)) || player.hasMetadata("MT-Teleported")) {
            return;
        }

        Inventory inventory = e.getPlayer().getInventory();

        List<String> items = ConfigManager.getItemList();
        List<ItemStack> configItems = convertItems(items);

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
            // Don't drop if item is in config
            if (!shouldDrop) {
                continue;
            }

            e.getPlayer().getWorld().dropItem(e.getFrom(), item);
            inventory.remove(item);

            int amountDropped = item.getAmount();

            // Send a message to the player with the dropped item's name
            String itemName = item.getType().toString();
            e.getPlayer().sendMessage("§cTeleporting Dropped " + "§8("+amountDropped+"x§8)§e" + " " + itemName);
        }
    }
}