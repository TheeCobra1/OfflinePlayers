package OP.offlinePlayers.listeners;

import OP.offlinePlayers.OfflinePlayers;
import OP.offlinePlayers.data.PlayerData;
import OP.offlinePlayers.gui.AdminPlayerInfoGUI;
import OP.offlinePlayers.gui.OfflinePlayersGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class GUIListener implements Listener {
    private final OfflinePlayers plugin;
    
    public GUIListener(OfflinePlayers plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        String title = event.getView().getTitle();
        
        if (title.startsWith("§8Offline Players")) {
            handleOfflinePlayersGUI(event, player, title);
        } else if (title.startsWith("§8Player Info:")) {
            handleAdminPlayerInfoGUI(event, player);
        }
    }
    
    private void handleOfflinePlayersGUI(InventoryClickEvent event, Player player, String title) {
        event.setCancelled(true);
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;
        
        int slot = event.getSlot();
        
        if (slot == 48 && clicked.getType() == Material.ARROW) {
            int currentPage = getCurrentPage(title);
            if (currentPage > 0) {
                new OfflinePlayersGUI(plugin, player).openPage(currentPage - 1);
            }
        } else if (slot == 50 && clicked.getType() == Material.ARROW) {
            int currentPage = getCurrentPage(title);
            new OfflinePlayersGUI(plugin, player).openPage(currentPage + 1);
        } else if (clicked.getType() == Material.PLAYER_HEAD && player.hasPermission("offlineplayers.admin")) {
            SkullMeta meta = (SkullMeta) clicked.getItemMeta();
            if (meta != null) {
                OfflinePlayer target = meta.getOwningPlayer();
                if (target != null) {
                    PlayerData data = plugin.getPlayerDataManager().getPlayerData(target.getUniqueId());
                    if (data != null) {
                        new AdminPlayerInfoGUI(plugin, player, data).open();
                    }
                }
            }
        }
    }
    
    private void handleAdminPlayerInfoGUI(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;
        
        if (clicked.getType() == Material.BARRIER) {
            new OfflinePlayersGUI(plugin, player).open();
        }
    }
    
    private int getCurrentPage(String title) {
        String pageInfo = title.substring(title.indexOf("(Page ") + 6);
        String pageNumber = pageInfo.substring(0, pageInfo.indexOf("/"));
        return Integer.parseInt(pageNumber) - 1;
    }
}