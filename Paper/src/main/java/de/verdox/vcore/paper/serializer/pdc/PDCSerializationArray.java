package de.verdox.vcore.paper.serializer.pdc;

import de.verdox.vcore.paper.serializer.PDCSerializationContext;
import de.verdox.vserializer.generic.SerializationArray;
import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.generic.SerializationElement;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.ListPersistentDataType;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PDCSerializationArray<P, C> extends PDCSerializationElement implements SerializationArray {
    private final NamespacedKey keyOfElement;
    private final ListPersistentDataType<P, C> listPersistentDataType;
    private final BiFunction<SerializationContext, C, SerializationElement> toElement;
    private final Function<SerializationElement, C> fromElement;

    public PDCSerializationArray(NamespacedKey keyOfElement, PersistentDataContainer parent, PDCSerializationContext pdcSerializationContext, ListPersistentDataType<P, C> listPersistentDataType, Function<SerializationElement, C> fromElement, BiFunction<SerializationContext, C, SerializationElement> toElement) {
        super(keyOfElement.namespace(), parent, pdcSerializationContext);
        this.keyOfElement = keyOfElement;
        this.listPersistentDataType = listPersistentDataType;
        this.toElement = toElement;
        this.fromElement = fromElement;
    }

    @Override
    public int length() {
        return readFromContainer().size();
    }

    @Override
    public SerializationElement get(int i) {
        return toElement.apply(pdcSerializationContext, readFromContainer().get(i));
    }

    @Override
    public void add(SerializationElement serializationElement) {
        List<C> list = readFromContainer();
        list.add(fromElement.apply(serializationElement));
        saveToContainer(list);
    }

    @Override
    public void set(int i, SerializationElement serializationElement) {
        List<C> list = readFromContainer();
        list.set(i, fromElement.apply(serializationElement));
        saveToContainer(list);
    }

    @Override
    public SerializationElement remove(int i) {
        List<C> list = readFromContainer();
        SerializationElement removed = get(i);
        list.remove(i);
        saveToContainer(list);
        return removed;
    }

    @Override
    public @NotNull Iterator<SerializationElement> iterator() {
        return new Iterator<>() {
            private int counter = 0;

            @Override
            public boolean hasNext() {
                return counter < length();
            }

            @Override
            public SerializationElement next() {
                return get(counter++);
            }
        };
    }

    private List<C> readFromContainer() {
        return parent.get(keyOfElement, listPersistentDataType);
    }

    private void saveToContainer(List<C> list) {
        parent.set(keyOfElement, listPersistentDataType, list);
    }
}
