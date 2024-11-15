package de.verdox.vcore.paper;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class PlayerConnectionTicket extends PlayerTicket {
    public PlayerConnectionTicket(@NotNull UUID playerTarget) {
        super(playerTarget);
    }

    public PlayerConnectionTicket() {
    }
}
