package de.verdox.vcore.paper.serializer.pdc;

import de.verdox.vcore.paper.serializer.PDCSerializationContext;
import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.generic.SerializationElement;
import org.bukkit.persistence.PersistentDataContainer;

public abstract class PDCSerializationElement implements SerializationElement {
    protected final String namespace;
    protected final PersistentDataContainer parent;
    protected final PDCSerializationContext pdcSerializationContext;

    public PDCSerializationElement(String namespace, PersistentDataContainer parent, PDCSerializationContext pdcSerializationContext){
        this.namespace = namespace;
        this.parent = parent;
        this.pdcSerializationContext = pdcSerializationContext;
    }

    @Override
    public boolean getAsBoolean() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public String getAsString() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public char getAsCharacter() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public Number getAsNumber() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public double getAsDouble() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public float getAsFloat() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public long getAsLong() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public int getAsInt() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public short getAsShort() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public byte getAsByte() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public SerializationContext getContext() {
        return pdcSerializationContext;
    }
}
