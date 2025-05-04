package de.verdox.vcore.paper.serializer.pdc;

import de.verdox.vcore.paper.serializer.PDCSerializationContext;
import de.verdox.vserializer.blank.BlankSerializationArray;
import de.verdox.vserializer.blank.BlankSerializationPrimitive;
import de.verdox.vserializer.generic.*;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.ListPersistentDataType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

public class PDCSerializationContainer extends PDCSerializationElement implements SerializationContainer {
    private static final NamespacedKey ARRAY_MARKER = new NamespacedKey("array", "array_marker");

    public PDCSerializationContainer(String namespace, PersistentDataContainer parent, PDCSerializationContext pdcSerializationContainer) {
        super(namespace, parent, pdcSerializationContainer);
    }

    @Override
    public Collection<String> getChildKeys() {
        return parent.getKeys().stream().filter(namespacedKey -> namespacedKey.namespace().equals(namespace)).map(NamespacedKey::getKey).toList();
    }

    @Override
    public @NotNull SerializationElement get(String s) {
        return deserializeFromPDC(new NamespacedKey(namespace, s), parent);
    }

    @Override
    public boolean contains(String s) {
        return false;
    }

    @Override
    public void set(String s, SerializationElement serializationElement) {

    }

    @Override
    public void remove(String s) {

    }

    @Override
    public boolean getAsBoolean() {
        return false;
    }

    @Override
    public String getAsString() {
        return "";
    }

    @Override
    public char getAsCharacter() {
        return 0;
    }

    @Override
    public Number getAsNumber() {
        return null;
    }

    @Override
    public double getAsDouble() {
        return 0;
    }

    @Override
    public float getAsFloat() {
        return 0;
    }

    @Override
    public long getAsLong() {
        return 0;
    }

    @Override
    public int getAsInt() {
        return 0;
    }

    @Override
    public short getAsShort() {
        return 0;
    }

    @Override
    public byte getAsByte() {
        return 0;
    }

    @Override
    public SerializationContext getContext() {
        return null;
    }

    public SerializationElement deserializeFromPDC(NamespacedKey key, PersistentDataContainer parent) {
        if (parent.has(key, PersistentDataType.BOOLEAN)) {
            return pdcSerializationContext.create(parent.get(key, PersistentDataType.BooleanPersistentDataType.BOOLEAN));
        }
        if (parent.has(key, PersistentDataType.STRING)) {
            String value = parent.get(key, PersistentDataType.BooleanPersistentDataType.STRING);
            if ("null".equals(value)) {
                return pdcSerializationContext.createNull();
            }
            return pdcSerializationContext.create(value);
        } else if (parent.has(key, PersistentDataType.BYTE))
            return pdcSerializationContext.create(parent.get(key, PersistentDataType.BooleanPersistentDataType.BYTE));
        else if (parent.has(key, PersistentDataType.SHORT))
            return pdcSerializationContext.create(parent.get(key, PersistentDataType.BooleanPersistentDataType.SHORT));
        else if (parent.has(key, PersistentDataType.INTEGER))
            return pdcSerializationContext.create(parent.get(key, PersistentDataType.BooleanPersistentDataType.INTEGER));
        else if (parent.has(key, PersistentDataType.LONG))
            return pdcSerializationContext.create(parent.get(key, PersistentDataType.BooleanPersistentDataType.LONG));
        else if (parent.has(key, PersistentDataType.FLOAT))
            return pdcSerializationContext.create(parent.get(key, PersistentDataType.BooleanPersistentDataType.FLOAT));
        else if (parent.has(key, PersistentDataType.DOUBLE))
            return pdcSerializationContext.create(parent.get(key, PersistentDataType.BooleanPersistentDataType.DOUBLE));
        else if (parent.has(key, ListPersistentDataType.BYTE))
            return new PDCSerializationArray<>(key, parent, pdcSerializationContext, PersistentDataType.LIST.bytes(),  SerializationElement::getAsByte, BlankSerializationPrimitive::new);
        else if (parent.has(key, ListPersistentDataType.SHORT))
            return new PDCSerializationArray<>(key, parent, pdcSerializationContext, PersistentDataType.LIST.shorts(),  SerializationElement::getAsShort, BlankSerializationPrimitive::new);
        else if (parent.has(key, ListPersistentDataType.INTEGER))
            return new PDCSerializationArray<>(key, parent, pdcSerializationContext, PersistentDataType.LIST.integers(),  SerializationElement::getAsInt, BlankSerializationPrimitive::new);
        else if (parent.has(key, ListPersistentDataType.LONG))
            return new PDCSerializationArray<>(key, parent, pdcSerializationContext, PersistentDataType.LIST.longs(),  SerializationElement::getAsLong, BlankSerializationPrimitive::new);
        else if (parent.has(key, ListPersistentDataType.FLOAT))
            return new PDCSerializationArray<>(key, parent, pdcSerializationContext, PersistentDataType.LIST.floats(),  SerializationElement::getAsFloat, BlankSerializationPrimitive::new);
        else if (parent.has(key, ListPersistentDataType.DOUBLE))
            return new PDCSerializationArray<>(key, parent, pdcSerializationContext, PersistentDataType.LIST.doubles(),  SerializationElement::getAsDouble, BlankSerializationPrimitive::new);
        else if (parent.has(key, ListPersistentDataType.BOOLEAN))
            return new PDCSerializationArray<>(key, parent, pdcSerializationContext, PersistentDataType.LIST.booleans(),  SerializationElement::getAsBoolean, BlankSerializationPrimitive::new);
        else if (parent.has(key, ListPersistentDataType.STRING))
            return new PDCSerializationArray<>(key, parent, pdcSerializationContext, PersistentDataType.LIST.strings(),  SerializationElement::getAsString, BlankSerializationPrimitive::new);
        else if (parent.has(key, PersistentDataType.TAG_CONTAINER)) {
            PersistentDataContainer pdc = parent.get(key, PersistentDataType.TAG_CONTAINER);
/*            if (pdc.has(ARRAY_MARKER, PersistentDataType.BOOLEAN)) {
                SerializationArray array = pdcSerializationContext.createArray();
                for (NamespacedKey pdcKey : pdc.getKeys()) {
                    if (pdcKey.equals(ARRAY_MARKER))
                        continue;

                    SerializationElement element = deserializeFromPDC(pdcKey, pdc);
                    array.add(element);
                }
                return array;
            } else {*/
                return new PDCSerializationContainer(key.namespace(), pdc, pdcSerializationContext);
            //}
        } else
            return pdcSerializationContext.createNull();
    }

