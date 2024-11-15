package de.verdox.vcore.paper.serializer;

import de.verdox.vserializer.blank.BlankSerializationArray;
import de.verdox.vserializer.blank.BlankSerializationContext;
import de.verdox.vserializer.generic.*;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.ListPersistentDataType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class PDCSerializationContext extends BlankSerializationContext {
    private static final NamespacedKey ARRAY_MARKER = new NamespacedKey("array", "array_marker");

    public <T> void serializeToPDC(Serializer<T> serializer, T object, NamespacedKey key, PersistentDataContainer parent) {
        SerializationElement element = serializer.serialize(this, object);
        serializeToPDC(element, key, parent);
    }

    public <T> T deserializeFromPDC(Serializer<T> serializer, NamespacedKey key, PersistentDataContainer parent) {
        return serializer.deserialize(deserializeFromPDC(key, parent));
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

    public SerializationElement deserializeFromPDC(NamespacedKey key, PersistentDataContainer parent) {
        if (parent.has(key, PersistentDataType.BOOLEAN))
            return create(parent.get(key, PersistentDataType.BooleanPersistentDataType.BOOLEAN));
        if (parent.has(key, PersistentDataType.STRING)) {
            String value = parent.get(key, PersistentDataType.BooleanPersistentDataType.STRING);
            if("null".equals(value))
                return createNull();
            return create(value);
        }
        else if (parent.has(key, PersistentDataType.BYTE))
            return create(parent.get(key, PersistentDataType.BooleanPersistentDataType.BYTE));
        else if (parent.has(key, PersistentDataType.SHORT))
            return create(parent.get(key, PersistentDataType.BooleanPersistentDataType.SHORT));
        else if (parent.has(key, PersistentDataType.INTEGER))
            return create(parent.get(key, PersistentDataType.BooleanPersistentDataType.INTEGER));
        else if (parent.has(key, PersistentDataType.LONG))
            return create(parent.get(key, PersistentDataType.BooleanPersistentDataType.LONG));
        else if (parent.has(key, PersistentDataType.FLOAT))
            return create(parent.get(key, PersistentDataType.BooleanPersistentDataType.FLOAT));
        else if (parent.has(key, PersistentDataType.DOUBLE))
            return create(parent.get(key, PersistentDataType.BooleanPersistentDataType.DOUBLE));
        else if (parent.has(key, ListPersistentDataType.BYTE))
            return BlankSerializationArray.byNumbers(this, parent.get(key, PersistentDataType.LIST.bytes()));
        else if (parent.has(key, ListPersistentDataType.SHORT))
            return BlankSerializationArray.byNumbers(this, parent.get(key, PersistentDataType.LIST.shorts()));
        else if (parent.has(key, ListPersistentDataType.INTEGER))
            return BlankSerializationArray.byNumbers(this, parent.get(key, PersistentDataType.LIST.integers()));
        else if (parent.has(key, ListPersistentDataType.LONG))
            return BlankSerializationArray.byNumbers(this, parent.get(key, PersistentDataType.LIST.longs()));
        else if (parent.has(key, ListPersistentDataType.FLOAT))
            return BlankSerializationArray.byNumbers(this, parent.get(key, PersistentDataType.LIST.floats()));
        else if (parent.has(key, ListPersistentDataType.DOUBLE))
            return BlankSerializationArray.byNumbers(this, parent.get(key, PersistentDataType.LIST.doubles()));
        else if (parent.has(key, ListPersistentDataType.BOOLEAN))
            return BlankSerializationArray.byBooleans(this, parent.get(key, PersistentDataType.LIST.booleans()));
        else if (parent.has(key, ListPersistentDataType.STRING))
            return BlankSerializationArray.byStrings(this, parent.get(key, PersistentDataType.LIST.strings()));
        else if (parent.has(key, PersistentDataType.TAG_CONTAINER)){
            PersistentDataContainer pdc = parent.get(key, PersistentDataType.TAG_CONTAINER);
            if(pdc.has(ARRAY_MARKER, PersistentDataType.BOOLEAN)){
                SerializationArray array = createArray();
                for (NamespacedKey pdcKey : pdc.getKeys()) {
                    if(pdcKey.equals(ARRAY_MARKER))
                        continue;

                    SerializationElement element = deserializeFromPDC(pdcKey, pdc);
                    array.add(element);
                }
                return array;
            }
            else {
                SerializationContainer container = createContainer();
                for (NamespacedKey pdcKey : pdc.getKeys()) {
                    SerializationElement element = deserializeFromPDC(pdcKey, pdc);
                    container.set(pdcKey.value(), element);
                }
                return container;
            }
        }
        else
            return createNull();
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
        SerializationArray array = element.getAsArray();

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
        }
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

    public static List<Integer> toList(int[] array) {
        return IntStream.of(array).boxed().collect(Collectors.toList());
    }

    public static List<Double> toList(double[] array) {
        return DoubleStream.of(array).boxed().collect(Collectors.toList());
    }

    public static List<Long> toList(long[] array) {
        return LongStream.of(array).boxed().collect(Collectors.toList());
    }

    public static List<Float> toList(float[] array) {
        List<Float> list = new ArrayList<>();
        for (float v : array) {
            list.add(v);
        }
        return list;
    }

    public static List<Boolean> toList(boolean[] array) {
        List<Boolean> list = new ArrayList<>();
        for (boolean v : array) {
            list.add(v);
        }
        return list;
    }

    public static List<Character> toList(char[] array) {
        return IntStream.range(0, array.length)
                .mapToObj(i -> array[i])
                .collect(Collectors.toList());
    }

    public static List<Byte> toList(byte[] array) {
        return IntStream.range(0, array.length)
                .mapToObj(i -> array[i])
                .collect(Collectors.toList());
    }

    public static List<Short> toList(short[] array) {
        return IntStream.range(0, array.length)
                .mapToObj(i -> array[i])
                .collect(Collectors.toList());
    }
}
