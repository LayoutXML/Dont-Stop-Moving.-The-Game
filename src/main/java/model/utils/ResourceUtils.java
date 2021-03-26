package model.utils;

import model.exceptions.ResourceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResourceUtils {
    public static String loadResource(String name) throws ResourceException {
        String resource;
        try (InputStream inputStream = ResourceUtils.class.getResourceAsStream(name)) {
            Scanner scanner = new Scanner(inputStream, "UTF-8");
            resource = scanner.useDelimiter("\\A").next();
        } catch (Exception e) {
            throw new ResourceException("RE1");
        }
        return resource;
    }

    public static List<String> readFile(String name) throws ResourceException {
        List<String> list = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Class.forName(ResourceUtils.class.getName()).getResourceAsStream(name)))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new ResourceException("RE10");
        }

        return list;
    }
}
