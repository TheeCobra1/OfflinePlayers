package OP.offlinePlayers.commands;

import OP.offlinePlayers.OfflinePlayers;
import OP.offlinePlayers.gui.OfflinePlayersGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OfflinePlayersCommand implements CommandExecutor {
    private final OfflinePlayers plugin;
    
    public OfflinePlayersCommand(OfflinePlayers plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }
        
        if (!player.hasPermission("offlineplayers.use")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }
        
        new OfflinePlayersGUI(plugin, player).open();
        return true;
    }
}