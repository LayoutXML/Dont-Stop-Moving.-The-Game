package utils;

import model.exceptions.ResourceException;

import java.io.InputStream;
import java.util.Scanner;

public class ResourceUtils {
    public static String loadResource(String name) throws ResourceException {
        String resource;
        try {
            InputStream inputStream = ResourceUtils.class.getResourceAsStream(name);
            Scanner scanner = new Scanner(inputStream, "UTF-8");
            resource = scanner.useDelimiter("\\A").next();
        } catch (Exception e) {
            throw new ResourceException("RE1");
        }
        return resource;
    }
}
