package org.cmc.shops;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.ghostchu.quickshop.QuickShop;

public class shopcommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            QuickShop api = QuickShop.getInstance();
            // Let's insert our code here when we can figure out the API
            return true;
        } else {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }
    }
}
