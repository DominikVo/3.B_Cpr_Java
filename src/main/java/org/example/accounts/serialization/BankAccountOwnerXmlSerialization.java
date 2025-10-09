// src/main/java/org/example/accounts/serialization/BankAccountOwnerXmlSerialization.java
package org.example.accounts.serialization;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import org.example.persons.BankAccountOwner;

public class BankAccountOwnerXmlSerialization implements Serialization {

    @XmlRootElement(name = "BankAccountOwner")
    public static class BankAccountOwnerData {
        @XmlElement
        public String uuid;

        @XmlElement(name = "jmeno")
        public String firstName;

        @XmlElement
        public String lastName;

        public BankAccountOwnerData() {}
        public BankAccountOwnerData(String uuid, String firstName, String lastName) {
            this.uuid = uuid;
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    @Override
    public String serialization(Object bankAccountOwner) {
        if (!(bankAccountOwner instanceof BankAccountOwner)) {
            throw new RuntimeException("BankAccountOwner must be an instance of BankAccountOwner");
        }
        BankAccountOwner owner = (BankAccountOwner) bankAccountOwner;
        BankAccountOwnerData data = new BankAccountOwnerData(
                owner.getUuid(), owner.getFirstName(), owner.getLastName()
        );
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BankAccountOwnerData.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(data, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object deserialization(String serializedObject) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BankAccountOwnerData.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            StringReader stringReader = new StringReader(serializedObject);
            BankAccountOwnerData data = (BankAccountOwnerData) unmarshaller.unmarshal(stringReader);

            // Map back to domain model (if needed)
            return new BankAccountOwner(data.uuid, data.firstName, data.lastName);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}