    public void serializeToPDC(SerializationElement element, NamespacedKey key, PersistentDataContainer parent) {
        if (element.isPrimitive()) {
            SerializationPrimitive primitive = element.getAsPrimitive();
            serializePrimitiveToPDC(key, parent, primitive);
        } else if (element.isNull()) {
            parent.set(key, PersistentDataType.STRING, "null");
        } else if (element.isArray()) {
            serializeArrayToPDC(element, key, parent);
        } else if (element.isContainer()) {
            serializeContainerToPDC(element, key, parent);
        }
    }

    private void serializeContainerToPDC(SerializationElement element, NamespacedKey key, PersistentDataContainer parent) {
        SerializationContainer container = element.getAsContainer();
        PersistentDataContainer newPDC = parent.getAdapterContext().newPersistentDataContainer();
        for (String childKey : container.getChildKeys()) {
            SerializationElement childElement = container.get(childKey);
            serializeToPDC(childElement, new NamespacedKey(key.namespace(), childKey), newPDC);
        }
        parent.set(key, PersistentDataType.TAG_CONTAINER, newPDC);
    }

    private void serializeArrayToPDC(SerializationElement element, NamespacedKey key, PersistentDataContainer parent) {
        /*SerializationArray array = element.getAsArray();

        if (array.isBoolArray())
            parent.set(key, ListPersistentDataType.LIST.booleans(), toList(array.getAsBooleanArray()));

        else if (array.isByteArray())
            parent.set(key, ListPersistentDataType.LIST.bytes(), toList(array.getAsByteArray()));
        else if (array.isShortArray())
            parent.set(key, ListPersistentDataType.LIST.shorts(), toList(array.getAsShortArray()));
        else if (array.isIntArray())
            parent.set(key, ListPersistentDataType.LIST.integers(), toList(array.getAsIntArray()));
        else if (array.isLongArray())
            parent.set(key, ListPersistentDataType.LIST.longs(), toList(array.getAsLongArray()));
        else if (array.isFloatArray())
            parent.set(key, ListPersistentDataType.LIST.floats(), toList(array.getAsFloatArray()));
        else if (array.isDoubleArray())
            parent.set(key, ListPersistentDataType.LIST.doubles(), toList(array.getAsDoubleArray()));
        else if (array.isStringArray() || array.isCharArray())
            parent.set(key, ListPersistentDataType.LIST.strings(), Arrays.stream(array.getAsStringArray()).toList());
        else if (array.isArrayOfArrays()) {
            PersistentDataContainer childPDC = parent.getAdapterContext().newPersistentDataContainer();
            childPDC.set(ARRAY_MARKER, PersistentDataType.BOOLEAN, true);
            int counter = 0;
            for (SerializationArray childArray : array.getAsArrayOfArrays()) {
                serializeToPDC(childArray, new NamespacedKey(key.namespace(), counter + ""), childPDC);
                counter++;
            }
            parent.set(key, PersistentDataType.TAG_CONTAINER, childPDC);
        } else if (array.isContainerArray()) {
            PersistentDataContainer childPDC = parent.getAdapterContext().newPersistentDataContainer();
            childPDC.set(ARRAY_MARKER, PersistentDataType.BOOLEAN, true);
            int counter = 0;
            for (SerializationContainer childContainer : array.getAsContainerArray()) {
                serializeToPDC(childContainer, new NamespacedKey(key.namespace(), counter + ""), childPDC);
                counter++;
            }
            parent.set(key, PersistentDataType.TAG_CONTAINER, childPDC);
        }*/
    }

    private static void serializePrimitiveToPDC(NamespacedKey key, PersistentDataContainer parent, SerializationPrimitive primitive) {
        if (primitive.isString() || primitive.isCharacter())
            parent.set(key, PersistentDataType.STRING, primitive.getAsString());
        else if (primitive.isBoolean())
            parent.set(key, PersistentDataType.BOOLEAN, primitive.getAsBoolean());
        else if (primitive.isByte())
            parent.set(key, PersistentDataType.BYTE, primitive.getAsByte());
        else if (primitive.isShort())
            parent.set(key, PersistentDataType.SHORT, primitive.getAsShort());
        else if (primitive.isInteger())
            parent.set(key, PersistentDataType.INTEGER, primitive.getAsInt());
        else if (primitive.isLong())
            parent.set(key, PersistentDataType.LONG, primitive.getAsLong());
        else if (primitive.isFloat())
            parent.set(key, PersistentDataType.FLOAT, primitive.getAsFloat());
        else if (primitive.isDouble())
            parent.set(key, PersistentDataType.DOUBLE, primitive.getAsDouble());
    }
}
