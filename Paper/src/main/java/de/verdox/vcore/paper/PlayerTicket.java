package de.verdox.vcore.paper;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import de.verdox.vpipeline.api.ticket.Ticket;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class PlayerTicket implements Ticket {
    protected UUID playerTargetUUID;

    public PlayerTicket(@NotNull UUID playerTarget) {
        this.playerTargetUUID = playerTarget;
    }

    public PlayerTicket() {

    }

    @Override
    public final TriState apply(Object[] objects) {
        Player player = (Player) objects[0];
        if (!player.getUniqueId().equals(playerTargetUUID))
            return TriState.FALSE;
        return consume(player, objects);
    }

    @Override
    public void readInputParameter(ByteArrayDataInput byteArrayDataInput) {
        playerTargetUUID = UUID.fromString(byteArrayDataInput.readUTF());
    }

    @Override
    public void writeInputParameter(ByteArrayDataOutput byteArrayDataOutput) {
        byteArrayDataOutput.writeUTF(playerTargetUUID.toString());
    }

    protected abstract TriState consume(@NotNull Player player, Object[] objects);
}
