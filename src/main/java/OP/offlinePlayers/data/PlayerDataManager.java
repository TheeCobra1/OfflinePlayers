package OP.offlinePlayers.data;

import OP.offlinePlayers.OfflinePlayers;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerDataManager {
    private final OfflinePlayers plugin;
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();
    private File dataFile;
    private FileConfiguration dataConfig;
    
    public PlayerDataManager(OfflinePlayers plugin) {
        this.plugin = plugin;
        loadData();
    }
    
    private void loadData() {
        dataFile = new File(plugin.getDataFolder(), "playerdata.yml");
        if (!dataFile.exists()) {
            plugin.getDataFolder().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        
        if (dataConfig.contains("players")) {
            for (String uuidString : dataConfig.getConfigurationSection("players").getKeys(false)) {
                UUID uuid = UUID.fromString(uuidString);
                String name = dataConfig.getString("players." + uuidString + ".name");
                long lastSeen = dataConfig.getLong("players." + uuidString + ".lastseen");
                long firstPlayed = dataConfig.getLong("players." + uuidString + ".firstplayed", 0);
                playerDataMap.put(uuid, new PlayerData(uuid, name, lastSeen, firstPlayed));
            }
        }
        
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (!playerDataMap.containsKey(offlinePlayer.getUniqueId()) && offlinePlayer.getName() != null) {
                long lastPlayed = offlinePlayer.getLastPlayed();
                if (lastPlayed == 0) lastPlayed = System.currentTimeMillis();
                long firstPlayed = offlinePlayer.getFirstPlayed();
                if (firstPlayed == 0) firstPlayed = lastPlayed;
                playerDataMap.put(offlinePlayer.getUniqueId(), 
                    new PlayerData(offlinePlayer.getUniqueId(), offlinePlayer.getName(), lastPlayed, firstPlayed));
            }
        }
        
        saveData();
    }
    
    public void updatePlayerData(Player player) {
        PlayerData existing = playerDataMap.get(player.getUniqueId());
        long firstPlayed = existing != null ? existing.getFirstPlayed() : player.getFirstPlayed();
        if (firstPlayed == 0) firstPlayed = System.currentTimeMillis();
        
        playerDataMap.put(player.getUniqueId(), 
            new PlayerData(player.getUniqueId(), player.getName(), System.currentTimeMillis(), firstPlayed));
        saveData();
    }
    
    public List<PlayerData> getOfflinePlayers() {
        return playerDataMap.values().stream()
            .filter(data -> Bukkit.getPlayer(data.getUuid()) == null)
            .sorted((a, b) -> Long.compare(b.getLastSeen(), a.getLastSeen()))
            .collect(Collectors.toList());
    }
    
    public PlayerData getPlayerData(UUID uuid) {
        return playerDataMap.get(uuid);
    }
    
    public void saveData() {
        for (Map.Entry<UUID, PlayerData> entry : playerDataMap.entrySet()) {
            PlayerData data = entry.getValue();
            dataConfig.set("players." + data.getUuid().toString() + ".name", data.getName());
            dataConfig.set("players." + data.getUuid().toString() + ".lastseen", data.getLastSeen());
            dataConfig.set("players." + data.getUuid().toString() + ".firstplayed", data.getFirstPlayed());
        }
        
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}