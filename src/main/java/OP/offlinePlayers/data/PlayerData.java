package OP.offlinePlayers.data;

import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private final String name;
    private final long lastSeen;
    private final long firstPlayed;
    
    public PlayerData(UUID uuid, String name, long lastSeen, long firstPlayed) {
        this.uuid = uuid;
        this.name = name;
        this.lastSeen = lastSeen;
        this.firstPlayed = firstPlayed;
    }
    
    public UUID getUuid() {
        return uuid;
    }
    
    public String getName() {
        return name;
    }
    
    public long getLastSeen() {
        return lastSeen;
    }
    
    public long getFirstPlayed() {
        return firstPlayed;
    }
}