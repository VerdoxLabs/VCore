package de.verdox.vcore.data;

import de.verdox.vpipeline.api.pipeline.core.Pipeline;
import de.verdox.vpipeline.api.pipeline.datatypes.PipelineData;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @param <P> Platform dependent player implementation
 */
public abstract class PlayerData<P> extends PipelineData {
    private transient P player;

    public PlayerData(@NotNull Pipeline pipeline, @NotNull UUID objectUUID) {
        super(pipeline, objectUUID);
    }

    public final void attach(P player) {
        this.player = player;
    }

    public final void detach() {

    }
}
