package OP.offlinePlayers.gui;

import OP.offlinePlayers.OfflinePlayers;
import OP.offlinePlayers.data.PlayerData;
import OP.offlinePlayers.utils.ItemBuilder;
import OP.offlinePlayers.utils.TimeFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class OfflinePlayersGUI {
    private final OfflinePlayers plugin;
    private final Player player;
    private int currentPage = 0;
    private List<PlayerData> offlinePlayers;
    
    private static final int ITEMS_PER_PAGE = 45;
    private static final int PREV_PAGE_SLOT = 48;
    private static final int NEXT_PAGE_SLOT = 50;
    private static final int INFO_SLOT = 49;
    
    public OfflinePlayersGUI(OfflinePlayers plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.offlinePlayers = plugin.getPlayerDataManager().getOfflinePlayers();
    }
    
    public void open() {
        openPage(0);
    }
    
    public void openPage(int page) {
        this.currentPage = page;
        
        Inventory inventory = Bukkit.createInventory(null, 54, 
            "§8Offline Players §7(Page " + (page + 1) + "/" + getTotalPages() + ")");
        
        int startIndex = page * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, offlinePlayers.size());
        
        for (int i = startIndex; i < endIndex; i++) {
            PlayerData data = offlinePlayers.get(i);
            ItemStack head = createPlayerHead(data);
            inventory.setItem(i - startIndex, head);
        }
        
        if (page > 0) {
            ItemStack prevPage = new ItemBuilder(Material.ARROW)
                .setName("§a« Previous Page")
                .setLore("§7Click to go to page " + page)
                .build();
            inventory.setItem(PREV_PAGE_SLOT, prevPage);
        }
        
        ItemStack info = new ItemBuilder(Material.BOOK)
            .setName("§6Offline Players")
            .setLore(
                "§7Total offline players: §e" + offlinePlayers.size(),
                "",
                "§7Hover over player heads",
                "§7to view their information!"
            )
            .build();
        inventory.setItem(INFO_SLOT, info);
        
        if (page < getTotalPages() - 1) {
            ItemStack nextPage = new ItemBuilder(Material.ARROW)
                .setName("§aNext Page »")
                .setLore("§7Click to go to page " + (page + 2))
                .build();
            inventory.setItem(NEXT_PAGE_SLOT, nextPage);
        }
        
        for (int i = 45; i < 54; i++) {
            if (i != PREV_PAGE_SLOT && i != NEXT_PAGE_SLOT && i != INFO_SLOT) {
                inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                    .setName(" ")
                    .build());
            }
        }
        
        player.openInventory(inventory);
    }
    
    private ItemStack createPlayerHead(PlayerData data) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(data.getUuid()));
        meta.setDisplayName("§e" + data.getName());
        
        List<String> lore = new ArrayList<>();
        lore.add("§8§m                              ");
        lore.add("§7Last Seen: §f" + TimeFormatter.formatTime(data.getLastSeen()));
        lore.add("§7Last Online: §f" + TimeFormatter.formatDate(data.getLastSeen()));
        
        if (data.getFirstPlayed() > 0) {
            lore.add("§7First Joined: §f" + TimeFormatter.formatDate(data.getFirstPlayed()));
        }
        
        lore.add("§7UUID: §f" + data.getUuid().toString().substring(0, 8) + "...");
        lore.add("§8§m                              ");
        
        if (player.hasPermission("offlineplayers.admin")) {
            lore.add("");
            lore.add("§eClick to view detailed info");
        }
        
        meta.setLore(lore);
        head.setItemMeta(meta);
        
        return head;
    }
    
    private int getTotalPages() {
        return (int) Math.ceil((double) offlinePlayers.size() / ITEMS_PER_PAGE);
    }
    
    public int getCurrentPage() {
        return currentPage;
    }
}