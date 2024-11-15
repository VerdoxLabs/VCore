package de.verdox.vcore;

import de.verdox.vpipeline.api.NetworkParticipant;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkPlayerCache {
    private final NetworkParticipant networkParticipant;
    private final Map<UUID, UUID> playerToServerMapping = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> serverToPlayerMapping = new ConcurrentHashMap<>();

    public NetworkPlayerCache(NetworkParticipant networkParticipant) {
        this.networkParticipant = networkParticipant;
    }

    public void addPlayer(UUID playerUUID, UUID networkParticipantUUID) {
        playerToServerMapping.put(playerUUID, networkParticipantUUID);
        serverToPlayerMapping.put(networkParticipantUUID, playerUUID);
    }

    public void removePlayer(UUID playerUUID){

    }

    public void removeServer(UUID serverUUID){

    }

}
