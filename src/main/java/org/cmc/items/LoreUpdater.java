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
    private static final String UNENCHANTED_MARKER = "§2★ §aUnenchanted §2★"; // Marker for unenchanted items
    private static final String ENCHANTS_PREFIX = "§2★ "; // Prefix for enchantments
    private static final String ENCHANTS_SUFFIX = " §2★"; // Suffix for enchantments
    private static final String COMMA_COLOR = "§7, "; // Dark gray comma
    private static final String ENCHANT_COLOR = "§6"; // Gold color for enchantments

    public static void updateItemLoreWithEnchantments(ItemStack item) {
        if (item == null || item.getType().isAir()) return;

        org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        // Get enchantments from the item
        Map<Enchantment, Integer> enchantments = item.getEnchantments();

        // Create or get existing lore
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }

        // Build the enchantment list
        List<String> enchantmentLines = buildEnchantmentList(enchantments);

        // Identify the lines where enchantments are located
        int enchantmentsIndex = findEnchantmentsIndex(lore);
        boolean hasUnenchantedMarker = lore.contains(UNENCHANTED_MARKER);

        // If there are enchantments
        if (!enchantmentLines.isEmpty()) {
            // If the unenchanted marker exists, remove it
            if (hasUnenchantedMarker) {
                lore.remove(UNENCHANTED_MARKER); // Remove the unenchanted marker if there are enchantments
            }

            // Remove existing enchantment lines
            removeEnchantmentLines(lore);

            // Add enchantments to the correct position
            if (enchantmentsIndex >= 0) {
                lore.addAll(enchantmentsIndex, enchantmentLines);
            } else {
                lore.addAll(0, enchantmentLines); // Fallback to the top if not found
            }
        } else {
            // If there are no enchantments, ensure the unenchanted marker is present
            if (!hasUnenchantedMarker) {
                // Remove existing enchantment lines
                removeEnchantmentLines(lore);

                // Add the unenchanted marker if it doesn't exist
                int firstStarIndex = findFirstStarIndex(lore);
                if (firstStarIndex >= 0) {
                    lore.add(firstStarIndex, UNENCHANTED_MARKER); // Insert unenchanted marker at the same line as the first star
                } else {
                    lore.add(UNENCHANTED_MARKER); // Fallback to the end if no stars are found
                }
            }
        }

        // Remove empty lines
        removeEmptyLines(lore);

        // Update the item's lore
        meta.setLore(lore);

        // Set HideFlags to hide default enchantment lore for all items
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
    }

    // Find the index of where enchantment lines should be added or replaced
    private static int findEnchantmentsIndex(List<String> lore) {
        for (int i = 0; i < lore.size(); i++) {
            if (lore.get(i).startsWith(ENCHANTS_PREFIX)) {
                return i; // Return the index if found
            }
        }
        return -1; // Return -1 if not found
    }

    // Find the first star index
    private static int findFirstStarIndex(List<String> lore) {
        for (int i = 0; i < lore.size(); i++) {
            if (lore.get(i).startsWith("§2★")) {
                return i; // Return the index of the first star
            }
        }
        return -1; // Return -1 if not found
    }

    // Helper method to remove enchantment lines from lore
    private static void removeEnchantmentLines(List<String> lore) {
        lore.removeIf(line -> line.startsWith(ENCHANTS_PREFIX) || line.equals(UNENCHANTED_MARKER));
    }

    // Helper method to remove empty lines from lore
    private static void removeEmptyLines(List<String> lore) {
        lore.removeIf(String::isEmpty);
    }

    // Helper method to format the enchantment names and levels
    private static List<String> buildEnchantmentList(Map<Enchantment, Integer> enchantments) {
        List<String> lines = new ArrayList<>();
    
        if (enchantments.isEmpty()) return lines;

        StringBuilder currentLine = new StringBuilder(ENCHANTS_PREFIX); // Start with the prefix

        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            String formattedEnchantment = formatEnchantment(entry.getKey().getKey().getKey(), entry.getValue());

            // Check if adding this enchantment exceeds the limit
            if (currentLine.length() + formattedEnchantment.length() + COMMA_COLOR.length() > LINE_LENGTH_LIMIT) {
                // Add the current line to the list without a trailing comma
                lines.add(currentLine.toString().trim() + ENCHANTS_SUFFIX); // Add the suffix
                currentLine = new StringBuilder(ENCHANTS_PREFIX); // Start a new line
            }
            currentLine.append(formattedEnchantment).append(COMMA_COLOR); // Add enchantment to the current line
        }

        // Add the last line if not empty, removing trailing comma and adding suffix
        if (currentLine.length() > ENCHANTS_PREFIX.length()) {
            lines.add(currentLine.substring(0, currentLine.length() - COMMA_COLOR.length()).trim() + ENCHANTS_SUFFIX); // Remove the last comma
        }

        // No need to change the color for enchantments here, as it will be done when constructing the list
        return lines.stream().map(line -> ENCHANT_COLOR + line).collect(Collectors.toList());
    }

    // Helper method to format a single enchantment
    private static String formatEnchantment(String enchantmentKey, int level) {
        return formatEnchantmentName(enchantmentKey) + " " + romanNumerals(level); // Stars will be added when updating lore
    }

    // Convert level to Roman numerals
    private static String romanNumerals(int level) {
        switch (level) {
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            case 5: return "V";
            case 6: return "VI";
            case 7: return "VII";
            case 8: return "VIII";
            case 9: return "IX";
            case 10: return "X";
            default: return String.valueOf(level); // Fallback to level if out of range
        }
    }

    // Helper method to format the enchantment name
    private static String formatEnchantmentName(String enchantmentKey) {
        String[] words = enchantmentKey.toLowerCase().split("_");
        StringBuilder formattedName = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            formattedName.append(i == 0 ? capitalize(word) : lowercaseSmallWord(word)).append(" ");
        }
        return formattedName.toString().trim();
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