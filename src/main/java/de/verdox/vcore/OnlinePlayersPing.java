package de.verdox.vcore;

import de.verdox.vpipeline.api.messaging.MessagingService;
import de.verdox.vpipeline.api.messaging.instruction.types.Update;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class OnlinePlayersPing extends Update {
    public OnlinePlayersPing(@NotNull UUID uuid) {
        super(uuid);
    }

    @Override
    public UpdateCompletion onInstructionReceive(MessagingService messagingService) {
        return null;
    }
}
