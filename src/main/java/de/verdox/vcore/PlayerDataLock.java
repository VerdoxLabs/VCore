package de.verdox.vcore;

import de.verdox.vpipeline.api.pipeline.annotations.DataStorageIdentifier;
import de.verdox.vpipeline.api.pipeline.annotations.PipelineDataProperties;
import de.verdox.vpipeline.api.pipeline.core.Pipeline;
import de.verdox.vpipeline.api.pipeline.datatypes.PipelineData;
import de.verdox.vpipeline.api.pipeline.enums.DataContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@PipelineDataProperties(dataContext = DataContext.CACHE_ONLY)
@DataStorageIdentifier(identifier = "player_data_lock")
public class PlayerDataLock extends PipelineData {
    public PlayerDataLock(@NotNull Pipeline pipeline, @NotNull UUID objectUUID) {
        super(pipeline, objectUUID);
    }
}
