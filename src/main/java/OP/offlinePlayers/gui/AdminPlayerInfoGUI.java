package OP.offlinePlayers.gui;

import OP.offlinePlayers.OfflinePlayers;
import OP.offlinePlayers.data.PlayerData;
import OP.offlinePlayers.utils.ItemBuilder;
import OP.offlinePlayers.utils.TimeFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdminPlayerInfoGUI {
    private final OfflinePlayers plugin;
    private final Player viewer;
    private final PlayerData playerData;
    private final OfflinePlayer targetPlayer;
    
    public AdminPlayerInfoGUI(OfflinePlayers plugin, Player viewer, PlayerData playerData) {
        this.plugin = plugin;
        this.viewer = viewer;
        this.playerData = playerData;
        this.targetPlayer = Bukkit.getOfflinePlayer(playerData.getUuid());
    }
    
    public void open() {
        Inventory inventory = Bukkit.createInventory(null, 45, "§8Player Info: §e" + playerData.getName());
        
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) playerHead.getItemMeta();
        headMeta.setOwningPlayer(targetPlayer);
        headMeta.setDisplayName("§e§l" + playerData.getName());
        List<String> headLore = new ArrayList<>();
        headLore.add("§7UUID: §f" + playerData.getUuid());
        headMeta.setLore(headLore);
        playerHead.setItemMeta(headMeta);
        inventory.setItem(4, playerHead);
        
        ItemStack timeInfo = new ItemBuilder(Material.CLOCK)
            .setName("§6Time Information")
            .setLore(
                "§7Last Seen: §f" + TimeFormatter.formatTime(playerData.getLastSeen()),
                "§7Last Online: §f" + TimeFormatter.formatDate(playerData.getLastSeen()),
                "§7First Joined: §f" + TimeFormatter.formatDate(playerData.getFirstPlayed()),
                "",
                "§7Time Since Join: §f" + TimeFormatter.formatDuration(playerData.getLastSeen() - playerData.getFirstPlayed()),
                "§7Days Registered: §f" + ((playerData.getLastSeen() - playerData.getFirstPlayed()) / 86400000L)
            )
            .build();
        inventory.setItem(20, timeInfo);
        
        Player onlinePlayer = Bukkit.getPlayer(playerData.getUuid());
        ItemStack statusInfo = new ItemBuilder(Material.EMERALD)
            .setName("§aPlayer Status")
            .setLore(
                "§7Online: §c" + (onlinePlayer != null ? "§aYes" : "§cNo"),
                "§7Banned: §f" + (targetPlayer.isBanned() ? "§cYes" : "§aNo"),
                "§7Whitelisted: §f" + (targetPlayer.isWhitelisted() ? "§aYes" : "§cNo"),
                "§7Op: §f" + (targetPlayer.isOp() ? "§aYes" : "§cNo"),
                "§7Game Mode: §f" + (onlinePlayer != null ? onlinePlayer.getGameMode().toString() : "Unknown")
            )
            .build();
        inventory.setItem(22, statusInfo);
        
        ItemStack worldInfo = new ItemBuilder(Material.GRASS_BLOCK)
            .setName("§2World Information")
            .setLore(
                "§7Bed Spawn: §f" + (targetPlayer.getBedSpawnLocation() != null ? 
                    formatLocation(targetPlayer.getBedSpawnLocation()) : "None"),
                "",
                "§7Player Data Files:",
                "§7- World Data: §f" + hasPlayerDataFile(targetPlayer.getUniqueId().toString()),
                "§7- Stats Data: §f" + hasStatsFile(targetPlayer.getUniqueId().toString())
            )
            .build();
        inventory.setItem(24, worldInfo);
        
        ItemStack statsInfo = createStatsItem();
        inventory.setItem(30, statsInfo);
        
        ItemStack advancementInfo = createAdvancementItem();
        inventory.setItem(32, advancementInfo);
        
        ItemStack backButton = new ItemBuilder(Material.BARRIER)
            .setName("§cBack")
            .setLore("§7Return to offline players list")
            .build();
        inventory.setItem(40, backButton);
        
        for (int i = 0; i < 45; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                    .setName(" ")
                    .build());
            }
        }
        
        viewer.openInventory(inventory);
    }
    
    private ItemStack createStatsItem() {
        List<String> lore = new ArrayList<>();
        
        try {
            int deaths = targetPlayer.getStatistic(Statistic.DEATHS);
            int mobKills = targetPlayer.getStatistic(Statistic.MOB_KILLS);
            int playerKills = targetPlayer.getStatistic(Statistic.PLAYER_KILLS);
            int distanceWalked = targetPlayer.getStatistic(Statistic.WALK_ONE_CM) / 100;
            int jumps = targetPlayer.getStatistic(Statistic.JUMP);
            int blocksPlaced = targetPlayer.getStatistic(Statistic.USE_ITEM);
            int blocksBroken = targetPlayer.getStatistic(Statistic.MINE_BLOCK);
            int damageTaken = targetPlayer.getStatistic(Statistic.DAMAGE_TAKEN) / 10;
            int damageDealt = targetPlayer.getStatistic(Statistic.DAMAGE_DEALT) / 10;
            
            lore.add("§7Deaths: §f" + deaths);
            lore.add("§7Mob Kills: §f" + mobKills);
            lore.add("§7Player Kills: §f" + playerKills);
            lore.add("§7Distance Walked: §f" + formatDistance(distanceWalked));
            lore.add("§7Blocks Broken: §f" + blocksBroken);
            lore.add("§7Damage Dealt: §f" + damageDealt);
            lore.add("§7Damage Taken: §f" + damageTaken);
        } catch (Exception e) {
            lore.add("§7Statistics unavailable");
            lore.add("§7for this player");
        }
        
        return new ItemBuilder(Material.BOOK)
            .setName("§bPlayer Statistics")
            .setLore(lore)
            .build();
    }
    
    private ItemStack createAdvancementItem() {
        List<String> lore = new ArrayList<>();
        
        lore.add("§7Advancement tracking");
        lore.add("§7requires the player");
        lore.add("§7to be online.");
        lore.add("");
        lore.add("§7Use advancement");
        lore.add("§7commands to check");
        
        return new ItemBuilder(Material.KNOWLEDGE_BOOK)
            .setName("§dAdvancements")
            .setLore(lore)
            .build();
    }
    
    private String formatDistance(int meters) {
        if (meters > 1000) {
            return String.format("%.1fkm", meters / 1000.0);
        }
        return meters + "m";
    }
    
    private String formatLocation(org.bukkit.Location loc) {
        if (loc == null) return "Unknown";
        return String.format("%s (X: %d, Y: %d, Z: %d)", 
            loc.getWorld() != null ? loc.getWorld().getName() : "Unknown",
            loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }
    
    private String hasPlayerDataFile(String uuid) {
        File worldFolder = Bukkit.getWorlds().get(0).getWorldFolder();
        File playerDataFile = new File(worldFolder, "playerdata/" + uuid + ".dat");
        return playerDataFile.exists() ? "§aExists" : "§cNot Found";
    }
    
    private String hasStatsFile(String uuid) {
        File worldFolder = Bukkit.getWorlds().get(0).getWorldFolder();
        File statsFile = new File(worldFolder, "stats/" + uuid + ".json");
        return statsFile.exists() ? "§aExists" : "§cNot Found";
    }
}