package de.verdox.vcore.data;


import de.verdox.vpipeline.api.pipeline.core.Pipeline;
import de.verdox.vpipeline.api.pipeline.parts.cache.local.AccessInvalidException;
import de.verdox.vpipeline.api.pipeline.parts.cache.local.DataAccess;
import de.verdox.vpipeline.api.pipeline.parts.cache.local.DataSubscriber;
import de.verdox.vpipeline.api.pipeline.parts.cache.local.LockableAction;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @param <P> Platform dependent player implementation
 */
public class PlayerDataLoader<P> {
    public static final Logger LOGGER = Logger.getLogger(PlayerData.class.getSimpleName());
    protected final Pipeline pipeline;
    private final Set<Class<? extends PlayerData<P>>> savedPlayerDataTypes = new HashSet<>();
    private final Map<UUID, Map<Class<? extends PlayerData<P>>, Data<?, ?>>> dataAccessMap = new HashMap<>();

    public PlayerDataLoader(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public PlayerDataLoader<P> registerPlayerDataForConnectSerialization(Class<? extends PlayerData<P>> type) {
        this.pipeline.getDataRegistry().registerType(type);
        savedPlayerDataTypes.add(type);
        DataAccess<?> access;
        return this;
    }

    public void loadPlayerData(UUID playerUUID) {
        for (Class<? extends PlayerData<P>> savedPlayerDataType : savedPlayerDataTypes) {
            load(savedPlayerDataType, playerUUID);
        }
        LOGGER.log(Level.FINER, "Loaded player data for " + playerUUID);
    }

    public void savePlayerData(UUID playerUUID) {
        List<Class<?>> savedTypes = new ArrayList<>();
        for (Class<? extends PlayerData<P>> savedPlayerDataType : savedPlayerDataTypes) {
            if (!dataAccessMap.containsKey(playerUUID)) {
                continue;
            }
            if (saveAndClean(savedPlayerDataType, playerUUID)) {
                savedTypes.add(savedPlayerDataType);
            }
        }
        LOGGER.log(Level.FINER, "Saved player data for " + playerUUID + " [" + savedTypes.stream().map(Class::getSimpleName).toList() + "]");
    }

    @Nullable
    public <T extends PlayerData<P>, RETURN> RETURN readOnly(Class<T> type, UUID playerUUID, Function<T, RETURN> readOnly) {
        if (dataAccessMap.containsKey(playerUUID)) {
            throw new IllegalArgumentException("No data found for player " + playerUUID);
        }
        Data<P, T> data = (Data<P, T>) dataAccessMap.get(playerUUID).getOrDefault(type, null);
        if (data == null) {
            LOGGER.log(Level.SEVERE, "Data not registered to PlayerDataLoader " + type.getSimpleName());
            return null;
        }
        if (data.dataAccess().killed()) {
            return null;
        }
        T current = data.readOnlyAccess.getCurrentValue();
        if (current == null) {
            return null;
        }
        return readOnly.apply(current);
    }

    public <T extends PlayerData<P>> void write(Class<T> type, UUID playerUUID, Consumer<T> writeAction) {
        if (dataAccessMap.containsKey(playerUUID)) {
            throw new IllegalArgumentException("No data found for player " + playerUUID);
        }
        Data<P, T> data = (Data<P, T>) dataAccessMap.get(playerUUID).getOrDefault(type, null);
        if (data == null) {
            LOGGER.log(Level.SEVERE, "Data not registered to PlayerDataLoader " + type.getSimpleName());
            return;
        }
        if (data.dataAccess().killed()) {
            return;
        }
        try(var write = data.dataAccess.write()) {
            writeAction.accept(write.get());
            write.commitChanges(true);
        } catch (AccessInvalidException e) {
            throw new RuntimeException(e);
        }
    }


    private <T extends PlayerData<P>> boolean saveAndClean(Class<T> type, UUID playerUUID) {
        Data<P, T> data = (Data<P, T>) dataAccessMap.get(playerUUID).getOrDefault(type, null);
        if (data == null) {
            return false;
        }
        if (!data.dataAccess().killed()) {
            try (LockableAction.Write<?> write = data.dataAccess().write()) {
                write.commitChanges(true);
            } catch (AccessInvalidException e) {
                LOGGER.log(Level.SEVERE, "Could not save " + type.getSimpleName() + " for player " + playerUUID + ": " + e.getMessage());
                return false;
            }
        }
        pipeline.getLocalCache().removeSubscriber(data.readOnlyAccess);
        dataAccessMap.get(playerUUID).remove(type);
        return true;
    }

    private <T extends PlayerData<P>> void load(Class<T> type, UUID playerUUID) {
        DataAccess<T> dataAccess = pipeline.loadOrCreate(type, playerUUID);
        DataSubscriber<T, T> subscriber = DataSubscriber.createSubscriber(t -> t, t -> {
        }, null);
        dataAccess.subscribe(subscriber);
        dataAccessMap.computeIfAbsent(playerUUID, uuid -> new HashMap<>()).put(type, new Data<>(dataAccess, subscriber));
    }

    private record Data<PLAYER, DATA extends PlayerData<PLAYER>>(DataAccess<DATA> dataAccess,
                                                                 DataSubscriber<DATA, DATA> readOnlyAccess) {}

}
