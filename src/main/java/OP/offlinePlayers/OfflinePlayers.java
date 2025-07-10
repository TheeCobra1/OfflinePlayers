package OP.offlinePlayers;

import OP.offlinePlayers.commands.OfflinePlayersCommand;
import OP.offlinePlayers.data.PlayerDataManager;
import OP.offlinePlayers.listeners.GUIListener;
import OP.offlinePlayers.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class OfflinePlayers extends JavaPlugin {
    private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        playerDataManager = new PlayerDataManager(this);
        
        getCommand("offlineplayers").setExecutor(new OfflinePlayersCommand(this));
        
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        
        getLogger().info("OfflinePlayers plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        if (playerDataManager != null) {
            playerDataManager.saveData();
        }
        getLogger().info("OfflinePlayers plugin has been disabled!");
    }
    
    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
}
