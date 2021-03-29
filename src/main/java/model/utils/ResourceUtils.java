package model.utils;

import model.exceptions.ResourceException;

import java.io.*;
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
            throw new ResourceException("File read error");
        }
        return resource;
    }

    public static List<String> readFile(String name) throws ResourceException {
        List<String> list = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(name)))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            throw new ResourceException("File read error");
        }

        return list;
    }

    public static List<String> readResourceFile(String name) throws ResourceException {
        List<String> list = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Class.forName(ResourceUtils.class.getName()).getResourceAsStream(name)))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new ResourceException("Resource read error");
        }

        return list;
    }
}
