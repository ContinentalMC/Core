package org.cmc.items;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class EnchantmentsPlaceholder extends PlaceholderExpansion {

    // List of words that should not be capitalized
    private static final List<String> SMALL_WORDS = Arrays.asList("of", "the", "and", "in");

    private final JavaPlugin plugin;

    public EnchantmentsPlaceholder(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "cmc";
    }

    @Override
    public @NotNull String getAuthor() {
        return "YourName";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // This ensures the placeholder stays even if PlaceholderAPI reloads
    }

    @Override
public String onPlaceholderRequest(Player player, @NotNull String params) {
    if (player == null) {
        return "";
    }

    if (params.equalsIgnoreCase("enchants")) {
        ItemStack item = player.getInventory().getItemInMainHand();

        // Debug log
        plugin.getLogger().info("Checking item in main hand for player: " + player.getName());

        // Check if the item has enchantments
        if (item == null || item.getType().isAir()) {
            return ChatColor.RED + "No item in hand";
        }

        Map<Enchantment, Integer> enchantments = item.getEnchantments();

        if (enchantments.isEmpty()) {
            return ChatColor.YELLOW + "No enchantments";
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
        String result = ChatColor.GREEN + String.join(", ", enchantmentList);
        plugin.getLogger().info("Returning enchantments: " + result); // Debug log
        return result;
    }

    return null; // Return null if no valid placeholder was found
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
