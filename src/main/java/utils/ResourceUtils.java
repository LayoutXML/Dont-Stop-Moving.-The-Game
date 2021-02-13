package utils;

import model.exceptions.InitializationException;

import java.io.InputStream;
import java.util.Scanner;

public class ResourceUtils {
    public static String loadResource(String name) throws InitializationException {
        String resource = null;
        try {
            InputStream inputStream = ResourceUtils.class.getResourceAsStream(name);
            Scanner scanner = new Scanner(inputStream, "UTF-8");
            resource = scanner.useDelimiter("\\A").next();
        } catch (Exception e) {
            throw new InitializationException("IE7");
        }
        return resource;
    }
}
