package utils;

import java.io.*;
import java.util.Base64;

public class Serialization {
    public static String serialize(Object object) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);

        byte[] byteArray = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(byteArray);
    }

    public static Object deserialize(String serializedString) {
        try {
            byte[] byteArray = Base64.getDecoder().decode(serializedString);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            return objectInputStream.readObject();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
