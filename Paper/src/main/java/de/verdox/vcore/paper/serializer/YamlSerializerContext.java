package de.verdox.vcore.paper.serializer;

import de.verdox.vserializer.blank.*;
import de.verdox.vserializer.generic.*;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.IOException;

public class YamlSerializerContext extends BlankSerializationContext {

    @Override
    public void writeToFile(SerializationElement serializationElement, File file) {

    }

    @Override
    public SerializationElement readFromFile(File file) {
        return null;
    }

    public void toYaml(SerializationElement element, String key, ConfigurationSection configurationSection) {
        BlankSerializationElement blankSerializationElement = (BlankSerializationElement) convert(element, false);

        if(blankSerializationElement.isPrimitive()){
            BlankSerializationPrimitive primitive = (BlankSerializationPrimitive) blankSerializationElement.getAsPrimitive();
            if(primitive.isString())
                configurationSection.set(key, primitive.getAsString());
            else if(primitive.isNumber())
                configurationSection.set(key, primitive.getAsNumber());
            else if(primitive.isBoolean())
                configurationSection.set(key, primitive.getAsBoolean());
        }
        else if(blankSerializationElement.isArray()){
            BlankSerializationArray array = (BlankSerializationArray) blankSerializationElement.getAsArray();

            for (SerializationElement serializationElement : array) {



            }

        }
/*
        if(blankSerializationElement.isContainer()){
            BlankSerializationContainer<?> container = blankSerializationElement.getAsContainer();
            for (String childKey : container.getChildKeys()) {

                configurationSection.set(childKey, );

            }
        }*/
    }
}
