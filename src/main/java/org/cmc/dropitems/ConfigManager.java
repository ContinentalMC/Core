package org.cmc.dropitems;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {

    private static FileConfiguration config;

    public static void setupConfig(DropItems di) {
        ConfigManager.config = di.getConfig();
        di.saveDefaultConfig();
    }

    public static List<String> getItemList() {
        return config.getStringList("Items");
    }

}
