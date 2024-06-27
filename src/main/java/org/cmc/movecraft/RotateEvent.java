package org.cmc.movecraft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.cmc.CMC;

import java.util.HashMap;
import java.util.Map;

public class RotateEvent implements Listener {

    private final Map<Location, Block> barrierLocations = new HashMap<>();

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack itemStack = e.getItemDrop().getItemStack();
        Player player = e.getPlayer();

        if (itemStack.getItemMeta().getDisplayName().equals("§6Controller")) {
            player.performCommand("rotate right");

            e.setCancelled(true);

            // Check if the player is considered airborne (falling or jumping)
            if (isAirborne(player)) {
                return; // Exit method if the player is airborne
            }

            // Calculate the location two blocks in front of the player's head direction
            Location playerEyeLocation = player.getEyeLocation();
            Vector direction = playerEyeLocation.getDirection().normalize().multiply(2); // Multiply by 2 for two blocks
            Location barrierLocation = playerEyeLocation.clone().add(direction).getBlock().getLocation();

            // Check if there's already a block in front of the player's reach
            Block blockInFront = barrierLocation.getBlock();
            if (blockInFront.isEmpty() || blockInFront.isLiquid()) {
                // Place a barrier block at the calculated location
                placeBarrierAt(barrierLocation);
            }
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        ItemStack itemStack = e.getOffHandItem();
        Player player = e.getPlayer();

        if (itemStack.getItemMeta().getDisplayName().equals("§6Controller")) {
            // Perform your rotation logic here
            player.performCommand("rotate left");

            // Cancel the swap event
            e.setCancelled(true);
        }
    }

    private boolean isAirborne(Player player) {
        Location playerLocation = player.getLocation();
        if (playerLocation.getY() <= 5) {
            return false; // Player is close to ground level, not considered airborne
        }

        // Check if there's solid ground within 5 blocks below the player's feet
        int minY = playerLocation.getBlockY() - 5;
        for (int y = playerLocation.getBlockY(); y >= minY; y--) {
            Block block = player.getWorld().getBlockAt(playerLocation.getBlockX(), y, playerLocation.getBlockZ());
            if (!block.isEmpty() && !block.isLiquid()) {
                return false; // Found solid ground within 5 blocks underneath player
            }
        }
        
        // Check player's velocity to determine if they are falling
        Vector velocity = player.getVelocity();
        if (velocity.getY() < -0.8) {
            return true; // Player is falling (considering a threshold of -0.8 for falling)
        }

        return false; // Player is not falling and no solid ground found within 5 blocks underneath
    }

    private void placeBarrierAt(Location location) {
        Block block = location.getBlock();
        if (block.isEmpty() || block.isLiquid()) {
            block.setType(Material.BARRIER);

            // Store the barrier block location for removal later
            barrierLocations.put(location, block);

            // Schedule a task to remove the barrier after a short delay 
            Bukkit.getScheduler().runTaskLater(CMC.getInstance(), () -> {
                removeBarrier(location);
            }, 1L); // Remove barrier after a short delay 
        }
    }

    private void removeBarrier(Location location) {
        Block barrier = barrierLocations.remove(location);
        if (barrier != null && barrier.getType() == Material.BARRIER) {
            barrier.setType(Material.AIR); // Remove the barrier block
        }
    }
}
