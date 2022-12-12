package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;


public class FileSerializer implements Serializer {

    private final ObjectMapper mapper = new ObjectMapper();
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(fileName))) {
            Gson gson = new Gson();
            String json = gson.toJson(data);
            writer.write(json);
        } catch (IOException e) {
            System.out.println("Can't write this map to file");
            throw new FileProcessException(e.getMessage());
        }
    }
}
