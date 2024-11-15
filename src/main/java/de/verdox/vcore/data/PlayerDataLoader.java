package de.verdox.vcore.data;

import de.verdox.vcore.PlayerDataLock;
import de.verdox.vpipeline.api.pipeline.core.Pipeline;
import de.verdox.vpipeline.api.pipeline.parts.cache.local.AccessInvalidException;
import de.verdox.vpipeline.api.pipeline.parts.cache.local.LockableAction;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @param <P> Platform dependent player implementation
 */
public class PlayerDataLoader<P> {
    protected final Pipeline pipeline;
    private final Set<Class<? extends PlayerData<P>>> savedPlayerDataTypes = new HashSet<>();

    public PlayerDataLoader(Pipeline pipeline) {
        this.pipeline = pipeline;
        this.pipeline.getDataRegistry().registerType(PlayerDataLock.class);
    }

    public PlayerDataLoader<P> registerPlayerDataForConnectSerialization(Class<? extends PlayerData<P>> type) {
        this.pipeline.getDataRegistry().registerType(type);
        savedPlayerDataTypes.add(type);
        return this;
    }

    public void loadPlayerData(UUID playerUUID) {
        for (Class<? extends PlayerData<P>> savedPlayerDataType : savedPlayerDataTypes) {
            pipeline.load(savedPlayerDataType, playerUUID);
        }
    }

    public void savePlayerData(UUID playerUUID) {
        for (Class<? extends PlayerData<P>> savedPlayerDataType : savedPlayerDataTypes) {
            try (LockableAction.Write<?> write = pipeline.loadOrCreate(savedPlayerDataType, playerUUID).write()) {
                write.commitChanges(true);
            }
            catch (AccessInvalidException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
