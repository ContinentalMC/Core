package org.cmc.dropitems;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TeleportDetectListener implements Listener {

    private DropItems di;

    public TeleportDetectListener(DropItems di) {
        this.di = di;
    }

    private List<ItemStack> convertItems(List<String> itemList) {

        List<ItemStack> items = new ArrayList<>();
        String itemValue = "item";
        try {
            for (String stringValue : itemList) {
                itemValue = stringValue;
                items.add(new ItemStack(Material.valueOf(stringValue)));
            }
        } catch (IllegalArgumentException e) {
            di.getLogger().warning(itemValue + " is not a real item!");
        }

        return items;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (!(e.getCause().equals(PlayerTeleportEvent.TeleportCause.COMMAND) || e.getCause().equals(PlayerTeleportEvent.TeleportCause.PLUGIN))) {
            return;
        }

        Inventory inventory = e.getPlayer().getInventory();

        List<String> items = ConfigManager.getItemList();

        List<ItemStack> configItems = convertItems(items);

        for (ItemStack item : inventory.getContents()) {

            for (ItemStack itemStack : configItems) {

                if (item == null) {
                    continue;
                }

                if (item.getType().equals(itemStack.getType())) {
                    e.getPlayer().getWorld().dropItem(e.getFrom(), item);
                    inventory.remove(item);
                }
            }

        }
    }

}
