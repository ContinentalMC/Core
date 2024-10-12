package org.cmc.items;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LoreUpdater {

    private static final int LINE_LENGTH_LIMIT = 48; // Maximum length per line for enchantments

    public static void updateItemLoreWithEnchantments(ItemStack item) {
        if (item == null || item.getType().isAir()) return;

        org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        // Get enchantments from the item
        Map<Enchantment, Integer> enchantments = item.getEnchantments();

        // Build the enchantment list
        List<String> enchantmentLines = buildEnchantmentList(enchantments);

        // Create or get existing lore
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }

        // Clear previous enchantment lines
        lore.removeIf(line -> line.startsWith(ChatColor.GREEN.toString()));

        // If there are no enchantments, remove empty lines created by enchantments
        if (enchantmentLines.isEmpty()) {
            // Remove any empty lines created by enchantments
            lore.removeIf(String::isEmpty);
            meta.setLore(lore); // Update the meta without adding an empty line
            item.setItemMeta(meta);
            return; // No enchantments to display
        }

        // Add the new enchantment lines
        lore.addAll(0, enchantmentLines); // Insert the enchantments lines at the top

        // Update the item's lore
        meta.setLore(lore);

        // Set HideFlags to hide default enchantment lore
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
    }

    // Helper method to format the enchantment names and levels
    private static List<String> buildEnchantmentList(Map<Enchantment, Integer> enchantments) {
        List<String> lines = new ArrayList<>();

        if (enchantments.isEmpty()) return lines;

        StringBuilder currentLine = new StringBuilder();

        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            String formattedEnchantment = formatEnchantment(entry.getKey().getKey().getKey(), entry.getValue());
            // Check if adding this enchantment exceeds the limit
            if (currentLine.length() + formattedEnchantment.length() + 2 > LINE_LENGTH_LIMIT) { // +2 for ", "
                // Add the current line to the list without a trailing comma
                lines.add(currentLine.toString().trim());
                currentLine = new StringBuilder(); // Start a new line
            }
            currentLine.append(formattedEnchantment).append(", "); // Add enchantment to the current line
        }

        // Add the last line if not empty, removing trailing comma
        if (currentLine.length() > 0) {
            lines.add(currentLine.substring(0, currentLine.length() - 2).trim()); // Remove the last ", "
        }

        return lines.stream().map(line -> ChatColor.GREEN + line).collect(Collectors.toList());
    }

    // Helper method to format a single enchantment
    private static String formatEnchantment(String enchantmentKey, int level) {
        String formattedLevel = toRoman(level);
        String[] words = enchantmentKey.toLowerCase().split("_");
        StringBuilder formattedName = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            formattedName.append(i == 0 ? capitalize(word) : lowercaseSmallWord(word)).append(" ");
        }
        return formattedName.toString().trim() + " " + formattedLevel;
    }

    // Helper method to convert integers to Roman numerals
    private static String toRoman(int number) {
        if (number < 1 || number > 10) {
            return String.valueOf(number); // Return as is if not in range
        }

        String[] romanNumerals = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        return romanNumerals[number];
    }

    // Helper method to capitalize the first letter of a word
    private static String capitalize(String word) {
        if (word.isEmpty()) return word;
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    // Helper method to lowercase small words
    private static String lowercaseSmallWord(String word) {
        List<String> smallWords = List.of("of", "the", "and", "in"); // Add any additional small words here
        return smallWords.contains(word) ? word.toLowerCase() : capitalize(word);
    }
}
