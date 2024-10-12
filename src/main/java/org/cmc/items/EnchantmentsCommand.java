package org.cmc.items;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class EnchantmentsCommand implements CommandExecutor {

    // List of words that should not be capitalized
    private static final List<String> SMALL_WORDS = Arrays.asList("of", "the", "and", "in");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Check if the player has the required permission
            if (!player.hasPermission("cmc.admin.getenchants")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return true;
            }

            ItemStack item = player.getInventory().getItemInMainHand();

            // Check if the item has enchantments
            if (item == null || item.getType().isAir()) {
                player.sendMessage(ChatColor.RED + "You are not holding an item!");
                return true;
            }

            Map<Enchantment, Integer> enchantments = item.getEnchantments();

            if (enchantments.isEmpty()) {
                player.sendMessage(ChatColor.YELLOW + "This item has no enchantments.");
                return true;
            }

            // Convert the enchantments to a list and sort alphabetically
            List<String> enchantmentList = new ArrayList<>();
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                Enchantment enchantment = entry.getKey();
                int level = entry.getValue();
                String enchantName = formatEnchantmentName(enchantment.getKey().getKey());
                enchantmentList.add(enchantName + " " + level);
            }

            // Sort the enchantment list alphabetically
            enchantmentList.sort(Comparator.naturalOrder());

            // Join the list into a string
            String result = String.join(", ", enchantmentList);

            player.sendMessage(ChatColor.GREEN + "Enchantments: " + result);
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }
    }

    // Helper method to format the enchantment name
    private String formatEnchantmentName(String enchantmentKey) {
        String[] words = enchantmentKey.toLowerCase().split("_");
        StringBuilder formattedName = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            // Capitalize the word if it's not in the SMALL_WORDS list or if it's the first word
            if (i == 0 || !SMALL_WORDS.contains(word)) {
                formattedName.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1));
            } else {
                formattedName.append(word);
            }
            if (i < words.length - 1) {
                formattedName.append(" ");
            }
        }
        return formattedName.toString();
    }
}
