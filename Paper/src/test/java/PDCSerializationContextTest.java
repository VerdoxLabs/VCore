import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import de.verdox.vcore.paper.serializer.PDCSerializationContext;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PDCSerializationContextTest {
    private ServerMock server;
    private PlayerMock playerMock;

    @BeforeEach
    public void setUp()
    {
        server = MockBukkit.mock();
        playerMock = server.addPlayer();
    }


    @Test
    public void testPDCSerialization(){
        PersistentDataContainer pdc = playerMock.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer();
        PDCSerializationContext serializationContext = new PDCSerializationContext();
        Person person = new Person("Hans", 23, Gender.MALE);
        person.setJob(new Job("test", 123));
        serializationContext.serializeToPDC(Person.SERIALIZER, person, new NamespacedKey("test", "person"), pdc);

        Person deserializedPerson = serializationContext.deserializeFromPDC(Person.SERIALIZER, new NamespacedKey("test", "person"), pdc);
        Assertions.assertEquals(person, deserializedPerson);
    }

}
